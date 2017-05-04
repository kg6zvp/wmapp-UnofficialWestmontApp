package enterprises.mccollum.wmapp.shuttle.control;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ShuttleDataService extends Service {
	public ShuttleDataService() {
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
