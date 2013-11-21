package com.snowd.android.jimi.ui;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends SherlockFragmentActivity {

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
