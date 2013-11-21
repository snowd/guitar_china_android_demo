package com.snowd.android.jimi.fragment;

import com.snowd.android.jimi.adapter.ForumNavigatorAdapter;
import com.snowd.android.jimi.adapter.ForumNavigatorAdapter.NavigationElement;

public class NavigationFragment extends BaseFragment implements NavigationElement {
	
	private ForumNavigatorAdapter mHostAdapter;

	/* (non-Javadoc)
	 * @see com.snowd.android.jimi.adapter.ForumNavigatorAdapter.NavigationElement#bindHostAdapter(com.snowd.android.jimi.adapter.ForumNavigatorAdapter)
	 */
	@Override
	public void bindHostAdapter(ForumNavigatorAdapter adapter) {
		mHostAdapter = adapter;
	}

}
