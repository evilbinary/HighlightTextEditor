package org.evilbinary.highliter.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;

import org.ccil.cowan.tagsoup.Parser;
import org.evilbinary.highliter.spans.SpanStyle;
import org.evilbinary.utils.PxAndDp;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

public class MyTagToSpannedConverter implements ContentHandler {

	private SpannableStringBuilder mSpannableStringBuilder;
	private XMLReader mReader;

	private HashMap<String, SpanStyle> mStyles;
	private Context mContext;
	private String mSp;

	public static String TAG_TITLE = "title";
	public static String TAG_BODY = "body";
	public static String TAG_PRE = "pre";
	public static String TAG_SPAN = "span";
	private String mCurTag;
	private String title;

	public MyTagToSpannedConverter(Context context) {
		mSpannableStringBuilder = new SpannableStringBuilder();
		mStyles = new HashMap<String, SpanStyle>();
		mContext = context;
		try {
			mReader = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
			mReader.setFeature(Parser.bogonsEmptyFeature, true);
			mReader.setFeature(Parser.ignorableWhitespaceFeature, true);
			// mReader =
			// XMLReaderFactory.createXMLReader("hotsax.html.sax.SaxParser");

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Spanned convert(String source) {
		InputSource is = new InputSource(new StringReader(source));
		try {
			mSpannableStringBuilder.clear();
			mReader.setContentHandler(this);
			mReader.parse(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mSpannableStringBuilder;
	}

	@Override
	public void startDocument() throws SAXException {

	}

	@Override
	public void endDocument() throws SAXException {

	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// System.out.println("-------->startElement() is invoked...");
		// System.out.println("	uri的属性值：" + uri);
		// System.out.println("	localName的属性值：" + localName);
		// System.out.println("	qName的属性值：" + qName);
		// if (attributes.getLength() > 0) {
		// System.out.println("	element属性值-->" + attributes.getQName(0) + "：" +
		// attributes.getValue(0)); //
		// 根据下标获取属性name和value，也可以直接传递属性name获取属性值：attributes.getValue("id")
		// }
		handleStartTag(localName, attributes);
	}

	private void handleStartTag(String tag, Attributes attributes) {
		mCurTag = tag;
		if (tag.equalsIgnoreCase("span")) {
			if (attributes.getLength() > 0) {
				String classValue = attributes.getValue("class");
				// System.out.println("############classValue:"+classValue);
				String[] classNames = classValue.split(" ");
				String name = "*";
				for (String s : classNames) {
					name += "." + s;
				}
				System.out.println("======name:"+name);
				SpanStyle style = mStyles.get(name);
				System.out.println("style=====:"+style);
				start(mSpannableStringBuilder, style);
			}

		} else if (tag.equalsIgnoreCase("pre")) {
			if (attributes.getLength() > 0) {
				String classValue = attributes.getValue("class");
				String name = "pre." + classValue;
				SpanStyle style = mStyles.get(name);
				// System.out.println("======name:" + name + " size:" +
				// style.getSize());
				// start(mSpannableStringBuilder, style);
			}
		} else if (tag.equalsIgnoreCase("title")) {
			// start(mSpannableStringBuilder, new BulletSpan());
		} else if (tag.equalsIgnoreCase("head")) {
			// start(mSpannableStringBuilder, new BulletSpan());
		} else if (tag.equalsIgnoreCase(this.TAG_BODY)) {
			// System.out.println("body start====================");
		} else if (tag.equalsIgnoreCase("link")) {
			if (attributes.getLength() > 0) {
				String cssFileName = attributes.getValue("href");
				System.out.println("pase css file:" + cssFileName);
				InputStream is = getClass().getResourceAsStream("/assets/" + cssFileName);
				paseCss(is);
			}
		}
	}

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
			// TODO Auto-generated catch block
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
					System.out.println("cssrule.getCssText:" + cssrule.getCssText());
					System.out.println("	cssrule.getSelectorText:" + cssrule.getSelectorText());
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
							System.out.println(" sizePx====:" + sizePx);
							int sizeDp = PxAndDp.px2dip(this.mContext, sizePx);
							AbsoluteSizeSpan span = new AbsoluteSizeSpan(sizeDp);
							spanStyles.addStyle(span, propName, styles);
						}

					}
					mStyles.put(cssrule.getSelectorText(), spanStyles);
				} else if (rule instanceof CSSImportRule) {
					CSSImportRule cssrule = (CSSImportRule) rule;
					System.out.println(cssrule.getHref());
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		SpanStyle s = mStyles.get("body.hl");
		if (s != null) {
			CSSStyleDeclaration style = s.getCssStyle();
			CSSPrimitiveValue val = (CSSPrimitiveValue) style.getPropertyCSSValue("background-color");
			Integer c = toColor(val);
			return c;
		}
		return null;

	}

	public Integer getForeground() {
		SpanStyle s = mStyles.get("pre.hl");
		if (s != null) {
			CSSStyleDeclaration style = s.getCssStyle();
			CSSPrimitiveValue val = (CSSPrimitiveValue) style.getPropertyCSSValue("color");
			Integer c = toColor(val);
			return c;
		}
		return null;
	}

	private void handleEndTag(String tag) {
		if (tag.equalsIgnoreCase("span")) {
			// System.out.println("span end===========================:" + tag);
			end(mSpannableStringBuilder, SpanStyle.class);
		} else if (tag.equalsIgnoreCase("pre")) {
			// System.out.println("span end===========================:" + tag);
			// end(mSpannableStringBuilder, SpanStyle.class);
		} else if (tag.equalsIgnoreCase("br")) {
			// System.out.println("span br===========================:" + tag);
			mSpannableStringBuilder.append(mSp);
		} else if (tag.equalsIgnoreCase("title")) {
			// end(mSpannableStringBuilder, BulletSpan.class);
		} else if (tag.equalsIgnoreCase("head")) {
			// end(mSpannableStringBuilder, BulletSpan.class);
		} else if (tag.equalsIgnoreCase(this.TAG_BODY)) {
			// System.out.println("body end====================");
		}
	}

	private static void start(SpannableStringBuilder text, Object mark) {
		int len = text.length();
		// System.out.println("start setSpan:" + len + " text:"+text);
		text.setSpan(mark, len, len, Spannable.SPAN_MARK_MARK);
	}

	private static void end(SpannableStringBuilder text, Class kind) {
		int len = text.length();
		Object obj = getLast(text, kind);
		int where = text.getSpanStart(obj);
		text.removeSpan(obj);
		// System.out.println(" where:" + where + " len:" + len);
		if (where != len) {
			// System.out.println("obj:" + obj);
			if (kind == SpanStyle.class) {
//				System.out.println("kind of spanStyle.class");
				SpanStyle spanStyle = (SpanStyle) obj;
				spanStyle.applyStyle(text, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else {
				text.clear();
			}
		}

		return;
	}

	private static Object getLast(Spanned text, Class kind) {
		/*
		 * This knows that the last returned object from getSpans() will be the
		 * most recently added.
		 */
		Object[] objs = text.getSpans(0, text.length(), kind);

		if (objs.length == 0) {
			return null;
		} else {
			return objs[objs.length - 1];
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// System.out.println("-------->endElement() is invoked...");
		// System.out.println("uri的属性值：" + uri);
		// System.out.println("localName的属性值：" + localName);
		// System.out.println("qName的属性值：" + qName);
		handleEndTag(localName);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// System.out.println("节点元素文本内容:" + new String(ch, start, length));
		if (TAG_SPAN.equals(this.mCurTag)) {
			mSpannableStringBuilder.append(new String(ch, start, length));
		} else if (TAG_TITLE.equals(this.mCurTag)) {
			title = new String(ch, start, length);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void endPrefixMapping(String arg0) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processingInstruction(String arg0, String arg1) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDocumentLocator(Locator arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void skippedEntity(String arg0) throws SAXException {
		// TODO Auto-generated method stub
	}

	@Override
	public void startPrefixMapping(String arg0, String arg1) throws SAXException {
		// TODO Auto-generated method stub

	}

}
