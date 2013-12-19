package com.snowd.android.jimi.ui;

import android.os.Bundle;
import com.snowd.android.jimi.fragment.TopicViewFragment;

/**
 * Created by xuelong.wenxl on 13-12-19.
 */
public class ThreadDetailActivity extends AbsForumActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Class<?> getClaz() {
        return TopicViewFragment.class;
    }

}