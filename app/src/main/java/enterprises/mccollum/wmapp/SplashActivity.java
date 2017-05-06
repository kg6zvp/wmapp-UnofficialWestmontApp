package enterprises.mccollum.wmapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import enterprises.mccollum.wmapp.authobjects.UserToken;
import enterprises.mccollum.wmapp.push.PushJunkie;
import enterprises.mccollum.wmapp.shuttle.control.ShuttleDataListener;
import enterprises.mccollum.wmapp.shuttle.control.ShuttleDataSyncService;
import enterprises.mccollum.wmapp.shuttle.control.ShuttleJunkie;
import enterprises.mccollum.wmapp.shuttle.model.ShuttlePersistenceManager;
import enterprises.mccollum.wmapp.shuttle.view.ShuttleActivity;

public class SplashActivity extends Activity implements ShuttleDataListener {
	
	AccountManager am;
	
	protected void initEntityManagers(){
		ShuttlePersistenceManager.getInstance(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initEntityManagers();
		setContentView(R.layout.activity_splash);
		ShuttlePersistenceManager.getInstance(this.getApplicationContext()); //init db
		am = AccountManager.get(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getAccountInfo();
		checkAccount();
		checkFirebase();
		ShuttleJunkie.getInstance(this.getApplicationContext()).updateStatic(this);
		//if(ApiJunkie.getInstance(this).hasCredentials())
	}
	
	/**
	 * Check Firebase Cloud Messaging registration
	 */
	private void checkFirebase() {
		String fcmToken = FirebaseInstanceId.getInstance().getToken();
		//System.out.println("FCM Token: "+fcmToken);
	}
	
	/**
	 * Check to see that the account is up to date
	 */
	private void checkAccount() {
		
	}
	
	private void getAccountInfo() {
		Account[] accounts = am.getAccountsByType(AuthenticatorService.ACCOUNT_TYPE);
		if(accounts.length < 1) {
			addAccount();
		}else {
			@SuppressWarnings("unused")
			/*final AccountManagerFuture<Bundle> authToken = am.getAuthToken(accounts[0], AuthenticatorService.KEY_TOKEN_REAL, null, this, new AccountManagerCallback<Bundle>() {
				@Override
				public void run(AccountManagerFuture<Bundle> future) {
					try {
						Bundle bnd = future.getResult();
						String tokenb64 = bnd.getString(AuthenticatorService.KEY_TOKEN_REAL);
						final String tokenSignature = bnd.getString(AuthenticatorService.KEY_TOKEN_SIGNATURE);
						byte[] tokenBytes = Base64.decode(tokenb64, Base64.DEFAULT);
						final String tokenString = new String(tokenBytes, StandardCharsets.UTF_8);
						Gson gson = new Gson();
						final UserToken token = gson.fromJson(tokenString, UserToken.class);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								setToken(token, tokenString, tokenSignature);
							}
						});
					} catch (IOException|AuthenticatorException|OperationCanceledException e) {
						e.printStackTrace();
					}
				}
			}, null);//*/
			String tokenb64 = am.peekAuthToken(accounts[0], AuthenticatorService.KEY_TOKEN_REAL);
			final String tokenSignature = am.peekAuthToken(accounts[0], AuthenticatorService.KEY_TOKEN_SIGNATURE);
			byte[] tokenBytes = Base64.decode(tokenb64, Base64.DEFAULT);
			final String tokenString = new String(tokenBytes, StandardCharsets.UTF_8);
			UserToken token = new Gson().fromJson(tokenString, UserToken.class);
			setToken(token, tokenString, tokenSignature);
		}
	}
	
	public void setToken(UserToken token, String tokenString, String tokenSignature){
		Log.d("app", String.format("Username: %s", token.getUsername()));
		PushJunkie.getInstance(this).tryFirebaseRegistration();
		//TODO: Start Main Activity
		//finish();
	}
	
	private void addAccount() {
		@SuppressWarnings("unused")
		final AccountManagerFuture<Bundle> acFuture = am.addAccount(AuthenticatorService.ACCOUNT_TYPE, null, null, null, this, new AccountManagerCallback<Bundle>() {
			@Override
			public void run(AccountManagerFuture<Bundle> future) {
				try{
					@SuppressWarnings("unused")
					Bundle bnd = future.getResult();
					getAccountInfo();
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}, null);
	}
	
	boolean finishedSplash = false;
	
	@Override
	protected void onPause() {
		super.onPause();
		//if(ApiJunkie.getInstance(this).hasCredentials()) {
		if(finishedSplash){
			finish();
		}
	}
	
	@Override
	public void doneLoading() {
		//start main activity
		finishedSplash = true;
		Intent intent = new Intent(this, ShuttleActivity.class);
		startActivity(intent);
	}
}
