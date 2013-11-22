package com.snowd.android.jimi.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.snowd.android.jimi.R;
import com.snowd.android.jimi.model.NavigatorMenuItem;

public class MenuNavigator extends ListView {

	public MenuNavigator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public MenuNavigator(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MenuNavigator(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		initBuiltIdItems(context);
	}
	
	private List<NavigatorMenuItem> mItemList;
	
	private void initBuiltIdItems(Context context) {
		mItemList = new ArrayList<NavigatorMenuItem>();
		// built-in item
		mItemList.add(new NavigatorMenuItem("登录", R.id.menu_item_profile, 0, true));
		mItemList.add(new NavigatorMenuItem("GC新闻", R.id.menu_item_hot_news, 0, true));
		mItemList.add(new NavigatorMenuItem("演出信息", R.id.menu_item_rock_shows, 0, true));
		mItemList.add(new NavigatorMenuItem("论坛首页", R.id.menu_item_board_list, 0, true));
		mItemList.add(new NavigatorMenuItem("收藏列表", R.id.menu_item_favorites, 0, true));
		mItemList.add(new NavigatorMenuItem("我的帖子", R.id.menu_item_threads, 0, true));
		
		setAdapter(mAdapter);
	}
	
	private BaseAdapter mAdapter = new BaseAdapter() {
		
		@Override
		public int getCount() {
			return mItemList.size();
		}
		
		@Override
		public NavigatorMenuItem getItem(int position) {
			// TODO Auto-generated method stub
			return mItemList.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return getItem(position).getId();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView itemView;
			if (convertView == null) {
				itemView = (TextView) View.inflate(getContext(), R.layout.menu_nav_item, null);
			} else {
				itemView = (TextView) convertView;
			}
			NavigatorMenuItem item = getItem(position);
			itemView.setText(item.getName());
			return itemView;
		}
		
	};
	
}
