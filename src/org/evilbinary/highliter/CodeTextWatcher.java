package org.evilbinary.highliter;

import org.evilbinary.highliter.parsers.MyTagToSpannedConverter;
import org.evilbinary.highliter.parsers.SyntaxHighlight;
import org.evilbinary.utils.Logger;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;

public class CodeTextWatcher implements TextWatcher {

	private SyntaxHighlight mHi;
	private EditText mText;
	private StringBuilder mString;
	private MyTagToSpannedConverter mConverter;
	
	public CodeTextWatcher(SyntaxHighlight hi,EditText text,MyTagToSpannedConverter converter){
		mHi=hi;
		mText=text;
		mString=new StringBuilder();
		mConverter=converter;
	}

	@Override
	public void afterTextChanged(Editable text) {
		// TODO Auto-generated method stub
		Logger.d("afterTextChanged:");
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		Logger.d("beforeTextChanged");

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, 
            int after) {
		// TODO Auto-generated method stub
		CharSequence sub=s.subSequence(start, start + after);

		Logger.d("onTextChanged:"+sub);
		if(sub!=null){
			mString.append(sub);
			System.out.println("#############@@@@@@@@@@@@@:"+mString);
			Spanned text=mConverter.convert(mString.toString());
			if(text!=null){
				
				System.out.println("#############"+text);
				SpannableStringBuilder spanText = (SpannableStringBuilder) mText.getText();
				spanText.setSpan(text, start, mString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//				mString.setLength(0);
				mString=new StringBuilder();
			}
		}

	}

}
