package com.snowd.android.jimi.model;

import android.text.TextUtils;

public class SessionHolder {
	
	private static SessionHolder mInstance = null;
	
	public static SessionHolder getInstance() {
		if (mInstance == null) {
			mInstance = new SessionHolder();
		}
		return mInstance;
	}
	
	public static void setLogin(String sid, String uid, String formhash) {
		SessionHolder holder = getInstance();
		holder.update(sid, uid, formhash);
	}
	
	public static String obtainSid() {
		return getInstance().getSid();
	}
	
	public static boolean isLogin() {
		return (getInstance() != null && !TextUtils.isEmpty(obtainSid()));
	}

	private String mSid;
	private String mUid;
	private String mFormhash;
	
	public String getSid() {
		return mSid;
	}
	public void setSid(String mSid) {
		this.mSid = mSid;
	}
	public String getUid() {
		return mUid;
	}
	public void setUid(String mUid) {
		this.mUid = mUid;
	}
	public String getFormhash() {
		return mFormhash;
	}
	public void setFormhash(String mFormhash) {
		this.mFormhash = mFormhash;
	}
	
	public void update(String sid, String uid, String formhash) {
		mSid = sid;
		mUid = uid;
		mFormhash = formhash;
	}
	
}
