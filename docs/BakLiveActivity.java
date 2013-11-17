/**
 *  ClassName: LiveActivity.java
 *  created on 2012-2-23
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.live;

import java.util.ArrayList;

import net.shopnc.android.R;
import net.shopnc.android.adapter.TopicListViewAdapter;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.handler.RemoteDataLoader2;
import net.shopnc.android.model.PushData;
import net.shopnc.android.model.Topic;
import net.shopnc.android.ui.BaseActivity;
import net.shopnc.android.ui.forum.TopicDetailActivity;
import net.shopnc.android.widget.MyFooterListView;

import org.apache.http.HttpStatus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * "生活"界面
 * @author qjyong
 */
public class LiveActivity extends BaseActivity {
	private MyApp myApp;
	private ImageButton btn_right;
	private ImageButton btn_left;
	
	/** 房产 */
	private RadioButton btn_live_house;
	/** 家装 */
	private RadioButton btn_live_housedecorate;
	/** 美食 */
	private RadioButton btn_live_food;
	/** 汽车 */
	private RadioButton btn_live_car;
	/** 婚嫁 */
	private RadioButton btn_live_marriage;
	
	/** 当前选中的二级栏目ID*/
	private int curr_banner_id;
	private int pagesize;
	private int house_pageno = 1;
	private int housedecorate_pageno = 1;
	private int food_pageno = 1;
	private int car_pageno = 1;
	private int marriage_pageno = 1;
	private MyFooterListView lv_topics;
	private TopicListViewAdapter adapter_house;
	private TopicListViewAdapter adapter_housedecorate;
	private TopicListViewAdapter adapter_food;
	private TopicListViewAdapter adapter_car;
	private TopicListViewAdapter adapter_marriage;
	
	private ArrayList<Topic> houseDatas = new ArrayList<Topic>();
	private ArrayList<Topic> housedecorateDatas = new ArrayList<Topic>();
	private ArrayList<Topic> foodDatas = new ArrayList<Topic>();
	private ArrayList<Topic> carDatas = new ArrayList<Topic>();
	private ArrayList<Topic> marriageDatas = new ArrayList<Topic>();
	
	private RemoteDataLoader2 handler = new RemoteDataLoader2(){
		@Override
		public void handleMessage(Message msg) {
			doMsg(msg);
		}
	};
	
	private void doMsg(Message msg){
		progressDialog.dismiss();
		lv_topics.setLoadingStop(); //停止"网络数据请求"文字提示
		if(HttpStatus.SC_NOT_MODIFIED == msg.what){ //再次查看原来查看过的二级栏目时
			switch(curr_banner_id){
			case R.id.btn_live_house:
				lv_topics.setAdapter(adapter_house);
				break;
			case R.id.btn_live_housedecorate:
				lv_topics.setAdapter(adapter_housedecorate);
				break;
			case R.id.btn_live_food:
				lv_topics.setAdapter(adapter_food);
				break;
			case R.id.btn_live_car:
				lv_topics.setAdapter(adapter_car);
				break;
			case R.id.btn_live_marriage:
				lv_topics.setAdapter(adapter_marriage);
				break;
			}
		}else if(HttpStatus.SC_OK == msg.what){
			String jsonDatas = (String)msg.obj;
			switch(curr_banner_id){
			case R.id.btn_live_house:
				if(house_pageno > 1){ 
					houseDatas.addAll(Topic.newInstanceList(jsonDatas));
				}else{ //刷新时显示的是第一页
					houseDatas = Topic.newInstanceList(jsonDatas);
				}
				adapter_house = new TopicListViewAdapter(LiveActivity.this, houseDatas);
				adapter_house.setHasMore(msg.getData().getBoolean("hasMore"));
				lv_topics.setAdapter(adapter_house);
				break;
			case R.id.btn_live_housedecorate:
				if(housedecorate_pageno > 1){ 
					housedecorateDatas.addAll(Topic.newInstanceList(jsonDatas));
				}else{ //刷新时显示的是第一页
					housedecorateDatas = Topic.newInstanceList(jsonDatas);
				}
				adapter_housedecorate = new TopicListViewAdapter(LiveActivity.this, housedecorateDatas);
				adapter_housedecorate.setHasMore(msg.getData().getBoolean("hasMore"));
				lv_topics.setAdapter(adapter_housedecorate);
				break;
			case R.id.btn_live_food:
				if(food_pageno > 1){ 
					foodDatas.addAll(Topic.newInstanceList(jsonDatas));
				}else{ //刷新时显示的是第一页
					foodDatas = Topic.newInstanceList(jsonDatas);
				}
				adapter_food = new TopicListViewAdapter(LiveActivity.this, foodDatas);
				adapter_food.setHasMore(msg.getData().getBoolean("hasMore"));
				lv_topics.setAdapter(adapter_food);
				break;
			case R.id.btn_live_car:
				if(car_pageno > 1){ 
					carDatas.addAll(Topic.newInstanceList(jsonDatas));
				}else{ //刷新时显示的是第一页
					carDatas = Topic.newInstanceList(jsonDatas);
				}
				adapter_car = new TopicListViewAdapter(LiveActivity.this, carDatas);
				adapter_car.setHasMore(msg.getData().getBoolean("hasMore"));
				lv_topics.setAdapter(adapter_car);
				break;
			case R.id.btn_live_marriage:
				if(marriage_pageno > 1){ 
					marriageDatas.addAll(Topic.newInstanceList(jsonDatas));
				}else{ //刷新时显示的是第一页
					marriageDatas = Topic.newInstanceList(jsonDatas);
				}
				adapter_marriage = new TopicListViewAdapter(LiveActivity.this, marriageDatas);
				adapter_marriage.setHasMore(msg.getData().getBoolean("hasMore"));
				lv_topics.setAdapter(adapter_marriage);
				break;
			}
		}else{
			Toast.makeText(LiveActivity.this, "服务器响应数据有误!", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.live);
		myApp = (MyApp)this.getApplicationContext();
		pagesize = myApp.getPageSize();
		
		//刷新
		btn_left = (ImageButton)this.findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				progressDialog.show();
				switch(curr_banner_id){
				case R.id.btn_live_house:
					house_pageno = 1;
					handler.executeLoadData(Constants.URL_LIVE_HOUSE, LiveActivity.this.pagesize, house_pageno);
					break;
				case R.id.btn_live_housedecorate:
					housedecorate_pageno = 1;
					handler.executeLoadData(Constants.URL_LIVE_HOSTE_DECORATE, LiveActivity.this.pagesize, housedecorate_pageno);
					break;
				case R.id.btn_live_food:
					food_pageno = 1;
					handler.executeLoadData(Constants.URL_LIVE_FOOT, LiveActivity.this.pagesize, food_pageno);
					break;
				case R.id.btn_live_car:
					car_pageno = 1;
					handler.executeLoadData(Constants.URL_LIVE_CAR, LiveActivity.this.pagesize, car_pageno);
					break;
				case R.id.btn_live_marriage:
					marriage_pageno = 1;
					handler.executeLoadData(Constants.URL_LIVE_MARRIAGE, LiveActivity.this.pagesize, marriage_pageno);
					break;
				}
			}
		});
		//退出
		btn_right = (ImageButton)this.findViewById(R.id.btn_right);
		btn_right.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				LiveActivity.this.showExitDialog();
			}
		});
		
		btn_live_house = (RadioButton)this.findViewById(R.id.btn_live_house);
		btn_live_housedecorate = (RadioButton)this.findViewById(R.id.btn_live_housedecorate);
		btn_live_food = (RadioButton)this.findViewById(R.id.btn_live_food);
		btn_live_car = (RadioButton)this.findViewById(R.id.btn_live_car);
		btn_live_marriage = (RadioButton)this.findViewById(R.id.btn_live_marriage);
		
		MyRadioButtonClickListener listener = new MyRadioButtonClickListener();
		btn_live_house.setOnClickListener(listener);
		btn_live_house.performClick();//手动触发一次
		
		btn_live_housedecorate.setOnClickListener(listener);
		btn_live_food.setOnClickListener(listener);
		btn_live_car.setOnClickListener(listener);
		btn_live_marriage.setOnClickListener(listener);
		
		lv_topics = (MyFooterListView)this.findViewById(R.id.lv_topics);
		lv_topics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Toast.makeText(LiveActivity.this, "id-->" + id, 0).show();
				
				Intent intent = new Intent(LiveActivity.this, TopicDetailActivity.class);
				Topic topic = (Topic)lv_topics.getItemAtPosition(position);
				
				intent.putExtra(Topic.Attr.TID, topic.getTid());
				intent.putExtra(Topic.Attr.SUBJECT, topic.getSubject());
				//Log.d(TAG, topic.toString());
				LiveActivity.this.startActivity(intent);
			}
		});
		
		//下一頁
		lv_topics.getLoadingButton().setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				switch(curr_banner_id){
				case R.id.btn_live_house:
					if(adapter_house.isHasMore()){
						handler.executeLoadData(Constants.URL_LIVE_HOUSE, LiveActivity.this.pagesize, ++house_pageno);
					}else{
						Toast.makeText(LiveActivity.this, "已经是底页了!", Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.btn_live_housedecorate:
					if(adapter_housedecorate.isHasMore()){
						handler.executeLoadData(Constants.URL_LIVE_HOSTE_DECORATE, LiveActivity.this.pagesize, ++housedecorate_pageno);
					}else{
						Toast.makeText(LiveActivity.this, "已经是底页了!", Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.btn_live_food:
					if(adapter_food.isHasMore()){
						handler.executeLoadData(Constants.URL_LIVE_FOOT, LiveActivity.this.pagesize, ++food_pageno);
					}else{
						Toast.makeText(LiveActivity.this, "已经是底页了!", Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.btn_live_car:
					if(adapter_car.isHasMore()){
						handler.executeLoadData(Constants.URL_LIVE_CAR, LiveActivity.this.pagesize, ++car_pageno);
					}else{
						Toast.makeText(LiveActivity.this, "已经是底页了!", Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.btn_live_marriage:
					if(adapter_marriage.isHasMore()){
						handler.executeLoadData(Constants.URL_LIVE_MARRIAGE, LiveActivity.this.pagesize, ++marriage_pageno);
					}else{
						Toast.makeText(LiveActivity.this, "已经是底页了!", Toast.LENGTH_SHORT).show();
					}
					break;
				}
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		//TODO 停止数据请求
		lv_topics.setLoadingStop();
	}
	
	class MyRadioButtonClickListener implements View.OnClickListener{
		public void onClick(View v) {
			RadioButton btn = (RadioButton)v;
			switch(btn.getId()){
			case R.id.btn_live_house:
				if(curr_banner_id != R.id.btn_live_house){
					if(adapter_house == null){
						progressDialog.show();
						handler.executeLoadData(Constants.URL_LIVE_HOUSE, LiveActivity.this.pagesize, 1);
					}else{
						handler.send304();
					}
				}
				curr_banner_id = R.id.btn_live_house;
				break;
			case R.id.btn_live_housedecorate:
				if(curr_banner_id != R.id.btn_live_housedecorate){
					if(adapter_housedecorate == null){
						progressDialog.show();
						handler.executeLoadData(Constants.URL_LIVE_HOSTE_DECORATE, LiveActivity.this.pagesize, 1);
					}else{
						handler.send304();
					}
				}
				curr_banner_id = R.id.btn_live_housedecorate;
				break;
			case R.id.btn_live_food:
				if(curr_banner_id != R.id.btn_live_food){
					if(adapter_food == null){
						progressDialog.show();
						handler.executeLoadData(Constants.URL_LIVE_FOOT, LiveActivity.this.pagesize, 1);
					}else{
						handler.send304();
					}
				}
				curr_banner_id = R.id.btn_live_food;
				break;
			case R.id.btn_live_car:
				if(curr_banner_id != R.id.btn_live_car){
					if(adapter_car == null){
						progressDialog.show();
						handler.executeLoadData(Constants.URL_LIVE_CAR, LiveActivity.this.pagesize, 1);
					}else{
						handler.send304();
					}
				}
				curr_banner_id = R.id.btn_live_car;
				break;
			case R.id.btn_live_marriage:
				if(curr_banner_id != R.id.btn_live_marriage){
					if(adapter_marriage == null){
						progressDialog.show();
						handler.executeLoadData(Constants.URL_LIVE_MARRIAGE, LiveActivity.this.pagesize, 1);
					}else{
						handler.send304();
					}
				}
				curr_banner_id = R.id.btn_live_marriage;
				break;
			}
		}
	}
}
