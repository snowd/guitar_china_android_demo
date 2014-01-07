package com.snowd.android.jimi.ui;

import android.content.Intent;
import android.os.Bundle;
import com.snowd.android.jimi.fragment.TopicListFragment;

/**
 * Created by xuelong.wenxl on 13-12-16.
 */
public class TopicListActivity extends AbsForumActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        String name = intent.getStringExtra("_key_title");
        setActionBarTitle("主题列表", name, true);
    }

    @Override
    protected Class<?> getClaz() {
        return TopicListFragment.class;
    }

}