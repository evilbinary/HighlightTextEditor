package org.evilbinary.managers;


public class Configure {
	public Settings mSettings;
	public  String mLineSeparator ;

	public Configure(){
		mSettings=new Settings();
		mLineSeparator= System.getProperty("line.separator");
	}
}
