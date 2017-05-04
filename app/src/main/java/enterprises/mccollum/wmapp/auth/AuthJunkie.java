package enterprises.mccollum.wmapp.auth;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import enterprises.mccollum.wmapp.API_URLs;
import enterprises.mccollum.wmapp.ApiJunkie;
import enterprises.mccollum.wmapp.authobjects.TokenResponseContainer;
import enterprises.mccollum.wmapp.authobjects.UserToken;

/**
 * Created by smccollum on 05.04.17.
 */

public class AuthJunkie {
	public static final String RENEW_URL = API_URLs.AUTH_BASE_URL+"/api/token/renewToken";
	
	public static class RenewalRequest extends Request<TokenResponseContainer> {
			private Response.Listener<TokenResponseContainer> listener;
		
		Map<String, String> credHeaders;
		
		public RenewalRequest(String tokenString, String tokenSignature, Response.Listener listener, Response.ErrorListener errorListener) {
			super(Method.GET, AuthJunkie.RENEW_URL, errorListener);
			this.listener = listener;
			credHeaders = new HashMap<>(3);
			credHeaders.put(UserToken.TOKEN_HEADER, tokenString);
			credHeaders.put(UserToken.SIGNATURE_HEADER, tokenSignature);
			credHeaders.put("Content-Type", ApiJunkie.BODY_CONTENT_TYPE_JSON_UTF8);
		}
		
		@Override
		public Map<String, String> getHeaders() throws AuthFailureError {
			return credHeaders;
		}
		
		@Override
		protected Response<TokenResponseContainer> parseNetworkResponse(NetworkResponse response) {
			if(response.statusCode >= 500)
				return Response.error(new VolleyError(response));//*/
			
			TokenResponseContainer con = new TokenResponseContainer();
			con.setToken(response.data);
			con.setStatus(response.statusCode);
			con.setTokenSignature(response.headers.get(UserToken.SIGNATURE_HEADER));
			
			return Response.success(con, HttpHeaderParser.parseCacheHeaders(response));
		}
	
		@Override
		protected void deliverResponse(TokenResponseContainer response) {
			listener.onResponse(response);
		}
	
		@Override
		public String getBodyContentType() {
			return ApiJunkie.BODY_CONTENT_TYPE_JSON_UTF8;
		}
	}
	
	public static class LoginRequest extends Request<TokenResponseContainer> {
		private Response.Listener<TokenResponseContainer> listener;
		
		JSONObject loginObj;
	
		public LoginRequest(String username, String deviceName, String password, Response.Listener listener, Response.ErrorListener errorListener) {
			super(Method.POST, API_URLs.AUTH_BASE_URL + "/api/token/getToken", errorListener);
			this.listener = listener;
			Map<String, String> params = new HashMap<>(3);
			params.put("username", username);
			params.put("password", password);
			params.put("devicename", deviceName);
			
			loginObj = new JSONObject(params);
		}
	
		@Override
		protected Response<TokenResponseContainer> parseNetworkResponse(NetworkResponse response) {
			if(response.statusCode >= 500)
				return Response.error(new VolleyError(response));//*/
			
			TokenResponseContainer con = new TokenResponseContainer();
			con.setToken(response.data);
			con.setStatus(response.statusCode);
			con.setTokenSignature(response.headers.get(UserToken.SIGNATURE_HEADER));
			
			return Response.success(con, HttpHeaderParser.parseCacheHeaders(response));
		}
	
		@Override
		protected void deliverResponse(TokenResponseContainer response) {
			listener.onResponse(response);
		}
	
		@Override
		public String getBodyContentType() {
			return "application/json; charset=utf-8";
		}
		
		@Override
		public byte[] getBody() throws AuthFailureError {
			return loginObj.toString().getBytes(StandardCharsets.UTF_8);
		}
	}
}
