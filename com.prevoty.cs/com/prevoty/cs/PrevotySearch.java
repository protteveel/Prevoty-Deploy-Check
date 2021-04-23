package com.prevoty.cs;

// Class to keep track of the strings we are looking for
public class PrevotySearch {
	// The string to look for
	private String searchStr = null;
	// Indicator it was found or not
	private boolean foundStr = false;
	// Line number it was found in
	private int lineNo = -1;
	// The line, which contains the string we are looking for
	private String line = null;
	// Collection of the line numbers
	private String lineNumbers = null;
	// Constructor with just the search string
	PrevotySearch(String searchStr) {this.init(); this.setSearchStr(searchStr);}
	// Set search string
	public void setSearchStr(String searchStr) {this.searchStr = searchStr;}
	// Set find indicator
	public void setFoundStr(boolean foundStr) {this.foundStr = foundStr;}
	// Set the line number
	public void setLineNo(int lineNo) {this.lineNo = lineNo;}
	// Set the line
	public void setLine(String line) {this.line = line;}
	// Set the line numbers
	public void setLineNumber(int lineNo) {
		// Reset the line number?
		if(lineNo == -1 ) {
			this.lineNumbers = null;
		}
		else {
			// Do we already have a line number?
			if((this.lineNumbers != null) && (this.lineNumbers.length() > 0)) {
				// Add the line number
				this.lineNumbers += (", " + lineNo);
			}
			else {
				// Start a new list
				this.lineNumbers = "" + lineNo;
			}
		}
	}
	// Initialize the class without arguments
	public void init() {
		this.setSearchStr(null);
		this.setFoundStr(false);
		this.setLineNo(-1);
		this.setLine(null);
		this.setLineNumber(-1);
	}
	// Initialize the class with arguments
	public void init(boolean foundStr, int lineNo, String line ) {
		this.setFoundStr(foundStr);
		this.setLineNo(lineNo);
		this.setLine(line);
		this.setLineNumber(lineNo);
	}
	// Reset the class
	public void reset() {
		setFoundStr(false);
		setLineNo(-1);
		setLine(null);
		// Leave in the line numbers!
	}
	// Get the search string
	public String getSearchStr() {return this.searchStr;}
	// Get the find indicator
	public boolean getFoundStr() {return this.foundStr;}
	// Get the line number
	public int getLineNo() {return this.lineNo;}
	// Get the line
	public String getLine() {return this.line;}
	// Get the list of line numbers
	public String getLineNumbers() {return this.lineNumbers;}
}
