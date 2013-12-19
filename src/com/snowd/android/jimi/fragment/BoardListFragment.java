package com.snowd.android.jimi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.snowd.android.jimi.adapter.BoardListAdapter;
import com.snowd.android.jimi.adapter.ForumNavigatorAdapter;
import com.snowd.android.jimi.adapter.ForumNavigatorAdapter.NavigationElement;
import com.snowd.android.jimi.common.Constants;
import com.snowd.android.jimi.model.Board;
import com.snowd.android.jimi.model.ResponseData;
import com.snowd.android.jimi.rpc.RpcHandler;
import com.snowd.android.jimi.ui.ThreadListActivity;
import com.snowd.android.jimi.view.PopoutDrawer;
import org.apache.http.HttpStatus;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarcompat.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


import java.io.Serializable;
import java.util.ArrayList;

public class BoardListFragment extends BaseListFragment implements
        OnRefreshListener, RpcHandler.Callback, NavigationElement {

	private ForumNavigatorAdapter mHostAdapter;
	private ArrayList<Board> mBoards;
	private BoardListAdapter mAdapter;
//	private PopoutDrawer mPopouDrawer;
	
	@Override
	public void bindHostAdapter(ForumNavigatorAdapter adapter) {
		mHostAdapter = adapter;
	}
	
	public void bindPopoutDrawer(PopoutDrawer p) {
//		mPopouDrawer = p;
	}
	
	public int getTotalPage() {
		return 1;
	}
	
	public int getCurrentPage() {
		return 1;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
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
				// pullable
				.theseChildrenArePullable(android.R.id.list, android.R.id.empty)
				.listener(this).setup(mPullToRefreshLayout);
//		mPopouDrawer.setVisibility(View.GONE);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Set the List Adapter to display the sample items
		setListShownNoAnimation(false);
		loadBoards();
	}

	@Override
	public void onRefreshStarted(View view) {
		// Hide the list
		setListShown(false);
		loadBoards();
	}
	
	private void loadBoards(){
		if (mAdapter != null) {
			setListAdapter(mAdapter);
			setListShown(true);
		} else {
			String url = Constants.URL_BOARD;
			RpcHandler.asyncGetList(url, 50, 1, this);
		}
	}

	@Override
	public Serializable dataPrepared(int code, String resp) {
		if (getActivity() != null && !getActivity().isFinishing()
				&& code == HttpStatus.SC_OK) {
			String json = resp;
			/*
			 * 加载全部数据
			 */
			ArrayList<Board> boards = Board.mainBoardList(json);
			return boards;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void dataLoaded(ResponseData resp, Object data) {
		if (getActivity() != null && !getActivity().isFinishing()
				&& resp.getCode() == HttpStatus.SC_OK && data != null) {
			mBoards = (ArrayList<Board>) data;
			if (mAdapter == null) {
				mAdapter = new BoardListAdapter(getActivity(), mBoards);
			} else {
				mAdapter.setData(mBoards);
			}
			setListAdapter(mAdapter);
			
			// pager
		} else {
			Toast.makeText(getView().getContext(), "网络故障！", Toast.LENGTH_SHORT)
					.show();
		}
		mPullToRefreshLayout.setRefreshComplete();
		if (getView() != null) {
			setListShown(true);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Board item = mAdapter.getItem(position);
//		if (mHostAdapter != null) {
            Intent intent = new Intent(l.getContext(), ThreadListActivity.class);
            intent.putExtra("_key_fid", item.getFid());
			startActivity(intent);
//        }
	}
	
}