package enterprises.mccollum.wmapp.push;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by smccollum on 31.03.17.
 */
public class FBMessagingDienst extends FirebaseMessagingService {
	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);
		if(remoteMessage.getData().size() < 1)
			return;
		//TODO: implement onMessageReceived
		Log.d("wmapp.push", "Message received!");
		for(Map.Entry<String, String> e : remoteMessage.getData().entrySet()) {
			Log.d("wmapp.push", String.format("%s -> %s", e.getKey(), e.getValue()));
		}
		//Log.d("wmapp.push", String.format("Time received from server: %s", remoteMessage.getData().get("time")));
	}
}
