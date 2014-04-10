package com.snowd.android.jimi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.snowd.android.jimi.R;
import com.snowd.android.jimi.adapter.ForumNavigatorAdapter;
import com.snowd.android.jimi.fragment.BoardListFragment;
import com.snowd.android.jimi.view.MenuNavigator;


public class MainActivity extends BaseActivity implements OnItemClickListener {

//    private SlidingMenu mMasterMenu;
	private MenuNavigator mMenuNavigator;
//	private static final String sTitle = "论坛";
	private ViewPager mNavigatorPager;
	private ForumNavigatorAdapter mNavAdapter;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
        setContentView(R.layout.main);
        getSupportActionBar().setHomeButtonEnabled(true);
        initBoardList();
//        initSlidingMenu();
	}

    private void initBoardList() {
        BoardListFragment boards = new BoardListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content_frame, boards)
                .commit();
//        getSupportActionBar().setTitle(R.string.app_name);
    }
	
//	private void initSlidingMenu() {
//		mMasterMenu = new SlidingMenu(this);
//		mMasterMenu.setMode(SlidingMenu.LEFT);
//		mMasterMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
//		// left
//		mMasterMenu.setShadowDrawable(R.drawable.shadow_left);
//		mMasterMenu.setShadowWidth(10);;
//		mMasterMenu.setBehindOffset(200);
//		mMasterMenu.setFadeDegree(0.35f);
//
//		mMenuNavigator = (MenuNavigator) LayoutInflater.from(this).inflate(
//				R.layout.menu_nav, null);
//		mMenuNavigator.setOnItemClickListener(this);
//		mMasterMenu.setMenu(mMenuNavigator);
//		mMasterMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//	}

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == android.R.id.home) {
//			mMasterMenu.showMenu();
    	};
		return super.onOptionsItemSelected(item);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public void onBackPressed() {
		if (mNavAdapter != null && mNavAdapter.getCount() > 1) {
			mNavigatorPager.setCurrentItem(mNavAdapter.getCount() - 2, true);
			return;
		}
		super.onBackPressed();
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch((int)id) {
		case R.id.menu_item_profile:
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			break;
		}
	}
	
}
