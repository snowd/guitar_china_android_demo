package com.snowd.android.jimi.ui;

import android.os.Bundle;
import com.snowd.android.jimi.fragment.ThreadListFragment;

/**
 * Created by xuelong.wenxl on 13-12-16.
 */
public class ThreadListActivity extends AbsForumActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Class<?> getClaz() {
        return ThreadListFragment.class;
    }

}