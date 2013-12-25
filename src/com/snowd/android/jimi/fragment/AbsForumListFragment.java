package com.snowd.android.jimi.fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.snowd.android.jimi.adapter.AbsEndlessAdapter;
import com.snowd.android.jimi.rpc.RpcHelper;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarcompat.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import java.util.List;

public abstract class AbsForumListFragment<E> extends BaseListFragment implements
        OnRefreshListener/*, RemoteHandler.Callback*/ {

    private PullToRefreshLayout mPullToRefreshLayout;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup viewGroup = (ViewGroup) view;

        // As we're using a ListFragment we create a PullToRefreshLayout
        // manually
        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

        // We can now setup the PullToRefreshLayout
        ActionBarPullToRefresh
                .from(getActivity())
                        // We need to insert the PullToRefreshLayout into the Fragment's
                        // ViewGroup
                .insertLayoutInto(viewGroup)
                        // Here we mark just the ListView and it's Empty View as
                        // pullable
                .theseChildrenArePullable(android.R.id.list, android.R.id.empty)
                .listener(this).setup(mPullToRefreshLayout);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set the List Adapter to display the sample items
        setListShownNoAnimation(false);
        loadData();
    }

    @Override
    public void onRefreshStarted(View view) {
        // Hide the list
        setListShown(false);
        loadData();
    }

    protected void loadData(){
        new LoadTask().execute();
    }

    protected void postResult(List<E> data) {
        if (getActivity() != null && !getActivity().isFinishing() && data != null && !data.isEmpty()) {
            MainAdapter adapter = new MainAdapter(getActivity(), data);
            setListAdapter(adapter);
        } else {
            Toast.makeText(getView().getContext(), "网络故障！", Toast.LENGTH_SHORT).show();
        }
        mPullToRefreshLayout.setRefreshComplete();
        if (getView() != null) {
            setListShown(true);
        }
    }

    protected abstract RpcHelper.RpcResult doInBackground();

    protected abstract List<E> onPostResult(RpcHelper.RpcResult o);

    class LoadTask extends AsyncTask<Void, Void, RpcHelper.RpcResult> {
        @Override
        protected RpcHelper.RpcResult doInBackground(Void ...params) {
            return AbsForumListFragment.this.doInBackground();
        }

        @Override
        protected void onPostExecute(RpcHelper.RpcResult o) {
            List<E> data = onPostResult(o);
            if (data != null) postResult(data);
        }
    }

    class MainAdapter extends AbsEndlessAdapter {
        public MainAdapter(Context context, List<E> data) {
            super(context, data);
        }
        public MainAdapter(Context context, List<E> data, int appendingResource) {
            super(context, data, appendingResource);
        }
        @Override protected View preparedView(int position, View convertView, ViewGroup parent) {
            return AbsForumListFragment.this.preparedView(position, convertView, parent);
        }
        @Override protected void bindData(int position, View contentView, ViewGroup parent, Object item) {
            AbsForumListFragment.this.bindData(position, contentView, parent, item);
        }
        @Override protected boolean cacheInBackground() throws Exception {
            return doInBackgroundAppend();
        }
    }

    protected abstract boolean doInBackgroundAppend();

    protected abstract View preparedView(int position, View convertView, ViewGroup parent);

    protected abstract void bindData(int position, View contentView, ViewGroup parent, Object item);

}
