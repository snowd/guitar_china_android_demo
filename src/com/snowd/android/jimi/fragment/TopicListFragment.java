package com.snowd.android.jimi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.snowd.android.jimi.R;
import com.snowd.android.jimi.adapter.AbsEndlessAdapter;
import com.snowd.android.jimi.common.Constants;
import com.snowd.android.jimi.common.DateAndTimeHepler;
import com.snowd.android.jimi.common.MyApp;
import com.snowd.android.jimi.model.Topic;
import com.snowd.android.jimi.rpc.RpcHelper;
import com.snowd.android.jimi.ui.TopicDetailActivity;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuelong.wenxl on 13-12-20.
 */
public class TopicListFragment extends AbsForumListFragment<Topic> implements RpcHelper.DataProcesser<List<Topic>> {

    private static final String TAG = "TAG";
    private static final int PAGE_SIZE = 20;
    private int pageStart = 1;
    private long id;

    private int mCurrent;
    private int mTotal;

    private MyApp myApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle param = getArguments();
        id = param.getLong("_key_fid");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myApp = (MyApp) getActivity().getApplication();
    }

    @Override
    protected RpcHelper.RpcResult doInBackground() {
        String url = Constants.URL_BOARD_TOPIC_LIST + id
                + "&" + Constants.PARAM_PAGESIZE + "=" + PAGE_SIZE
                + "&" + Constants.PARAM_PAGENO + "=" + pageStart;
        return RpcHelper.get(url, this);
    }

    @Override
    protected boolean doInBackgroundAppend() {
        String url = Constants.URL_BOARD_TOPIC_LIST + id
                + "&" + Constants.PARAM_PAGESIZE + "=" + PAGE_SIZE
                + "&" + Constants.PARAM_PAGENO + "=" + (mCurrent + 1);
        RpcHelper.RpcResult result = RpcHelper.get(url, this);
        if (result != null && result.data != null && result.data instanceof List)
        ((AbsEndlessAdapter) getListAdapter()).postAppendData((List) result.data);
        return mCurrent < mTotal;
    }

    @Override
    public List<Topic> process(String input) throws Exception {
        JSONObject json = new JSONObject(input);
        ArrayList<Topic> topics = Topic.newInstanceList(json
                .getJSONArray("thread_list"));
        JSONObject pageInfo = json.getJSONObject("page");
        mTotal = pageInfo.getInt("max_pages");
        mCurrent = pageInfo.getInt("curpage");
        return topics;
    }


    @Override
    protected List<Topic> onPostResult(RpcHelper.RpcResult o) {
        if (o != null && o.data != null) return (List<Topic>) o.data;
        return null;
    }


    @Override
    protected View preparedView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_threadlist, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.txt_id = (TextView) convertView.findViewById(R.id.topic_id);
            holder.txt_title = (TextView) convertView.findViewById(R.id.topic_title);
            holder.txt_author = (TextView) convertView.findViewById(R.id.topic_author);
            holder.txt_visit_replies = (TextView) convertView.findViewById(R.id.topic_visits_replies);
            holder.txt_pubtime = (TextView) convertView.findViewById(R.id.topic_pubtime);
            convertView.setTag(holder);
        }
        return convertView;
    }

    @Override
    protected void bindData(int position, View contentView, ViewGroup parent, Object item) {
        ViewHolder holder = (ViewHolder) contentView.getTag();
        Topic topic = (Topic) item;
        holder.txt_id.setText(String.valueOf(topic.getTid()));
        holder.txt_title.setText(topic.getSubject());
        holder.txt_author.setText(topic.getAuthor());
        if(null == topic.getViews() || "".equals(topic.getViews())){
            holder.txt_visit_replies.setText("");
        }else{
            holder.txt_visit_replies.setText(getActivity().getString(R.string.topic_visits_replies,
                    topic.getReplies(), topic.getViews()));
        }
        if(topic.getDateline() > 0L){
            holder.txt_pubtime.setText(DateAndTimeHepler.friendly_time(getActivity(), topic.getDateline() * 1000));
        }else{
            holder.txt_pubtime.setText("");
        }

    }

    static class ViewHolder {
        TextView txt_id;
        TextView txt_title;
        TextView txt_author;
        TextView txt_visit_replies;
        TextView txt_pubtime;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Topic topic = (Topic) l.getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
        intent.putExtra("_key_tid", topic.getTid());
        intent.putExtra("_key_title", topic.getSubject());
        startActivity(intent);
    }

}
