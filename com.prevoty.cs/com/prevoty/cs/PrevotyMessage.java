package com.prevoty.cs;

// Class to contain a Prevoty message and for what version / filter insertion it is applicable for
public class PrevotyMessage {
	// This is the message
	private String message;
	// The message is applicable for Java for all Prevoty versions, for agent-based filter insertion, for all versions 
	private boolean forJavaAllAutomatic;
	// The message is applicable for Java for all Prevoty versions, for manual filter insertion, for all versions 
	private boolean forJavaAllManual;
	// The message is applicable for Java for Prevoty versions 3.9.0 and above, for agent-based filter insertion, for all versions 
	private boolean forJava390Automatic;
	// The message is applicable for Java for Prevoty versions 3.9.0 and above, for manual filter insertion, for all versions 
	private boolean forJava390Manual;
	// The message is applicable for Java for Prevoty versions 3.9.2 and above, for agent-based filter insertion, for all versions 
	private boolean forJava392Automatic;
	// The message is applicable for Java for Prevoty versions 3.9.2 and above, for manual filter insertion, for all versions 
	private boolean forJava392Manual;
	// The message is applicable for Java for Prevoty versions 3.10.0 and above, for agent-based filter insertion, for all versions 
	private boolean forJava310Automatic;
	// The message is applicable for Java for Prevoty versions 3.10.0 and above, for manual filter insertion, for all versions 
	private boolean forJava310Manual;
	
	// Constructor
	public PrevotyMessage(String message, boolean forJavaAllAutomatic, boolean forJavaAllManual, 
			                              boolean forJava390Automatic, boolean forJava390Manual, 
			                              boolean forJava392Automatic, boolean forJava392Manual,
			                              boolean forJava310Automatic, boolean forJava310Manual) {
		this.setMessage(message);
		this.setForJavaAllAutomatic(forJavaAllAutomatic);
		this.setForJavaAllManual(forJavaAllManual);
		this.setForJava390Automatic(forJava390Automatic);
		this.setForJava390Manual(forJava390Manual);
		this.setForJava392Automatic(forJava392Automatic);
		this.setForJava392Manual(forJava392Manual);
		this.setForJava310Automatic(forJava310Automatic);
		this.setForJava310Manual(forJava310Manual);
	}
	
	// Constructor
	public PrevotyMessage(String message, char forJavaAllAutomatic, char forJavaAllManual, 
			                              char forJava390Automatic, char forJava390Manual, 
			                              char forJava392Automatic, char forJava392Manual,
			                              char forJava310Automatic, char forJava310Manual) {
		this.setMessage(message);
        this.setForJavaAllAutomatic((forJavaAllAutomatic == 'X') || (forJavaAllAutomatic == 'x'));
        this.setForJavaAllManual(   (forJavaAllManual    == 'X') || (forJavaAllManual    == 'x'));
        this.setForJava390Automatic((forJava390Automatic == 'X') || (forJava390Automatic == 'x'));
        this.setForJava390Manual(   (forJava390Manual    == 'X') || (forJava390Manual    == 'x'));
        this.setForJava392Automatic((forJava392Automatic == 'X') || (forJava392Automatic == 'x'));
        this.setForJava392Manual(   (forJava392Manual    == 'X') || (forJava392Manual    == 'x'));
        this.setForJava310Automatic((forJava310Automatic == 'X') || (forJava310Automatic == 'x'));
        this.setForJava310Manual(   (forJava310Manual    == 'X') || (forJava310Manual    == 'x'));
	}
	
	// Setters
	public void setMessage(String message) {this.message= message;}
	public void setForJavaAllAutomatic(boolean forJavaAllAutomatic) {this.forJavaAllAutomatic = forJavaAllAutomatic;}
	public void setForJavaAllManual(boolean forJavaAllManual) {this.forJavaAllManual = forJavaAllManual;}
	public void setForJava390Automatic(boolean forJava390Automatic) {this.forJava390Automatic = forJava390Automatic;}
	public void setForJava390Manual(boolean forJava390Manual) {this.forJava390Manual = forJava390Manual;}
	public void setForJava392Automatic(boolean forJava392Automatic) {this.forJava392Automatic = forJava392Automatic;}
	public void setForJava392Manual(boolean forJava392Manual) {this.forJava392Manual = forJava392Manual;}
	public void setForJava310Automatic(boolean forJava310Automatic) {this.forJava310Automatic = forJava310Automatic;}
	public void setForJava310Manual(boolean forJava310Manual) {this.forJava310Manual = forJava310Manual;}

	// Getters
	public String  getMessage() { return this.message;}
	public boolean getForJavaAllAutomatic() { return this.forJavaAllAutomatic;}
	public boolean getForJavaAllManual() { return this.forJavaAllManual;}
	public boolean getForJava390Automatic() { return this.forJava390Automatic;}
	public boolean getForJava390Manual() { return this.forJava390Manual;}
	public boolean getForJava392Automatic() { return this.forJava392Automatic;}
	public boolean getForJava392Manual() { return this.forJava392Manual;}
	public boolean getForJava310Automatic() { return this.forJava310Automatic;}
	public boolean getForJava310Manual() { return this.forJava310Manual;}
}
