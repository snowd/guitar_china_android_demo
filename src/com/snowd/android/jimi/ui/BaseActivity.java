package com.snowd.android.jimi.ui;

import android.support.v7.app.ActionBarActivity;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends ActionBarActivity {

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

}
