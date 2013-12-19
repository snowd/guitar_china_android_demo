package com.snowd.android.jimi.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import com.snowd.android.jimi.fragment.BoardListFragment;
import com.snowd.android.jimi.view.PopoutDrawer;
import com.snowd.android.jimi.view.PopoutDrawer.OnIndexChangedListener;

import java.util.ArrayList;

public class ForumNavigatorAdapter extends FragmentPagerAdapter implements
		ViewPager.OnPageChangeListener {
	
	public interface NavigationElement {
		public void bindHostAdapter(ForumNavigatorAdapter adapter);
		public void bindPopoutDrawer(PopoutDrawer p);
		public int getTotalPage();
		public int getCurrentPage();
	}
	
	private Context mContext;
	private FragmentManager mFragmentManager;
	private ViewPager mViewPager;
	private ArrayList<Fragment> mPages;
	private int mCurPosition = -1;
	private PopoutDrawer mPopout;
	
//	private int mNavIndex = 1;
	
	public ForumNavigatorAdapter(Context context, FragmentManager fm, ViewPager pager) {
		super(fm);
		mFragmentManager = fm;
		mContext = context;
		mViewPager = pager;
		pager.setOnPageChangeListener(this);
		initPages();
	}

	@Override
	public Fragment getItem(int arg0) {
		return mPages.get(arg0);
	}

	@Override
	public int getCount() {
		return mPages.size();
	}
	
	private void initPages() {
		mPages = new ArrayList<Fragment>();
		BoardListFragment boardFragment = (BoardListFragment) Fragment.instantiate(
				mContext, BoardListFragment.class.getName());
		boardFragment.bindHostAdapter(this);
		pushPage(boardFragment);
		mCurPosition = 0;
	}
	
	private Fragment popPage() {
		if (mPages == null || mPages.isEmpty()) return null;
		Fragment t = mPages.remove(mPages.size() - 1);
//		destroyItem(mViewPager, mPages.size() - 1, t);
		mFragmentManager.beginTransaction().remove(t).commit();
		return t;
	}
	
	private void pushPage(Fragment f) {
        if (mFragmentManager != null && f != null) {
            mPages.add(f);
        }
	}
	
	public void setPopouDrawer(PopoutDrawer p) {
		mPopout = p;
	}
	
	public void enterPage(Class<?> claz, Bundle data) {
		Fragment fragment = Fragment
				.instantiate(mContext, claz.getName(), data);
		((NavigationElement)fragment).bindHostAdapter(this);
		((NavigationElement)fragment).bindPopoutDrawer(mPopout);
		
		Log.d("", "frag=" + fragment);
		Log.d("", "frag param=" + fragment.getArguments());
		pushPage(fragment);
		notifyDataSetChanged();
		mViewPager.setCurrentItem(getCount() - 1, true);
	}

	private int lastPageState = ViewPager.SCROLL_STATE_IDLE;
	@Override
	public void onPageScrollStateChanged(int state) {
		if (lastPageState == ViewPager.SCROLL_STATE_SETTLING
				&& state == ViewPager.SCROLL_STATE_IDLE) {
			if (mCurPosition < getCount() - 1) {
				popPage();
				notifyDataSetChanged();			
			}
		}
		lastPageState = state;
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		mCurPosition = position;
		NavigationElement navFragment = (NavigationElement)getItem(position);
		if (position == 0) {
			mPopout.setVisibility(View.GONE);
		} else if (((NavigationElement)getItem(position)).getTotalPage() > 1) {
			mPopout.setVisibility(View.VISIBLE);
			mPopout.setTotal(navFragment.getTotalPage(), navFragment.getCurrentPage() - 1);
			mPopout.setOnIndexChangedListener((OnIndexChangedListener) navFragment);
		}
	}

}
