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

package org.evilbinary.highliter.spans;

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

	public void applyStyle(SpannableStringBuilder text, int start, int end, int flags) {
//		 System.out.println("styles size:" + styles.size());
		for (int i = 0; i < styles.size(); i++) {
 			text.setSpan(CharacterStyle.wrap(styles.get(i)), start, end, flags);
// 			text.setSpan(new ForegroundColorSpan(Color.GREEN), start, end, flags);
		}
	}

	public int getSize() {
		return styles.size();
	}

	public void addStyle(CharacterStyle style) {
		styles.add(style);
	}

	public void addStyle(CharacterStyle style, String tag, CSSStyleDeclaration cssStyle) {
		styles.add(style);
		this.tag = tag;
		this.cssStyle = cssStyle;
	}

	public CSSStyleDeclaration getCssStyle() {
		return this.cssStyle;
	}

}
