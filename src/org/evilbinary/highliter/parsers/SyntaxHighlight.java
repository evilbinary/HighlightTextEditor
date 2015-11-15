/* Copyright (C) 2015 evilbinary.
 * rootdebug@163.com
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.evilbinary.highliter.parsers;

import org.evilbinary.managers.Configure;

public class SyntaxHighlight {
	static {
		System.loadLibrary("lua");
		System.loadLibrary("highlight");
	}
	private String mArgs = "hilighlight";
	private String mDelim = ":";

	public SyntaxHighlight(Configure conf) {
		initArgs(conf);
	}
	private void initArgs(Configure conf){
		addArg("-D" + conf.mDataPath);
		addArg("-d" + conf.mDataPath);
		if (conf.mLanguage != null && !conf.mLanguage.equals(""))
			addArg("--syntax=" + conf.mLanguage);
		addArg("-f");
		addArg("--print-style");// first gencssfile
		if (conf.mTheme != null && !conf.mTheme.equals(""))
			addArg("-s" + conf.mTheme);
		if (conf.mHighlightCss != null && !conf.mHighlightCss.equals(""))
			addArg("-c" + conf.mHighlightCss);
		
		if (conf.mFont != null && !conf.mFont.equals(""))
			addArg("--font=" + conf.mFont);
		if (conf.mFontSize != 0)
			addArg("--font-size=" + conf.mFontSize);
		if (conf.mEncoding != null && !conf.mEncoding.equals(""))
			addArg("--encoding=" + conf.mEncoding);
		init(mArgs);
	}
	public void loadConfigure(Configure conf){
		initArgs(conf);
	}

	private void addArg(String arg) {
		mArgs += mDelim + arg;
	}

	private native int init(String args);

	public native String pase(String codeBlock);

	public native String getTheme(String name);

}
