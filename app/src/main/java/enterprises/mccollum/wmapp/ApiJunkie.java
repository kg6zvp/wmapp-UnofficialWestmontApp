package enterprises.mccollum.wmapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
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
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.StandardConstants;
import javax.net.ssl.TrustManagerFactory;

import enterprises.mccollum.wmapp.authobjects.UserToken;

/**
 * Created by smccollum on 13.03.17.
 */
public class ApiJunkie implements AcctInfoReceiver {
	public static final String BODY_CONTENT_TYPE_JSON_UTF8 = "application/json; charset=utf-8";
	private static final String LOG_TAG = "ApiJunkie";
	private static ApiJunkie myInst;
	
	private Context ctx;
	
	private RequestQueue reqQueue;
	private ImageLoader imgLoader;
	
	private String tokenString = null;
	private String tokenSignature = null;
	private UserToken token = null;
	
	public static synchronized ApiJunkie getInstance(Context ctx){
		if(myInst == null) {
			myInst = new ApiJunkie(ctx);
			myInst.refreshTokens();
		}
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
				if(request.getHeaders() == null)
					Log.d(LOG_TAG, "Headers null");
				else
					Log.d(LOG_TAG, String.format("Headers size: %d", request.getHeaders().size()));
				request.getHeaders().put(UserToken.TOKEN_HEADER, getTokenString());
				request.getHeaders().put(UserToken.SIGNATURE_HEADER, getTokenSignature());
				request.getHeaders().put("Content-Type", BODY_CONTENT_TYPE_JSON_UTF8);
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
	
	public void refreshTokens(){
		AccountManager am = AccountManager.get(ctx);
		Account[] accounts = am.getAccountsByType(AuthenticatorService.ACCOUNT_TYPE);
		if(accounts.length < 1)
			return;
		this.tokenString = new String(Base64.decode(am.peekAuthToken(accounts[0], AuthenticatorService.KEY_TOKEN_REAL), Base64.DEFAULT), StandardCharsets.UTF_8);
		this.tokenSignature = am.peekAuthToken(accounts[0], AuthenticatorService.KEY_TOKEN_SIGNATURE);
		this.token = new Gson().fromJson(tokenString, UserToken.class);
	}
	
	public UserToken getToken() {
		return token;
	}
	
	public String getTokenString() {
		return tokenString;
	}
	
	public String getTokenSignature() {
		return tokenSignature;
	}
	
	/**
	 * Tell whether or not this ApiJunkie instance has received credentials
	 * @return
	 */
	public boolean hasCredentials() {
		Log.d("ApiJunkie", String.format(
				"hasCredentials(): %b",
			(tokenSignature != null && tokenString != null)));
		/*return (AccountManager.get(ctx).getAccountsByType(AuthenticatorService.ACCOUNT_TYPE).length > 0); //*/
		return (tokenSignature != null && tokenString != null);
	}
	
}
