package com.snowd.android.jimi.fragment;

import java.io.Serializable;
import java.util.ArrayList;

import net.shopnc.android.common.Constants;
import net.shopnc.android.model.ResponseData;
import net.shopnc.android.model.Topic;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.snowd.android.jimi.adapter.TopicListViewAdapter;
import com.snowd.android.jimi.adapter.ForumNavigatorAdapter;
import com.snowd.android.jimi.adapter.ForumNavigatorAdapter.NavigationElement;
import com.snowd.android.jimi.rpc.RpcHandler;

public class ThreadViewFragment extends BaseListFragment implements
		OnRefreshListener, RpcHandler.Callback, NavigationElement {

	
	private ArrayList<Topic> mThreads;
	private TopicListViewAdapter mAdapter;
	
	private ForumNavigatorAdapter mHostAdapter;
	@Override
	public void bindHostAdapter(ForumNavigatorAdapter adapter) {
		mHostAdapter = adapter;
	}

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
				// pullable1.
				.theseChildrenArePullable(android.R.id.list, android.R.id.empty)
				.listener(this).setup(mPullToRefreshLayout);
		Log.d("", "Fragment >>> onViewCreated view=" + getView());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Set the List Adapter to display the sample items
		setListShownNoAnimation(false);
		loadBoards();
		Log.d("", "Fragment >>> onActivityCreated view=" + getView());
	}

	@Override
	public void onRefreshStarted(View view) {
		// Hide the list
		setListShown(false);
		loadBoards();
	}

	private void loadBoards(){
		Bundle b = getArguments();
		//((MainActivity)myParent.getParent()).showDialog(Constants.DIALOG_LOADDATA_ID);
		String url = Constants.URL_BOARD_TOPIC_LIST + b.getLong("_key_fid");
//		if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
//				&& null != myApp.getSid() && !"".equals(myApp.getSid())){//登录用户
//			url += myApp.getUid();
//		}
		RpcHandler.asyncGet(url, 30, 1, this);
	}	

	@Override
	public Serializable dataPrepared(int code, String resp) {
		if (getActivity() != null && !getActivity().isFinishing()
				&& code == HttpStatus.SC_OK) {
			/*
			 * 加载全部数据
			 */
			try {
				JSONObject obj = new JSONObject(resp);
				ArrayList<Topic> topics = Topic.newInstanceList(obj
						.getJSONArray("thread_list"));
				return topics;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void dataLoaded(ResponseData resp, Object data) {
		if (getActivity() != null && !getActivity().isFinishing()
				&& resp.getCode() == HttpStatus.SC_OK && data != null) {
			mThreads = (ArrayList<Topic>) data;
			if (mAdapter == null) {
				mAdapter = new TopicListViewAdapter(getActivity());
				mAdapter.setDatas(mThreads);
			} else {
				mAdapter.setDatas(mThreads);
			}
			setListAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();
		} else {
			Toast.makeText(getView().getContext(), "网络故障！", Toast.LENGTH_SHORT)
					.show();
		}
		mPullToRefreshLayout.setRefreshComplete();
		if (getView() != null) {
			setListShown(true);
//			if (mAdapter != null) mAdapter.notifyDataSetChanged();
		}
	}
	
	public void clear() {
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Topic item = (Topic) mAdapter.getItem(position);
		Bundle bundle = new Bundle();
		bundle.putLong("_key_tid", item.getTid());
		mHostAdapter.enterPage(TopicViewFragment.class, bundle);
	}
	
}