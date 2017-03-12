package enterprises.mccollum.wmapp;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by smccollum on 26.02.17.
 */
public class LoginAuthenticator extends AbstractAccountAuthenticator {
	private final Context mContext;

	private static final String LOGTAG = "wm_auther";

	public LoginAuthenticator(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
		log("addAccount()");
		final Intent intent = new Intent(mContext, LoginActivity.class);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle; //We think this is enough to start
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
		log("getAuthToken()");

		//if invalid token type, exit:

		//if requested token okay, return it:
		AccountManager am = AccountManager.get(mContext);
		String tokenString = null;
		String signature = am.getPassword(account);
		if(tokenString != null){
			//UserToken token;
		}

		//case tokenExpired:
		if(1 == 0) {
			return null;
		}
		
		//if it's good:
		Bundle bundle = new Bundle();
		bundle.putString("token", am.getUserData(account, "token"));
		bundle.putString("token", am.getPassword(account));
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

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
		return null;
	}

	private void log(String m){
		Log.v(LOGTAG, m);
	}
}
