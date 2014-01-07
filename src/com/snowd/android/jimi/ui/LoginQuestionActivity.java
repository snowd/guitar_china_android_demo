/**
 *  ClassName: LoginActivity.java
 *  created on 2012-2-25
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package com.snowd.android.jimi.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.snowd.android.jimi.R;
import com.snowd.android.jimi.common.MyApp;
import com.snowd.android.jimi.model.LoginQuestion;
import com.snowd.android.jimi.model.ResponseData;
import com.snowd.android.jimi.model.SessionHolder;
import com.snowd.android.jimi.rpc.RemoteHandler;
import com.snowd.android.jimi.rpc.RemoteHandler.Callback;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * 登录
 * @author qjyong
 */
public class LoginQuestionActivity extends BaseActivity {
	private MyApp myApp;
	
//	private TextView txt_title;
//	private ImageButton btn_left;
//	private ImageButton btn_right;
	
	private Spinner sp_question;
	private EditText txt_pwd;
//	private CheckBox cbx_rememberpwd;
//	private CheckBox cbx_autologin;
	private Button btn_login;
	
	private ProgressDialog progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = (MyApp)this.getApplication();
		this.setContentView(R.layout.login_questions);
		
		initTitleBar();
		
		initContent();
	}
//	
//	@Override
//	protected Dialog onCreateDialog(int id) {
//		if(id == Constants.DIALOG_LOGIN_ID){
//			return createProgressDialog();
//		}
//		return super.onCreateDialog(id);
//	}
//	
//	private MyProcessDialog createProgressDialog(){
//		MyProcessDialog dialog = new MyProcessDialog(this);
//		dialog.setMsg(getString(R.string.login_wait));
//		return dialog;
//	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.finish();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	
	
	private void doLoginAnswer(String questionId, String answer){
		if (progress == null) {
			progress = ProgressDialog.show(this, "", "login...");
		} else {
			progress.show();
		}
		Intent intent = getIntent();
		RemoteHandler.asyncLoginQuestions(intent.getStringExtra("username"), intent.getStringExtra("loginauth"), questionId, answer, new Callback() {
            @Override
            public Serializable dataPrepared(int code, String resp) {
                return null;
            }

            @Override
            public void dataLoaded(ResponseData data, Object dataObj) {
                if (data.getCode() == HttpStatus.SC_OK && !TextUtils.isEmpty(String.valueOf(dataObj))) {
                    try {
                        JSONObject json = new JSONObject(String.valueOf(dataObj));
                        String sid = json.getString("sid");
                        String uid = json.getString("uid");
                        String formhash = json.getString("formhash");
                        Toast.makeText(LoginQuestionActivity.this,
                                "Login Success uid=" + uid,
                                Toast.LENGTH_LONG).show();

                        Intent intent = new Intent();
                        intent.putExtra("sid", sid);
                        intent.putExtra("uid", uid);
                        intent.putExtra("formhash", formhash);
                        SessionHolder.setLogin(sid, uid, formhash);
                        setResult(RESULT_FIRST_USER, intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginQuestionActivity.this,
                                "网络错误，请重试！", Toast.LENGTH_SHORT).show();
                    }
//					String json = data.getJson();
//					User user = User.newInstance(json);
//					String sessionid = user.getSessionid();
//					if(null != sessionid){
//						if(!"aperror".equals(sessionid)){
//							user.setAuthor(author);
//							user.setPwd(pwd);
//							//成功
//							if(autologin || rememberpwd){ //自动登录或记住密码
//								myApp.setUserpw(pwd);
//							}
//							myApp.setUseracc(author);
//							myApp.setSid(user.getSessionid());
//							myApp.setUid(user.getAuthorid());
//							myApp.setGroupid(user.getGroupid());
//							
//							UserDao userDao = new UserDao(LoginQuestionActivity.this);
//							if(null == userDao.get(author)){
//								userDao.save(user);
//							}
//							//QIUJY 还需要更新版块列表的权限
//							myApp.setSubBoardMap(RemoteDataHandler.loadSubBoardMap(user.getAuthorid()));
//							
//							LoginQuestionActivity.this.setResult(200);
//							LoginQuestionActivity.this.finish();
//						}else{
//							Toast.makeText(LoginQuestionActivity.this, "用户名或密码错误，请重试！", Toast.LENGTH_SHORT).show();
//						}
//					}else{
//						//失败
//						//myApp.setSid("");
//						//myApp.setUid("");
//						//myApp.setGroupid("");
//						Toast.makeText(LoginQuestionActivity.this, "登陆失败，请重试！", Toast.LENGTH_SHORT).show();
//					}
                } else {
                    Toast.makeText(LoginQuestionActivity.this, "网络错误，请重试！", Toast.LENGTH_SHORT).show();
                }
//				dismissDialog(Constants.DIALOG_LOGIN_ID);
                if (progress != null && progress.isShowing())
                    progress.dismiss();
            }
        });
		//this.dismissDialog(Constants.DIALOG_LOGIN_ID);
	}
	
	@SuppressWarnings("unchecked")
	private void initContent(){
		Intent intent = getIntent();
		sp_question = (Spinner)this.findViewById(R.id.spinner_questions);
		List<LoginQuestion> question = (List<LoginQuestion>)(intent.getSerializableExtra("question"));
		ArrayAdapter<LoginQuestion> adapter = new ArrayAdapter<LoginQuestion>(
				this, android.R.layout.simple_spinner_item, question);
		sp_question.setAdapter(adapter);
//		txt_loginname.setText(myApp.getUseracc());
		
		txt_pwd = (EditText)this.findViewById(R.id.txt_answer);
		if(myApp.isRemember_pwd()){
			txt_pwd.setText(myApp.getUserpw());
		}
//		cbx_rememberpwd = (CheckBox)this.findViewById(R.id.cbx_rememberpwd);
//		cbx_autologin = (CheckBox)this.findViewById(R.id.cbx_autologin);
		btn_login = (Button)this.findViewById(R.id.btn_login);
		
		
		btn_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String id = ((LoginQuestion) sp_question.getSelectedItem()).id;
				String answer = txt_pwd.getText().toString();
				
				if(TextUtils.isEmpty(id)){
					Toast.makeText(LoginQuestionActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
					return ;
				}
				
				if(TextUtils.isEmpty(answer)){
					Toast.makeText(LoginQuestionActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
					return ;
				}
				
				doLoginAnswer(id, answer);
			}
		});
	}
	
	private void initTitleBar(){
//		//设置标题
//		txt_title = (TextView)this.findViewById(R.id.txt_title);
//		txt_title.setText(this.getString(R.string.login));
//		
//		//设置标题栏按钮
//		btn_left = (ImageButton)this.findViewById(R.id.btn_left);
//		btn_left.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				LoginActivity.this.finish();
//			}
//		});
//		
//		btn_right = (ImageButton)this.findViewById(R.id.btn_right);
//		btn_right.setVisibility(View.INVISIBLE);
	}
}
