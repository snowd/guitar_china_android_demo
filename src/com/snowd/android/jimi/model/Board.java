/**
 *  ClassName: Board.java
 *  created on 2012-3-3
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package com.snowd.android.jimi.model;

import android.text.Html;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 版块
 * @author qjyong
 */
public class Board implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7035100489331687315L;

	public static class Attr implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -4122661220481331184L;
		public static final String FID = "fid";
		public static final String FUP = "fup";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String TYPE = "type";
		public static final String TODAY = "todayposts";
		public static final String THREADS = "threads";
		public static final String POSTS = "posts";
		public static final String ISPOST = "ispost";
		public static final String ISREPLY = "isreply";
		public static final String ISPOSTIMAGE = "ispostimage";
	}
	
	
	private String groupName;
	/**ID */
	private long fid;
	/** 父版块ID */
	private long fup;
	/** 版块标题 */
	private String name;
	/** 类型 */
	private String type;
	
	private String description;
	
	private long todayPosts;
	
	private long threads;
	
	private long posts;
	
	/**ispost（是否允许当前用户发帖）1是允许0是不允许*/
	private int ispost;
	/**isreply（是否允许当前用户回帖）；1是允许0是不允许*/
	private int isreply;
	/** 发帖时是否有发图片的权限： 1是允许0是不允许*/
	private int ispostimage;
	
	private ArrayList<Board> subBoards = new ArrayList<Board>();
	
	
	public static HashMap<Long, Board> newSubBoardMap(String json){
		HashMap<Long, Board> map = new HashMap<Long, Board>();
		try {
			JSONArray arr = new JSONArray(json);
			int size = arr == null ? 0 : arr.length();
			
			for(int i = 0; i < size; i++){
				JSONObject obj = arr.getJSONObject(i);
				
				Board b = new Board();
				
				b.setFid(obj.optLong(Attr.FID));
				b.setFup(obj.optLong(Attr.FUP));
				b.setType(obj.optString(Attr.TYPE));
				b.setName(obj.optString(Attr.NAME));
				b.setIspost(obj.optInt(Attr.ISPOST));
				b.setIsreply(obj.optInt(Attr.ISREPLY));
			
				b.setIspostimage(obj.optInt(Attr.ISPOSTIMAGE));
				
				if(!"group".equals(b.getType()) && 0L != b.getFup()){
					map.put(b.getFid(), b);
				}
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	/***
	 * 查出全部板块信息
	 * */
	public static ArrayList<Board> new_SB_list(String json){
		ArrayList<Board> sb = new ArrayList<Board>();
		try {
			JSONArray arr = new JSONArray(json);
			int size = arr == null ? 0 : arr.length();
			
			for(int i = 0; i < size; i++){
				JSONObject obj = arr.getJSONObject(i);
				
				Board b = new Board();
				
				b.setFid(obj.optLong(Attr.FID));
				b.setFup(obj.optLong(Attr.FUP));
				b.setType(obj.optString(Attr.TYPE));
				b.setName(obj.optString(Attr.NAME));
				b.setDescription(obj.optString(Attr.DESCRIPTION));
				b.setTodayPosts(obj.optLong(Attr.TODAY));
				b.setThreads(obj.optLong(Attr.THREADS));
				b.setPosts(obj.optLong(Attr.POSTS));
				b.setIspost(obj.optInt(Attr.ISPOST));
				b.setIsreply(obj.optInt(Attr.ISREPLY));
				b.setIspostimage(obj.optInt(Attr.ISPOSTIMAGE));
				sb.add(b);
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return sb;
	}
	
	public static ArrayList<Board> newBoardList(String json){
		ArrayList<Board> group = new ArrayList<Board>();
		
		try {
			JSONArray arr = new JSONArray(json);
			int size = arr == null ? 0 : arr.length();
			
			ArrayList<Board> sub = new ArrayList<Board>();
			for(int i = 0; i < size; i++){
				JSONObject obj = arr.getJSONObject(i);
				
				Board b = new Board();
				
				b.setFid(obj.optLong(Attr.FID));
				b.setFup(obj.optLong(Attr.FUP));
				b.setType(obj.optString(Attr.TYPE));
				b.setName(Html.fromHtml(obj.optString(Attr.NAME)).toString());
				b.setDescription(Html.fromHtml(obj.optString(Attr.DESCRIPTION))
						.toString());
				b.setIspost(obj.optInt(Attr.ISPOST));
				b.setIsreply(obj.optInt(Attr.ISREPLY));
				b.setIspostimage(obj.optInt(Attr.ISPOSTIMAGE));
				b.setTodayPosts(obj.optLong(Attr.TODAY));
				b.setThreads(obj.optLong(Attr.THREADS));
				b.setPosts(obj.optLong(Attr.POSTS));
				
				if("group".equals(b.getType()) && 0L == b.getFup()){
					group.add(b);
				} else {
					sub.add(b);
				}
			}
			
			for (Board board : sub) {
				for (Board g : group) {
					if(board.getFup() == g.getFid()){
						g.getSubBoards().add(board);
					}
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return group;
	}
	
	public static ArrayList<Board> mainBoardList(String json) {
		ArrayList<Board> mainList = new ArrayList<Board>();
		ArrayList<Board> group = newBoardList(json);
		for (Board g : group) {
			ArrayList<Board> subs = g.getSubBoards();
			subs.get(0).groupName = g.name;
			mainList.addAll(subs);
		}
		return mainList;
	}

	public ArrayList<Board> getSubBoards() {
		return subBoards;
	}

	public void setSubBoards(ArrayList<Board> subBoards) {
		this.subBoards = subBoards;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public long getFid() {
		return fid;
	}

	public void setFid(long fid) {
		this.fid = fid;
	}

	public long getFup() {
		return fup;
	}

	public void setFup(long fup) {
		this.fup = fup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getIspost() {
		return ispost;
	}

	public void setIspost(int ispost) {
		this.ispost = ispost;
	}

	public int getIsreply() {
		return isreply;
	}

	public void setIsreply(int isreply) {
		this.isreply = isreply;
	}

	public int getIspostimage() {
		return ispostimage;
	}

	public void setIspostimage(int ispostimage) {
		this.ispostimage = ispostimage;
	}
	
	public long getTodayPosts() {
		return todayPosts;
	}

	public void setTodayPosts(long todayPosts) {
		this.todayPosts = todayPosts;
	}

	public long getThreads() {
		return threads;
	}

	public void setThreads(long threads) {
		this.threads = threads;
	}

	public long getPosts() {
		return posts;
	}

	public void setPosts(long posts) {
		this.posts = posts;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "Board [fid=" + fid + ", fup=" + fup + ", name=" + name
				+ ", type=" + type + ", ispost=" + ispost + ", isreply="
				+ isreply + ", ispostimage=" + ispostimage + ", subBoards="
				+ subBoards + "]";
	}
	
}
