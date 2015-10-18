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
import org.evilbinary.highliter.parsers.SyntaxHighlight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Spanned;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Scroller;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnKeyListener;

public class HighlightEditText extends EditText implements OnKeyListener, OnGestureListener {

	private MyTagToSpannedConverter converter;
	private SyntaxHighlight maker;
	private CodeTextWatcher watcher;

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

	public HighlightEditText(Context context) {
		super(context);
		converter = new MyTagToSpannedConverter(this.getContext());
		maker = new SyntaxHighlight();
		watcher = new CodeTextWatcher(maker, this, converter);
		this.addTextChangedListener(watcher);

		mPaintNumbers = new Paint();
		mPaintNumbers.setTypeface(Typeface.MONOSPACE);
		mPaintNumbers.setAntiAlias(true);

		mPaintHighlight = new Paint();

		mScale = context.getResources().getDisplayMetrics().density;
		mPadding = (int) (mPaddingDP * mScale);

		mHighlightedLine = mHighlightStart = -1;

		mDrawingRect = new Rect();
		mLineBounds = new Rect();

		mGestureDetector = new GestureDetector(getContext(), this);

		updateFromSettings();
	}

	public void setHtml(String source) {
		try {
			Spanned spanText = converter.convert(source);
			if (converter.getForeground() != null)
				this.setTextColor(converter.getForeground());
			if (converter.getBackground() != null)
				this.setBackgroundColor(converter.getBackground());
			this.setText(spanText);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		int count, lineX, baseline;
		count = getLineCount();
		if (Settings.SHOW_LINE_NUMBERS) {
			int padding = (int) (Math.floor(Math.log10(count)) + 1);
			padding = (int) ((padding * mPaintNumbers.getTextSize()) + mPadding + (Settings.TEXT_SIZE * mScale * 0.5));
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
		lineX = (int) (mDrawingRect.left + mLinePadding - (Settings.TEXT_SIZE * mScale * 0.5));
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
			if ((i == mHighlightedLine) && (!Settings.WORDWRAP)) {
				canvas.drawRect(mLineBounds, mPaintHighlight);
			}

			if (Settings.SHOW_LINE_NUMBERS) {
				canvas.drawText("" + (i + 1), mDrawingRect.left + mPadding, baseline, mPaintNumbers);
			}
			if (Settings.SHOW_LINE_NUMBERS) {
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
				i = text.indexOf("\n", i);
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

	/**
	 * Update view settings from the app preferences
	 * 
	 * @category Custom
	 */
	public void updateFromSettings() {

		if (isInEditMode()) {
			return;
		}

		setTypeface(Settings.getTypeface(getContext()));

		// wordwrap
		setHorizontallyScrolling(!Settings.WORDWRAP);
		setTextColor(Color.BLACK);
		mPaintHighlight.setColor(Color.BLACK);
		mPaintNumbers.setColor(Color.GRAY);
		mPaintHighlight.setAlpha(48);
		
		// text size
		setTextSize(Settings.TEXT_SIZE);
		mPaintNumbers.setTextSize(Settings.TEXT_SIZE * mScale * 0.85f);

		// refresh view
		postInvalidate();
		refreshDrawableState();

		// use Fling when scrolling settings ?
		if (Settings.FLING_TO_SCROLL) {
			mTedScroller = new Scroller(getContext());
			mMaxSize = new Point();
		} else {
			mTedScroller = null;
			mMaxSize = null;
		}

		// padding
		mLinePadding = mPadding;
		int count = getLineCount();
		if (Settings.SHOW_LINE_NUMBERS) {
			mLinePadding = (int) (Math.floor(Math.log10(count)) + 1);
			mLinePadding = (int) ((mLinePadding * mPaintNumbers.getTextSize()) + mPadding + (Settings.TEXT_SIZE
					* mScale * 0.5));
			setPadding(mLinePadding, mPadding, mPadding, mPadding);
		} else {
			setPadding(mPadding, mPadding, mPadding, mPadding);
		}
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}

}
