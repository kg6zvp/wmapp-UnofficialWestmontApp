package enterprises.mccollum.wmapp.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by smccollum on 05.04.17.
 */

public class TokenSyncService extends Service{
	private static final Object sAdapLock = new Object();
	private static TokenSyncAdapter tsa = null;
	
	@Override
	public void onCreate() {
		synchronized (sAdapLock){
			if(tsa == null)
				tsa = new TokenSyncAdapter(getApplicationContext(), true);
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return tsa.getSyncAdapterBinder();
	}
}
