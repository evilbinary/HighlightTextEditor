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

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

import org.evilbinary.highliter.spans.SpanStyle;
import org.evilbinary.utils.PxAndDp;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

public class MyTagToSpannedConverter {

    private SpannableStringBuilder mSpannableStringBuilder;

    private HashMap<String, SpanStyle> mStyles;
    private Context mContext;
    private String mSp;

    private String mCurTag;
    private String title;
    private SyntaxHighlight mSyntaxHighlight;

    public MyTagToSpannedConverter(Context context, SyntaxHighlight syntaxHighlight) {
        mSpannableStringBuilder = new SpannableStringBuilder();
        mStyles = new HashMap<String, SpanStyle>();
        mContext = context;
        mSp = System.getProperty("line.separator");
        mSyntaxHighlight = syntaxHighlight;

    }

    public String getLineSeparator() {
        return mSp;
    }

    public void convert(EditText editText) {
        try {

            mSyntaxHighlight.pase(editText.getText().toString());
            List<Token> tokenList = mSyntaxHighlight.getTokenList();
            SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder) editText.getText();

            int start = 0;
            for (Token token : tokenList) {
                int len = token.string.length();
                SpanStyle style = mStyles.get(token.style);
                if (style != null) {
                    style.applyStyle(spannableStringBuilder, start, start + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                start += len;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //加载css
    public void loadCss(String filePath) {
        paseCss(filePath);
    }

    private void paseCss(String filePath) {
        paseCss(new File(filePath));
    }

    private void paseCss(File file) {
        try {
            InputStream is = new FileInputStream(file);
            paseCss(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void paseCss(InputStream is) {
        InputStreamReader ir = new InputStreamReader(is);
        org.w3c.css.sac.InputSource source = new org.w3c.css.sac.InputSource(ir);

        CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
        try {
            CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);
            CSSRuleList cssrules = sheet.getCssRules();
            for (int i = 0; i < cssrules.getLength(); i++) {
                CSSRule rule = cssrules.item(i);
                if (rule instanceof CSSStyleRule) {
                    CSSStyleRule cssrule = (CSSStyleRule) rule;
//					System.out.println("cssrule.getCssText:" + cssrule.getCssText());
//					System.out.println("	cssrule.getSelectorText:" + cssrule.getSelectorText());
                    CSSStyleDeclaration styles = cssrule.getStyle();

                    SpanStyle spanStyles = new SpanStyle();
                    for (int j = 0, n = styles.getLength(); j < n; j++) {
                        String propName = styles.item(j);
                        if ("color".equalsIgnoreCase(propName)) {
                            int color = toColor((CSSPrimitiveValue) styles.getPropertyCSSValue(propName));
                            ForegroundColorSpan span = new ForegroundColorSpan(color);
                            spanStyles.addStyle(span, propName + " rgb:" + color, styles);
                        } else if ("background-color".equalsIgnoreCase(propName)) {
                            int color = toColor((CSSPrimitiveValue) styles.getPropertyCSSValue(propName));
                            BackgroundColorSpan span = new BackgroundColorSpan(color);
                            spanStyles.addStyle(span, propName, styles);
                        } else if ("font-size".equalsIgnoreCase(propName)) {
                            CSSPrimitiveValue val = (CSSPrimitiveValue) styles.getPropertyCSSValue(propName);
                            int sizePx = (int) val.getFloatValue(CSSPrimitiveValue.CSS_NUMBER);
//							System.out.println(" sizePx====:" + sizePx);
                            int sizeDp = PxAndDp.px2dip(this.mContext, sizePx);
                            AbsoluteSizeSpan span = new AbsoluteSizeSpan(sizeDp, true);
                            spanStyles.addStyle(span, propName, styles);
                        }

                    }
//                    System.out.println("style:"+cssrule.getSelectorText().replace("*.",""));

                    mStyles.put(cssrule.getSelectorText().replace("*.","").replace("."," "), spanStyles);
                } else if (rule instanceof CSSImportRule) {
                    CSSImportRule cssrule = (CSSImportRule) rule;
                    System.out.println(cssrule.getHref());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int toColor(org.w3c.dom.css.RGBColor color) {
        int red = (int) color.getRed().getFloatValue(CSSPrimitiveValue.CSS_NUMBER);
        int green = (int) color.getGreen().getFloatValue(CSSPrimitiveValue.CSS_NUMBER);
        int blue = (int) color.getBlue().getFloatValue(CSSPrimitiveValue.CSS_NUMBER);
        int c = Color.rgb(red, green, blue);
        return c;
    }

    private int toColor(CSSPrimitiveValue val) {
        return toColor(val.getRGBColorValue());
    }

    public Integer getBackground() {
        SpanStyle s = mStyles.get("body hl");
        if (s != null) {
            CSSStyleDeclaration style = s.getCssStyle();
            CSSPrimitiveValue val = (CSSPrimitiveValue) style.getPropertyCSSValue("background-color");
            Integer c = toColor(val);
            return c;
        }
        return null;

    }

    public Integer getForeground() {
        SpanStyle s = mStyles.get("pre hl");
        if (s != null) {
            CSSStyleDeclaration style = s.getCssStyle();
            CSSPrimitiveValue val = (CSSPrimitiveValue) style.getPropertyCSSValue("color");
            Integer c = toColor(val);
            return c;
        }
        return null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
