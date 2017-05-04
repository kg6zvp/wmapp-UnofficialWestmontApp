package enterprises.mccollum.wmapp.shuttle.control;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import enterprises.mccollum.wmapp.ApiJunkie;
import enterprises.mccollum.wmapp.shuttle.model.JaxDirectory;
import enterprises.mccollum.wmapp.shuttle.model.PhysicalStop;
import enterprises.mccollum.wmapp.shuttle.model.PhysicalStopShadow;
import enterprises.mccollum.wmapp.shuttle.model.Route;
import enterprises.mccollum.wmapp.shuttle.model.RouteShadow;
import enterprises.mccollum.wmapp.shuttle.model.ScheduledStop;
import enterprises.mccollum.wmapp.shuttle.model.ScheduledStopShadow;
import enterprises.mccollum.wmapp.shuttle.model.SequentialStop;
import enterprises.mccollum.wmapp.shuttle.model.SequentialStopShadow;
import enterprises.mccollum.wmapp.shuttle.model.ShuttlePersistenceManager;

/**
 * Created by smccollum on 02.05.17.
 */
public class ShuttleJunkie {
	//urls JaxDirectory
	private static ShuttleJunkie myInst;
	private static final String LOG_TAG = ShuttleJunkie.class.getSimpleName();
	
	private Context ctx;
	
	private RequestQueue reqQueue;
	
	private ShuttleJunkie(Context ctx){
		this.ctx = ctx.getApplicationContext();
	}
	
	public RequestQueue getRequestQueue() {
		if(reqQueue == null)
			reqQueue = Volley.newRequestQueue(ctx); //, new HurlStack(null, newSslSocketFactory()));
		return reqQueue;
	}//*/
	
	public static ShuttleJunkie getInstance(Context ctx){
		if(myInst == null)
			myInst = new ShuttleJunkie(ctx);
		return myInst;
	}
	
	ShuttleDataListener listener;
	
	public void updateStatic(ShuttleDataListener listener){
		this.listener = listener;
		System.out.println("Updating static content");
		/*getRequestQueue().add(
				buildPhysicalStopRequest()
		);
		getRequestQueue().start();//*/
		ApiJunkie.getInstance(ctx).addRequestToQueue(buildPhysicalStopRequest());
	}
	
	List<PhysicalStopShadow> physicalStopShadows;
	List<RouteShadow> routeShadows;
	List<SequentialStopShadow> sequentialStopShadows;
	List<ScheduledStopShadow> scheduledStopShadows;
	
	/**
	 * 0.)
	 * Save physical stops -> save Routes
	 */
	private void savePhysical(List<PhysicalStopShadow> physicalStopShadows){
		this.physicalStopShadows = physicalStopShadows;
		ApiJunkie.getInstance(ctx).addRequestToQueue(buildRouteRequest());
		/*getRequestQueue().add(buildRouteRequest());
		getRequestQueue().start();//*/
	}
	
	/**
	 * 1.)
	 * Save routes -> save sequential stops
	 */
	private void saveRoutes(List<RouteShadow> routeShadows){
		this.routeShadows = routeShadows;
		ApiJunkie.getInstance(ctx).addRequestToQueue(buildSequentialStopRequest());
		/*getRequestQueue().add(
				buildSequentialStopRequest()
		);
		getRequestQueue().start();//*/
	}
	
	/**
	 * 2.)
	 * Save sequential stops -> save scheduled stops
	 */
	private void saveSequential(List<SequentialStopShadow> sequentialStopShadows){
		this.sequentialStopShadows = sequentialStopShadows;
		//ApiJunkie.getInstance(ctx).addRequestToQueue(buildScheduledStopRequest()); //not now
		saveScheduled(null); //just call it, we don't need to do the schedules now
		/*getRequestQueue().add(
				buildScheduledStopRequest()
		);
		getRequestQueue().start();//*/
	}
	
	/**
	 * 3.)
	 * save scheduled stops -> done
	 */
	private void saveScheduled(List<ScheduledStopShadow> scheduledStopShadows){
		this.scheduledStopShadows = scheduledStopShadows;
		persistOrUpdate();
	}
	
	/**
	 * 4.)
	 * Everything has been persisted in memory, time to link the objects with one another and save to the database
	 */
	private void persistOrUpdate(){
		final Context lCtx = ctx;
		if(physicalStopShadows != null){
			//Save physical stops
			for(PhysicalStopShadow pss : physicalStopShadows) {
				pss.populate(ShuttlePersistenceManager.getInstance(ctx));
			}
			Log.d("ShJunkie", String.format("Saved %d physical stops to db",
					ShuttlePersistenceManager.getInstance(ctx).getPhysicalStops().getAll().size()));
			this.physicalStopShadows = null;
		}
		if(routeShadows != null){
			//save routes
			for(RouteShadow rs : routeShadows){
				rs.populate(ShuttlePersistenceManager.getInstance(ctx));
			}
			Log.d("ShJunkie", String.format("Saved %d routes to db",
					ShuttlePersistenceManager.getInstance(ctx).getRoutes().getAll().size()));
			this.routeShadows = null;
		}
		if(sequentialStopShadows != null){
			//save sequential stops
			for(SequentialStopShadow sss : sequentialStopShadows){
				sss.populate(ShuttlePersistenceManager.getInstance(ctx));
			}
			Log.d("ShJunkie", String.format("Saved %d sequential stops to db",
					ShuttlePersistenceManager.getInstance(ctx).getSequentialStops().getAll().size()));
			this.sequentialStopShadows = null;
		}
		if(scheduledStopShadows != null){
			//save scheduled stops
			for(ScheduledStopShadow sss : scheduledStopShadows){
				sss.populate(ShuttlePersistenceManager.getInstance(ctx));
			}
			Log.d("ShJunkie", String.format("Saved %d scheduled stops to db?",
					ShuttlePersistenceManager.getInstance(ctx).getScheduledStops().getAll().size()));
			this.scheduledStopShadows = null;
		}
		listenerDoneLoading();
	}
	
	private void listenerDoneLoading(){
		if(listener != null){
			ShuttleDataListener old = listener;
			listener = null;
			old.doneLoading();
		}
	}
	
	private StringRequest buildPhysicalStopRequest() {
		return new StringRequest(JaxDirectory.PHYSICAL_STOPS_PATH,
			new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					savePhysical(
							(List<PhysicalStopShadow>) new Gson().fromJson(response, new TypeToken<List<PhysicalStopShadow>>(){}.getType())
					);
				}
			}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(LOG_TAG, error.getMessage()+"Error in physical stop request");
				error.printStackTrace();
			}
		});
	}
	
	private StringRequest buildRouteRequest() {
		return new StringRequest(JaxDirectory.ROUTES_PATH,
			new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					saveRoutes(
							(List<RouteShadow>) new Gson().fromJson(response, new TypeToken<List<RouteShadow>>(){}.getType())
					);
				}
			}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(LOG_TAG, error.getMessage());
				error.printStackTrace();
			}
		});
	}
	
	private StringRequest buildSequentialStopRequest() {
		return new StringRequest(JaxDirectory.SEQUENTIAL_STOPS_PATH,
			new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					saveSequential(
							(List<SequentialStopShadow>) new Gson().fromJson(response, new TypeToken<List<SequentialStopShadow>>(){}.getType())
					);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					error.printStackTrace();
				}
		});
	}
		
	private StringRequest buildScheduledStopRequest() {
		return new StringRequest(JaxDirectory.SCHEDULED_STOPS_PATH,
			new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					saveScheduled(
							(List<ScheduledStopShadow>) new Gson().fromJson(response, new TypeToken<List<ScheduledStopShadow>>(){}.getType())
					);
				}
			}, new Response.ErrorListener(){
			@Override
			public void onErrorResponse(VolleyError error) {
				
			}
		});
	}
	
	public void doSomething(){
		if(!ApiJunkie.getInstance(ctx).hasCredentials())
			return;
		
	}
}
