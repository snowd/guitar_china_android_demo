package com.snowd.android.jimi.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;

/**
 * Created by xuelong.wenxl on 13-12-16.
 */
public abstract class AbsForumActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment();
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, getInstance(getIntent()), getFragmentTag())
                .commit();
    }

    protected void setActionBarSubTitle(String sub, boolean showAsUp) {
        ActionBar actionBar = getSupportActionBar();
        if (!TextUtils.isEmpty(sub)) {
            actionBar.setSubtitle(sub);
        }
    }

    protected void setActionBarTitle(String main, String sub, boolean showAsUp) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(main);
        if (!TextUtils.isEmpty(sub)) {
            actionBar.setSubtitle(sub);
        }
        actionBar.setDisplayHomeAsUpEnabled(showAsUp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    protected Fragment getInstance(Intent intent) {
        Fragment f = Fragment.instantiate(this,
                getClaz().getName(), intent.getExtras());
        return f;
    }

    protected String getFragmentTag() {
        return getClaz().getCanonicalName();
    }

    protected abstract Class<?> getClaz();

}
