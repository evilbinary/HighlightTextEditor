package org.evilbinary.highliter.parsers;

public class SyntaxHighlight {
	static {
		System.loadLibrary("lua");
		System.loadLibrary("highlight");
	}
	public SyntaxHighlight(){
		
	}
	public native String pase(String codeBlock);

}
