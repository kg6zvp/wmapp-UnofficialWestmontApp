package enterprises.mccollum.wmapp;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;

import enterprises.mccollum.wmapp.authobjects.UserToken;

/**
 * Created by smccollum on 26.02.17.
 */
public class LoginAuthenticator extends AbstractAccountAuthenticator {
	private final Context mContext;
	
	private long renewalDifference = 0;

	private static final String LOGTAG = "wm_auther";

	public LoginAuthenticator(Context context) {
		super(context);
		mContext = context;
		
		Calendar c = Calendar.getInstance();
		long earlyTime = c.getTimeInMillis();
		c.add(Calendar.WEEK_OF_YEAR, 2);
		long laterTime = c.getTimeInMillis();
		
		renewalDifference = laterTime - earlyTime;
	}
	
	/**
	 * Return an activity to edit properties like profile picture, etc.
	 * @param response
	 * @param accountType
	 * @return
	 */
	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
		log("addAccount()");
		return getAddIntent(response, true);
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
		return null;
	}
	
	private Bundle getAddIntent(AccountAuthenticatorResponse response, boolean isNewAccount){
		final Intent intent = new Intent(mContext, LoginActivity.class);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AuthenticatorService.ACCOUNT_TYPE);
		intent.putExtra(AuthenticatorService.ARG_NEW_ACCOUNT, isNewAccount);
		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle; //We think this is enough to start
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
		log("getAuthToken()");

		//if invalid token type, exit:

		//if requested token okay, return it:
		AccountManager am = AccountManager.get(mContext);
		String tokenStringB64 = am.peekAuthToken(account, AuthenticatorService.KEY_TOKEN_REAL);
		String signature = am.peekAuthToken(account, AuthenticatorService.KEY_TOKEN_SIGNATURE);
		
		if(TextUtils.isEmpty(tokenStringB64)){
			//do authentication all over again
			return getAddIntent(response, false);
		}
		
		byte[] tokenBytes = Base64.decode(tokenStringB64, Base64.DEFAULT);
		//verify signature, maybe?
		//signature verification not necessary for the time being
		
		//instantiate token to check it
		Gson gson = new Gson();
		UserToken token = gson.fromJson(new String(tokenBytes, StandardCharsets.UTF_8), UserToken.class);
		
		if(token.getExpirationDate() <= System.currentTimeMillis()) { //if it's expired, ask for authentication again
			return getAddIntent(response, false);
		}
		
		if((token.getExpirationDate()-renewalDifference) <= System.currentTimeMillis()){
			//renew token
		}
		
		//if it's all good:
		Bundle bundle = new Bundle();
		bundle.putString(AuthenticatorService.KEY_TOKEN_REAL, tokenStringB64);
		bundle.putString(AuthenticatorService.KEY_TOKEN_SIGNATURE, signature);
		return bundle;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		return null;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
		return null;
	}
	
	/**
	 * Please figure out what in the world this thing is
	 *
	 * @param response
	 * @param account
	 * @param features
	 * @return
	 * @throws NetworkErrorException
	 */
	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
		return null;
	}

	private void log(String m){
		Log.v(LOGTAG, m);
	}
}
