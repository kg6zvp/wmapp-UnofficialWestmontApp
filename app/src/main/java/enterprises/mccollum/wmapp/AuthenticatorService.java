package enterprises.mccollum.wmapp;

import android.accounts.Account;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {
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
