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
import org.evilbinary.managers.Configure;
import org.evilbinary.managers.Settings;
import org.evilbinary.utils.DirUtil;
import org.evilbinary.utils.Logger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Scroller;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;

public class HighlightEditText extends EditText implements Constants, OnKeyListener, OnGestureListener {

	private MyTagToSpannedConverter mConverter;
	private SyntaxHighlight mMaker;
	private CodeTextWatcher mWatcher;

	/** The line numbers paint */
	protected Paint mPaintNumbers;
	/** The line numbers paint */
	protected Paint mPaintHighlight;
	/** the offset value in dp */
	protected int mPaddingDP = 6;
	/** the padding scaled */
	protected int mPadding, mLinePadding;
	/** the scale for desnity pixels */
	protected float mScale;

	/** the scroller instance */
	protected Scroller mTedScroller;
	/** the velocity tracker */
	protected GestureDetector mGestureDetector;
	/** the Max size of the view */
	protected Point mMaxSize;

	/** the highlighted line index */
	protected int mHighlightedLine;
	protected int mHighlightStart;

	protected Rect mDrawingRect, mLineBounds;

	protected Configure mConfigure;

	public HighlightEditText(Context context, Configure conf) {
		super(context);
		mConfigure = conf;

		mPaintNumbers = new Paint();
		mPaintNumbers.setTypeface(Typeface.MONOSPACE);
		mPaintNumbers.setAntiAlias(true);

		mPaintHighlight = new Paint();
		mScale = getResources().getDisplayMetrics().density;
		mPadding = (int) (mPaddingDP * mScale);

		mHighlightedLine = mHighlightStart = -1;

		mDrawingRect = new Rect();
		mLineBounds = new Rect();

		mGestureDetector = new GestureDetector(getContext(), this);
		mConverter = new MyTagToSpannedConverter(this.getContext());

		mMaker = new SyntaxHighlight(mConfigure);
		mWatcher = new CodeTextWatcher(mMaker, this, mConverter);
		this.addTextChangedListener(mWatcher);

		loadFromConfigure(conf);
	}

	public void loadFromConfigure(Configure configure) {
		mConfigure = configure;
		mMaker.loadConfigure(mConfigure);
		mConverter.loadCss(mConfigure.mDataPath + "/" + mConfigure.mHighlightCss);

		updateSettings();
		if (mConverter != null) {
			if (mConverter.getForeground() != null)
				this.setTextColor(mConverter.getForeground());
			if (mConverter.getBackground() != null)
				this.setBackgroundColor(mConverter.getBackground());
		}

	}

	public Configure getConfigure() {
		return mConfigure;
	}

	public void setHtml(String htmlSource) {
		try {
			Spanned spanText = mConverter.convert(htmlSource);
			this.setText(spanText);

		} catch (Exception e) {
			Logger.e(e);
		}
	}

	public void setSource(String source) {
		if (source != null && !source.equals("")) {
			setText(source);
			String result = mMaker.pase(source);
			Spanned spanText = mConverter.convert(result);
			render(spanText, 0);
		}
	}

	public void render(Spanned spanText, int begin) {
		if (spanText != null) {
			SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder) this.getText();
			CharacterStyle[] allSpans = spanText.getSpans(0, spanText.length(), CharacterStyle.class);
			// System.out.println("allSpans size:"+allSpans.length);

			if (allSpans.length == 0) {
				allSpans = spannableStringBuilder.getSpans(begin, begin + spanText.length(), CharacterStyle.class);
				// System.out.println("	allSpans size:"+allSpans.length);
				for (CharacterStyle span : allSpans) {
					spannableStringBuilder.removeSpan(span);
				}
			} else {
				for (CharacterStyle span : allSpans) {
					int spanStart = spanText.getSpanStart(span);
					int spanEnd = spanText.getSpanEnd(span);
					int flag = spanText.getSpanFlags(span);
					spannableStringBuilder.setSpan(span, begin + spanStart, begin + spanEnd, flag);
				}
			}

		}
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		int count, lineX, baseline;
		count = getLineCount();
		if (mConfigure.mSettings.SHOW_LINE_NUMBERS) {
			int padding = (int) (Math.floor(Math.log10(count)) + 1);
			padding = (int) ((padding * mPaintNumbers.getTextSize()) + mPadding + (mConfigure.mSettings.TEXT_SIZE
					* mScale * 0.5));
			if (mLinePadding != padding) {
				mLinePadding = padding;
				setPadding(mLinePadding, mPadding, mPadding, mPadding);
			}
		}

		// get the drawing boundaries
		getDrawingRect(mDrawingRect);

		// display current line
		computeLineHighlight();

		// draw line numbers
		lineX = (int) (mDrawingRect.left + mLinePadding - (mConfigure.mSettings.TEXT_SIZE * mScale * 0.5));
		int min = 0;
		int max = count;
		getLineBounds(0, mLineBounds);
		int startBottom = mLineBounds.bottom;
		int startTop = mLineBounds.top;
		getLineBounds(count - 1, mLineBounds);
		int endBottom = mLineBounds.bottom;
		int endTop = mLineBounds.top;
		if (count > 1 && endBottom > startBottom && endTop > startTop) {
			min = Math.max(min, ((mDrawingRect.top - startBottom) * (count - 1)) / (endBottom - startBottom));
			max = Math.min(max, ((mDrawingRect.bottom - startTop) * (count - 1)) / (endTop - startTop) + 1);
		}
		for (int i = min; i < max; i++) {
			baseline = getLineBounds(i, mLineBounds);
			if ((mMaxSize != null) && (mMaxSize.x < mLineBounds.right)) {
				mMaxSize.x = mLineBounds.right;
			}
			if ((i == mHighlightedLine) && (!mConfigure.mSettings.WORDWRAP) && mConfigure.mSettings.SHOW_LINE_HIGHLIGHT) {
				canvas.drawRect(mLineBounds, mPaintHighlight);
			}

			if (mConfigure.mSettings.SHOW_LINE_NUMBERS) {
				canvas.drawText("" + (i + 1), mDrawingRect.left + mPadding, baseline, mPaintNumbers);
			}
			if (mConfigure.mSettings.SHOW_LINE_NUMBERS) {
				canvas.drawLine(lineX, mDrawingRect.top, lineX, mDrawingRect.bottom, mPaintNumbers);
			}
		}
		getLineBounds(count - 1, mLineBounds);
		if (mMaxSize != null) {
			mMaxSize.y = mLineBounds.bottom;
			mMaxSize.x = Math.max(mMaxSize.x + mPadding - mDrawingRect.width(), 0);
			mMaxSize.y = Math.max(mMaxSize.y + mPadding - mDrawingRect.height(), 0);
		}

	}

	protected void computeLineHighlight() {
		int i, line, selStart;
		String text;
		if (!isEnabled()) {
			mHighlightedLine = -1;
			return;
		}
		selStart = getSelectionStart();
		if (mHighlightStart != selStart) {
			text = getText().toString();
			line = i = 0;
			while (i < selStart) {
				i = text.indexOf(mConfigure.mLineSeparator, i);
				if (i < 0) {
					break;
				}
				if (i < selStart) {
					++line;
				}
				++i;
			}
			mHighlightedLine = line;
		}
	}

	private void updateSettings() {

		if (isInEditMode() || mConfigure == null || mPaintNumbers == null || mPaintHighlight == null) {
			return;
		}

		setTypeface(mConfigure.mSettings.getTypeface(getContext()));

		// wordwrap
		setHorizontallyScrolling(!mConfigure.mSettings.WORDWRAP);
		if (!mConfigure.mSettings.WORDWRAP)// 卡顿问题
			setMovementMethod(ScrollingMovementMethod.getInstance());

		// setTextColor(Color.BLACK);
		mPaintHighlight.setColor(mConfigure.mLineHighlightColor);
		mPaintNumbers.setColor(mConfigure.mLineNumberColor);
		mPaintHighlight.setAlpha(mConfigure.mLineHighlightAlpha);

		// text size
		setTextSize(mConfigure.mSettings.TEXT_SIZE);
		mPaintNumbers.setTextSize(mConfigure.mSettings.TEXT_SIZE * mScale * 0.85f);

		// refresh view
		postInvalidate();
		refreshDrawableState();

		// use Fling when scrolling settings ?
		if (mConfigure.mSettings.FLING_TO_SCROLL) {
			mTedScroller = new Scroller(getContext());
			mMaxSize = new Point();
		} else {
			mTedScroller = null;
			mMaxSize = null;
		}

		// padding
		mLinePadding = mPadding;
		int count = getLineCount();
		if (mConfigure.mSettings.SHOW_LINE_NUMBERS) {
			mLinePadding = (int) (Math.floor(Math.log10(count)) + 1);
			mLinePadding = (int) ((mLinePadding * mPaintNumbers.getTextSize()) + mPadding + (mConfigure.mSettings.TEXT_SIZE
					* mScale * 0.5));
			setPadding(mLinePadding, mPadding, mPadding, mPadding);
		} else {
			setPadding(mPadding, mPadding, mPadding, mPadding);
		}
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	public void computeScroll() {

		if (mTedScroller != null) {
			if (mTedScroller.computeScrollOffset()) {
				scrollTo(mTedScroller.getCurrX(), mTedScroller.getCurrY());
			}
		} else {
			super.computeScroll();
		}
	}

	public boolean onTouchEvent(MotionEvent event) {

		super.onTouchEvent(event);
		if (mGestureDetector != null) {
			return mGestureDetector.onTouchEvent(event);
		}

		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (!mConfigure.mSettings.FLING_TO_SCROLL) {
			return true;
		}

		if (mTedScroller != null) {
			mTedScroller.fling(getScrollX(), getScrollY(), -(int) velocityX, -(int) velocityY, 0, mMaxSize.x, 0,
					mMaxSize.y);
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub

		if (isEnabled()) {
			((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(this,
					InputMethodManager.SHOW_IMPLICIT);
		}
		return true;
	}

	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}

}
