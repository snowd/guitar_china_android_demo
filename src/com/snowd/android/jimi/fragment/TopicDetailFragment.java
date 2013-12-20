package com.snowd.android.jimi.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.snowd.android.jimi.common.Constants;
import com.snowd.android.jimi.model.Topic;
import com.snowd.android.jimi.model.TopicItem;
import com.snowd.android.jimi.rpc.RpcHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuelong.wenxl on 13-12-20.
 */
public class TopicDetailFragment extends AbsForumListFragment<Topic> implements RpcHelper.DataProcesser<List<Topic>> {

    private static final int PAGE_SIZE = 20;
    private int page = 1;
    private long id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle param = getArguments();
        id = getArguments().getLong("_key_tid");
    }

    @Override
    protected RpcHelper.RpcResult doInBackground() {
        String url = Constants.URL_TOPIC_DETAIL_DEFAULT + id
                + "&" + Constants.PARAM_PAGESIZE + "=" + PAGE_SIZE
                + "&" + Constants.PARAM_PAGENO + "=" + page;
        return RpcHelper.get(url, this);
    }

    @Override
    protected boolean doInBackgroundEndless() {
        return false;
    }

    @Override
    public List<Topic> process(String input) throws Exception {
        JSONObject json = new JSONObject();
        ArrayList<Topic> topics = Topic.newInstanceList(json
                .getJSONArray("post"));
        return topics;
    }


    @Override
    protected List<Topic> onPostResult(RpcHelper.RpcResult o) {
        if (o != null && o.data != null) return (List<Topic>) o.data;
        return null;
    }

    @Override
    protected View preparedView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    protected void bindData(int position, View contentView, ViewGroup parent, Object item) {

    }

}
