package org.evilbinary.highliter.parsers;

public class SyntaxHighlight {
	static {
		System.loadLibrary("lua");
		System.loadLibrary("highlight");
	}
	public SyntaxHighlight(String dataPath){
		init(dataPath);
		
	}
	
	public native int init(String path);
	public native String pase(String codeBlock);

}
