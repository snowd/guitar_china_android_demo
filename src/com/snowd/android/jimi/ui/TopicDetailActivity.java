package com.snowd.android.jimi.ui;

import android.content.Intent;
import android.os.Bundle;
import com.snowd.android.jimi.fragment.TopicDetailFragment;

/**
 * Created by xuelong.wenxl on 13-12-19.
 */
public class TopicDetailActivity extends AbsForumActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        String name = intent.getStringExtra("_key_title");
        setActionBarTitle("主题详情", name, true);
    }


    @Override
    protected Class<?> getClaz() {
        return TopicDetailFragment.class;
    }

}