package com.snowd.android.jimi.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snowd.android.jimi.R;

public class PopoutDrawer extends LinearLayout {
	
	private static final int WIDTH_BUTTON = 80;
	private static final int HEIGHT_BUTTON = 100;
	private int SCREEN_WIDTH = 0;
	private int SCREEN_HEIGHT = 0;
	
	private int mTotal = 5;
	private int mCurrent = 1;
	private int mVisible = mTotal;
	private int mVisibleFirst = 1;
	
	private int mColorNormal;
	private int mColorHighlight;
	private int mColorCurrent;

	private LinearLayout mButtonsPanel;
	private TextView mGoToIndex;
	private TextView mTouchHandler;
	
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
		initButtons();
	}
	
//	private void computeVisibleButtons() {
//		
//	}
	
	private void initButtons() {
		
		mColorNormal = Color.argb(0xe0, 0xe0, 0xe0, 0xe0);
		mColorHighlight = Color.argb(0xe0, 0x00, 0x99, 0xcc);
		mColorCurrent = Color.argb(0xe0, 0x33, 0xb5, 0xe5);
		
//		mColorHighlight = getResources().getColor(R.color.abs__holo_blue_light);
		
		mButtonsPanel = (LinearLayout) findViewById(R.id.buttons_panel);
		mGoToIndex = (TextView) findViewById(R.id.go_to_index);
		mGoToIndex.getLayoutParams().height = HEIGHT_BUTTON;
		for (int i = 0; i < mVisible; i++) {
			Button b = new Button(getContext());
			b.setLayoutParams(new LinearLayout.LayoutParams(WIDTH_BUTTON, HEIGHT_BUTTON));
			b.setText(String.valueOf(i + 1));
			b.setBackgroundColor(mCurrent == i ? mColorCurrent : mColorNormal);
//			b.setTextColor(Color.rgb(0x00, 0x99, 0xcc));
			mButtonsPanel.addView(b, i);
		}
	}
	
	private int pointer = -1;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			findViewById(R.id.buttons_panel).setVisibility(VISIBLE);
			return true;
		case MotionEvent.ACTION_UP:
			if (pointer > 0) {
				mCurrent = pointer + mVisibleFirst;
			}
			findViewById(R.id.buttons_panel).setVisibility(GONE);
			for (int i = 0; i < mButtonsPanel.getChildCount(); i++) {
				Button b = (Button) mButtonsPanel.getChildAt(i);
				if (i != mCurrent)
					b.setBackgroundColor(mColorNormal);
			}
			if (pointer != -2)
				mGoToIndex.setBackgroundColor(mColorNormal);
			return true;
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
						Button b = (Button) mButtonsPanel.getChildAt(i);
						if (i == pos) {
							b.setBackgroundColor(mColorHighlight);
						} else if (i == mCurrent) {
							b.setBackgroundColor(mColorCurrent);
						} else {
							b.setBackgroundColor(mColorNormal);
						}
					}
					mGoToIndex.setBackgroundColor(mColorNormal);
				} else {
					pointer = -2;
					for (int i = 0; i < mVisible; i++) {
						Button b = (Button) mButtonsPanel.getChildAt(i);
						if (i == mCurrent) {
							b.setBackgroundColor(mColorCurrent);
						} else {
							b.setBackgroundColor(mColorNormal);
						}
					}
					mGoToIndex.setBackgroundColor(mColorHighlight);
				}
			} else {
				pointer = -1;
				mGoToIndex.setBackgroundColor(mColorNormal);
			}
		}
		return false;
	}
	
}
