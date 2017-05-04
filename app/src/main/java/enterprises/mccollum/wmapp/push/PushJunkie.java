package enterprises.mccollum.wmapp.push;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import enterprises.mccollum.wmapp.API_URLs;
import enterprises.mccollum.wmapp.ApiJunkie;

/**
 * Created by smccollum on 04.04.17.
 */
public class PushJunkie {
	private static final String REGISTRATION_URL = API_URLs.PUSH_API_BASE+"/reg/client";
	private static final String LOG_TAG = "PushJunkie";
	private static PushJunkie myInst;
	
	private Context ctx;
	
	public static PushJunkie getInstance(Context ctx){
		if(myInst == null);
			myInst = new PushJunkie(ctx);
		return myInst;
	}
	
	public PushJunkie(Context ctx) {
		this.ctx = ctx;
	}
	
	public void tryFirebaseRegistration() {
		if(!ApiJunkie.getInstance(ctx).hasCredentials())
			return;
		String fbToken = FirebaseInstanceId.getInstance().getToken();
		final PushClient pc = new PushClient();
		pc.setRegistrationId(fbToken);
		pc.setType(PushClient.FIREBASE_TOKEN_TYPE);
		ApiJunkie.getInstance(ctx).addRequestToQueue(new StringRequest(Request.Method.POST, REGISTRATION_URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.d(LOG_TAG, String.format("onResponse(): %s", response));
				//TODO: persist push registration token to local DB
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//TODO: do something
			}
		}){
			Map<String, String> mHeaders;
			
			@Override
			protected Response<String> parseNetworkResponse(NetworkResponse response) {
				Log.d(LOG_TAG, String.format("status: %d", response.statusCode));
				Log.d(LOG_TAG, String.format("Message: %s", new String(response.data, StandardCharsets.UTF_8)));
				return super.parseNetworkResponse(response);
			}
			
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				if(mHeaders == null) {
					mHeaders = new HashMap<String, String>();
					mHeaders.put("Content-Type", ApiJunkie.BODY_CONTENT_TYPE_JSON_UTF8);
				}
				return mHeaders;
			}
			
			@Override
			public String getBodyContentType() {
				return ApiJunkie.BODY_CONTENT_TYPE_JSON_UTF8;
			}
			
			@Override
			public byte[] getBody() throws AuthFailureError {
				return new Gson().toJson(pc).getBytes(StandardCharsets.UTF_8);
			}
		});
	}
}
