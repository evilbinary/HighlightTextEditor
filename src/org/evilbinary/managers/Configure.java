package org.evilbinary.managers;

import org.evilbinary.utils.DirUtil;

import android.content.Context;
import android.graphics.Color;


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
	public int mLineNumberColor;
	public int mLineHighlightColor;
	public int mLineHighlightAlpha;

	public Configure(Context context){
		mSettings=new Settings();
		mLineSeparator= System.getProperty("line.separator");
		mDataPath=DirUtil.getFilesDir(context);
		mHighlightCss="highlight.css";
		mLanguage="c";
		mTheme="molokai";
		mFontSize=mSettings.TEXT_SIZE;
		mLineHighlightColor=Color.BLACK;
		mLineNumberColor=Color.GRAY;
		mLineHighlightAlpha=48;
		
	}
}
