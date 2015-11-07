package org.evilbinary.managers;

import org.evilbinary.utils.DirUtil;

import android.content.Context;


public class Configure {
	public Settings mSettings;
	public  String mLineSeparator ;
	public String mDataPath;
	public String mHighlightCss;
	public String mLanguage;
	public String mTheme;
	public String mEncoding;
	public int mFontSize;
	public String mFont;
	public String mPlugin;

	public Configure(Context context){
		mSettings=new Settings();
		mLineSeparator= System.getProperty("line.separator");
		mDataPath=DirUtil.getFilesDir(context);
		mHighlightCss="highlight.css";
		mLanguage="c";
		mTheme="molokai";
		
	}
}
