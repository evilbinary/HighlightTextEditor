package org.evilbinary.highliter;

import java.io.File;

import android.os.Environment;

/**
 * @author x.gouchet
 * 
 */
public interface Constants {

	/** Tag for the log ( = {@value} ) */
	public static final String TAG = "TED";

	public static final String ACTION_WIDGET_OPEN = "org.evilbinary.ACTION_TED_WIDGET_OPEN";

	/** Fonts folder */
	public static final String FONT_FOLDER_NAME = "fonts";
	/** Font file */
	public static final String FONT_FILE_NAME = "ted_font.ttf";

	/** clears the current text ( = {@value} ) */
	public static final int MENU_ID_NEW = 0;
	/** Saves the file ( = {@value} ) */
	public static final int MENU_ID_SAVE = 1;
	/** Savesas a new file ( = {@value} ) */
	public static final int MENU_ID_SAVE_AS = 2;
	/** open an existing file */
	public static final int MENU_ID_OPEN = 3;
	/** open an existing file ( = {@value} ) */
	public static final int MENU_ID_OPEN_RECENT = 4;
	/** open the settings ( = {@value} ) */
	public static final int MENU_ID_SETTINGS = 5;
	/** open the about page ( = {@value} ) */
	public static final int MENU_ID_ABOUT = 6;
	/** Search a string in the file ( = {@value} ) */
	public static final int MENU_ID_SEARCH = 7;
	/** undo last change ( = {@value} ) */
	public static final int MENU_ID_UNDO = 8;
	/** redo last change ( = {@value} ) */
	public static final int MENU_ID_REDO = 9;
	/** Quit the app ( = {@value} ) */
	public static final int MENU_ID_QUIT = 666;

	/** File of the external storage data */
	public static final File STORAGE = Environment
			.getExternalStorageDirectory();
	/** Path to the external storage data */
	public static final String STORAGE_PATH = STORAGE.getAbsolutePath();
	/** name of the backup file */
	public static final String BACKUP_FILE_NAME = "temp.bak";

	/** name of the shared preferences for this app ( = {@value} ) */
	public static final String PREFERENCES_NAME = "fr.xgouchet.texteditor";
	/** Preference tag to retrieve the recent files ( = {@value} ) */
	public static final String PREFERENCE_RECENTS = "recent_files";
	/** Preference tag to retrieve the recent files ( = {@value} ) */
	public static final String PREFERENCE_MAX_RECENTS = "max_recent_files";
	/** Preference tag to retrieve the show line number ( = {@value} ) */
	public static final String PREFERENCE_SHOW_LINE_NUMBERS = "show_line_numbers";
	/** Preference tag to retrieve the wordwrap ( = {@value} ) */
	public static final String PREFERENCE_WORDWRAP = "auto_break_lines";
	/** Preference tag to retrieve the searchwrap ( = {@value} ) */
	public static final String PREFERENCE_SEARCHWRAP = "search_wrap";
	/** Preference tag to retrieve the search match case ( = {@value} ) */
	public static final String PREFERENCE_SEARCH_MATCH_CASE = "search_match_case";
	/** Preference tag to retrieve the Syntax Highlight ( = {@value} ) */
	public static final String PREFERENCE_HIGHLIGHT = "highlight_syntax";
	/** Preference tag to retrieve the Text Size ( = {@value} ) */
	public static final String PREFERENCE_TEXT_SIZE = "text_size";
	/** Preference tag to retrieve the End of lines pref ( = {@value} ) */
	public static final String PREFERENCE_END_OF_LINES = "end_of_lines";
	/** Preference tag to retrieve the Encoding pref ( = {@value} ) */
	public static final String PREFERENCE_ENCODING = "encoding";
	/** Preference tag to retrieve the Auto mode ( = {@value} ) */
	public static final String PREFERENCE_AUTO_SAVE = "force_auto_save";
	/** Preference tag to retrieve the Auto mode ( = {@value} ) */
	public static final String PREFERENCE_AUTO_SAVE_OVERWRITE = "auto_save_overwrite";
	/** Preference tag to retrieve the Color Theme ( = {@value} ) */
	public static final String PREFERENCE_COLOR_THEME = "color_theme";
	/** Preference tag to retrieve the fling to scroll ( = {@value} ) */
	public static final String PREFERENCE_FLING_TO_SCROLL = "fling_to_scroll";
	/** Preference tag to retrieve if undo history is allowed ( = {@value} ) */
	public static final String PREFERENCE_ALLOW_UNDO = "allow_undo";
	/** Preference tag to retrieve the max undo stack ( = {@value} ) */
	public static final String PREFERENCE_MAX_UNDO_STACK = "max_undo_stack";
	/** Preference tag to retrieve the back button as undo ( = {@value} ) */
	public static final String PREFERENCE_BACK_BUTTON_AS_UNDO = "back_button_as_undo";
	/** Preference tag to retrieve the use home page ( = {@value} ) */
	public static final String PREFERENCE_USE_HOME_PAGE = "use_home_page";
	/** Preference tag to retrieve the home page path ( = {@value} ) */
	public static final String PREFERENCE_HOME_PAGE_PATH = "home_page_path";
	/** Preference tag to select the font ( = {@value} ) */
	public static final String PREFERENCE_SELECT_FONT = "select_font";
	/** Preference tag to the text font ( = {@value} ) */
	public static final String PREFERENCE_FONT = "text_font";

	/** minimum text size */
	public static final int TEXT_SIZE_MIN = 9;
	/** maximum text size */
	public static final int TEXT_SIZE_MAX = 40;

	/** End of line setting for Linux files ( = {@value} ) */
	public static final int EOL_LINUX = 0;
	/** End of line setting for Windows files ( = {@value} ) */
	public static final int EOL_WINDOWS = 1;
	/** End of line setting for Mac files ( = {@value} ) */
	public static final int EOL_MAC = 2;

	/** Encoding : 7 bit ASCII ( = {@value} ) */
	public static final String ENC_ASCII = "US-ASCII";
	/** Encoding : ISO Latin Alphabet, aka ISO-LATIN-1 ( = {@value} ) */
	public static final String ENC_LATIN = "ISO-8859-1";
	/** Encoding : 8 bit UCS Transformation Format ( = {@value} ) */
	public static final String ENC_UTF8 = "UTF-8";

	/** color theme default : black on white ( = {@value} ) */
	public static final int COLOR_CLASSIC = 0;
	/** color theme negative : white on black ( = {@value} ) */
	public static final int COLOR_NEGATIVE = 1;
	/** color theme matrix : green on dark green ( = {@value} ) */
	public static final int COLOR_MATRIX = 2;
	/** color theme sky : darkblue on skyblue ( = {@value} ) */
	public static final int COLOR_SKY = 3;
	/** color theme dracula : red on black ( = {@value} ) */
	public static final int COLOR_DRACULA = 4;

	/** Request code for Save As Activity */
	public static final int REQUEST_SAVE_AS = 107;
	/** Request code for Open Activity */
	public static final int REQUEST_OPEN = 108;
	/** Request code for Home Page Activity */
	public static final int REQUEST_HOME_PAGE = 109;
	/** Request code for Font Activity */
	public static final int REQUEST_FONT = 110;

	/** extra when browsing for file */
	public static final String EXTRA_REQUEST_CODE = "request_code";
	/** Extra to force read only from widget */
	public static final String EXTRA_FORCE_READ_ONLY = "force_read_only";

	/** an error result */
	public static final int RESULT_ERROR = 1;
}
