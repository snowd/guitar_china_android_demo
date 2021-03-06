/**
 *  ClassName: TopicsDataHandler.java
 *  created on 2012-3-3
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package com.snowd.android.jimi.rpc;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.snowd.android.jimi.common.Constants;
import com.snowd.android.jimi.common.HttpHelper;
import com.snowd.android.jimi.model.ResponseData;
import com.snowd.android.jimi.model.SessionHolder;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 用于发送HTTP请求并处理响应返回的数据的Handler
 * @author qjyong
 */
public class RemoteHandler {
	public static final String TAG = "RemoteDataLoader";
	private static final String _CODE = "code";
	private static final String _DATAS = "datas"; 
	private static final String _HASMORE = "haseMore"; 
	private static final String _COUNT = "count";
//	private static final String _PAGE_INFO = "page";
	private static final String _RESULT = "result";
	private static final String _URL = "url";
	//线程池
	//private ExecutorService pool = Executors.newCachedThreadPool();
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(6, 30, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	private RemoteHandler(){}
	
	public interface Callback {
		/**
		 *  HTTP响应完成的回调方法 
		 * @param data 响应返回的数据对象
		 */
		public void dataLoaded(ResponseData resp, Object data);
		public Serializable dataPrepared(int code, String resp);
	}
	
	public interface StringCallback{
		
		public void dataLoaded(String str);
	}
	
	/**
	 * 用户发帖时获取主题的分类<br/>
	 * @param uid
	 * @param callback
	 */
	public static String loadtopictype(long fid){
		 	
		String json="";
		try {
			json = HttpHelper.get(Constants.URL_TOPIC_TYPE + fid);
			Log.d(TAG, "topic type===>"+Constants.URL_TOPIC_TYPE + fid);
			//注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
//			json = json.replaceAll("\\x0a|\\x0d","");			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  json;		
	}
		
	
	/**
	 * 异步的回帖
	 * @param fid
	 * @param tid
	 * @param params
	 * @return
	 */
	public static void replyTopic(long fid, long tid, HashMap<String, String> params, HashSet<String> uploading_img, final Callback callback){
		String url = Constants.URL_TOPIC_REPLY + fid + "&tid=" + tid;
		int size = uploading_img == null ? 0 : uploading_img.size();
		HashMap<String, File> fileMap = new HashMap<String, File>();
		
		//有图片还需要添加get参数img=1, 1代表数量
		if(size > 0){
			url +="&img=1";
			int i = 1;
			for (String str : uploading_img) {
				File file = new File(Constants.CACHE_DIR_UPLOADING_IMG, str);
				fileMap.put(file.getName(), file);
				
				i++;
				params.put("image"+ i, file.getName());
			}
		}
		params.put("status", "a");
		Log.d(TAG, "replyTopic:url-->" + url);
		Log.d(TAG, "replyTopic:parm-->" + params.toString());
		Log.d(TAG, "replyTopic:file-->" + fileMap.toString());
		
		asyncMultipartPost(url, params, fileMap, callback);
	}
	/**
	 * 异步的回复
	 * @param fid
	 * @param tid
	 * @param params
	 * @return
	 */
	public static void quoteTopic(long fid, long tid, HashMap<String, String> params, HashSet<String> uploading_img, final Callback callback){
		String url = Constants.URL_TOPIC_REPLY + fid + "&tid=" + tid;
		int size = uploading_img == null ? 0 : uploading_img.size();
		HashMap<String, File> fileMap = new HashMap<String, File>();
		
		//有图片还需要添加get参数img=1, 1代表数量
		if(size > 0){
			url +="&img=1";
			int i = 1;
			for (String str : uploading_img) {
				File file = new File(Constants.CACHE_DIR_UPLOADING_IMG, str);
				fileMap.put(file.getName(), file);
				
				i++;
				params.put("image"+ i, file.getName());
			}
		}
		params.put("status", "a");
		Log.d(TAG, "replyTopic:url-->" + url);
		Log.d(TAG, "replyTopic:parm-->" + params.toString());
		Log.d(TAG, "replyTopic:file-->" + fileMap.toString());
		url +="&quote=1";
		asyncMultipartPost(url, params, fileMap, callback);
	}
	/**
	 * 异步发帖
	 * @param fid
	 * @param params
	 * @return
	 */
	public static void sendTopic(long fid, HashMap<String, String> params, HashSet<String> uploading_img, Long typeid,final Callback callback){
		String url = Constants.URL_TOPIC_SEND + fid;
		
		int size = uploading_img == null ? 0 : uploading_img.size();
		HashMap<String, File> fileMap = new HashMap<String, File>();
		
		//有图片还需要添加get参数img=1, 1代表数量
		if(size > 0){
			url +="&img=1";
			int i = 0;
			for (String str : uploading_img) {
				File file = new File(Constants.CACHE_DIR_UPLOADING_IMG, str);
				fileMap.put(file.getName(), file);
				
				i++;
				params.put("image"+ i, file.getName());
			}
		}
		params.put("typeid", String.valueOf(typeid));
		params.put("status", "a");
		Log.d(TAG, "sendTopic:url-->" + url);
		Log.d(TAG, "sendTopic:parm-->" + params.toString());
		Log.d(TAG, "sendTopic:file-->" + fileMap.toString());
		
		asyncMultipartPost(url, params, fileMap, callback);
	}
	
//	/***
//	 * 加载远程表情列表
//	 * @return
//	 */
//	public static ArrayList<Smiley> loadSmiley(){
//		ArrayList<Smiley> list = new ArrayList<Smiley>();
//		try {
//			String json = HttpHelper.get(Constants.URL_SMILEY);
//			JSONObject obj = new JSONObject(json);
//			if(null != obj && obj.has(_DATAS)){
//				JSONArray array = obj.getJSONArray(_DATAS);
//
//				int size = array == null ? 0 : array.length();
//				for(int i = 0; i < size; i++){
//					Smiley f = new Smiley();
//					JSONObject o = array.getJSONObject(i);
//
//					if(o.has(_CODE)){
//						String code = o.getString(_CODE);
//						code = code.replaceAll("\\x0a", "").replaceAll("\\x0d","");
//						f.setCode(code);
//					}
//					if(o.has(_URL)){
//						final String url = o.getString(_URL);
//						f.setPath(url);
//						f.setLocalName(IOHelper.getName(url));
//						Log.d(TAG, f.toString());
//						//下载
//						final File dest = new File(Constants.CACHE_DIR_SMILEY + "/" + f.getLocalName());
//						System.out.println("f.getLocalName()===>"+f.getLocalName());
//						if(!dest.exists()){
//							threadPool.execute(new Runnable() {
//								@Override
//								public void run() {
//									try {
//										HttpHelper.download(url, dest);
//										Log.d(TAG, dest.getAbsolutePath());
//									} catch (IOException e) {
//										e.printStackTrace();
//									}
//								}
//							});
//						}
//					}
//					list.add(f);
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return list;
//	}
	
	/**
	 * 异步的回复
	 * @param message
	 * @param uid
	 * @param username
	 */
	public static void feedback(String message, String uid, String username, final Callback callback){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("message", message);
		params.put("uid", uid);
		params.put("username", username);
		
		asyncPost(Constants.URL_FEEDBACK, params, callback);
		
	}
	
	/**
	 * 异步的登录验证
	 */
	public static void asyncLogin(final String author, final String md5_pwd, final Callback callback){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", author);
		params.put("password", md5_pwd);
		asyncPost(Constants.URL_LOGIN, params, callback);
	}
	
	/**
	 * 异步的登录验证
	 */
	public static void asyncLoginQuestions(final String author,
			final String loginauth, final String questionId,
			final String answer, final Callback callback) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", author);
		params.put("questionid", questionId);
		params.put("answer", answer);
		params.put("loginauth", loginauth);
		asyncPost(Constants.URL_LOGIN, params, callback);
	}
		///////////////////////////////////////////////////////////////////////////
	
	/**
	 * 异步GET请求封装
	 * @param url
	 * @param callback
	 */
	public static void asyncGet(final String url, final Callback callback){
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				ResponseData data = new ResponseData();
				data.setCode(msg.what);
				data.setHasMore(msg.getData().getBoolean(_HASMORE));
				data.setJson((String)msg.obj);
				data.setResult(msg.getData().getString(_RESULT));
				data.setCount(msg.getData().getLong(_COUNT));
				Log.d(TAG, data.toString());
				
				callback.dataLoaded(data, null);
			}
		};
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage(HttpStatus.SC_OK);
				msg.getData().putBoolean(_HASMORE, false);
				
				Log.d(TAG, url);
				try {
					String surl = url;
					if (SessionHolder.isLogin()) {
						surl += "&sid=" + SessionHolder.obtainSid();
					}
					String json = HttpHelper.get(surl);
					
					//注意:目前服务器返回的JSON数据串中会有特殊字符（换行、回车）。需要处理一下
//					json = json.replaceAll("\\x0a|\\x0d","");
					
					JSONObject obj = new JSONObject(json);
					if(null != obj && obj.has(_CODE)){
						msg.what = Integer.valueOf(obj.getString(_CODE));
						
						if(obj.has(_DATAS)){
							JSONArray array = obj.getJSONArray(_DATAS);
							msg.obj = array.toString();
						}
						if(obj.has(_HASMORE)){
							msg.getData().putBoolean(_HASMORE, obj.getBoolean(_HASMORE));
						}
						if(obj.has(_COUNT)){
							msg.getData().putLong(_COUNT, obj.getLong(_COUNT));
						}
						
						if(obj.has(_RESULT)){
							msg.getData().putString(_RESULT, obj.getString(_RESULT));
						}
					}
				} catch (IOException e) {
					msg.what = HttpStatus.SC_REQUEST_TIMEOUT;
					e.printStackTrace();
				} catch (JSONException e) {
					msg.what = HttpStatus.SC_INTERNAL_SERVER_ERROR;
					e.printStackTrace();
				}
				
				handler.sendMessage(msg);
			}
		});
	}
	
	/**
	 * 异步get分页数据请求封装
	 * @param url
	 * @param pagesize
	 * @param pageno
	 * @param callback
	 */
	public static void asyncGetList(final String url, final int pagesize, final int pageno, final Callback callback){
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				ResponseData data = new ResponseData();
				data.setCode(msg.what);
				data.setHasMore(msg.getData().getBoolean(_HASMORE));
				data.setJson((String)msg.obj);
				data.setData(msg.getData().getSerializable(_DATAS));
				data.setResult(msg.getData().getString(_RESULT));
				data.setCount(msg.getData().getLong(_COUNT));
				Log.d(TAG, data.toString());
				
				callback.dataLoaded(data, msg.getData().getSerializable(_DATAS));
			}
		};
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				
				Message msg = handler.obtainMessage(HttpStatus.SC_OK);
				msg.getData().putBoolean(_HASMORE, false);
				
				String realUrl = url + "&" + Constants.PARAM_PAGESIZE + "=" + pagesize 
						  + "&" + Constants.PARAM_PAGENO + "=" + pageno;
				
				Log.d(TAG, realUrl);
				try {
					Thread.sleep(1000);
					
					if (SessionHolder.isLogin()) {
						realUrl += "&sid=" + SessionHolder.obtainSid();
					}
					String json = HttpHelper.get(realUrl);
					Log.d(TAG, json);
					
					//注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
//					json = json.replaceAll("\\x0a|\\x0d","");
					Serializable obj = callback.dataPrepared(HttpStatus.SC_OK, json);
					if(null != obj){
						msg.getData().putSerializable(_DATAS, obj);
					}

				} catch (IOException e) {
					msg.what = HttpStatus.SC_REQUEST_TIMEOUT;
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				handler.sendMessage(msg);
			}
		});
	}
	
	/**
	 * 异步get分页数据请求封装
	 * @param url
	 * @param pagesize
	 * @param pageno
	 * @param callback
	 */
	public static void asyncGet(final String url, final int pagesize,
			final int pageno, final Callback callback) {
		asyncGet(url, _DATAS, pagesize, pageno, callback);
	}
	
	/**
	 * 异步get分页数据请求封装
	 * @param url
	 * @param pagesize
	 * @param pageno
	 * @param callback
	 */
	public static void asyncGet(final String url, final String datakey,
			final int pagesize, final int pageno, final Callback callback) {
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				ResponseData data = new ResponseData();
				data.setCode(msg.what);
				data.setHasMore(msg.getData().getBoolean(_HASMORE));
				data.setJson((String)msg.obj);
				data.setResult(msg.getData().getString(_RESULT));
				data.setCount(msg.getData().getLong(_COUNT));
				Log.d(TAG, data.toString());
				
				callback.dataLoaded(data, msg.getData().getSerializable(_DATAS));
			}
		};
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				
				Message msg = handler.obtainMessage(HttpStatus.SC_OK);
				msg.getData().putBoolean(_HASMORE, false);
				
				String realUrl = url + "&" + Constants.PARAM_PAGESIZE + "=" + pagesize 
						+ "&" + Constants.PARAM_PAGENO + "=" + pageno;
				
				Log.d(TAG, realUrl);
				try {
					Thread.sleep(1000);
					
					if (SessionHolder.isLogin()) {
						realUrl += "&sid=" + SessionHolder.obtainSid();
					}
					String json = HttpHelper.get(realUrl);
					Log.d(TAG, json);
					
//					//注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
//					json = json.replaceAll("\\x0a|\\x0d","");
					
					Serializable obj = callback.dataPrepared(HttpStatus.SC_OK, json);
					if(null != obj){
						msg.getData().putSerializable(_DATAS, obj);
					}
//					JSONObject obj = new JSONObject(json);
//					if(null != obj/* && obj.has(_CODE)*/){
////						msg.what = Integer.valueOf(obj.getString(_CODE));
//					
//						if (obj.has(datakey)) {
//							JSONArray array = obj.getJSONArray(datakey);
//							msg.obj = array.toString();
//
//							if (pagesize == array.length()) {
//								msg.getData().putBoolean(_HASMORE, true);
//							}
//						}
//
//						if (obj.has(_COUNT)) {
//							msg.getData().putLong(_COUNT,
//								Long.valueOf(obj.getString(_COUNT)));
//						} else if (obj.has(_PAGE_INFO)) {
//							JSONObject page = obj.getJSONObject("page");
//							msg.getData().putLong(_COUNT,
//								Long.valueOf(page.getString("max_pages"))
//									* pagesize);
//						}
//						if (obj.has(_RESULT)) {
//							msg.getData().putString(_RESULT,
//									obj.getString(_RESULT));
//						}
//					}
				} catch (IOException e) {
					msg.what = HttpStatus.SC_REQUEST_TIMEOUT;
					e.printStackTrace();
//				} catch (JSONException e) {
//					msg.what = HttpStatus.SC_INTERNAL_SERVER_ERROR;
//					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				handler.sendMessage(msg);
			}
		});
	}
	
	/**
	 * 同步GET请求封装
	 * @param url
	 * @param pagesize
	 * @param pageno
	 * @return
	 */
	public static ResponseData get(final String url){
		ResponseData rd = new ResponseData();
		
		Log.d(TAG, url);
		try {
			String json = HttpHelper.get(url);
			
			//注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
//			json = json.replaceAll("\\x0a|\\x0d","");
			
			JSONObject obj = new JSONObject(json);
			if(null != obj && obj.has(_CODE)){
				rd.setCode(obj.getInt(_CODE));
				
				if(obj.has(_DATAS)){
					JSONArray array = obj.getJSONArray(_DATAS);
					rd.setJson(array.toString());
				}
				if(obj.has(_HASMORE)){
					rd.setHasMore(obj.getBoolean(_HASMORE));
				}
				
				if(obj.has(_RESULT)){
					rd.setResult(obj.getString(_RESULT));
				}
				
				if(obj.has(_COUNT)){
					rd.setCount(obj.getLong(_COUNT));
				}
			}
		} catch (IOException e) {
			rd.setCode(HttpStatus.SC_REQUEST_TIMEOUT);
			e.printStackTrace();
		} catch (JSONException e) {
			rd.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		
		return rd;
	}
	
	/**
	 * 同步GET请求封装
	 * @param url
	 * @param pagesize
	 * @param pageno
	 * @return
	 */
	public static ResponseData get(final String url, final int pagesize, final int pageno){
		ResponseData rd = new ResponseData();
		
		String realUrl = url + "&" + Constants.PARAM_PAGESIZE + "=" + pagesize 
				  + "&" + Constants.PARAM_PAGENO + "=" + pageno;
		
		Log.d(TAG, realUrl);
		try {
			String json = HttpHelper.get(realUrl);
			
			//注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
//			json = json.replaceAll("\\x0a|\\x0d","");
			
			JSONObject obj = new JSONObject(json);
			if(null != obj && obj.has(_CODE)){
				rd.setCode(obj.getInt(_CODE));
				
				if(obj.has(_DATAS)){
					JSONArray array = obj.getJSONArray(_DATAS);
					rd.setJson(array.toString());
					
					if(pagesize == array.length()){
						rd.setHasMore(true);
					}
				}
				if(obj.has(_HASMORE)){
					rd.setHasMore(obj.getBoolean(_HASMORE));
				}
				
				if(obj.has(_RESULT)){
					rd.setResult(obj.getString(_RESULT));
				}
				
				if(obj.has(_COUNT)){
					rd.setCount(obj.getLong(_COUNT));
				}
			}
		} catch (IOException e) {
			rd.setCode(HttpStatus.SC_REQUEST_TIMEOUT);
			e.printStackTrace();
		} catch (JSONException e) {
			rd.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		
		return rd;
	}
	
	public static ResponseData post(final String url, final HashMap<String, String> params){
		ResponseData rd = new ResponseData();
		try {
			String json = HttpHelper.post(url, params);
			
			//注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
//			json = json.replaceAll("\\x0a|\\x0d","");
			
			JSONObject obj = new JSONObject(json);
			if(null != obj && obj.has(_CODE)){
				rd.setCode(obj.getInt(_CODE));
				
				if(obj.has(_DATAS)){
					JSONArray array = obj.getJSONArray(_DATAS);
					rd.setJson(array.toString());
				}
				if(obj.has(_HASMORE)){
					rd.setHasMore(obj.getBoolean(_HASMORE));
				}
				
				if(obj.has(_RESULT)){
					rd.setResult(obj.getString(_RESULT));
				}
				
				if(obj.has(_COUNT)){
					rd.setCount(obj.getLong(_COUNT));
				}
			}
		} catch (IOException e) {
			rd.setCode(HttpStatus.SC_REQUEST_TIMEOUT);
			e.printStackTrace();
		} catch (JSONException e) {
			rd.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		
		return rd;
	}
	
	/**
	 * 异步的POST请求
	 * @param url
	 * @param params
	 * @param callback
	 */
	public static void asyncPost(final String url, final HashMap<String, String> params, final Callback callback){
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				ResponseData data = new ResponseData();
				data.setCode(msg.what);
				data.setHasMore(msg.getData().getBoolean(_HASMORE));
				data.setJson((String)msg.obj);
				data.setResult(msg.getData().getString(_RESULT));
				data.setCount(msg.getData().getLong(_COUNT));
				
				Log.d(TAG, data.toString());
				
				callback.dataLoaded(data, msg.obj);
			}
		};
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage(HttpStatus.SC_OK);
				msg.getData().putBoolean("hasMore", false);
				try {
					String json = HttpHelper.post(url, params);
					//注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
//					json = json.replaceAll("\\x0a|\\x0d","");
					msg.obj = json;
//					JSONObject obj = new JSONObject(json);
//					if(null != obj && obj.has(_CODE)){
//						msg.what = Integer.valueOf(obj.getString(_CODE));
//						
//						if(obj.has(_DATAS)){
//							JSONArray array = obj.getJSONArray(_DATAS);
//							msg.obj = array.toString();
//						}
//						
//						if(obj.has(_RESULT)){
//							msg.getData().putString(_RESULT, obj.getString(_RESULT));
//						}
//					}
				} catch (IOException e) {
					msg.what = HttpStatus.SC_REQUEST_TIMEOUT;
					e.printStackTrace();
				} catch (Exception e) {
					msg.what = HttpStatus.SC_INTERNAL_SERVER_ERROR;
					e.printStackTrace();
				}
				
				handler.sendMessage(msg);
			}
		});
	}
	
	/**
	 * 异步的多消息体POST请求封装
	 * @param url
	 * @param params
	 * @param fileMap
	 * @param callback
	 */
	public static void asyncMultipartPost(final String url, final HashMap<String, String> params, final HashMap<String, File> fileMap, final Callback callback){
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				ResponseData data = new ResponseData();
				data.setCode(msg.what);
				data.setHasMore(msg.getData().getBoolean(_HASMORE));
				data.setJson((String)msg.obj);
				data.setResult(msg.getData().getString(_RESULT));
				data.setCount(msg.getData().getLong(_COUNT));
				Log.d(TAG, data.toString());
				
				callback.dataLoaded(data, msg.getData().getSerializable(_DATAS));
			}
		};
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage(HttpStatus.SC_OK);
				msg.getData().putBoolean("hasMore", false);
				Log.d(TAG, url);
				try {
					String json = HttpHelper.multipartPost(url, params, fileMap);
					
					//注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
//					json = json.replaceAll("\\x0a|\\x0d","");
					
					JSONObject obj = new JSONObject(json);
					if(null != obj && obj.has(_CODE)){
						msg.what = Integer.valueOf(obj.getString(_CODE));
						
						if(obj.has(_DATAS)){
							JSONArray array = obj.getJSONArray(_DATAS);
							msg.obj = array.toString();
						}
						
						if(obj.has(_RESULT)){
							msg.getData().putString(_RESULT, obj.getString(_RESULT));
						}
					}
				} catch (IOException e) {
					msg.what = HttpStatus.SC_REQUEST_TIMEOUT;
					e.printStackTrace();
				} catch (JSONException e) {
					msg.what = HttpStatus.SC_INTERNAL_SERVER_ERROR;
					e.printStackTrace();
				}
				
				handler.sendMessage(msg);
			}
		});
	}
	
	/**
	 * 请求二级栏目名称
	 * @param url
	 * @throws JSONException 
	 */
	public static String loadTopName() throws JSONException{		 
		
		String json="";
		try {
			json = HttpHelper.get(Constants.URL_TOP_NAME);
			Log.d(TAG, "top_name===>"+Constants.URL_TOP_NAME);
			//注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
//			json = json.replaceAll("\\x0a|\\x0d","");			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  json;		
	}


	/**
	 * 异步统计安装数据
	 * @param 
	 * @param 
	 * @return
	 */
	public static void intall(final String install,final String hardtype,final Callback callback){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("install", install);
		params.put("hardtype", hardtype);
		asyncPost(Constants.URL_INSTALL, params, callback);
	}	

}



