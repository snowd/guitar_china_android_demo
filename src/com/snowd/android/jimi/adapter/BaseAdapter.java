package com.snowd.android.jimi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

	private Context mContext;
	private List<T> mItems;
	
	public BaseAdapter(Context context) {
		mContext = context;
	}

	public BaseAdapter(Context context, List<T> items) {
		mContext = context;
		mItems = items;
	}
	
	public Context getContext() {
		return mContext;
	}
	
	public void setData(List<T> items) {
		mItems = items;
	}
	
	public List<T> getData() {
		return mItems;
	}
	
	@Override
	public int getCount() {
		if (mItems != null) {
			return mItems.size();
		}
		return 0;
	}

	@Override
	public T getItem(int position) {
		if (mItems != null && !mItems.isEmpty()) {
			return mItems.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = preparedView(position, convertView, parent);
		T item = getItem(position);
		bindData(position, convertView, parent, item);
		return convertView;
	}
	
	protected abstract View preparedView(int position, View convertView, ViewGroup parent);
	
	protected abstract void bindData(int position, View contentView, ViewGroup parent, T item);
	

}
