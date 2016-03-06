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

import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;

import org.evilbinary.highliter.parsers.MyTagToSpannedConverter;
import org.evilbinary.highliter.parsers.SyntaxHighlight;
import org.evilbinary.utils.Logger;

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
        // Logger.d("afterTextChanged:");
    }

    @Override
    public void beforeTextChanged(CharSequence sub, int start, int before, int after) {
        // Logger.d("beforeTextChanged");
        Logger.d("onTextChanged");
        int end = mText.getSelectionStart();
        int begin = end;
        boolean tagOn = false;
        Logger.d(start + "," + before + "," + after);

        while (begin >= 1) {
            String str = sub.subSequence(begin - 1, begin).toString();
            if (!tagOn && (str.equals(" ") || str.equals(mConverter.getLineSeparator()))) {
                break;
            } else if (str.equals("\"")) {
                if (tagOn) {
                    begin--;
                    tagOn = false;
                }
            } else if (str.equals("/")) {
                begin--;
                if (begin >= 1) {
                    String s = sub.subSequence(begin - 1, begin).toString();
                    if (s.equals("*")) {
                        tagOn = true;
                    }
                }
            } else if (str.equals("*")) {
                begin--;
                if (begin >= 1) {
                    String s = sub.subSequence(begin - 1, begin).toString();
                    if (s.equals("/")) {
                        tagOn = false;
                    }
                }
            }
            begin--;
        }
//		System.out.println("begin-end2="+begin + "," + end);

        if (begin >= 0 && begin < end) {
            CharSequence str = sub.subSequence(begin, end);
//			 System.out.println(begin + " " + end + " str:" + str);
            if (str != null && !str.equals("")) {
                String result = mHi.pase(str.toString());
//				 System.out.println("@@@@@@@@@@@@@:" + result);
                Spanned spanText = mConverter.convert(result);
                mText.render(spanText, begin);
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence sub, int start, int before, int after) {


    }
}
