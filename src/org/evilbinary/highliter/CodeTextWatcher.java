package org.evilbinary.highliter;

import org.evilbinary.highliter.parsers.MyTagToSpannedConverter;
import org.evilbinary.highliter.parsers.SyntaxHighlight;
import org.evilbinary.utils.Logger;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
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
			System.out.println(begin + " " + end + " str:" + str);

			if (str != null && !str.equals("")) {
				String result = mHi.pase(str.toString());
				System.out.println("#############@@@@@@@@@@@@@:" + result);

				 Spanned spanText=mConverter.convert(result);
				 if(spanText!=null){
					 System.out.println("#############"+spanText);
					 
					 SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder)mText.getText();
					 spannableStringBuilder.setSpan(spanText, begin, end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//					 spanText.setSpan(new ForegroundColorSpan(Color.GREEN), begin, end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					 
				 }
			}
		}

	}
}
