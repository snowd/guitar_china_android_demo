package com.snowd.android.jimi.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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

import com.snowd.android.jimi.adapter.ForumNavigatorAdapter;
import com.snowd.android.jimi.adapter.ForumNavigatorAdapter.NavigationElement;
import com.snowd.android.jimi.adapter.TopicListViewAdapter;
import com.snowd.android.jimi.rpc.RpcHandler;
import com.snowd.android.jimi.view.PopoutDrawer;
import com.snowd.android.jimi.view.PopoutDrawer.OnIndexChangedListener;

public class ThreadViewFragment extends BaseListFragment implements
		OnRefreshListener, RpcHandler.Callback, NavigationElement, OnIndexChangedListener {

	
	private AtomicBoolean mRpcInWork = new AtomicBoolean(false);
	
	private ArrayList<Topic> mThreads;
	private TopicListViewAdapter mAdapter;
	private PopoutDrawer mPopoutDrawer;
	private ForumNavigatorAdapter mHostAdapter;
	@Override
	public void bindHostAdapter(ForumNavigatorAdapter adapter) {
		mHostAdapter = adapter;
	}

	public void bindPopoutDrawer(PopoutDrawer p) {
		mPopoutDrawer = p;
		mPopoutDrawer.setOnIndexChangedListener(this);
	}
	
	public int getTotalPage() {
		return mTotal;
	}
	
	public int getCurrentPage() {
		return mCurrent;
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
		
//		PopoutDrawer popout = new PopoutDrawer(view.getContext());
//		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//				FrameLayout.LayoutParams.WRAP_CONTENT,
//				FrameLayout.LayoutParams.WRAP_CONTENT);
//		params.gravity = Gravity.RIGHT;
//		popout.setLayoutParams(params);
//		popout.setTotal(10);
//		viewGroup.addView(popout);
		mPopoutDrawer.setVisibility(View.GONE);
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
		loadBoards(1);
	}
	
	private void loadBoards(int page) {
		if (mRpcInWork.compareAndSet(false, true)) {
			if (id == 0 && getArguments() != null) {
				id = getArguments().getLong("_key_fid");
			}
			String url = Constants.URL_BOARD_TOPIC_LIST + id;
			RpcHandler.asyncGet(url, mPageSize, page, this);
		}
	}
	
	private long id = 0;
	private int mPageSize = 20;
	private int mCurrent = 0;
	private int mTotal = 0;
	private JSONObject mPageInfo;

	@Override
	public Serializable dataPrepared(int code, String resp) {
		if (getActivity() == null || getView() == null) return null;
		if (code == HttpStatus.SC_OK) {
			/*
			 * 加载全部数据
			 */
			try {
				JSONObject obj = new JSONObject(resp);
				mPageInfo = obj.optJSONObject("page");
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
		if (getActivity() == null || getView() == null) return;
		if (resp.getCode() == HttpStatus.SC_OK && data != null) {
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
			mRpcInWork.set(false);
			setListShown(true);
//			if (mAdapter != null) mAdapter.notifyDataSetChanged();
			try {
				if (mPageInfo != null && mPageInfo.length() > 0) {
					mTotal = mPageInfo.getInt("max_pages");
					mCurrent = mPageInfo.getInt("curpage");
					mPopoutDrawer.setTotal(mTotal, mCurrent - 1);
					if (mTotal > 1) mPopoutDrawer.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	@Override
	public void onChangeIndexTo(int from, int to, int total) {
		if (getView() != null) {
			setListShown(false);
			loadBoards(to + 1);
		}
	}

	@Override
	public boolean onChangeIndexExtra(int from, int total) {
		// TODO Auto-generated method stub
		return false;
	}
	
}