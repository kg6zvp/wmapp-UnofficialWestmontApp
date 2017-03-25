package enterprises.mccollum.wmapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by smccollum on 13.03.17.
 */
public class ApiJunkie {
	public static final String AUTH_BASE_URL = "http://auth.wmapp.mccollum.enterprises";
	private static ApiJunkie myInst;
	
	private Context ctx;
	
	private RequestQueue reqQueue;
	private ImageLoader imgLoader;
	
	public static synchronized ApiJunkie getInstance(Context ctx){
		if(myInst == null)
			myInst = new ApiJunkie(ctx);
		return myInst;
	}
	
	public ApiJunkie(){}
	public ApiJunkie(Context ctx){
		this.ctx = ctx;
	}
	
	public Context getCtx() {
		return ctx;
	}
	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}
	
	public RequestQueue getRequestQueue() {
		if(reqQueue == null)
			reqQueue = Volley.newRequestQueue(getCtx().getApplicationContext());
		return reqQueue;
	}
	
	public ImageLoader getImageLoader(){
		if(imgLoader == null){
			//init
			imgLoader = new ImageLoader(getRequestQueue(), new ImageLoader.ImageCache() {
				private final LruCache<String, Bitmap> cache = new LruCache<>(16);
				
				@Override
				public Bitmap getBitmap(String url) {
					return cache.get(url);
				}
				
				@Override
				public void putBitmap(String url, Bitmap bitmap) {
					cache.put(url, bitmap);
				}
			});
		}
		return imgLoader;
	}
	
	public <T> void addRequestToQueue(Request<T> request) {
		getRequestQueue().add(request);
		getRequestQueue().start();
	}
	
	public <T> void addApiRequest(Request<T> request){
		try {
			request.getHeaders().put("key", "value");
			getRequestQueue().add(request);
		} catch (AuthFailureError authFailureError) {
			authFailureError.printStackTrace();
		}
		getRequestQueue().start();
	}
}
