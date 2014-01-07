package com.snowd.android.jimi.widget;

/**
 * Created by xuelong.wenxl on 13-12-20.
 */

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public class AdapterWrapper extends BaseAdapter {
    private ListAdapter wrapped = null;

    public AdapterWrapper(ListAdapter wrapped) {
        this.wrapped = wrapped;

        wrapped.registerDataSetObserver(new DataSetObserver() {
            public void onChanged() {
                AdapterWrapper.this.notifyDataSetChanged();
            }

            public void onInvalidated() {
                AdapterWrapper.this.notifyDataSetInvalidated();
            }
        });
    }

    public Object getItem(int position) {
        return this.wrapped.getItem(position);
    }

    public int getCount() {
        return this.wrapped.getCount();
    }

    public int getViewTypeCount() {
        return this.wrapped.getViewTypeCount();
    }

    public int getItemViewType(int position) {
        return this.wrapped.getItemViewType(position);
    }

    public boolean areAllItemsEnabled() {
        return this.wrapped.areAllItemsEnabled();
    }

    public boolean isEnabled(int position) {
        return this.wrapped.isEnabled(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return this.wrapped.getView(position, convertView, parent);
    }

    public long getItemId(int position) {
        return this.wrapped.getItemId(position);
    }

    protected ListAdapter getWrappedAdapter() {
        return this.wrapped;
    }
}
