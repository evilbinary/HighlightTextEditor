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

package org.evilbinary.managers;

import java.io.File;

import org.evilbinary.highliter.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;

public class Settings implements Constants {

	/** Number of recent files to remember */
	public int MAX_RECENT_FILES = 10;

	/** Show the lines numbers */
	public boolean SHOW_LINE_NUMBERS = true;
	/** automatic break line to fit one page */
	public boolean WORDWRAP = false;
	/** color setting */
	public int COLOR = COLOR_CLASSIC;

	/** when search reaches the end of a file, search wrap */
	public boolean SEARCHWRAP = false;
	/** only search for matchin case */
	public boolean SEARCHMATCHCASE = false;

	/** Text size setting */
	public int TEXT_SIZE = 12;

	/** Default end of line */
	public int DEFAULT_END_OF_LINE = EOL_LINUX;
	/** End Of Line style */
	public int END_OF_LINE = EOL_LINUX;
	/** Encoding */
	public String ENCODING = ENC_UTF8;

	/** Let auto save on quit be triggered */
	public boolean FORCE_AUTO_SAVE = false;
	public boolean AUTO_SAVE_OVERWRITE = false;

	/** enable fling to scroll */
	public boolean FLING_TO_SCROLL = false;

	/** Use Undo instead of quit ? */
	public boolean UNDO = true;
	/** Undo stack capacity */
	public int UNDO_MAX_STACK = 25;
	/** Use back button as undo */
	public boolean BACK_BTN_AS_UNDO = false;

	/** Use a Home Page */
	public boolean USE_HOME_PAGE = false;
	/** Home Page Path */
	public String HOME_PAGE_PATH = "";

	/**
	 * @return the end of line characters according to the current settings
	 */
	public String getEndOfLine() {
		switch (END_OF_LINE) {
		case EOL_MAC: // Mac OS
			return "\r";
		case EOL_WINDOWS: // Windows
			return "\r\n";
		case EOL_LINUX: // Linux / Android
		default:
			return "\n";
		}
	}

	/**
	 * Update the settings from the given {@link SharedPreferences}
	 * 
	 * @param settings
	 *            the settings to read from
	 */
	public void updateFromPreferences(SharedPreferences settings) {

		MAX_RECENT_FILES = getStringPreferenceAsInteger(settings, PREFERENCE_MAX_RECENTS, "10");
		SHOW_LINE_NUMBERS = settings.getBoolean(PREFERENCE_SHOW_LINE_NUMBERS, true);
		WORDWRAP = settings.getBoolean(PREFERENCE_WORDWRAP, false);
		TEXT_SIZE = getStringPreferenceAsInteger(settings, PREFERENCE_TEXT_SIZE, "12");
		DEFAULT_END_OF_LINE = getStringPreferenceAsInteger(settings, PREFERENCE_END_OF_LINES, ("" + EOL_LINUX));
		FORCE_AUTO_SAVE = settings.getBoolean(PREFERENCE_AUTO_SAVE, false);
		AUTO_SAVE_OVERWRITE = settings.getBoolean(PREFERENCE_AUTO_SAVE_OVERWRITE, false);
		COLOR = getStringPreferenceAsInteger(settings, PREFERENCE_COLOR_THEME, ("" + COLOR_CLASSIC));
		SEARCHWRAP = settings.getBoolean(PREFERENCE_SEARCHWRAP, false);
		SEARCHMATCHCASE = settings.getBoolean(PREFERENCE_SEARCH_MATCH_CASE, false);
		ENCODING = settings.getString(PREFERENCE_ENCODING, ENC_UTF8);
		FLING_TO_SCROLL = settings.getBoolean(PREFERENCE_FLING_TO_SCROLL, true);

		BACK_BTN_AS_UNDO = settings.getBoolean(PREFERENCE_BACK_BUTTON_AS_UNDO, false);
		UNDO = settings.getBoolean(PREFERENCE_ALLOW_UNDO, true);
		UNDO_MAX_STACK = getStringPreferenceAsInteger(settings, PREFERENCE_MAX_UNDO_STACK, "25");

		USE_HOME_PAGE = settings.getBoolean(PREFERENCE_USE_HOME_PAGE, false);
		HOME_PAGE_PATH = settings.getString(PREFERENCE_HOME_PAGE_PATH, "");

		// RecentFiles.loadRecentFiles(settings.getString(PREFERENCE_RECENTS,
		// ""));
	}

	/**
	 * Reads a preference stored as a string and returns the numeric value
	 * 
	 * @param prefs
	 *            the prefernce to read from
	 * @param key
	 *            the key
	 * @param def
	 *            the default value
	 * @return the value as an int
	 */
	protected int getStringPreferenceAsInteger(SharedPreferences prefs, String key, String def) {
		String strVal;
		int intVal;

		strVal = null;
		try {
			strVal = prefs.getString(key, def);
		} catch (Exception e) {
			strVal = def;
		}

		try {
			intVal = Integer.parseInt(strVal);
		} catch (NumberFormatException e) {
			intVal = 0;
		}

		return intVal;
	}

	/**
	 * Save the Home page settings
	 * 
	 * @param settings
	 *            the settings to write to
	 */
	public void saveHomePage(SharedPreferences settings) {
		Editor editor = settings.edit();
		editor.putString(PREFERENCE_HOME_PAGE_PATH, HOME_PAGE_PATH);
		editor.commit();

	}

	public File getFontFile(Context ctx) {
		return new File(ctx.getDir(FONT_FOLDER_NAME, Context.MODE_PRIVATE), FONT_FILE_NAME);
	}

	public Typeface getTypeface(Context ctx) {
		File fontFile = getFontFile(ctx);
		Typeface res = Typeface.MONOSPACE;
		if (fontFile.exists() && fontFile.canRead()) {
			res = Typeface.createFromFile(getFontFile(ctx));
		}
		return res;
	}
}
