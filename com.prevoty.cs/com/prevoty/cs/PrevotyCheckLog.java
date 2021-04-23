package com.prevoty.cs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PrevotyCheckLog {
	
	PrevotyMessage[] prevotyMessages = null;
	
	// Track the number of parameters
	private int nofParameters = 0;
	
    // Track the Prevoty version (n.n.n)
    private String prevotyVersion = null;
    
    // Track the Prevoty filter insertion type [automatic|manual]
    private String prevotyFilterInsertionType = null;
    
    // Track the program language [java|.net]
    private String programLanguage = null;

    // List of Prevoty generations
    private enum PrevotyGeneration {
    	First,	 //  < 3.9.0
    	Second,  // >= 3.9.0
    	Third,   // >= 3.9.2
    	Fourth   // >= 3.10.0
    }
    
    // List of Prevoty supported languages
    private enum PrevotyLanguage {
    	Java,   // Java
    	dotNet  // .NET
    }
    
    // List of Prevoty filter insertions
    private enum PrevotyInsertion {
    	Automatic, // Agent-based filter insertion through JVM arguments
    	Manual     // Manual filter insertion into web.xml
    }
    
    // Track Prevoty generation
    PrevotyGeneration prevGen;
    
    // Track Prevoty filter insertion
    PrevotyInsertion prevIns;
    
    // Track Prevoty language
    PrevotyLanguage prevLang;
    
    // Track the Prevoty diagnostic log file path
    private String prevFile = null;
    
    // Track the Prevoty message to look for
	PrevotySearch prvMsg[] = null;

    // List of Prevoty message to look for, for the different settings
    private void initPrevotyMessageList() {
    	prevotyMessages = new PrevotyMessage[15];
    	// Manual filter insertion        3.10.0 >= version          ────────────────────────────────────────────────┐
    	// Agent-based filter insertion   3.10.0 >= version          ───────────────────────────────────────────┐    │
    	// Manual filter insertion        3.9.2  >= version < 3.10.0 ──────────────────────────────────────┐    │    │
    	// Agent-based filter insertion   3.9.2  >= version < 3.10.0 ─────────────────────────────────┐    │    │    │
    	// Manual filter insertion        3.9.0  >= version < 3.9.2  ────────────────────────────┐    │    │    │    │
    	// Agent-based filter insertion   3.9.0  >= version < 3.9.2  ───────────────────────┐    │    │    │    │    │
    	// Manual filter insertion                  version < 3.9.0  ──────────────────┐    │    │    │    │    │    │
    	// Agent-based filter insertion             version < 3.9.0  ─────────────┐    │    │    │    │    │    │    │
    	//                                                                        │    │    │    │    │    │    │    │
    	prevotyMessages[ 0] = new PrevotyMessage(
	    	  "Prevoty Agent: Premain Invoked",                                  'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X');
	    	prevotyMessages[ 1] = new PrevotyMessage(
	    	  "Prevoty Version: ",                                               'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X');
	    	prevotyMessages[ 2] = new PrevotyMessage(
	    	  "Found application configuration from disk:",                      'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X');
	    	prevotyMessages[ 3] = new PrevotyMessage(
	    	  "Successfully deserialized and loaded application configuration:", 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X');
	    	prevotyMessages[ 4] = new PrevotyMessage(
	    	  "Using RASP jar:",                                                 ' ', ' ', 'X', ' ', 'X', ' ', 'X', ' ');
	    	prevotyMessages[ 5] = new PrevotyMessage(
	    	  "Agent version: ",                                                 ' ', ' ', ' ', ' ', 'X', 'X', 'X', 'X');
	    	prevotyMessages[ 6] = new PrevotyMessage(
	    	  "Installing CmdinjectionAgentRuntime",                             'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X');
	    	prevotyMessages[ 7] = new PrevotyMessage(
	    	  "Installing NetworkAgentRuntime",                                  ' ', ' ', ' ', ' ', ' ', ' ', 'X', 'X');
	    	prevotyMessages[ 8] = new PrevotyMessage(
	    	  "Installing PathTraversalAgentRuntime",                            'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X');
	    	prevotyMessages[ 9] = new PrevotyMessage(
	    	  "Installing QueryAgentRuntime",                                    'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X');
	    	prevotyMessages[10] = new PrevotyMessage(
	    	  "Installing ClassLoaderAgentRuntime",                              ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '); // Only available in 3.9.1. Hence, removed
	    	prevotyMessages[11] = new PrevotyMessage(
	    	  "Installing ServletAPIAgentRuntime",                               'X', ' ', 'X', ' ', 'X', ' ', 'X', ' ');
	    	prevotyMessages[12] = new PrevotyMessage(
	    	  "Installing WeakCryptoAgentRuntime",                               ' ', ' ', ' ', ' ', ' ', ' ', 'X', 'X');    	
	    	prevotyMessages[13] = new PrevotyMessage(
	    	  "Could not find RASP jar",                                         ' ', 'X', ' ', 'X', ' ', ' ', ' ', ' ');
	    	prevotyMessages[14] = new PrevotyMessage(
	    	  "Prevoty Servlet Context Listener handling context event",         ' ', 'X', ' ', 'X', ' ', 'X', ' ', 'X');
    }
    
    // List of Prevoty message to look for
    String lstPrvMsg[] = null;
    
    // Stretch a string to a certain length
    private String strStretch(String msg, int length) {
    	// Look until the right length
    	while(msg.length() < length) {
    		// Append a space at the end
    		msg += " ";
    	}
    	// Return the message
    	return msg;
    }
    
    // Print Prevoty Indexes
    void printPrevotyIndexes() {
    	// Loop through the Prevoty generations
    	for(PrevotyGeneration pg: PrevotyGeneration.values()) {
    		// Loop through the Prevoty supported languages
    		for(PrevotyLanguage pl: PrevotyLanguage.values()) {
    			// Loop through the Prevoty filter insertions
    			for(PrevotyInsertion pi: PrevotyInsertion.values()) {
    				// Loop through the list of indexes
//    				for(int index = 0; index < PrevotyIndexes[pg.ordinal()][pl.ordinal()][pi.ordinal()].length; index++) {
        			for(int index = 0; index < lstPrvMsg.length; index++) {
    					// Get the message
    					String msg = strStretch("PrevotyIndexes["+pg.name()+"]["+pl.name()+"]["+pi.name()+"]["+index+"]: ", 46);
    					// Print the index
//    					System.out.println( msg + PrevotyMessage[PrevotyIndexes[pg.ordinal()][pl.ordinal()][pi.ordinal()][index]]);
    					System.out.println( msg + lstPrvMsg[index]);
    				}
    			}
    		}
    	}
    }
    
    // Print Program settings
    void printProgramSettings() {
    	// Print the header
    	System.out.println("╔═════════════════════════════════╗");
    	System.out.println("║ Prevoty Diagnostic Log Summary  ║");
    	System.out.println("╚═════════════════════════════════╝");
    	// Prevoty generation
    	System.out.print("  Prevoty version:                  ");
    	switch(prevGen){
			case First:
				System.out.println("< 3.9.0");
				break;
			case Second:
				System.out.println(">= 3.9.0 and < 3.9.2");
				break;
			case Third:
				System.out.println(">= 3.9.2 and < 3.10.0");
				break;
			case Fourth:
				System.out.println(">= 3.10.0");
				break;
    	}
    	// Prevoty filter insertion
    	System.out.println("  Prevoty filter insertion:         "+ (prevIns == PrevotyInsertion.Automatic ? "Agent-based" : "Manual (change web.xml)"));
    	// Prevoty language
    	System.out.println("  Prevoty language:                 " + (prevLang == PrevotyLanguage.Java ? "Java" : ".NET"));
    	// Prevoty diagnostic log file path
    	System.out.println("  Prevoty diagnostic log file path: " + prevFile );
    }
    
    // Add one string to a list of strings
    String [] addString(String [] theList, String theString) {
    	// Set the return value
    	String [] retVal = null;
    	// Do we have a list?
    	if(theList != null ) {
    		// Get the length of the list
    		int length = theList.length;
    		// Create a new list
    		retVal = new String[length+1];
    		// Copy the strings
    		for(int index = 0; index < length; index++ ) {
    			// Copy one string
    			retVal[index] = theList[index];
    		}
    		// Add the new string
			retVal[length] = theString;
    	}
    	else {
    		retVal = new String[1];
    		retVal[0] = theString;
    	}
    	// Return the result
    	return retVal;
    }
    
    // Create the list of Prevoty messages we need to look for
    void getPrevotyMessages() {
    	// Is this for .NET?
    	if(prevLang == PrevotyLanguage.dotNet) {
    		if(prevIns == PrevotyInsertion.Automatic) {
    			// Create the array of strings for .NET with agent-based filter insertion
    			lstPrvMsg = new String[6];
    			lstPrvMsg[0] = "prevoty.qos.all [(null)] - {\"application\":";
    			lstPrvMsg[1] = "prevoty.qos.content [(null)] - Content initialized";
    			lstPrvMsg[2] = "prevoty.qos.csrf [(null)] - CSRF initialized";
    			lstPrvMsg[3] = "prevoty.qos.query [(null)] - Query initialized";
    			lstPrvMsg[4] = "prevoty.qos.ci [(null)] - CI initialized";
    			lstPrvMsg[5] = "prevoty.qos.path [(null)] - Path Traversal initialized";
    		}
    	}
    	else {
    		// Loop through the list of all messages
    		for(int index=0; index < prevotyMessages.length; index++) {
    			switch(prevGen){
	    			case First:
	    				switch(prevIns) {
		    				case Automatic:
		    					if(prevotyMessages[index].getForJavaAllAutomatic()) {
		    						lstPrvMsg = addString(lstPrvMsg, prevotyMessages[index].getMessage());
		    					}
		    					break;
		    				case Manual:
		    					if(prevotyMessages[index].getForJavaAllManual()) {
		    						lstPrvMsg = addString(lstPrvMsg, prevotyMessages[index].getMessage());
		    					}
		    					break;
		    				}
	    				break;
	    			case Second:
	    				switch(prevIns) {
		    				case Automatic:
		    					if(prevotyMessages[index].getForJava390Automatic()) {
		    						lstPrvMsg = addString(lstPrvMsg, prevotyMessages[index].getMessage());
		    					}
		    					break;
		    				case Manual:
		    					if(prevotyMessages[index].getForJava390Manual()) {
		    						lstPrvMsg = addString(lstPrvMsg, prevotyMessages[index].getMessage());
		    					}
		    					break;
		    				}
	    				break;
	    			case Third:
	    				switch(prevIns) {
		    				case Automatic:
		    					if(prevotyMessages[index].getForJava392Automatic()) {
		    						lstPrvMsg = addString(lstPrvMsg, prevotyMessages[index].getMessage());
		    					}
		    					break;
		    				case Manual:
		    					if(prevotyMessages[index].getForJava392Manual()) {
		    						lstPrvMsg = addString(lstPrvMsg, prevotyMessages[index].getMessage());
		    					}
		    					break;
		    				}
	    				break;
	    			case Fourth:
	    				switch(prevIns) {
		    				case Automatic:
		    					if(prevotyMessages[index].getForJava310Automatic()) {
		    						lstPrvMsg = addString(lstPrvMsg, prevotyMessages[index].getMessage());
		    					}
		    					break;
		    				case Manual:
		    					if(prevotyMessages[index].getForJava310Manual()) {
		    						lstPrvMsg = addString(lstPrvMsg, prevotyMessages[index].getMessage());
		    					}
		    					break;
		    				}
	    				break;
    			}
    		}
    	}
    }
        
    // Constructor
    PrevotyCheckLog(String[] arguments) {
    	// Get the program arguments
    	getParameters(arguments);
	}
	
    // Display how to use the program
    void displayHelp(String msg) {
    	System.out.println("╔══════════════════════════════════════════════════════");
        System.out.println("║ PrevotyCheckLog");
        System.out.println("║   Checks if a Prevoty Diagnostic log file contains all the required message, in the right order.");
        System.out.println("║ Version:");
        System.out.println("║   1.0.0 - Sun Sep 30, 2018 - PWR Created");
        System.out.println("║   1.0.1 - Mon Oct 01, 2018 - PWR Changed the order of the search strings");
        System.out.println("║   1.0.2 - Wed Oct 03, 2018 - PWR Fixed syntax errors in the help text");
        System.out.println("║   1.0.3 - Thu Nov 01, 2018 - PWR Added \"Installing ClassLoaderAgentRuntime\" for Prevoty 3.9.x");
        System.out.println("║   1.1.0 - Mon Feb 04, 2019 - PWR Added support for Prevoty 3.10.x");
        System.out.println("║ Usage:");
        System.out.println("║   java -jar PrevotyCheckLog.jar -v <version [n.n.n]> -i <filter insertion type [Automatic|Manual]> -l <language [Java|.NET]> -f <path to Prevoty diagnostic log file>");
        System.out.println("║ Example:");
        System.out.println("║   java -jar PrevotyCheckLog.jar -v 3.6.7 -i Manual -l Java -f /opt/Apache/Tomcat-8.5.5/logs/prevoty.log");
        System.out.println("║ Notes:");
        System.out.println("║   - Written by PWR on his own accord");
        System.out.println("║   - Prevoty cannot be held liable for any errors, mistakes, omissions, etc.");
        System.out.println("║   - USE AND INTERPRETATION OF THE RESULTS AT YOUR OWN RISK!!!");
        if ((msg != null) && (msg.length() > 0)) {
            System.out.println("║ Error:   " + msg); 
        }
    	System.out.println("╚══════════════════════════════════════════════════════");
    }

    // Get the program parameters
    private void getParameters(String[] arguments) {
        // Do we have arguments?
        if ((arguments != null) && (arguments.length > 0)){
        	// Save the number of parameters
        	nofParameters = arguments.length;
    		// The key contains one character of the argument list
    		char theKey = ' ';
            // Get the arguments
        	for (int i = 0; i < arguments.length; i++) {
        		// Is the first argument a flag?
        		if ((arguments[i] != null) && (arguments[i].length() == 2) && (arguments[i].charAt(0) == '-')) {
        			// Get the key
        			theKey = arguments[i].toLowerCase().charAt(1);
        			// Is there a second argument?
        			if((arguments[i+1] != null) && (arguments[i+1].length() > 0)) {
        				// Determine what to do
        				switch(theKey) {
	        				// Get the Prevoty version
	        				case 'v':
	        					prevotyVersion = arguments[i+1];
	        					break;
	        				// Get the Prevoty filter insertion type
	        				case 'i':
	        					prevotyFilterInsertionType = arguments[i+1];
	        					break;
	        				// Get the program language
	        				case 'l':
	        					programLanguage = arguments[i+1];
	        					break;
	        				// Get the Prevoty diagnostic log file path
	        				case 'f':
	        					prevFile = arguments[i+1];
	        					break;
        				}
        			}
        		}
        		// Move to the next set of arguments
        		i++;
        	}
        }
    }
    
    /**
     * Compares two version strings. 
     * 
     * Use this instead of String.compareTo() for a non-lexicographical 
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     * 
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     * 
     * @param str1 a string of ordinal numbers separated by decimal points. 
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2. 
     *         The result is a positive integer if str1 is _numerically_ greater than str2. 
     *         The result is zero if the strings are _numerically_ equal.
     */
    public static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
          i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }    
    
    // Collect a string of numbers from a string, starting at an index
    String getVersionNumber(String version, int index) {
    	// Track we are collecting numbers
    	boolean inNumbers = true;
    	// Track the string of numbers
    	String theVal = "";
    	// Loop through the string until we hit a dot
    	while (inNumbers) {
    		// Are we within the string
    		if(index < version.length()) {
    			// Get the key at the index
    			char key = version.charAt(index);
    			// Is the key a number?
    			if((key >= '0') && (key <= '9')) {
    				// Add the key to the value
    				theVal += key;
    				// Move to the next character
    				index++;
    			}
    			else {
    				// The key is not a number, so terminate
        			inNumbers = false;
    			}
    			
    		}
    		else {
    			// Outside the string, so terminate
    			inNumbers = false;
    		}
    	}
    	// Try to convert the string to an integer
    	try {
    		// Convert the string to a number
    		Integer.parseInt(theVal);
    	}
    	catch(NumberFormatException e) {
    		// The conversion failed, so set the number to -1
    		theVal = "-1";
    	}
    	// Return the result
    	return theVal;
    }
    
    // Check the Prevoty version (n.n.n)
    private boolean checkPrevotyVersion() {
    	// Set the default return value
    	boolean retVal = false;
    	// Keep track on where we are in the string
    	int index = 0;
    	// Do we have a Prevoty version?
    	if((prevotyVersion != null) && (prevotyVersion.length() >= 5)) {
    		// Get major version string
    		String majorVersionStr = getVersionNumber(prevotyVersion, index);
    		// Move to the next number, including the dot
    		index += majorVersionStr.length() + 1;
    		// Get the minor version string
    		String minorVersionStr = getVersionNumber(prevotyVersion, index);
    		// Move to the next number, including the dot
    		index += minorVersionStr.length() + 1;
    		// Get the patch level string
    		String patchLevelStr = getVersionNumber(prevotyVersion, index);
    		// Get the major version
    		int majorVersion = Integer.parseInt(majorVersionStr);
    		// Get the minor version
    		int minorVersion = Integer.parseInt(minorVersionStr);
    		// Get the patch level
    		int patchLevel = Integer.parseInt(patchLevelStr);
    		// Is it of the right format?
    		if((majorVersion != -1 ) &&
    		   (minorVersion != -1 ) &&
    		   (patchLevel != -1 )) {
    			// Prevoty version format is valid
    			retVal = true;
    			// Compare the Prevoty version against 3.9.0
    	    	if(versionCompare(prevotyVersion,"3.9.0") < 0 ) {
    	    		// Set to first Prevoty generation
    	    		prevGen = PrevotyGeneration.First;
    	    	}
    	    	else
    	    	{
        			// Compare the Prevoty version against 3.9.2
        	    	if(versionCompare(prevotyVersion,"3.9.3") < 0 ) {
        	    		// Set to second Prevoty generation
        	    		prevGen = PrevotyGeneration.Second;
        	    	}
        	    	else
        	    	{
            			// Compare the Prevoty version against 3.9.2
            	    	if(versionCompare(prevotyVersion,"3.10.0") < 0 ) {
            	    		// Set to third Prevoty generation
            	    		prevGen = PrevotyGeneration.Third;
            	    	}
            	    	else
            	    	{
            	    		// Set to fourth Prevoty generation
            	    		prevGen = PrevotyGeneration.Fourth;
            	    	}
        	    	}
    	    	}
    		}
    		else {
        		// Invalid Prevoty version format provided
        		displayHelp("Invalid version format provided ("+prevotyVersion+"); should be of the format n.n.n");
    		}
    	}
    	else {
    		// No or invalid Prevoty version provided
    		displayHelp("No or invalid version provided; should be of the format n.n.n");
    	}
    	// Return the result
    	return retVal;
    }
    
    // Check the Prevoty filter insertion type
    private boolean checkPrevotyFilterInsertionType() {
    	// Set the default return value
    	boolean retVal = false;
    	// Do we have a Prevoty filter insertion type?
    	if((prevotyFilterInsertionType != null) && (prevotyFilterInsertionType.length() > 0)) {
    		// Is it automatic?
    		if (prevotyFilterInsertionType.equalsIgnoreCase("automatic")) {
    			// Prevoty filter insertion type is valid
    			retVal = true;
    			// Set to automatic filter insertion
    			prevIns = PrevotyInsertion.Automatic;
    		}
    		else {
    			// Is it manual?
        		if (prevotyFilterInsertionType.equalsIgnoreCase("manual")) {
        			// Prevoty filter insertion type is valid
        			retVal = true;
        			// Set to manual filter insertion
        			prevIns = PrevotyInsertion.Manual; 
        		}
        		else {
        			// Invalid Prevoty filter insertion type provided
        			displayHelp("Invalid filter insertion type provided ("+prevotyFilterInsertionType+"); should be either Automatic or Manual");
        		}
    		}
    	}
    	else {
    		// No or invalid Prevoty filter insertion type provided
    		displayHelp("No or invalid filter insertion type provided; should be either Automatic or Manual");
    	}
    	// Return the result
    	return retVal;
    }
    
    // Check the program language
    private boolean checkProgramLanguage() {
    	// Set the default return value
    	boolean retVal = false;
    	// Do we have a program language?
    	if((programLanguage != null) && (programLanguage.length() > 0)) {
    		// Is it java?
    		if (programLanguage.equalsIgnoreCase("java")) {
    			// program language is valid
    			retVal = true;
    			// Set to Java
    			prevLang = PrevotyLanguage.Java; 
    		}
    		else {
    			// Is it .net?
        		if (programLanguage.equalsIgnoreCase(".net")) {
        			// program language is valid
        			retVal = true;
        			// Set to Java
        			prevLang = PrevotyLanguage.dotNet;
        		}
        		else {
        			// Invalid program language provided
        			displayHelp("Invalid language provided ("+programLanguage+"); should be either Java or .NET");
        		}
    		}
    	}
    	else {
    		// No or invalid program language provided
    		displayHelp("No or invalid language provided; should be either Java or .NET");
    	}
    	// Return the result
    	return retVal;
    }
    
    // Check if the file can be read
    private boolean fileIsReadable(File file) {
    	// Try tom open the file for reading
        try {
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            fileReader.read();
            fileReader.close();
        } catch (Exception e) {
            // File cannot be read
            return false;
        }
        return true;
    }
    
    // Check the Prevoty diagnostic log file path
    private boolean checkPrevotyDiagnosticLogFilePath() {
    	// Set the default return value
    	boolean retVal = false;
    	// Do we have a Prevoty diagnostic log file path?
    	if((prevFile != null) && (prevFile.length() > 0 )) {
    		// Does the file exists?
    		File file = new File(prevFile);
    		if((file.exists()) && (!file.isDirectory())) { 
        		// Can the file be opened for reading?
    			if(fileIsReadable(file)) {
    				// File can be opened for reading
    				retVal = true;
    			}
    			else {
        			// Prevoty diagnostic log file cannot be opened for reading
        			displayHelp("Prevoty diagnostic log file ("+prevFile+") cannot be opened for reading");
    			}
    		}    		
    		else {
    			// Path to Prevoty diagnostic log file does not exists
    			displayHelp("Path to Prevoty diagnostic log file ("+prevFile+") does not exists");
    		}
    	}
    	else {
    		// No or invalid path to Prevoty diagnostic log file provided
    		displayHelp("No or invalid path to Prevoty diagnostic log file provided");
    	}
    	// Return the result
    	return retVal;
    }
    
    // Make sure all parameters are provided
    private boolean checkParameters() {
    	// Set the default return value
    	boolean retVal = false;
    	// Do we have some parameters?
    	if(nofParameters > 0) {
        	// Check all the parameters
    		retVal =  (checkPrevotyVersion() &&
        		   	   checkPrevotyFilterInsertionType() &&
        			   checkProgramLanguage()) &&
        			   checkPrevotyDiagnosticLogFilePath();
    	}
    	else {
    		// Display the help information
    		displayHelp(null);
    		
    	}
    	// Return the result
    	return retVal;
    }
    
    // Initialize the Prevoty message to look for
    private void initPrevotyMessages(int nofMsg) {
    	// Create the structure
    	prvMsg = new PrevotySearch[nofMsg];
    	// Initialize the  structure
    	for (int index = 0; index < nofMsg; index++) {
    		//  Initialize one Prevoty message
    		prvMsg[index] = new PrevotySearch(lstPrvMsg[index]);
    	}
    }
    
    // Reset the Prevoty message to look for
    private void resetPrevotyMessages(int nofMsg) {
    	// Initialize the  structure
    	for (int index = 0; index < nofMsg; index++) {
    		//  Reset the Prevoty message, but don't lose the list of line numbers
    		prvMsg[index].reset();
    	}
    }
    
	// Report on the results
	void printCheckLogFileResults(boolean foundAll, int nextToFind) {
    	// Did we find everything:
    	System.out.println("  Prevoty deployment check:         " + ( foundAll ? "OK" : "ERROR"));
    	// Print the header
    	System.out.println("╔═════════════════════════════════╗");
    	System.out.println("║ Prevoty Diagnostic Log Analysis ║");
    	System.out.println("╚═════════════════════════════════╝");
    	// Loop through the search structure
    	for(int index = 0; index < prvMsg.length; index++) {
    		// Print the search string no
    		System.out.println("    String " + index + ":          " + (prvMsg[index].getFoundStr() ? "Found": "Not found"));
    		// Print the search string
    		System.out.println("      Search string:   " + prvMsg[index].getSearchStr()); 
    		// Print the occurrences
    		String occurrences = prvMsg[index].getLineNumbers();
    		System.out.println("      Occurrence(s):   " + (((occurrences != null) && (occurrences.length() > 0))? occurrences : "None"));
    		// Do we have a last occurrence?
    		if (prvMsg[index].getFoundStr()) {
    			// Print the last occurrence
        	System.out.println("      Last occurrence: " + prvMsg[index].getLine());
    		}
    	}
	}
	
    // Check if all required messages are in the Prevoty diagnostic log
    private boolean checkLogFile() {
    	// Set the default return value
    	boolean  retVal = false;
    	// Get the number of message to check for
    	int nofCheckMessages = lstPrvMsg.length;
    	// Is this combination of parameters supported?
    	if(nofCheckMessages > 0) {
        	// Initialize the Prevoty message to look for
        	initPrevotyMessages(nofCheckMessages);
        	// The current index of the message to check for
        	int searchIndex = 0;
        	// The current line number
        	int lineNo = 0;
            // Try to parse the file
            try{
                // Create the file handle
            	FileReader fileHandle = new FileReader(prevFile);
            	// Create the buffer handle 
            	BufferedReader bufferHandle = new BufferedReader(fileHandle);
            	// Read the file line-by-line
            	for(String line; (line = bufferHandle.readLine()) != null; ) {
            		// Increase the line number
            		lineNo++;
            		// Does this line contains the first search string?
            		if(line.indexOf(prvMsg[0].getSearchStr()) != -1 ) {
            			// Reset the search structure
            			resetPrevotyMessages(nofCheckMessages);
            			// Set the first object
            			prvMsg[0].init(true, lineNo, line );
            			// Set the second string to search for
            			searchIndex = 1;
            			// Reset we found everything
            			retVal = false;
            		}
            		else {
            			// Does this line contain the next string to find?
                		if(line.indexOf(prvMsg[searchIndex].getSearchStr()) != -1 ) {
                			// Set the next object
                			prvMsg[searchIndex].init(true, lineNo, line );
                			// Set the next string to find
                			searchIndex++;
                		}
            		}
            		// Have we reached the end of the list of search strings?
            		if(searchIndex >= nofCheckMessages) {
            			// We found everything
            			retVal = true;
            			// Reset the search index
            			searchIndex = 0;
            		}
            	}
            	// Close the buffer reader
            	bufferHandle.close();
            	// Report on the results
            	printCheckLogFileResults(retVal, searchIndex);
            } catch (FileNotFoundException e) {
                displayHelp("Could not find the Prevoty diagnostic log file \"" + prevFile + "\"");
            } catch (IOException e) {
                displayHelp("Could not read the Prevoty diagnostic log file \"" + prevFile + "\"");
            }
    	}
    	else {
    		// This combination of parameters is not supported
    		displayHelp("This combination of parameters is not supported");
    	}
       	// Return the results
    	return retVal;
    }
    
	static public void main(String[] args) {
		// Create the object
		PrevotyCheckLog prevotyCheckLog = new PrevotyCheckLog(args);
		// Do we have an object
		if(prevotyCheckLog != null) {
			// Are all parameters OK?
			if(prevotyCheckLog.checkParameters()) {
				// Initialize the Prevoty message
				prevotyCheckLog.initPrevotyMessageList();
				// Get the list of Prevoty message to look for
				prevotyCheckLog.getPrevotyMessages();
				// Print the program settings
				prevotyCheckLog.printProgramSettings();
				// Check the Prevoty diagnostic log
				prevotyCheckLog.checkLogFile();
			}
		}
	}
}