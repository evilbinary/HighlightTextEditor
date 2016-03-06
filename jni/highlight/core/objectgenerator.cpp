/***************************************************************************
                     objectgenerator.cpp  -  description
                             -------------------
    begin                : Wed Nov 28 2015
    copyright            : (C) 20015-2020 by evilbianry
    email                : rootdebug@163.com
 ***************************************************************************/


#include <fstream>
#include <iostream>
#include <sstream>
#include <algorithm>

#include "ObjectGenerator.h"
#include "version.h"

#if ANDROID
#include <android/log.h>
#ifndef TAG_NAME
    #define TAG_NAME "highliter"
#endif
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, TAG_NAME, __VA_ARGS__))
#define LOGW(...) ((void)__android_log_print(ANDROID_LOG_WARN, TAG_NAME, __VA_ARGS__))
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, TAG_NAME, __VA_ARGS__))
#define LOGV(...) ((void)__android_log_print(ANDROID_LOG_VERBOSE, TAG_NAME, __VA_ARGS__))
#else
#define LOGI(...)
#define LOGW(...)
#define LOGE(...)
#endif

using namespace std;

namespace highlight {

    ObjectGenerator::ObjectGenerator() :
            CodeGenerator(OBJECT),
            brTag("<br>"),
            hrTag("<hr>"),
            idAttr("name"),
            fileSuffix(".obj"),
            cssClassName("hl"),
            orderedList(false),
            useInlineCSS(false),
            enclosePreTag(false),
            attachAnchors(false),
            anchorPrefix("l") {
        spacer = " ";
        styleCommentOpen = "/*";
        styleCommentClose = "*/";

        LOGI("ObjectGenerator");

    }


    string ObjectGenerator::getHeaderStart(const string &title) {
        //LOGI("getHeaderStart");

        ostringstream header;
        header << "<!DOCTYPE html>\n<html>\n<head>\n";
        if (encodingDefined()) {
            header << "<meta charset=\""
            << encoding
            << "\">\n";
        }
        header << "<title>" << title << "</title>\n";
        return header.str();
    }

    string ObjectGenerator::getHeader() {
        LOGI("getHeader");
        ostringstream os;
        os << getHeaderStart(docTitle);
        if (!useInlineCSS) {
            if (includeStyleDef) {
                os << "<style type=\"text/css\">\n";
                os << getStyleDefinition();
                os << CodeGenerator::readUserStyleDef();
                os << "</style>\n";
            }
            else {
                os << "<link rel=\"stylesheet\" type=\"text/css\" href=\""
                << getStyleOutputPath()
                << "\">\n";
            }
            os << "</head>\n<body";
            if (!cssClassName.empty())
                os << " class=\"" << cssClassName << "\"";
            os << ">\n";
        }
        else {
            os << "</head>\n<body style=\""
            << "background-color:#"
            << (docStyle.getBgColour().getRed(HTML))
            << (docStyle.getBgColour().getGreen(HTML))
            << (docStyle.getBgColour().getBlue(HTML))
            << "\">\n";
        }

        return os.str();
    }

    string ObjectGenerator::getFooter() {
        LOGI("getFooter");
        return getGeneratorComment();
    }

    void ObjectGenerator::printBody() {
        LOGI("printBody");

        if ((!(showLineNumbers && orderedList) && !fragmentOutput) || enclosePreTag) {
            if (!useInlineCSS) {
                *out << "<pre";
                if (!cssClassName.empty())
                    *out << " class=\"" << cssClassName << "\"";
                *out << ">";
            }
            else {
                bool quoteFont = getBaseFont().find_first_of(",'") == string::npos;
                *out << "<pre style=\""
                << "color:#"
                << (docStyle.getDefaultStyle().getColour().getRed(HTML))
                << (docStyle.getDefaultStyle().getColour().getGreen(HTML))
                << (docStyle.getDefaultStyle().getColour().getBlue(HTML))
                << "; background-color:#"
                << (docStyle.getBgColour().getRed(HTML))
                << (docStyle.getBgColour().getGreen(HTML))
                << (docStyle.getBgColour().getBlue(HTML))
                << "; font-size:" << this->getBaseFontSize()
                << "pt; font-family:"
                << ((quoteFont) ? "'" : "")
                << this->getBaseFont()
                << ((quoteFont) ? "'" : "")
                << ";\">";
            }
        }
        if (showLineNumbers && orderedList) *out << "<ol>\n";

        processRootState();

        if (showLineNumbers && orderedList) *out << "</ol>";

        if ((!(showLineNumbers && orderedList) && !fragmentOutput) || enclosePreTag)
            *out << "</pre>";
    }


    void ObjectGenerator::initOutputTags() {
        //LOGI("initOutputTags");

        openTags.push_back("");
        if (useInlineCSS) {
            openTags.push_back(getOpenTag(docStyle.getStringStyle()));
            openTags.push_back(getOpenTag(docStyle.getNumberStyle()));
            openTags.push_back(getOpenTag(docStyle.getSingleLineCommentStyle()));
            openTags.push_back(getOpenTag(docStyle.getCommentStyle()));
            openTags.push_back(getOpenTag(docStyle.getEscapeCharStyle()));
            openTags.push_back(getOpenTag(docStyle.getPreProcessorStyle()));
            openTags.push_back(getOpenTag(docStyle.getPreProcStringStyle()));
            openTags.push_back(getOpenTag(docStyle.getLineStyle()));
            openTags.push_back(getOpenTag(docStyle.getOperatorStyle()));
            openTags.push_back(getOpenTag(docStyle.getInterpolationStyle()));
        }
        else {
            openTags.push_back(getOpenTag(STY_NAME_STR));
            openTags.push_back(getOpenTag(STY_NAME_NUM));
            openTags.push_back(getOpenTag(STY_NAME_SLC));
            openTags.push_back(getOpenTag(STY_NAME_COM));
            openTags.push_back(getOpenTag(STY_NAME_ESC));
            openTags.push_back(getOpenTag(STY_NAME_DIR));
            openTags.push_back(getOpenTag(STY_NAME_DST));
            openTags.push_back(getOpenTag(STY_NAME_LIN));
            openTags.push_back(getOpenTag(STY_NAME_SYM));
            openTags.push_back(getOpenTag(STY_NAME_IPL));
        }

        closeTags.push_back("");
        for (unsigned int i = 1; i < NUMBER_BUILTIN_STATES; i++) {
            closeTags.push_back("</span>");
        }

    }

    string  ObjectGenerator::getAttributes(const string &elemName, const ElementStyle &elem) {

        //LOGI("getAttributes");

        ostringstream s;
        if (!elemName.empty()) {
            if (!cssClassName.empty())
                s << "." << cssClassName;
            s << "." << elemName << " { ";
        }
        s << "color:#"
        << (elem.getColour().getRed(HTML))
        << (elem.getColour().getGreen(HTML))
        << (elem.getColour().getBlue(HTML))
        << (elem.isBold() ? "; font-weight:bold" : "")
        << (elem.isItalic() ? "; font-style:italic" : "")
        << (elem.isUnderline() ? "; text-decoration:underline" : "");
        if (!elemName.empty()) {
            s << "; }\n";
        }
        return s.str();
    }

    string  ObjectGenerator::getOpenTag(const string &styleName) {
        //LOGI("getOpenTag:%s", styleName.c_str());
        return "" + (cssClassName.empty() ? "" : cssClassName + " ") + styleName +
               "";
    }

    string  ObjectGenerator::getOpenTag(const ElementStyle &elem) {
        //LOGI("getOpenTag2:%s", getAttributes("", elem).c_str());
        return "" + getAttributes("", elem) + "";
    }

    string ObjectGenerator::getGeneratorComment() {
        LOGI("getGeneratorComment");

        ostringstream s;
        s << "\n</body>\n</html>\n<!--HTML generated by highlight "
        << HIGHLIGHT_VERSION << ", " << HIGHLIGHT_URL << "-->\n";

        return s.str();
    }

    string ObjectGenerator::getStyleDefinition() {
        //LOGI("getStyleDefinition");

        if (styleDefinitionCache.empty()) {
            bool quoteFont = getBaseFont().find_first_of(",'") == string::npos;
            ostringstream os;
            os << "body";
            if (!cssClassName.empty())
                os << "." << cssClassName;
            os << "\t{ background-color:#"
            << (docStyle.getBgColour().getRed(HTML))
            << (docStyle.getBgColour().getGreen(HTML))
            << (docStyle.getBgColour().getBlue(HTML))
            << "; }\n";
            os << (orderedList ? "li" : "pre");
            if (!cssClassName.empty())
                os << "." << cssClassName;
            os << "\t{ color:#"
            << (docStyle.getDefaultStyle().getColour().getRed(HTML))
            << (docStyle.getDefaultStyle().getColour().getGreen(HTML))
            << (docStyle.getDefaultStyle().getColour().getBlue(HTML))
            << "; background-color:#"
            << (docStyle.getBgColour().getRed(HTML))
            << (docStyle.getBgColour().getGreen(HTML))
            << (docStyle.getBgColour().getBlue(HTML))
            << "; font-size:" << this->getBaseFontSize();

            os << "pt; font-family:" << ((quoteFont) ? "'" : "") << getBaseFont() <<
            ((quoteFont) ? "'" : "")
            << ";}\n";

            os << getAttributes(STY_NAME_NUM, docStyle.getNumberStyle())
            << getAttributes(STY_NAME_ESC, docStyle.getEscapeCharStyle())
            << getAttributes(STY_NAME_STR, docStyle.getStringStyle())
            << getAttributes(STY_NAME_DST, docStyle.getPreProcStringStyle())
            << getAttributes(STY_NAME_SLC, docStyle.getSingleLineCommentStyle())
            << getAttributes(STY_NAME_COM, docStyle.getCommentStyle())
            << getAttributes(STY_NAME_DIR, docStyle.getPreProcessorStyle())
            << getAttributes(STY_NAME_SYM, docStyle.getOperatorStyle())
            << getAttributes(STY_NAME_IPL, docStyle.getInterpolationStyle())
            << getAttributes(STY_NAME_LIN, docStyle.getLineStyle());

            KeywordStyles styles = docStyle.getKeywordStyles();
            for (KSIterator it = styles.begin(); it != styles.end(); it++) {
                os << getAttributes(it->first, it->second);
            }
            styleDefinitionCache = os.str();
        }
        return styleDefinitionCache;
    }

    string ObjectGenerator::maskCharacter(unsigned char c) {
        //LOGI("maskCharacter");
        return string(1, c);
    }

    string ObjectGenerator::getNewLine() {
        //LOGI("getNewLine");

        string nlStr;
        if (showLineNumbers && orderedList) nlStr += "</li>";
        if (printNewLines) nlStr += "\n";
        return nlStr;
    }

    void ObjectGenerator::insertLineNumber(bool insertNewLine) {
        //LOGI("insertLineNumber");

        if (insertNewLine) {
            wsBuffer += getNewLine();
        }
        if (showLineNumbers) {
            ostringstream numberPrefix;
            int lineNo = lineNumber + lineNumberOffset;
            if (orderedList) {
                if (useInlineCSS) {
                    bool quoteFont = getBaseFont().find_first_of(",'") == string::npos;
                    numberPrefix << "<li style=\""
                    << getAttributes("", docStyle.getLineStyle())
                    << "; font-size:" << this->getBaseFontSize()
                    << "pt; font-family:" << ((quoteFont) ? "'" : "")
                    << getBaseFont() << ((quoteFont) ? "'" : "")
                    << ";\">";
                }
                else {
                    if (!cssClassName.empty())
                        numberPrefix << "<li class=\"" << cssClassName << "\">";
                    else
                        numberPrefix << "<li>";
                }
                // Opera 8 ignores empty list items -> add &nbsp;
                //if ( line.empty() ) numberPrefix<<"&nbsp;";
            }

            //attach Anchor only if we're in a new line.
            if (attachAnchors && numberCurrentLine) {
                numberPrefix << "<a "
                << idAttr
                << "=\""
                << anchorPrefix
                << "_"
                << lineNo
                << "\"></a>";
            }

            if (!orderedList) {
                //if we're in a wrapped line, don't fill with zeroes.
                ostringstream os;
                if (lineNumberFillZeroes && numberCurrentLine) {
                    os.fill('0');
                }

                os << setw(getLineNumberWidth()) << right;
                //if we're in a wrapped line, don't attach lineNo.
                if (numberCurrentLine) {
                    os << lineNo;
                } else {
                    os << "";
                }

                numberPrefix << openTags[LINENUMBER]
                << os.str()
                << spacer
                << closeTags[LINENUMBER];
            }
            wsBuffer += numberPrefix.str();
        }

    }


    bool ObjectGenerator::printIndexFile(const vector <string> &fileList,
                                         const string &outPath) {
        string suffix = fileSuffix;
        string outFilePath = outPath + "index" + suffix;
        ofstream indexfile(outFilePath.c_str());

        if (!indexfile.fail()) {
            string inFileName;
            string inFilePath, newInFilePath;
            std::set <string> usedFileNames;
            indexfile << getHeaderStart("Source Index");
            indexfile << "</head>\n<body>\n<h1> Source Index</h1>\n"
            << hrTag
            << "\n<ul>\n";
            string::size_type pos;
            for (unsigned int i = 0; i < fileList.size(); i++) {
                pos = (fileList[i]).find_last_of(Platform::pathSeparator);
                if (pos != string::npos) {
                    newInFilePath = (fileList[i]).substr(0, pos + 1);
                }
                else {
                    newInFilePath = Platform::pathSeparator;
                }
                if (newInFilePath != inFilePath) {
                    indexfile << "</ul>\n<h2>";
                    indexfile << newInFilePath;
                    indexfile << "</h2>\n<ul>\n";
                    inFilePath = newInFilePath;
                }
                inFileName = (fileList[i]).substr(pos + 1);

                if (usedFileNames.count(inFileName)) {
                    string prefix = fileList[i].substr(0, pos + 1);
                    replace(prefix.begin(), prefix.end(), Platform::pathSeparator, '_');
                    inFileName.insert(0, prefix);
                } else {
                    usedFileNames.insert(inFileName);
                }

                indexfile << "<li><a href=\"" << inFileName << suffix << "\">";
                indexfile << inFileName << suffix << "</a></li>\n";
            }

            indexfile << "</ul>\n"
            << hrTag << brTag
            << "<small>Generated by highlight "
            << HIGHLIGHT_VERSION
            << ", <a href=\"" << HIGHLIGHT_URL << "\" target=\"new\">"
            << HIGHLIGHT_URL << "</a></small>";
            indexfile << getGeneratorComment();
        }
        else {
            return false;
        }
        return true;
    }


    string ObjectGenerator::getKeywordOpenTag(unsigned int styleID) {

        //LOGI("getKeywordOpenTag:%d", styleID);

        if (useInlineCSS) {
            return getOpenTag(
                    docStyle.getKeywordStyle(currentSyntax->getKeywordClasses()[styleID]));
        }
        return getOpenTag(currentSyntax->getKeywordClasses()[styleID]);
    }

    string ObjectGenerator::getKeywordCloseTag(unsigned int styleID) {
        //LOGI("getKeywordCloseTag:%d", styleID);
        return "";
    }

    void ObjectGenerator::setHTMLOrderedList(bool b) {
        //LOGI("setHTMLOrderedList");

        orderedList = b;
        spacer = b ? "&nbsp;" : " ";
        maskWs = b;

        if (b && !preFormatter.getReplaceTabs()) {
            preFormatter.setReplaceTabs(true);
            preFormatter.setNumberSpaces(4);
        }
    }

}
