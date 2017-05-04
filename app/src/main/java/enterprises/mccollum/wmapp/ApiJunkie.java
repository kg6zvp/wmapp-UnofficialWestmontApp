package enterprises.mccollum.wmapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import enterprises.mccollum.wmapp.authobjects.UserToken;

/**
 * Created by smccollum on 13.03.17.
 */
public class ApiJunkie implements AcctInfoReceiver {
	public static final String BODY_CONTENT_TYPE_JSON_UTF8 = "application/json; charset=utf-8";
	private static ApiJunkie myInst;
	
	private Context ctx;
	
	private RequestQueue reqQueue;
	private ImageLoader imgLoader;

	private UserToken token;
	private String tokenString;
	private String tokenSignature;
	
	public static synchronized ApiJunkie getInstance(Context ctx){
		if(myInst == null)
			myInst = new ApiJunkie(ctx);
		return myInst;
	}
	
	public ApiJunkie(Context ctx) {
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
			//reqQueue = Volley.newRequestQueue(getCtx().getApplicationContext(), new HttpClientStack());
			reqQueue = Volley.newRequestQueue(getCtx().getApplicationContext()); //, new HurlStack(null, newSslSocketFactory()));
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
		if(hasCredentials()){
			try {
				request.getHeaders().put(UserToken.TOKEN_HEADER, tokenString);
				request.getHeaders().put(UserToken.SIGNATURE_HEADER, tokenSignature);
			} catch (AuthFailureError authFailureError) {
				authFailureError.printStackTrace();
			}
		}
		getRequestQueue().add(request);
		getRequestQueue().start();
	}
	
	///**
	// * load account info from the account store
	// */
	/*public void loadAccountInfo() {
		AccountManager am = AccountManager.get(ctx);
		Account[] accounts = am.getAccountsByType(AuthenticatorService.ACCOUNT_TYPE);
		if(accounts.length < 1) {
			receiveAccountInfo(null, null);
		}else {
			final AccountManagerFuture<Bundle> authToken = am.getAuthToken(accounts[0], AuthenticatorService.KEY_TOKEN_REAL, null, false, new AccountManagerCallback<Bundle>() {
				@Override
				public void run(AccountManagerFuture<Bundle> future) {
					try {
						Bundle bnd = future.getResult();
						String tokenb64 = bnd.getString(AuthenticatorService.KEY_TOKEN_REAL);
						String tokenSignature = bnd.getString(AuthenticatorService.KEY_TOKEN_SIGNATURE);
						byte[] tokenBytes = Base64.decode(tokenb64, Base64.DEFAULT);
						receiveAccountInfo(new String(tokenBytes, StandardCharsets.UTF_8), tokenSignature);
					} catch (OperationCanceledException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (AuthenticatorException e) {
						e.printStackTrace();
					}
				}
			}, null);
		}
	}//*/
	
	public void receiveAccountInfo(String tokenString, String tokenSignature) {
		this.token = new Gson().fromJson(tokenString, UserToken.class);
		this.tokenString = tokenString;
		this.tokenSignature = tokenSignature;
	}
	
	/*public UserToken getToken() {
		return token;
	}
	
	public String getTokenString() {
		return tokenString;
	}
	
	public String getTokenSignature() {
		return tokenSignature;
	}*/
	
	/**
	 * Tell whether or not this ApiJunkie instance has received credentials
	 * @return
	 */
	public boolean hasCredentials() {
		return (tokenString != null && tokenSignature != null);
	}
	
}
