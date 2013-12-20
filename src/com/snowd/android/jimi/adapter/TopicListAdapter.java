/**
 *  ClassName: TopicListAdapter.java
 *  created on 2012-2-25
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package com.snowd.android.jimi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.snowd.android.jimi.R;
import com.snowd.android.jimi.common.DateAndTimeHepler;
import com.snowd.android.jimi.model.TopicItem;

/**
 * 帖子列表适配器
 * 
 * @author qjyong
 */
public class TopicListAdapter extends com.snowd.android.jimi.adapter.BaseAdapter<TopicItem> {

	/**
	 * 构造方法
	 * 
	 * @param ctx
	 */
	public TopicListAdapter(Context ctx) {
        super(ctx);
	}

    @Override
    protected View preparedView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_threadlist, parent, false);
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
    protected void bindData(int position, View contentView, ViewGroup parent, TopicItem item) {
        ViewHolder holder = (ViewHolder) contentView.getTag();
        TopicItem topic = getItem(position);
        holder.txt_id.setText(String.valueOf(topic.getTid()));
        holder.txt_title.setText(topic.getSubject());
        holder.txt_author.setText(topic.getAuthor());
        if(null == topic.getViews() || "".equals(topic.getViews())){
            holder.txt_visit_replies.setText("");
        }else{
            holder.txt_visit_replies.setText(getContext().getString(R.string.topic_visits_replies,
                    topic.getReplies(), topic.getViews()));
        }
        if(topic.getDateline() > 0L){
            holder.txt_pubtime.setText(DateAndTimeHepler.friendly_time(getContext(), topic.getDateline() * 1000));
        }else{
            holder.txt_pubtime.setText("");
        }

    }

//    public void setDatas(ArrayList<TopicItem> datas) {
//		this.datas = datas;
//	}
	
	static class ViewHolder {
		TextView txt_id;
		TextView txt_title;
		TextView txt_author;
		TextView txt_visit_replies;
		TextView txt_pubtime;
	}
}
