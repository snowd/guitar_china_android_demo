package com.snowd.android.jimi.ui;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.snowd.android.jimi.R;
import com.snowd.android.jimi.view.MenuNavigator;


public class MainActivity extends BaseActivity {
	
	private SlidingMenu mMasterMenu;
	private MenuNavigator mMenuNavigator;
//	private static final String sTitle = "论坛"; 

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main_struct);
		
		// configure the SlidingMenu
		initSlidingMenu();
		
		// action bar
		getSupportActionBar().setHomeButtonEnabled(true);
	}
	
	private void initSlidingMenu() {
		mMasterMenu = new SlidingMenu(this);
		mMasterMenu.setMode(SlidingMenu.LEFT);
		mMasterMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// left
//		menu.setShadowWidthRes(R.dimen.shadow_width);
		mMasterMenu.setShadowDrawable(R.drawable.shadow_left);
//		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mMasterMenu.setShadowWidth(10);;
		mMasterMenu.setBehindOffset(200);
		mMasterMenu.setFadeDegree(0.35f);
		
		mMenuNavigator = (MenuNavigator) LayoutInflater.from(this).inflate(
				R.layout.menu_nav, null);
		mMasterMenu.setMenu(mMenuNavigator);
		
		// right
//		mMasterMenu.setSecondaryMenu(R.layout.menu_nav);
		
		mMasterMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
	}
	
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == android.R.id.home) {
			mMasterMenu.showMenu();
    	};
		return super.onOptionsItemSelected(item);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
//		menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//			@Override
//			public boolean onMenuItemClick(MenuItem item) {
//				mMasterMenu.showMenu();
//				return true;
//			}
//		});
        menu.add("Save")
//            .setIcon(isLight ? R.drawable.ic_compose_inverse : R.drawable.ic_compose)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        menu.add("Search")
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add("Refresh")
//            .setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }	

}
