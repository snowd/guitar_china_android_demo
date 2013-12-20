package com.snowd.android.jimi.ui;

import android.os.Bundle;
import com.snowd.android.jimi.fragment.TopicDetailFragment_old;

/**
 * Created by xuelong.wenxl on 13-12-19.
 */
public class TopicDetailActivity extends AbsForumActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Class<?> getClaz() {
        return TopicDetailFragment_old.class;
    }

}