package enterprises.mccollum.wmapp.model;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InterruptedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import enterprises.mccollum.wmapp.ApiJunkie;
import enterprises.mccollum.wmapp.AuthenticatorService;
import enterprises.mccollum.wmapp.control.GsonListRequest;
import enterprises.mccollum.wmapp.shuttle.control.ShuttleJunkie;
import enterprises.mccollum.wmapp.shuttle.model.JaxDirectory;
import enterprises.mccollum.wmapp.shuttle.model.PhysicalStop;
import enterprises.mccollum.wmapp.shuttle.model.PhysicalStopShadow;
import enterprises.mccollum.wmapp.shuttle.model.RouteShadow;
import enterprises.mccollum.wmapp.shuttle.model.ScheduledStopShadow;
import enterprises.mccollum.wmapp.shuttle.model.SequentialStop;
import enterprises.mccollum.wmapp.shuttle.model.SequentialStopShadow;
import enterprises.mccollum.wmapp.shuttle.model.ShuttlePersistenceManager;

/**
 * Created by smccollum on 03.05.17.
 */
public class ShuttleSyncAdapter extends AbstractThreadedSyncAdapter {
	private static final String LOG_TAG = "ShuttleSync";
	AccountManager am;
	
	public ShuttleSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		am = AccountManager.get(context);
	}
	
	@Override
	public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
		logf("Syncing shuttle data");
		String tokenB64 = am.peekAuthToken(account, AuthenticatorService.KEY_TOKEN_REAL);
		final String tokenString = new String(Base64.decode(tokenB64, Base64.DEFAULT), StandardCharsets.UTF_8);
		final String signature = am.peekAuthToken(account, AuthenticatorService.KEY_TOKEN_SIGNATURE);
		List<PhysicalStopShadow> physicalStopShadows = null;
		List<RouteShadow> routeShadows = null;
		RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
		requestQueue.stop();
		try {
			logf("Syncing physical stops...");
			RequestFuture<List<PhysicalStopShadow>> physicalStopShadowFuture = RequestFuture.newFuture();
			GsonListRequest<PhysicalStopShadow> physicalStopShadowRequest = new GsonListRequest<PhysicalStopShadow>(
				JaxDirectory.PHYSICAL_STOPS_PATH, tokenString, signature, PhysicalStopShadow[].class, physicalStopShadowFuture
			);//*/
			ApiJunkie.getInstance(getContext()).addRequestToQueue(physicalStopShadowRequest);
			/*requestQueue.add(physicalStopShadowRequest);
			requestQueue.start();//*/
			physicalStopShadows = physicalStopShadowFuture.get(10, TimeUnit.SECONDS);
			
			/*RequestFuture<String> requestFuture = RequestFuture.newFuture();
			StringRequest physicalStopShadowRequest = buildPhysicalStopRequest(requestFuture);
			ApiJunkie.getInstance(getContext()).addRequestToQueue(physicalStopShadowRequest);
			String psss = requestFuture.get();
			PhysicalStopShadow[] stopShadows = new Gson().fromJson(psss, new TypeToken<PhysicalStopShadow[]>(){}.getType());
			physicalStopShadows = Arrays.asList(stopShadows);//*/
			
		
			logf("Syncing routes...");
			RequestFuture<List<RouteShadow>> routeShadowFuture = RequestFuture.newFuture();
			GsonListRequest<RouteShadow> routeShadowRequest = new GsonListRequest<RouteShadow>(
					JaxDirectory.ROUTES_PATH, tokenString, signature, RouteShadow[].class, routeShadowFuture
			);
			ApiJunkie.getInstance(getContext()).addRequestToQueue(routeShadowRequest);
			/*requestQueue.add(routeShadowRequest);
			requestQueue.start();//*/
			routeShadows = routeShadowFuture.get(10, TimeUnit.SECONDS);
			
			logf("Syncing sequential stops...");
			RequestFuture<List<SequentialStopShadow>> sequentialStopShadowFuture = RequestFuture.newFuture();
			GsonListRequest<SequentialStopShadow> sequentialStopShadowRequest = new GsonListRequest<SequentialStopShadow>(
					JaxDirectory.SEQUENTIAL_STOPS_PATH, tokenString, signature, SequentialStopShadow[].class, sequentialStopShadowFuture
			);
			ApiJunkie.getInstance(getContext()).addRequestToQueue(sequentialStopShadowRequest);
			/*requestQueue.add(sequentialStopShadowRequest);
			requestQueue.start();//*/
			List<SequentialStopShadow> sequentialStopShadows = sequentialStopShadowFuture.get(15, TimeUnit.SECONDS);
			
			/*logf("Syncing scheduled stops...");
			RequestFuture<List<ScheduledStopShadow>> scheduledStopShadowFuture = RequestFuture.newFuture();
			GsonListRequest<ScheduledStopShadow> scheduledStopShadowRequest = new GsonListRequest<ScheduledStopShadow>(
					JaxDirectory.SCHEDULED_STOPS_PATH, tokenString, signature, ScheduledStopShadow[].class, scheduledStopShadowFuture
			);
			ApiJunkie.getInstance(getContext()).addRequestToQueue(scheduledStopShadowRequest);
			/*requestQueue.add(scheduledStopShadowRequest);
			requestQueue.start();//*/
			List<ScheduledStopShadow> scheduledStopShadows = null; //scheduledStopShadowFuture.get(30, TimeUnit.SECONDS);
			
			persistOrUpdate(
					physicalStopShadows,
					routeShadows,
					sequentialStopShadows,
					scheduledStopShadows
			);
			
			logf("Synced %d routes, %d stops", routeShadows.size(), physicalStopShadows.size());
		}catch (Exception e){
			/*if(e instanceof InterruptedIOException
					|| e instanceof NoConnectionError
					|| e instanceof ExecutionException) {
				e.printStackTrace();
				syncResult.madeSomeProgress();
				return;
			}//*/
			throw new RuntimeException(e);
		}
	}
	
	private void persistOrUpdate(List<PhysicalStopShadow> physicalStopShadows, List<RouteShadow> routeShadows, List<SequentialStopShadow> sequentialStopShadows, List<ScheduledStopShadow> scheduledStopShadows){
		if(physicalStopShadows != null){
			//Save physical stops
			for(PhysicalStopShadow pss : physicalStopShadows) {
				pss.populate(ShuttlePersistenceManager.getInstance(getContext()));
			}
			Log.d("ShJunkie", String.format("Saved %d physical stops to db",
					ShuttlePersistenceManager.getInstance(getContext()).getPhysicalStops().getAll().size()));
		}
		if(routeShadows != null){
			//save routes
			for(RouteShadow rs : routeShadows){
				rs.populate(ShuttlePersistenceManager.getInstance(getContext()));
			}
			Log.d("ShJunkie", String.format("Saved %d routes to db",
					ShuttlePersistenceManager.getInstance(getContext()).getRoutes().getAll().size()));
		}
		if(sequentialStopShadows != null){
			//save sequential stops
			for(SequentialStopShadow sss : sequentialStopShadows){
				sss.populate(ShuttlePersistenceManager.getInstance(getContext()));
			}
			Log.d("ShJunkie", String.format("Saved %d sequential stops to db",
					ShuttlePersistenceManager.getInstance(getContext()).getSequentialStops().getAll().size()));
		}
		if(scheduledStopShadows != null){
			//save scheduled stops
			for(ScheduledStopShadow sss : scheduledStopShadows){
				sss.populate(ShuttlePersistenceManager.getInstance(getContext()));
			}
			Log.d("ShJunkie", String.format("Saved %d scheduled stops to db",
					ShuttlePersistenceManager.getInstance(getContext()).getScheduledStops().getAll().size()));
		}
	}
	
	public StringRequest buildPhysicalStopRequest(RequestFuture<String> rf) {
		return new StringRequest(JaxDirectory.PHYSICAL_STOPS_PATH, rf, rf);
	}
	
	private StringRequest buildRouteRequest(RequestFuture<String> rf) {
		return new StringRequest(JaxDirectory.ROUTES_PATH, rf, rf);
	}
	
	private StringRequest buildSequentialStopRequest(RequestFuture<String> rf) {
		return new StringRequest(JaxDirectory.SEQUENTIAL_STOPS_PATH, rf, rf);
	}
		
	private StringRequest buildScheduledStopRequest(RequestFuture<String> rf) {
		return new StringRequest(JaxDirectory.SCHEDULED_STOPS_PATH, rf, rf);
	}
	
	private void logf(String format, Object... args){
		Log.d(LOG_TAG, String.format(format, args));
	}
}
