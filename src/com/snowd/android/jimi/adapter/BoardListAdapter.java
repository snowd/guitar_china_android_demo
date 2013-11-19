package com.snowd.android.jimi.adapter;

import java.util.List;

import com.snowd.android.jimi.R;

import net.shopnc.android.model.Board;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class BoardListAdapter extends BaseAdapter<Board> {

	public BoardListAdapter(Context context, List<Board> items) {
		super(context, items);
	}

	public BoardListAdapter(Context context) {
		super(context);
	}

	@Override
	protected View preparedView(int position, View convertView) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(getContext(), R.layout.item_boardlist, null);
			holder = new ViewHolder();
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
	protected void bindData(int position, View itemView, Board item) {
		ViewHolder holder = (ViewHolder) itemView.getTag();
		if (!TextUtils.isEmpty(item.getGroupName())) {
			holder.group.setVisibility(View.VISIBLE);
			holder.group.setText(item.getGroupName());
		} else {
			holder.group.setVisibility(View.GONE);
		}
		holder.title.setText(item.getName());
		holder.today.setText("今日：" + item.getTodayPosts());
		holder.description.setText(item.getDescription());
		holder.threads.setText("主题：" + item.getThreads());
		holder.posts.setText("帖子：" + item.getPosts());
	}

	static class ViewHolder {
		TextView group;
		TextView title;
		TextView today;
		TextView description;
		TextView threads;
		TextView posts;
	}
}
