package enterprises.mccollum.wmapp.push;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import enterprises.mccollum.wmapp.AcctInfoReceiver;
import enterprises.mccollum.wmapp.ApiJunkie;

/**
 * Created by smccollum on 31.03.17.
 */
public class FBTokenDienst extends FirebaseInstanceIdService {
	private static final String TAG = "wm.fb";
	private static final String REGISTER_URL = "http://push.wmapp.mccollum.enterprises/api/reg/client";
	private String newToken = null;
	
	@Override
	public void onTokenRefresh() {
		super.onTokenRefresh();
		this.newToken = FirebaseInstanceId.getInstance().getToken();
		Log.d(TAG, "Firebase token: "+newToken);
		PushJunkie.getInstance(this).tryFirebaseRegistration();
	}
}
