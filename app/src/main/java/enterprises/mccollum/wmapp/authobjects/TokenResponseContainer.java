package enterprises.mccollum.wmapp.authobjects;

import android.util.Base64;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;

/**
 * Created by smccollum on 14.03.17.
 */
public class TokenResponseContainer {
	String b64Token;
	String tokenSignature;
	Integer status;
	
	public TokenResponseContainer(){}
	public TokenResponseContainer(byte[] token, String tokenSignature, Integer status){
		setToken(token);
		this.tokenSignature = tokenSignature;
		this.status = status;
	}
	
	public String getB64Token() {
		return b64Token;
	}
	public void setToken(String b64Token) {
		this.b64Token = b64Token;
	}
	public void setToken(byte[] rawToken) {
		this.b64Token = Base64.encodeToString(rawToken, Base64.DEFAULT);
	}
	public UserToken getToken() {
		Gson gson = new Gson();
		return gson.fromJson(new String(Base64.decode(b64Token, Base64.DEFAULT), StandardCharsets.UTF_8), UserToken.class);
	}
	public String getTokenSignature() {
		return tokenSignature;
	}
	public void setTokenSignature(String tokenSignature) {
		this.tokenSignature = tokenSignature;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
