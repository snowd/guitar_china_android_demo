package com.snowd.android.jimi.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.snowd.android.jimi.R;

import java.util.LinkedList;

public class PopoutDrawer extends LinearLayout {
	
	private static final int WIDTH_BUTTON = 80;
	private static final int HEIGHT_BUTTON = 100;
	private int SCREEN_WIDTH = 0;
	private int SCREEN_HEIGHT = 0;
	private static final int POINTER_CHANGE_EXTRA = -2;
	private static final int POINTER_NOT_CHANGE = -1;
	
	public interface OnIndexChangedListener {
		public void onChangeIndexTo(int from, int to, int total);
		public boolean onChangeIndexExtra(int from, int total);
	}
	
	private int mTotal = 1;
	private int mOldValue = 1;
	private int mCurrent = 1;
	private int mVisible = 1;
	private int mVisibleFirst = 1;
	private int mCurrentOffset = 1;
	
	private int mColorNormal;
	private int mColorHighlight;
	private int mColorCurrent;

	private LinearLayout mButtonsPanel;
	private TextView mGoToIndex;
	private TextView mTouchHandler;
	
	private LinkedList<TextView> mRemovedViews;
	private OnIndexChangedListener mOnIndexChangedListener;
	
	public PopoutDrawer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public PopoutDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PopoutDrawer(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		setOrientation(HORIZONTAL);
		setGravity(Gravity.RIGHT);
		setFocusable(true);
		setFocusableInTouchMode(true);
		setLongClickable(true);
		
		DisplayMetrics outMetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(outMetrics);
		SCREEN_WIDTH = outMetrics.widthPixels;
		SCREEN_HEIGHT = outMetrics.heightPixels;
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.widget_popout_drawer, this, true);
		
		initViews();
	}
	
	private void initViews() {
		
		mColorNormal = Color.argb(0xe0, 0xe0, 0xe0, 0xe0);
		mColorHighlight = Color.argb(0xe0, 0x00, 0x99, 0xcc);
		mColorCurrent = Color.argb(0xe0, 0x33, 0xb5, 0xe5);
		
		mTouchHandler = (TextView) findViewById(R.id.touch_handler);
		mButtonsPanel = (LinearLayout) findViewById(R.id.buttons_panel);
		mGoToIndex = (TextView) findViewById(R.id.go_to_index);
		mGoToIndex.getLayoutParams().height = HEIGHT_BUTTON;

		mRemovedViews = new LinkedList<TextView>();
	}
	
	private int computeVisibleButtons(int total) {
		if (mTouchHandler.getMeasuredWidth() == 0) {
			if (mTouchHandler.getLayoutParams().width > 0) {
				mTouchHandler.setMinWidth(mTouchHandler.getLayoutParams().width);
			}
			mTouchHandler.measure(
					MeasureSpec.makeMeasureSpec(
							mTouchHandler.getLayoutParams().width,
							MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
							SCREEN_HEIGHT, MeasureSpec.AT_MOST));
		}
		int visible = (SCREEN_WIDTH - mTouchHandler.getMeasuredWidth())
				/ WIDTH_BUTTON;
		if (visible < total) {
			mGoToIndex.setVisibility(VISIBLE);
			if (mGoToIndex.getMeasuredWidth() == 0) {
				mGoToIndex.measure(MeasureSpec.makeMeasureSpec(SCREEN_WIDTH,
						MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(
						HEIGHT_BUTTON, MeasureSpec.EXACTLY));
			}
			if (mGoToIndex.getMeasuredWidth() < WIDTH_BUTTON) {
				mGoToIndex.getLayoutParams().width = WIDTH_BUTTON;
				mGoToIndex.measure(MeasureSpec.makeMeasureSpec(WIDTH_BUTTON,
						MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
						HEIGHT_BUTTON, MeasureSpec.EXACTLY));
			}
			visible = (SCREEN_WIDTH - mTouchHandler.getMeasuredWidth() - mGoToIndex
					.getMeasuredWidth()) / WIDTH_BUTTON;
		} else {
			visible = total;
			mGoToIndex.setVisibility(GONE);
		}
		return visible;
	}
	
	public void setOnIndexChangedListener(OnIndexChangedListener l) {
		mOnIndexChangedListener = l;
	}
	
	public void setTotal(int total) {
		setTotal(total, 0);
	}
	
	/**
	 * 
	 * @param total total count
	 * @param index should be offset with 1
	 */
	public void setTotal(int total, int index) {
		mVisibleFirst = 1;
		mCurrent = index + mVisibleFirst;
		mTotal = total;
		mVisible = computeVisibleButtons(total);
		mCurrentOffset = mVisible / 2;
		initButtons(mVisible);
		moveTape(index);
	}
	
	private void initButtons(int visible) {
		while (visible < mButtonsPanel.getChildCount() - 1 && mButtonsPanel.getChildCount() > 1) {
			TextView child = (TextView) mButtonsPanel.getChildAt(0);
			mRemovedViews.add(child);
			mButtonsPanel.removeView(child);
		}
		int start = mButtonsPanel.getChildCount() - 1;
		for (int i = start; i < visible; i++) {
			TextView b = new TextView(getContext());
			b.setLayoutParams(new LinearLayout.LayoutParams(WIDTH_BUTTON,
					HEIGHT_BUTTON));
			b.setGravity(Gravity.CENTER);
//			b.setText(String.valueOf(i + 1));
			b.setBackgroundColor(mCurrent == i + mVisibleFirst ? mColorCurrent
					: mColorNormal);
			// b.setTextColor(Color.rgb(0x00, 0x99, 0xcc));
			mButtonsPanel.addView(b, i);
		}	
	}
	
	private void moveTape(int pointer) {
		if (pointer >= 0) {
			mCurrent = pointer + mVisibleFirst;
		}
		Log.d("", "touch >> up cur=" + mCurrent);
		findViewById(R.id.buttons_panel).setVisibility(GONE);
		mVisibleFirst = mCurrent <= mCurrentOffset ? 1 : mCurrent - mCurrentOffset;
		if (mVisibleFirst + mVisible > mTotal) {
			mVisibleFirst = mTotal - mVisible + 1;
		}
		for (int i = 0; i < mButtonsPanel.getChildCount(); i++) {
			TextView b = (TextView) mButtonsPanel.getChildAt(i);
			if (b.getId() != R.id.go_to_index) {
				b.setText(String.valueOf(i + mVisibleFirst));
			}
			if (i + mVisibleFirst != mCurrent) {
				b.setBackgroundColor(mColorNormal);
			}
		}
		if (pointer != -2) {
			mGoToIndex.setBackgroundColor(mColorNormal);
		}
	}
	
	private int pointer = -1;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			findViewById(R.id.buttons_panel).setVisibility(VISIBLE);
			mOldValue = mCurrent;
			return true;
		case MotionEvent.ACTION_UP:
			moveTape(pointer);
			if (mOnIndexChangedListener != null) {
				if (pointer == POINTER_NOT_CHANGE) {
					// value not change
				} else if (pointer == POINTER_CHANGE_EXTRA) {
					mOnIndexChangedListener.onChangeIndexExtra(mOldValue - 1,
							mTotal - 1);
				} else if (pointer >= 0) {
					mOnIndexChangedListener.onChangeIndexTo(mOldValue - 1,
							mCurrent - 1, mTotal);
				}
			}
			return true;
		case MotionEvent.ACTION_OUTSIDE: // 穿透
		case MotionEvent.ACTION_MOVE:
			if (mButtonsPanel == null) break;
			int pos = (int) (event.getX() / WIDTH_BUTTON);
			int rightEdge = mGoToIndex.getVisibility() == VISIBLE ? mVisible
					* WIDTH_BUTTON + mGoToIndex.getWidth() : mVisible
					* WIDTH_BUTTON;
			if (event.getX() < rightEdge && pos != pointer) {
				if (pos < mVisible) {
					pointer = pos;
					for (int i = 0; i < mVisible; i++) {
						TextView b = (TextView) mButtonsPanel.getChildAt(i);
						if (i == pos) {
							b.setBackgroundColor(mColorHighlight);
						} else if (i + mVisibleFirst == mCurrent) {
							b.setBackgroundColor(mColorCurrent);
						} else {
							b.setBackgroundColor(mColorNormal);
						}
					}
					mGoToIndex.setBackgroundColor(mColorNormal);
				} else {
					pointer = POINTER_CHANGE_EXTRA;
					for (int i = 0; i < mVisible; i++) {
						TextView b = (TextView) mButtonsPanel.getChildAt(i);
						if (i + mVisibleFirst == mCurrent) {
							b.setBackgroundColor(mColorCurrent);
						} else {
							b.setBackgroundColor(mColorNormal);
						}
					}
					mGoToIndex.setBackgroundColor(mColorHighlight);
				}
				Log.d("", "touch >> move ptr=" + pointer);
			} else {
				if (pos != pointer) {
					pointer = POINTER_NOT_CHANGE;
					mGoToIndex.setBackgroundColor(mColorNormal);
					Log.d("", "touch >> move ptr=" + pointer);
				}
			}
			return true;
		}
		return super.dispatchTouchEvent(event);
	}
	
}
