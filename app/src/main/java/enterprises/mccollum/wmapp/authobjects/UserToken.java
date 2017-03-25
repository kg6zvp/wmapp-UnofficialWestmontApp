package enterprises.mccollum.wmapp.authobjects;

import java.util.LinkedList;
import java.util.List;

public class UserToken {
	public static final String SIGNATURE_HEADER = "TokenSignature";
	public static final String TOKEN_HEADER = "Token";
	
	Long tokenId;
	Long studentID;
	String username;
	String deviceName;
	String employeeType;
	
	//Is the group thing necessary?
	List<UserGroup> groups; //should be turned into a list of group id numbers or strings in json
	
	Long expirationDate;
	Boolean blacklisted;
	
	public Long getTokenId() {
		return tokenId;
	}
	public void setTokenId(Long tokenId) {
		this.tokenId = tokenId;
	}
	public Long getStudentID() {
		return studentID;
	}
	public void setStudentID(Long studentID) {
		this.studentID = studentID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public List<UserGroup> getGroups() {
		if(groups == null)
			groups = new LinkedList<>();
		return groups;
	}
	public void setGroups(List<UserGroup> groups) {
		this.groups = groups;
	}
	public Long getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Long expirationDate) {
		this.expirationDate = expirationDate;
	}
	public Boolean getBlacklisted() {
		return blacklisted;
	}
	public void setBlacklisted(Boolean blacklisted) {
		this.blacklisted = blacklisted;
	}
	public String getEmployeeType() {
		return employeeType;
	}
	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}
}