package com.snowd.android.jimi.fragment;


import android.app.Activity;
import android.support.v4.app.ListFragment;
import com.snowd.android.jimi.ui.BaseActivity;

public class BaseListFragment extends ListFragment {

    public BaseActivity getHostActivity() {
        Activity activity = getActivity();
        if (activity != null && activity instanceof  BaseActivity) {
            return (BaseActivity) activity;
        }
        return null;
    }
}
