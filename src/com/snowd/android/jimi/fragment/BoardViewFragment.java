package com.snowd.android.jimi.fragment;

import java.io.Serializable;
import java.util.ArrayList;

import net.shopnc.android.common.Constants;
import net.shopnc.android.model.Board;
import net.shopnc.android.model.ResponseData;

import org.apache.http.HttpStatus;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.snowd.android.jimi.adapter.BoardListAdapter;
import com.snowd.android.jimi.adapter.ForumNavigatorAdapter;
import com.snowd.android.jimi.adapter.ForumNavigatorAdapter.NavigationElement;
import com.snowd.android.jimi.rpc.RpcHandler;

public class BoardViewFragment extends BaseListFragment implements
		OnRefreshListener, RpcHandler.Callback, NavigationElement {

	private ForumNavigatorAdapter mHostAdapter;
	private ArrayList<Board> mBoards;
	private BoardListAdapter mAdapter;
	
	@Override
	public void bindHostAdapter(ForumNavigatorAdapter adapter) {
		mHostAdapter = adapter;
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
		//((MainActivity)myParent.getParent()).showDialog(Constants.DIALOG_LOADDATA_ID);
		String url = Constants.URL_BOARD;
//		if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
//				&& null != myApp.getSid() && !"".equals(myApp.getSid())){//登录用户
//			url += myApp.getUid();
//		}
		RpcHandler.asyncGetList(url, 50, 1, this);
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
	@Override
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
		Bundle bundle = new Bundle();
		bundle.putLong("_key_fid", item.getFid());
		if (mHostAdapter != null)
			mHostAdapter.enterPage(ThreadViewFragment.class, bundle);
	}
	
}