package enterprises.mccollum.wmapp.control;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enterprises.mccollum.wmapp.ApiJunkie;
import enterprises.mccollum.wmapp.authobjects.UserToken;

/**
 * Created by smccollum on 04.05.17.
 */

public class GsonListRequest<T> extends StringRequest {
	Map<String, String> httpHeaders;
	Response.Listener<List<T>> listener;
	Class<T[]> clazz;

	public GsonListRequest(int method, String url, String tokenString, String tokenSignature, Class<T[]> clazz, Response.Listener<List<T>> listener, Response.ErrorListener errorListener) {
		super(method, url, null, errorListener);
		this.listener = listener;
		this.clazz = clazz;
		httpHeaders = new HashMap<>(3);
		httpHeaders.put(UserToken.TOKEN_HEADER, tokenString);
		httpHeaders.put(UserToken.SIGNATURE_HEADER, tokenSignature);
		httpHeaders.put("Content-Type", ApiJunkie.BODY_CONTENT_TYPE_JSON_UTF8);
	}
	
	public GsonListRequest(int method, String url, String tokenString, Class<T[]> clazz, String tokenSignature, RequestFuture<List<T>> listener) {
		this(method, url, tokenString, tokenSignature, clazz, listener, listener);
	}
	
	public GsonListRequest(String url, String tokenString, String tokenSignature, Class<T[]> clazz, Response.Listener<List<T>> listener, Response.ErrorListener errorListener){
		this(Method.GET, url, tokenString, tokenSignature, clazz, listener, errorListener);
	}
	
	public GsonListRequest(String url, String tokenString, String tokenSignature, Class<T[]> clazz, RequestFuture<List<T>> listener){
		this(url, tokenString, tokenSignature, clazz, listener, listener);
	}
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return httpHeaders;
	}
	
	/*@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		if(response.statusCode >= 500)
			return Response.error(new VolleyError(response));
		final String jsonResponse = new String(response.data);
		System.out.println(String.format("Success on %s: %s", getUrl(), jsonResponse));
		List<T> list = null;
		try {
			list = Arrays.asList((T[]) new Gson().fromJson(jsonResponse, clazz));
		}catch (Exception e){
			System.out.println("Throwing runtime exception: "+e.getMessage());
			return Response.error(new VolleyError(e));
			//throw new RuntimeException(e);
		}
		return Response.success(
				list,
				HttpHeaderParser.parseCacheHeaders(response)
		);
	}//*/
	
	@Override
	protected void deliverResponse(String response) {
		//listener.onResponse(response);
		listener.onResponse(Arrays.asList((T[]) new Gson().fromJson(response, clazz)));
	}
}
