package com.snowd.android.jimi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import com.snowd.android.jimi.widget.EndlessAdapter;

import java.util.List;

/**
 * Created by xuelong.wenxl on 13-12-20.
 */
public abstract class AbsEndlessAdapter<T> extends EndlessAdapter {

    public AbsEndlessAdapter(ListAdapter wrapped) {
        super(wrapped);
    }

    public AbsEndlessAdapter(ListAdapter wrapped, boolean keepOnAppending) {
        super(wrapped, keepOnAppending);
    }

    public AbsEndlessAdapter(Context context, ListAdapter wrapped, int pendingResource) {
        super(context, wrapped, pendingResource);
    }

    public AbsEndlessAdapter(Context context, ListAdapter wrapped, int pendingResource, boolean keepOnAppending) {
        super(context, wrapped, pendingResource, keepOnAppending);
    }

    public AbsEndlessAdapter(Context context, List<T> data) {
        super(context, new AdapterWrapped(context, data), android.R.layout.simple_list_item_1, true);
        ((AdapterWrapped) getWrappedAdapter()).bindHostAdapter(this);
    }

    public AbsEndlessAdapter(Context context, List<T> data, int appendingResource) {
        super(context, new AdapterWrapped(context, data), appendingResource, true);
        ((AdapterWrapped) getWrappedAdapter()).bindHostAdapter(this);
    }

    private List<T> mDataToAppend;

    @Override
    protected boolean cacheInBackground() throws Exception {
        return false;
    }

    public void postAppendData(List<T> data) {
        mDataToAppend = data;
    }

    @Override
    protected void appendCachedData() {
        if (mDataToAppend != null) {
            AdapterWrapped<T> wrapped = ((AdapterWrapped<T>) getWrappedAdapter());
            wrapped.getData().addAll(mDataToAppend);
            mDataToAppend = null;
        }
    }

    static class AdapterWrapped<T> extends BaseAdapter {

        AbsEndlessAdapter<T> hostAdapter;

        public AdapterWrapped(Context context) {
            super(context);
        }

        public AdapterWrapped(Context context, List<T> items) {
            super(context, items);
        }

        void bindHostAdapter(AbsEndlessAdapter<T> host) {
            hostAdapter = host;
        }

        @Override
        protected View preparedView(int position, View convertView, ViewGroup parent) {
            return hostAdapter.preparedView(position, convertView, parent);
        }

        @Override
        protected void bindData(int position, View contentView, ViewGroup parent, Object item) {
            hostAdapter.bindData(position, contentView, parent, (T) item);
        }
    }

    protected abstract View preparedView(int position, View convertView, ViewGroup parent);

    protected abstract void bindData(int position, View contentView, ViewGroup parent, T item);

}
