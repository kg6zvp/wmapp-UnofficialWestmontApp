package enterprises.mccollum.wmapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import enterprises.mccollum.wmapp.authobjects.UserToken;

public class SplashActivity extends Activity {
	
	TextView studentId, username;
	AccountManager am;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//username = (TextView) findViewById(R.id.usernameTV);
		//studentId = (TextView) findViewById(R.id.studentIdTV);
		am = AccountManager.get(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		showAccountInfo();
	}
	
	private void showAccountInfo() {
		Account[] accounts = am.getAccountsByType(AuthenticatorService.ACCOUNT_TYPE);
		if(accounts.length < 1) {
			addAccount();
		}else {
			final AccountManagerFuture<Bundle> authToken = am.getAuthToken(accounts[0], AuthenticatorService.KEY_TOKEN_REAL, null, this, new AccountManagerCallback<Bundle>() {
				@Override
				public void run(AccountManagerFuture<Bundle> future) {
					try {
						Bundle bnd = future.getResult();
						String tokenb64 = bnd.getString(AuthenticatorService.KEY_TOKEN_REAL);
						//String tokenSignature = bnd.getString(AuthenticatorService.KEY_TOKEN_SIGNATURE);
						byte[] tokenBytes = Base64.decode(tokenb64, Base64.DEFAULT);
						Gson gson = new Gson();
						final UserToken token = gson.fromJson(new String(tokenBytes, StandardCharsets.UTF_8), UserToken.class);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								setToken(token);
							}
						});
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
	}
	
	public void setToken(UserToken token){
		Log.d("app", String.format("Username: %s", token.getUsername()));
		//username.setText(String.format("Username: %s", token.getUsername()));
		//studentId.setText(String.format("Student ID: %d", token.getStudentID()));
	}
	
	private void addAccount() {
		final AccountManagerFuture<Bundle> acFuture = am.addAccount(AuthenticatorService.ACCOUNT_TYPE, null, null, null, this, new AccountManagerCallback<Bundle>() {
			@Override
			public void run(AccountManagerFuture<Bundle> future) {
				try{
					Bundle bnd = future.getResult();
					showAccountInfo();
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}, null);
	}
}
