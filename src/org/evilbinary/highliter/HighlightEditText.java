/* Copyright (C) 2015 evilbinary.
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
import org.evilbinary.utils.FileUtil;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

public class HighlightEditText extends EditText {

	public HighlightEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public void setHtml(String source) {
		try {
			MyTagToSpannedConverter converter = new MyTagToSpannedConverter(this.getContext(), source);
			Spanned spanText = converter.convert();
			if(converter.getForeground()!=null)
				this.setTextColor(converter.getForeground());
			if(converter.getBackground()!=null)
				this.setBackgroundColor(converter.getBackground());
			this.setText(spanText);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
