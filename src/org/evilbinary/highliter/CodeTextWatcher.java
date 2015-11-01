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

package org.evilbinary.highliter;

import org.evilbinary.highliter.parsers.MyTagToSpannedConverter;
import org.evilbinary.highliter.parsers.SyntaxHighlight;
import org.evilbinary.utils.DirUtil;
import org.evilbinary.utils.Logger;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.EditText;

public class CodeTextWatcher implements TextWatcher {

	private SyntaxHighlight mHi;
	private HighlightEditText mText;
	private MyTagToSpannedConverter mConverter;

	public CodeTextWatcher(SyntaxHighlight hi, HighlightEditText text, MyTagToSpannedConverter converter) {
		mHi = hi;
		mText = text;
		mConverter = converter;
	}

	@Override
	public void afterTextChanged(Editable text) {
		// TODO Auto-generated method stub
		Logger.d("afterTextChanged:");
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		Logger.d("beforeTextChanged");

	}

	@Override
	public void onTextChanged(CharSequence sub, int start, int before, int after) {
		// TODO Auto-generated method stub
		Logger.d("onTextChanged");

		int end = mText.getSelectionStart();
		int begin = end;
		System.out.println(begin + "," + end);
		boolean tagOn = false;
		while (begin >= 1) {
			String str = sub.subSequence(begin - 1, begin).toString();
			if (!tagOn && (str.equals(" ") || str.equals("\n"))) {
				break;
			} else if (str.equals("\"")) {
				if (tagOn) {
					begin--;
					tagOn = false;
				}
			}
			begin--;
		}
		if (begin < end) {
			CharSequence str = sub.subSequence(begin, end);
//			System.out.println(begin + " " + end + " str:" + str);
			if (str != null && !str.equals("")) {
				String result = mHi.pase(str.toString());
				System.out.println("@@@@@@@@@@@@@:" + result);
				Spanned spanText = mConverter.convert(result);
				if (spanText != null) {
					System.out.println("#############" + spanText);
					SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder) mText.getText();
					CharacterStyle[] allSpans =spanText.getSpans(0, spanText.length(), CharacterStyle.class);
//					System.out.println("allSpans.length:"+allSpans.length);
					for(CharacterStyle span:allSpans){
						int spanStart = spanText.getSpanStart(span);
				        int spanEnd = spanText.getSpanEnd(span);
				        int flag=spanText.getSpanFlags(span);
				        System.out.println("start:"+spanStart+" end:"+spanEnd);
				        System.out.println("estart:"+begin+spanStart+" eend:"+begin+spanEnd);
						spannableStringBuilder.setSpan(span, begin+spanStart, begin+spanEnd, flag);
					}
			 
 
				}
			}
		}

	}
}
