package org.evilbinary.highliter;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.css.CSSStyleDeclaration;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

public class SpanStyle {
	private List<CharacterStyle> styles;
	private String tag;
	private CSSStyleDeclaration cssStyle;

	public SpanStyle() {
		styles = new ArrayList<CharacterStyle>();
	}

	ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
	ForegroundColorSpan span2 = new ForegroundColorSpan(Color.GREEN);

	public void applyStyle(SpannableStringBuilder text, int start, int end, int flags) {
//		System.out.println("tag:" + tag);
		for (int i = 0; i < styles.size(); i++) {
			text.setSpan(CharacterStyle.wrap(styles.get(i)), start, end, flags);

		}
	}
	public int getSize(){
		return styles.size();
	}
	 

	public void addStyle(CharacterStyle style) {
		styles.add(style);
	}

	public void addStyle(CharacterStyle style, String tag,CSSStyleDeclaration cssStyle) {
		styles.add(style);
		this.tag = tag;
		this.cssStyle=cssStyle;
	}
	public CSSStyleDeclaration getCssStyle(){
		return this.cssStyle;
	}

}
