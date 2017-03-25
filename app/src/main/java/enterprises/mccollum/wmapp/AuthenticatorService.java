package enterprises.mccollum.wmapp;

import android.accounts.Account;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {
	public static final String ACCOUNT_TYPE = "enterprises.mccollum.wmapp";
	public static final String ARG_NEW_ACCOUNT = "ent.mcc.newaccount";
	public static final String KEY_TOKEN_REAL = "token";
	public static final String KEY_TOKEN_SIGNATURE = "signature";
	
	private LoginAuthenticator mAuth;
	
	public AuthenticatorService() {
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mAuth.getIBinder();
	}

	@Override
	public void onCreate(){
		mAuth =  new LoginAuthenticator(this);
	}
}
