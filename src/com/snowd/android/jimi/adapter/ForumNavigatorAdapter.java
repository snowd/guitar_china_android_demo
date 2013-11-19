package com.snowd.android.jimi.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.snowd.android.jimi.fragment.BoardViewFragment;

public class ForumNavigatorAdapter extends FragmentPagerAdapter implements
		ViewPager.OnPageChangeListener {
	
	private Context mContext;
	private ViewPager mViewPager;
	private ArrayList<Fragment> mPages;
	
//	private int mNavIndex = 1;
	
	public ForumNavigatorAdapter(Context context, FragmentManager fm, ViewPager pager) {
		super(fm);
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
		BoardViewFragment boardFragment = (BoardViewFragment) Fragment.instantiate(
				mContext, BoardViewFragment.class.getName());
		boardFragment.bindHostAdapter(this);
		pushPage(boardFragment);
	}
	
	private Fragment popPage() {
		if (mPages == null || mPages.isEmpty()) return null;
		Fragment t = mPages.remove(mPages.size() - 1);
		return t;
	}
	
	private void pushPage(Fragment f) {
		mPages.add(f);
	}
	
	public void enterPage(Class<?> claz, Bundle data) {
		Fragment fragment = Fragment
				.instantiate(mContext, claz.getName());
		fragment.setArguments(data);
//		 boardFragment.bindHostAdapter(this);
		Log.d("", "frag=" + fragment);
		Log.d("", "frag param=" + fragment.getArguments());
		pushPage(fragment);
		notifyDataSetChanged();
		mViewPager.setCurrentItem(getCount() - 1, true);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		if (position < getCount() - 1) {
			popPage();
			notifyDataSetChanged();
		}
	}

}
