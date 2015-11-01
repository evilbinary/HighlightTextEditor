package org.evilbinary.managers;

import org.evilbinary.utils.DirUtil;

import android.content.Context;


public class Configure {
	public Settings mSettings;
	public  String mLineSeparator ;
	public String mDataPath;
	public String mHighlightCss;
	

	public Configure(Context context){
		mSettings=new Settings();
		mLineSeparator= System.getProperty("line.separator");
		mDataPath=DirUtil.getFilesDir(context);
		mHighlightCss=mDataPath+ "/highlight.css";
	}
}
