#include "app.h"

#include <memory>
#include <algorithm>
#include <Diluculum/LuaState.hpp>

#include "main.h"
#include "../include/datadir.h"
#include "syntaxreader.h"

using namespace std;

static HighliterAndroid* highliter = NULL;

JNIEXPORT jint JNICALL Java_org_evilbinary_highliter_parsers_SyntaxHighlight_init(
		JNIEnv *env, jobject obj, jstring path) {

	highliter = new HighliterAndroid();
	jboolean iscopy = JNI_TRUE;
	const char* str = env->GetStringUTFChars(path, &iscopy);
	if (str == NULL) {
		return -1;
	}
	string dataDirPath(str);
	env->ReleaseStringUTFChars((jstring) path, str);
	LOGI("dataDirPath:%s", dataDirPath.c_str());
	highliter->init(dataDirPath);
	LOGI("init highliter:%p", highliter);
}

JNIEXPORT jstring JNICALL Java_org_evilbinary_highliter_parsers_SyntaxHighlight_pase(
		JNIEnv *env, jobject obj, jstring code) {
	LOGI("pase highliter:%p", highliter);

	jboolean iscopy = JNI_TRUE;
	const char* str = env->GetStringUTFChars(code, &iscopy);
	if (str == NULL) {
		return -1;
	}
	string ccontent(str);
	env->ReleaseStringUTFChars((jstring) code, str);

	string out;

	int ret = highliter->parse(ccontent, out);
	char *cstr = out.c_str();
	jstring result = env->NewStringUTF(cstr);

	LOGI("out:%s",cstr);
	return result;

}

HighliterAndroid::HighliterAndroid() {

}
void HighliterAndroid::destroy() {

	if (generator != NULL) {
		delete generator;
	}
}
int HighliterAndroid::init(string dataDirPath) {
	int argc = 5;
	dataDirPath += "/";
	string outflag = "-d" + dataDirPath;
	char *argv[5] = { "hilighlight", "--syntax=c","-f","-smolokai", outflag.c_str() };

	LOGI("init====");
	options.init(argc, argv);

	LOGI("hoptions finish");

	LOGI("initSearchDirectories");
	dataDir.initSearchDirectories(dataDirPath);
	LOGI("loadFileTypeConfig");
	//call before printInstalledLanguages!
	loadFileTypeConfig("filetypes", &extensions, &scriptShebangs);

	if (options.showLangdefs()) {
		return printInstalledLanguages();
	}

	inFileList = options.getInputFileNames();

	if (options.enableBatchMode() && inFileList[0].empty()) {
		return EXIT_FAILURE;
	}
	LOGI("create generator");

	generator = highlight::CodeGenerator::getInstance(options.getOutputType());

	LOGI("themePath");

	string themePath =
			options.getAbsThemePath().empty() ?
					dataDir.getThemePath(options.getThemeName()) :
					options.getAbsThemePath();
	LOGI(
			"themePath empty:%d %s %s", options.getAbsThemePath().empty(), dataDir.getThemePath(options.getThemeName()).c_str(), options.getAbsThemePath().c_str());

	LOGI("generator set finish");

	generator->setHTMLAttachAnchors(options.attachLineAnchors());
	generator->setHTMLOrderedList(options.orderedList());
	generator->setHTMLInlineCSS(options.inlineCSS());
	generator->setHTMLEnclosePreTag(options.enclosePreTag());
	generator->setHTMLAnchorPrefix(options.getAnchorPrefix());
	generator->setHTMLClassName(options.getClassName());

	LOGI("generator set finish2");

	generator->setLATEXReplaceQuotes(options.replaceQuotes());
	generator->setLATEXNoShorthands(options.disableBabelShorthands());
	generator->setLATEXPrettySymbols(options.prettySymbols());

	generator->setRTFPageSize(options.getPageSize());
	generator->setRTFCharStyles(options.includeCharStyles());

	generator->setSVGSize(options.getSVGWidth(), options.getSVGHeight());

	LOGI("generator set finish3");

	if (options.useCRDelimiter())
		generator->setEOLDelimiter('\r');

	generator->setValidateInput(options.validateInput());
	generator->setNumberWrappedLines(options.numberWrappedLines());

	generator->setStyleInputPath(options.getStyleInFilename());
	generator->setStyleOutputPath(options.getStyleOutFilename());
	generator->setIncludeStyle(options.includeStyleDef());
	generator->setPrintLineNumbers(options.printLineNumbers(),
			options.getNumberStart());
	generator->setPrintZeroes(options.fillLineNrZeroes());
	generator->setFragmentCode(options.fragmentOutput());
	generator->setPreformatting(options.getWrappingStyle(),
			(generator->getPrintLineNumbers()) ?
					options.getLineLength() - options.getNumberWidth() :
					options.getLineLength(), options.getNumberSpaces());

	LOGI("generator set finish4");

	generator->setEncoding(options.getEncoding());
	generator->setBaseFont(options.getBaseFont());
	generator->setBaseFontSize(options.getBaseFontSize());
	generator->setLineNumberWidth(options.getNumberWidth());
	generator->setStartingNestedLang(options.getStartNestedLang());
	generator->disableTrailingNL(options.disableTrailingNL());
	generator->setPluginReadFile(options.getPluginReadFilePath());

	LOGI("generator set finish5");

	styleFileWanted = !options.fragmentOutput()
			|| options.styleOutPathDefined();

	const vector<string> pluginFileList = collectPluginPaths(
			options.getPluginPaths());
	for (unsigned int i = 0; i < pluginFileList.size(); i++) {
		if (!generator->initPluginScript(pluginFileList[i])) {
			cerr << "highlight: " << generator->getPluginScriptError() << " in "
					<< pluginFileList[i] << "\n";
			LOGE(
					"erro:%s in %s\n", generator->getPluginScriptError().c_str(), pluginFileList[i].c_str());

			return EXIT_FAILURE;
		}
	}
	LOGI("generator set finish6 themePath:%s", themePath.c_str());

	if (!generator->initTheme(themePath)) {
		cerr << "highlight: " << generator->getThemeInitError() << "\n";
		LOGE("erro:%s", generator->getThemeInitError().c_str());
		return EXIT_FAILURE;
	}
	LOGI("generator set finish7");

	if (options.printOnlyStyle()) {
		if (!options.formatSupportsExtStyle()) {
			cerr << "highlight: output format supports no external styles.\n";
			LOGE("output format supports no external styles.\n");
			return EXIT_FAILURE;
		}
		bool useStdout = options.getStyleOutFilename() == "stdout";
		string cssOutFile = options.getOutDirectory()
				+ options.getStyleOutFilename();
		bool success = generator->printExternalStyle(
				useStdout ? "" : cssOutFile);
		if (!success) {
			cerr << "highlight: Could not write " << cssOutFile << ".\n";
			LOGE("highlight: Could not write %s.\n", cssOutFile.c_str());
			return EXIT_FAILURE;
		}
		return EXIT_SUCCESS;
	}
	LOGI("generator set finish8");

	formattingEnabled = generator->initIndentationScheme(
			options.getIndentScheme());

	LOGI("generator set finish9");

	if (!formattingEnabled && !options.getIndentScheme().empty()) {
		cerr << "highlight: Undefined indentation scheme "
				<< options.getIndentScheme() << ".\n";
		LOGE(
				"Undefined indentation scheme %s.\n", options.getIndentScheme().c_str());

		return EXIT_FAILURE;
	}

	LOGI("init finish");
	LOGI("generator:%p", &generator);

}

int HighliterAndroid::parse(string content, string &out) {
	LOGI("parse generator:%p", &generator);
	string outDirectory = options.getOutDirectory();
	LOGI("outDirectory:%s", outDirectory.c_str());

	bool initError = false, IOError = false;
	unsigned int fileCount = inFileList.size(), fileCountWidth = getNumDigits(
			fileCount), i = 0, numBadFormatting = 0, numBadInput = 0,
			numBadOutput = 0;

	vector<string> badFormattedFiles, badInputFiles, badOutputFiles;
	std::set < string > usedFileNames;
	string inFileName, outFilePath;
	string suffix, lastSuffix;

	if (options.syntaxGiven()) // user defined language definition, valid for all files
	{
		suffix = guessFileType(options.getSyntax(), "", true);
	}
	LOGI("suffix:%s", suffix.c_str());

	if (!options.syntaxGiven()) // determine file type for each file
	{
		suffix = guessFileType(getFileSuffix(inFileList[i]), inFileList[i]);
	}
	if (suffix.empty() && options.forceOutput())
		suffix = "txt"; //avoid segfault
	if (suffix.empty()) {
		if (!options.enableBatchMode()) {
			cerr << "highlight: Undefined language definition. Use --"
					<< OPT_SYNTAX << " option.\n";
			LOGE(
					"highlight: Undefined language definition. Use --%s option.\n", OPT_SYNTAX);
		}
		if (!options.forceOutput()) {
			initError = true;

		}
	}

	if (suffix != lastSuffix) {
		string langDefPath =
				options.getAbsLangPath().empty() ?
						dataDir.getLangPath(suffix + ".lang") :
						options.getAbsLangPath();
		LOGI("langDefPath:%s", langDefPath.c_str());
		LOGI("generator addr:%p", &generator);
		highlight::LoadResult loadRes = generator->loadLanguage(langDefPath);

		if (loadRes == highlight::LOAD_FAILED_REGEX) {
			cerr << "highlight: Regex error ( "
					<< generator->getSyntaxRegexError() << " ) in " << suffix
					<< ".lang\n";
			LOGE(
					"Regex error ( %s ) in %s.lang\n", generator->getSyntaxRegexError().c_str(), suffix.c_str());
			initError = true;

		} else if (loadRes == highlight::LOAD_FAILED_LUA) {
			cerr << "highlight: Lua error ( " << generator->getSyntaxLuaError()
					<< " ) in " << suffix << ".lang\n";
			LOGE(
					"Lua error ( %s ) in %s.lang\n", generator->getSyntaxLuaError().c_str(), suffix.c_str());
			initError = true;

		} else if (loadRes == highlight::LOAD_FAILED) {
			// do also ignore error msg if --syntax parameter should be skipped
			if (!(options.quietMode() || options.isSkippedExt(suffix))) {
				cerr << "highlight: Unknown source file extension \"" << suffix
						<< "\".\n";
				LOGE(
						"Unknown source file extension \"%s\".\n", suffix.c_str());
			}
			if (!options.forceOutput()) {
				initError = true;
			}
		}
		if (options.printDebugInfo() && loadRes == highlight::LOAD_OK) {
			printDebugInfo(generator->getSyntaxReader(), langDefPath);
		}
		lastSuffix = suffix;
	}

	if (options.useFNamesAsAnchors()) {
		generator->setHTMLAnchorPrefix(inFileName);
	}

	generator->setTitle(
			options.getDocumentTitle().empty() ?
					inFileList[i] : options.getDocumentTitle());

	generator->setKeyWordCase(options.getKeywordCase());

	out = generator->generateString(content);
	LOGI("generate out:%s",out.c_str() );

	if (formattingEnabled && !generator->formattingIsPossible()) {
		if (numBadFormatting++ < IO_ERROR_REPORT_LENGTH
				|| options.printDebugInfo()) {
			badFormattedFiles.push_back(outFilePath);
		}
	}
	++i;

	if (i && !options.includeStyleDef() && styleFileWanted
			&& options.formatSupportsExtStyle()) {
		string cssOutFile = outDirectory + options.getStyleOutFilename();
		bool success = generator->printExternalStyle(cssOutFile);
		if (!success) {
			cerr << "highlight: Could not write " << cssOutFile << ".\n";
			LOGE("highlight: Could not write %s.\n", cssOutFile.c_str());
			IOError = true;
		}
	}

	if (i && options.printIndexFile()) {
		bool success = generator->printIndexFile(inFileList, outDirectory);
		if (!success) {
			cerr << "highlight: Could not write index file.\n";
			LOGE("highlight: Could not write index file.\n");
			IOError = true;
		}
	}

	if (numBadInput) {
		printIOErrorReport(numBadInput, badInputFiles, "read input");
		IOError = true;
	}
	if (numBadOutput) {
		printIOErrorReport(numBadOutput, badOutputFiles, "write output");
		IOError = true;
	}
	if (numBadFormatting) {
		printIOErrorReport(numBadFormatting, badFormattedFiles, "reformat");
	}
	return (initError || IOError) ? EXIT_FAILURE : EXIT_SUCCESS;
}

int HighliterAndroid::getNumDigits(int i) {
	int res = 0;
	while (i) {
		i /= 10;
		++res;
	}
	return res;
}

void HighliterAndroid::printProgressBar(int total, int count) {
	if (!total)
		return;
	int p = 100 * count / total;
	int numProgressItems = p / 10;
	cout << "\r[";
	for (int i = 0; i < 10; i++) {
		cout << ((i < numProgressItems) ? "#" : " ");
	}
	cout << "] " << setw(3) << p << "%, " << count << " / " << total << "  "
			<< flush;
	if (p == 100) {
		cout << endl;
	}
}

void HighliterAndroid::printCurrentAction(const string&outfilePath, int total,
		int count, int countWidth) {
	cout << "Writing file " << setw(countWidth) << count << " of " << total
			<< ": " << outfilePath << "\n";
}

void HighliterAndroid::printIOErrorReport(unsigned int numberErrorFiles,
		vector<string> & fileList, const string &action) {
	cerr << "highlight: Could not " << action << " file"
			<< ((numberErrorFiles > 1) ? "s" : "") << ":\n";
	LOGE(
			"highlight: Could not %s file%s:\n", action.c_str(), ((numberErrorFiles > 1) ? "s" : ""));

	copy(fileList.begin(), fileList.end(),
			ostream_iterator < string > (cerr, "\n"));

	if (fileList.size() < numberErrorFiles) {
		cerr << "... [" << (numberErrorFiles - fileList.size()) << " of "
				<< numberErrorFiles << " failures not shown, use --"
				<< OPT_VERBOSE << " switch to print all failures]\n";
		LOGE(
				"... [%d of %d failures not shown, use -- %s switch to print all failures]\n", (numberErrorFiles - fileList.size()), numberErrorFiles, OPT_VERBOSE);

	}
}

string HighliterAndroid::analyzeFile(const string& file) {
	string firstLine;
	if (!file.empty()) {
		ifstream inFile(file.c_str());
		getline(inFile, firstLine);
	} else {
		//  This copies all the data to a new buffer, uses the data to get the
		//  first line, then makes cin use the new buffer that underlies the
		//  stringstream instance
		cin_bufcopy << cin.rdbuf();
		getline(cin_bufcopy, firstLine);
		cin_bufcopy.seekg(0, ios::beg);
		cin.rdbuf(cin_bufcopy.rdbuf());
	}
	StringMap::iterator it;
	boost::xpressive::sregex rex;
	boost::xpressive::smatch what;
	for (it = scriptShebangs.begin(); it != scriptShebangs.end(); it++) {
		rex = boost::xpressive::sregex::compile(it->first);
		if (boost::xpressive::regex_search(firstLine, what, rex))
			return it->second;
	}
	return "";
}

string HighliterAndroid::guessFileType(const string& suffix,
		const string &inputFile, bool useUserSuffix) {
	string lcSuffix = StringTools::change_case(suffix);
	if (extensions.count(lcSuffix)) {
		return extensions[lcSuffix];
	}

	if (!useUserSuffix) {
		string shebang = analyzeFile(inputFile);
		if (!shebang.empty())
			return shebang;
	}
	return lcSuffix;
}
void HighliterAndroid::printDebugInfo(const highlight::SyntaxReader *lang,
		const string & langDefPath) {
	if (!lang)
		return;
	cerr << "\nLoading language definition:\n" << langDefPath;
	cerr << "\n\nDescription: " << lang->getDescription();

	Diluculum::LuaState* luaState = lang->getLuaState();
	if (luaState) {
		cerr << "\n\nLUA GLOBALS:\n";
		Diluculum::LuaValueMap::iterator it;
		Diluculum::LuaValueMap glob = luaState->globals();
		for (it = glob.begin(); it != glob.end(); it++) {
			Diluculum::LuaValue first = it->first;
			Diluculum::LuaValue second = it->second;
			std::cerr << first.asString() << ": ";
			switch (second.type()) {
			case LUA_TSTRING:
				cerr << "string [ " << second.asString() << " ]";
				break;
			case LUA_TNUMBER:
				cerr << "number [ " << second.asNumber() << " ]";
				break;
			case LUA_TBOOLEAN:
				cerr << "boolean [ " << second.asBoolean() << " ]";
				break;
			default:
				cerr << second.typeName();
			}
			cerr << endl;
		}

	}
	/*
	 cerr << "\nREGEX:\n";
	 highlight::RegexElement *re=NULL;
	 for ( unsigned int i=0; i<lang->getRegexElements().size(); i++ )
	 {
	 re = lang->getRegexElements() [i];
	 cerr << "State "<<re->open<<":\t"<<re->rex. <<"\n";
	 }*/
	cerr << "\nKEYWORDS:\n";
	highlight::KeywordMap::iterator it;
	highlight::KeywordMap keys = lang->getKeywords();
	for (it = keys.begin(); it != keys.end(); it++) {
		cerr << " " << it->first << "(" << it->second << ")";
	}
	cerr << "\n\n";
}

void HighliterAndroid::printConfigInfo() {
	cout << "\nConfig file search directories:\n";
	dataDir.printConfigPaths();
	cout << "\nFiletype config file:\n"
			<< dataDir.getFiletypesConfPath("filetypes") << "\n";
	cout << endl;
#ifdef HL_DATA_DIR
	cout << "Compiler directive HL_DATA_DIR = " <<HL_DATA_DIR<< "\n";
#endif
#ifdef HL_CONFIG_DIR
	cout << "Compiler directive HL_CONFIG_DIR = " <<HL_CONFIG_DIR<< "\n";
#endif
	cout << endl;
}

int HighliterAndroid::printInstalledLanguages() {
	vector < string > filePaths;
	string wildcard = "*.lang";
	string directory = dataDir.getLangPath();
	string searchDir = directory + wildcard;

	bool directoryOK = Platform::getDirectoryEntries(filePaths, searchDir,
			true);
	if (!directoryOK) {
		cerr << "highlight: Could not access directory " << searchDir
				<< ", aborted.\n";
		return EXIT_FAILURE;
	}

	sort(filePaths.begin(), filePaths.end());
	string suffix, desc;
	cout << "\nInstalled language definitions" << " (located in " << directory
			<< "):\n\n";

	for (unsigned int i = 0; i < filePaths.size(); i++) {
		Diluculum::LuaState ls;
		highlight::SyntaxReader::initLuaState(ls, filePaths[i], "");
		ls.doFile(filePaths[i]);
		desc = ls["Description"].value().asString();
		suffix = (filePaths[i]).substr(directory.length());
		suffix = suffix.substr(1, suffix.length() - wildcard.length());
		cout << setw(30) << setiosflags(ios::left) << desc << ": " << suffix;
		int extCnt = 0;
		for (StringMap::iterator it = extensions.begin();
				it != extensions.end(); it++) {
			if (it->second == suffix) {
				cout << ((++extCnt == 1) ? " ( " : " ") << it->first;
			}
		}
		cout << ((extCnt) ? " )" : "") << endl;
	}
	cout << "\nUse name of the desired language"
			<< " with the --" OPT_SYNTAX " option.\n" << endl;
	return EXIT_SUCCESS;
}
string HighliterAndroid::getFileSuffix(const string& fileName) {
	size_t ptPos = fileName.rfind(".");
	size_t psPos = fileName.rfind(Platform::pathSeparator);
	if (ptPos == string::npos) {
		return (psPos == string::npos) ?
				fileName : fileName.substr(psPos + 1, fileName.length());
	}
	//return (ptPos == string::npos || (psPos!=string::npos && psPos>ptPos)) ? "" : fileName.substr(ptPos+1, fileName.length());
	return (psPos != string::npos && psPos > ptPos) ?
			"" : fileName.substr(ptPos + 1, fileName.length());

}
vector<string> HighliterAndroid::collectPluginPaths(
		const vector<string>& plugins) {
	vector < string > absolutePaths;
	for (unsigned int i = 0; i < plugins.size(); i++) {
		if (Platform::fileExists(plugins[i])) {
			absolutePaths.push_back(plugins[i]);
		} else {
			absolutePaths.push_back(dataDir.getPluginPath(plugins[i] + ".lua"));
		}
	}
	return absolutePaths;
}

bool HighliterAndroid::loadFileTypeConfig(const string& name, StringMap* extMap,
		StringMap* shebangMap) {
	if (!extMap || !shebangMap)
		return false;

	//  string confPath=dataDir.getConfDir() + name + ".conf";
	string confPath = dataDir.getFiletypesConfPath(name);
	try {
		Diluculum::LuaState ls;
		Diluculum::LuaValueList ret = ls.doFile(confPath);

		int idx = 1;
		string langName;
		Diluculum::LuaValue mapEntry;
		while ((mapEntry = ls["FileMapping"][idx].value()) != Diluculum::Nil) {
			langName = mapEntry["Lang"].asString();
			if (mapEntry["Extensions"] != Diluculum::Nil) {
				int extIdx = 1;
				while (mapEntry["Extensions"][extIdx] != Diluculum::Nil) {
					extMap->insert(
							make_pair(mapEntry["Extensions"][extIdx].asString(),
									langName));
					extIdx++;
				}
			} else if (mapEntry["Shebang"] != Diluculum::Nil) {
				shebangMap->insert(
						make_pair(mapEntry["Shebang"].asString(), langName));
			}
			idx++;
		}

	} catch (Diluculum::LuaError err) {
		cerr << err.what() << "\n";
		return false;
	}
	return true;
}
