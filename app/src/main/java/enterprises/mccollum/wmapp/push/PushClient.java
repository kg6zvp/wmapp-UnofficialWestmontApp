package enterprises.mccollum.wmapp.push;

/**
 * Created by smccollum on 03.04.17.
 */
public class PushClient {
	public static final String PUSH_DEST_HEADER = "PushDest";
	public static final String FIREBASE_TOKEN_TYPE = "FIREBASE";
	
	Long id;
	
	Long studentId;
	
	String username;
	
	String type;
	
	String registrationId;
	
	Long lastPush;
	
	Long tokenId;
	
	public PushClient(){}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getStudentId() {
		return studentId;
	}
	
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getRegistrationId() {
		return registrationId;
	}
	
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Returs the epoch time of the last push notification
	 * @return
	 */
	public Long getLastPush() {
		return lastPush;
	}
	
	/**
	 * Sets the epoch time of the last push notification
	 * @param lastPush
	 */
	public void setLastPush(Long lastPush) {
		this.lastPush = lastPush;
	}
	
	/**
	 * Updates the time of the last push notification to the current time
	 */
	public void updateLastPush(){
		setLastPush(System.currentTimeMillis());
	}
	
	/**
	 * @return the tokenId
	 */
	public Long getTokenId() {
		return tokenId;
	}
	
	/**
	 * @param tokenId the tokenId to set
	 */
	public void setTokenId(Long tokenId) {
		this.tokenId = tokenId;
	}
}
