package enterprises.mccollum.wmapp.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import enterprises.mccollum.wmapp.ApiJunkie;
import enterprises.mccollum.wmapp.AuthenticatorService;
import enterprises.mccollum.wmapp.authobjects.TokenResponseContainer;
import enterprises.mccollum.wmapp.authobjects.UserToken;

/**
 * Created by smccollum on 30.03.17.
 */

public class TokenSyncAdapter extends AbstractThreadedSyncAdapter {
	private static final String LOG_TAG = "TOKEN_SYNC";
	AccountManager am;
	
	public TokenSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		am = AccountManager.get(getContext());
	}
	
	@Override
	public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
		//synchronize token
		String tokenB64 = am.peekAuthToken(account, AuthenticatorService.KEY_TOKEN_REAL);
		final String tokenString = new String(Base64.decode(tokenB64, Base64.DEFAULT), StandardCharsets.UTF_8);
		final String signature = am.peekAuthToken(account, AuthenticatorService.KEY_TOKEN_SIGNATURE);
		Log.d(LOG_TAG, String.format("peekAuthToken(): %s\n%s", tokenString, signature));
		
		//See if Token is too old to keep around
		UserToken token = new Gson().fromJson(tokenString, UserToken.class);
		if(token.getExpirationDate() > getTimeSlew()) {
			//No sync necessary
			Log.d(LOG_TAG, "Not necessary to renew token");
			return;
		}
		
		//TODO: Get new token from the server
		RequestFuture<TokenResponseContainer> requestFuture = RequestFuture.newFuture();
		AuthJunkie.RenewalRequest req = new AuthJunkie.RenewalRequest(tokenString, signature, requestFuture, requestFuture);
		ApiJunkie.getInstance(getContext()).addRequestToQueue(req);
		try {
			TokenResponseContainer result = requestFuture.get();
			am.setAuthToken(account, AuthenticatorService.KEY_TOKEN_REAL, result.getB64Token());
			am.setAuthToken(account, AuthenticatorService.KEY_TOKEN_SIGNATURE, result.getTokenSignature());
			String nTokenString = new Gson().toJson(result.getToken());
			String nTokenSignature = result.getTokenSignature();
			Log.d(LOG_TAG, String.format("Result: %s", tokenString));
			ApiJunkie.getInstance(getContext()).receiveAccountInfo(nTokenString, nTokenSignature);
		} catch (ExecutionException|InterruptedException e) {
			e.printStackTrace(); //TODO: Throw useful error into SyncAcapter land
		}
		/*mAccountManager.setAuthToken(account, AuthenticatorService.KEY_TOKEN_REAL, response.getB64Token());
		mAccountManager.setAuthToken(account, AuthenticatorService.KEY_TOKEN_SIGNATURE, response.getTokenSignature());//*/
		
		//TODO: Put the new token into service inside the ApiJunkie
	}
	
	/**
	 * Retrieve a date/time 2 weeks from now to check against
	 * @return
	 */
	private long getTimeSlew() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.WEEK_OF_YEAR, 2);
		return c.getTimeInMillis();
	}
}
