package com.snowd.android.jimi.ui;

import android.os.Bundle;
import com.snowd.android.jimi.fragment.TopicListFragment;

/**
 * Created by xuelong.wenxl on 13-12-16.
 */
public class TopicListActivity extends AbsForumActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Class<?> getClaz() {
        return TopicListFragment.class;
    }

}