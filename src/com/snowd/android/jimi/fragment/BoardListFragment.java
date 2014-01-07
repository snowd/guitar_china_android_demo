package com.snowd.android.jimi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.snowd.android.jimi.R;
import com.snowd.android.jimi.common.Constants;
import com.snowd.android.jimi.common.DateAndTimeHepler;
import com.snowd.android.jimi.common.MyApp;
import com.snowd.android.jimi.model.Board;
import com.snowd.android.jimi.model.Topic;
import com.snowd.android.jimi.rpc.RpcHelper;
import com.snowd.android.jimi.ui.TopicListActivity;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuelong.wenxl on 13-12-20.
 */
public class BoardListFragment extends AbsForumListFragment<Board> implements RpcHelper.DataProcesser<List<Board>> {


    private MyApp myApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myApp = (MyApp) getActivity().getApplication();
    }

    @Override
    protected RpcHelper.RpcResult doInBackground() {
        String url = Constants.URL_BOARD;
        return RpcHelper.get(url, this);
    }

    @Override
    protected boolean doInBackgroundAppend() {
        // only one page here
        return false;
    }

    @Override
    public List<Board> process(String input) throws Exception {
        List<Board> list = Board.mainBoardList(input);
        return list;
    }

    @Override
    protected List<Board> onPostResult(RpcHelper.RpcResult o) {
        if (o != null && o.data != null) return (List<Board>) o.data;
        return null;
    }


    @Override
    protected View preparedView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_boardlist, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.group = (TextView) convertView.findViewById(R.id.board_group);
            holder.title = (TextView) convertView.findViewById(R.id.board_title);
            holder.today = (TextView) convertView.findViewById(R.id.board_today);
            holder.description = (TextView) convertView.findViewById(R.id.board_description);
            holder.threads = (TextView) convertView.findViewById(R.id.board_threads);
            holder.posts = (TextView) convertView.findViewById(R.id.board_posts);
            convertView.setTag(holder);
        }
        return convertView;
    }

    @Override
    protected void bindData(int position, View contentView, ViewGroup parent, Object item) {
        ViewHolder holder = (ViewHolder) contentView.getTag();
        Board board = (Board) item;
        if (!TextUtils.isEmpty(board.getGroupName())) {
            holder.group.setVisibility(View.VISIBLE);
            holder.group.setText(board.getGroupName());
        } else {
            holder.group.setVisibility(View.GONE);
        }
        holder.title.setText(board.getName());
        holder.today.setText("今日：" + board.getTodayPosts());
        holder.description.setText(board.getDescription());
        holder.threads.setText("主题：" + board.getThreads());
        holder.posts.setText("帖子：" + board.getPosts());

    }

    static class ViewHolder {
        TextView group;
        TextView title;
        TextView today;
        TextView description;
        TextView threads;
        TextView posts;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Board board = (Board) l.getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), TopicListActivity.class);
        intent.putExtra("_key_fid", board.getFid());
        intent.putExtra("_key_title", board.getName());
        startActivity(intent);
    }
}
