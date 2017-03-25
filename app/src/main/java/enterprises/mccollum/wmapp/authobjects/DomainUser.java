package enterprises.mccollum.wmapp.authobjects;

import java.util.LinkedList;
import java.util.List;

public class DomainUser {
	Long studentId; //uidNumber && datatelID
	String firstName; //givenName
	String lastName; //sn
	String fullName; //displayName
	String username; //cn && name && sAMAccountName
	String phone; //telephoneNumber
	String phoneExtension;
	String email; //mail
	String employeeType; //employeeType
	
	List<UserGroup> groups; //memberOf; given by DN of group
	String department; //department; can be null for students, can be a title
	
	String description; //description
	String dn; //dn; Distinguished name from LDAP

	public void addToGroup(UserGroup group){
		if(groups == null)
			groups = new LinkedList<UserGroup>();
		if(!groups.contains(group))
			groups.add(group);
		if(!group.getUsers().contains(this))
			group.getUsers().add(this);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof DomainUser))
			return false;
		DomainUser other = (DomainUser) obj;
		return (studentId == other.studentId);
	}
	
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhoneExtension() {
		return phoneExtension;
	}
	public void setPhoneExtension(String phoneExtension) {
		this.phoneExtension = phoneExtension;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmployeeType() {
		return employeeType;
	}
	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}
	public List<UserGroup> getGroups() {
		if(groups == null)
			groups = new LinkedList<UserGroup>();
		return groups;
	}
	public void setGroups(List<UserGroup> groups) {
		this.groups = groups;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
}
