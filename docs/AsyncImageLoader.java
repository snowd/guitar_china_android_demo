package net.shopnc.android.handler;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.shopnc.android.common.BitmapHelper;
import net.shopnc.android.common.HttpHelper;

import org.apache.http.HttpStatus;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 用于异步获取HTTP请求返回的图片的Handler
 * @author qjyong
 */
@Deprecated
public class AsyncImageLoader {
	public static final String TAG = "AsyncImageLoader";
	private static ConcurrentHashMap<String, SoftReference<Bitmap>> imageCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>();
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(6,30, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	private AsyncImageLoader(){}
	
	public static void loadDrawable(final String url, final DrawableCallback callback) {
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(HttpStatus.SC_OK == msg.what){
					callback.imageLoaded((Drawable)msg.obj, url);
				}
			}
		};
		
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Bitmap bmp = HttpHelper.downloadBitmap(url);
					Drawable d = new BitmapDrawable(bmp);
					bmp.recycle();

					Message msg = handler.obtainMessage(HttpStatus.SC_OK);
					msg.obj = d;
					handler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 异步加载URL图片（带内存缓存）
	 * @param url 图片的URL
	 * @param w 宽度
	 * @param h 高度
	 * @param callback 加载完成后的回调接口
	 */
	public static void loadBmp(final String url, final ImageCallback callback) {
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(HttpStatus.SC_OK == msg.what){
					callback.imageLoaded((Bitmap)msg.obj, url);
				}
			}
		};
		
		SoftReference<Bitmap> softReference = imageCache.get(url);
		if (null != softReference && null != softReference.get()) { //内存缓存中有
			Message msg = handler.obtainMessage(HttpStatus.SC_OK);
			msg.obj = softReference.get();
			handler.sendMessage(msg);
			
			Log.d(TAG, "in cache-->" + url);
			return;
		}else{ //如果为空，需要将其从缓存中删除（其bitmap对象已被回收释放，需要重新加载）
			imageCache.remove(url);
		}
		/**
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			File f = new File(Constants.CACHE_DIR, IOHelper.getName(url));
			if(f.exists()){
				Message msg = handler.obtainMessage(HttpStatus.SC_OK);
				msg.obj = BitmapFactory.decodeFile(f.getAbsolutePath());
				handler.sendMessage(msg);
				return;
			}
		}*/
			
		threadPool.execute(new Runnable(){
			@Override
			public void run() {
				try {
					Bitmap bmp = null; 
					/**
					if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
						//写到SD卡缓存中
						String name = IOHelper.getName(url);
						File dest = new File(Constants.CACHE_DIR, name);
						HttpHelper.download(url, dest);

						bmp = BitmapFactory.decodeFile(dest.getAbsolutePath());
					}else{
						bmp = HttpHelper.downloadBitmap(url);
					}*/
					
					bmp = HttpHelper.downloadBitmap(url);
					imageCache.put(url, new SoftReference<Bitmap>(bmp)); //放入内存缓存
					Message msg = handler.obtainMessage(HttpStatus.SC_OK);
					msg.obj = bmp;
					Log.d(TAG, "bmp.width=" + bmp.getWidth() +",bmp.height" + bmp.getHeight() + "-->" + url);
					handler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 异步加载URL图片（带内存缓存）并缩小到指定宽度或高度
	 * @param url 图片的URL
	 * @param w 宽度
	 * @param h 高度
	 * @param callback 加载完成后的回调接口
	 */
	public static void loadBmp(final String url,final int w, final int h,final ImageCallback callback) {
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(HttpStatus.SC_OK == msg.what){
					callback.imageLoaded((Bitmap)msg.obj, url);
				}
			}
		};
		
		SoftReference<Bitmap> softReference = imageCache.get(url);
		if (null != softReference && null != softReference.get()) { //内存缓存中有
			Message msg = handler.obtainMessage(HttpStatus.SC_OK);
			msg.obj = softReference.get();
			handler.sendMessage(msg);
			
			Log.d(TAG, "in cache-->" + url);
			return;
		}else{ //如果为空，需要将其从缓存中删除（其bitmap对象已被回收释放，需要重新加载）
			imageCache.remove(url);
		}
		/**
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			File f = new File(Constants.CACHE_DIR, IOHelper.getName(url));
			if(f.exists()){
				Message msg = handler.obtainMessage(HttpStatus.SC_OK);
				msg.obj = BitmapFactory.decodeFile(f.getAbsolutePath());
				handler.sendMessage(msg);
				return;
			}
		}*/
			
		threadPool.execute(new Runnable(){
			@Override
			public void run() {
				try {
					Bitmap bmp = null; 
					/**
					if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
						//写到SD卡缓存中
						String name = IOHelper.getName(url);
						File dest = new File(Constants.CACHE_DIR, name);
						HttpHelper.download(url, dest);

						bmp = BitmapFactory.decodeFile(dest.getAbsolutePath());
					}else{
						bmp = HttpHelper.downloadBitmap(url);
					}*/
					
					bmp = HttpHelper.downloadBitmap(url);
					
					bmp = BitmapHelper.zoom(bmp, w, h);
					
					imageCache.put(url, new SoftReference<Bitmap>(bmp)); //放入内存缓存
					Message msg = handler.obtainMessage(HttpStatus.SC_OK);
					msg.obj = bmp;
					Log.d(TAG, "bmp.width=" + bmp.getWidth() +",bmp.height" + bmp.getHeight() + "-->" + url);
					handler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public interface ImageCallback {
		/** 图片下载完成的回调方法 */
		public void imageLoaded(Bitmap bmp, String url);
	}
	
	public interface DrawableCallback{
		public void imageLoaded(Drawable bmp, String url);
	}

}
