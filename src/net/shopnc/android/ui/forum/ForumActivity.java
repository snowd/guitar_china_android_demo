/**
 *  ClassName: ForumActivity.java
 *  created on 2012-2-23
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.forum;

import net.shopnc.android.R;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.ui.MainActivity;
import net.shopnc.android.ui.more.LoginActivity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * 论坛Tab界面
 * @author qjyong
 */
public class ForumActivity extends TabActivity {
	/** tab标签名*/
	public final static String TAB_TAG_LIST = "board_list";
	public final static String TAB_TAG_FAVORITE = "board_favorite";
	public final static String TAB_TAG_BROWSE = "lastest_browse";
	
	public static TabHost tabHost;
	public static TextView titleleft_btn;
	
	private RadioButton btn_board_list;
	private RadioButton btn_board_favorite;
	private RadioButton btn_lastest_browse;
	
	private Intent board_list_intent;
	private Intent board_favorite_intent;
	private Intent lastest_browse_intent;
	
	protected MainActivity myParent;
	
	
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.tab_forum);
		myParent = (MainActivity)this.getParent();
		
		//////////////////// init ///////////////////////////
		board_list_intent = new Intent(this, BoardListActivity.class);
		board_favorite_intent = new Intent(this, BoardFavoriteActivity.class);
		lastest_browse_intent = new Intent(this, LastestBrowseActivity.class);
		
		tabHost = this.getTabHost();
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_LIST).setIndicator(TAB_TAG_LIST).setContent(board_list_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_FAVORITE).setIndicator(TAB_TAG_FAVORITE).setContent(board_favorite_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_BROWSE).setIndicator(TAB_TAG_BROWSE).setContent(lastest_browse_intent));
	
		////////////////////// find View ////////////////////////////
		btn_board_list = (RadioButton)this.findViewById(R.id.btn_forum_board_list);
		btn_board_favorite = (RadioButton)this.findViewById(R.id.btn_forum_board_favorite);
		btn_lastest_browse = (RadioButton)this.findViewById(R.id.btn_forum_lastest_browse);
		
		MyRadioButtonClickListener listener = new MyRadioButtonClickListener();
		btn_board_list.setOnClickListener(listener);
		btn_board_favorite.setOnClickListener(listener);
		btn_lastest_browse.setOnClickListener(listener);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("homeactivity", "home--resume");
		//退出
		ImageButton btn_right = (ImageButton)this.findViewById(R.id.btn_right);
		final MyApp myApp = (MyApp)getApplication();
		if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
				&& null != myApp.getSid() && !"".equals(myApp.getSid())){//登录
			btn_right.setBackgroundResource(R.drawable.btn_exit_normal);
		}else{
			btn_right.setBackgroundResource(R.drawable.btn_login);
		}
		
		btn_right.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
						&& null != myApp.getSid() && !"".equals(myApp.getSid())){//登录
					ForumActivity.this.getParent().showDialog(Constants.DIALOG_EXITAPP_ID);
				}else{
					ForumActivity.this.startActivityForResult(new Intent(ForumActivity.this, LoginActivity.class), 200);
				}
			}
		});
	}
	

	class MyRadioButtonClickListener implements View.OnClickListener{
		public void onClick(View v) {
			RadioButton btn = (RadioButton)v;
			switch(btn.getId()){
			case R.id.btn_forum_board_list:
				tabHost.setCurrentTabByTag(TAB_TAG_LIST);
				break;
			case R.id.btn_forum_board_favorite:
				tabHost.setCurrentTabByTag(TAB_TAG_FAVORITE);
				break;
			case R.id.btn_forum_lastest_browse:
				tabHost.setCurrentTabByTag(TAB_TAG_BROWSE);
				break;
			}
		}
	}
}
