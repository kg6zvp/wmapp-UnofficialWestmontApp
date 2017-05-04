package enterprises.mccollum.wmapp.shuttle.control;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import enterprises.mccollum.wmapp.model.ShuttleSyncAdapter;

public class ShuttleDataSyncService extends Service {
	private static final Object sAdapLock = new Object();
	public static final String SHUTTLE_SYNC_PROVIDER = "enterprises.mccollum.wmapp.shuttle.provider";
	private static ShuttleSyncAdapter ssa = null;
	
	public ShuttleDataSyncService() {
	}
	
	@Override
	public void onCreate() {
		synchronized (sAdapLock){
			if(ssa == null){
				ssa = new ShuttleSyncAdapter(getApplicationContext(), true);
			}
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return ssa.getSyncAdapterBinder();
	}
}
