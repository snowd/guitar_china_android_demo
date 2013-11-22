package com.snowd.android.jimi.fragment;

import com.snowd.android.jimi.adapter.ForumNavigatorAdapter;
import com.snowd.android.jimi.adapter.ForumNavigatorAdapter.NavigationElement;
import com.snowd.android.jimi.view.PopoutDrawer;

public class NavigationFragment extends BaseFragment implements NavigationElement {
	
//	private ForumNavigatorAdapter mHostAdapter;
//
//	private PopoutDrawer mPopoutDrawer;
	/* (non-Javadoc)
	 * @see com.snowd.android.jimi.adapter.ForumNavigatorAdapter.NavigationElement#bindHostAdapter(com.snowd.android.jimi.adapter.ForumNavigatorAdapter)
	 */
	@Override
	public void bindHostAdapter(ForumNavigatorAdapter adapter) {
//		mHostAdapter = adapter;
	}
	
	public void bindPopoutDrawer(PopoutDrawer p) {
//		mPopoutDrawer = p;
	}

	public int getTotalPage() {
		return 1;
	}
	
	public int getCurrentPage() {
		return 1;
	}
	
}
