/**
 *  ClassName: AboutUsActivity.java
 *  created on 2012-2-24
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.more;

import net.shopnc.android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 关于我们
 * @author qjyong
 */
public class AboutUsActivity extends Activity {
	private TextView txt_title;
	private ImageButton btn_left;
	private ImageButton btn_right;
	private TextView txt_version;
	private Button btn_feedback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.more_aboutus);
		
		//设置标题
		txt_title = (TextView)this.findViewById(R.id.txt_title);
		txt_title.setText(this.getString(R.string.more_aboutus));
		
		//设置标题栏按钮
		btn_left = (ImageButton)this.findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AboutUsActivity.this.finish();
			}
		});
		
		btn_right = (ImageButton)this.findViewById(R.id.btn_right);
		btn_right.setVisibility(View.INVISIBLE);
		
		//version
		txt_version = (TextView)this.findViewById(R.id.txt_version);
		txt_version.setText(this.getString(R.string.more_aboutus_version, this.getString(R.string.app_version)));
		
		btn_feedback = (Button)this.findViewById(R.id.btn_feedback);
		btn_feedback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AboutUsActivity.this, FeedbackActivity.class);
				
				AboutUsActivity.this.startActivity(intent);
				AboutUsActivity.this.finish();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.finish();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
}
