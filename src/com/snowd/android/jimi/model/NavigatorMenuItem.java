package com.snowd.android.jimi.model;

public class NavigatorMenuItem {

	private int mId;
	private String mName;
	private int mIconResId;
	private boolean mEnable;
	
	public NavigatorMenuItem() {}
	
	public NavigatorMenuItem(String name, int id, int icon, boolean enable) {
		mName = name;
		mId = id;
		mIconResId = icon;
		mEnable = enable;
	}
	
	public int getId() {
		return mId;
	}
	public void setId(int mId) {
		this.mId = mId;
	}
	public String getName() {
		return mName;
	}
	public void setName(String mName) {
		this.mName = mName;
	}
	public int getIconResId() {
		return mIconResId;
	}
	public void setIconResId(int mIconResId) {
		this.mIconResId = mIconResId;
	}
	public boolean isEnable() {
		return mEnable;
	}
	public void setEnable(boolean mEnable) {
		this.mEnable = mEnable;
	}
	
}
