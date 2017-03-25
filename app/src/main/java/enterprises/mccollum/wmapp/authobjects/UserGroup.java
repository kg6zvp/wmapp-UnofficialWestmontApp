package enterprises.mccollum.wmapp.authobjects;

import java.util.LinkedList;
import java.util.List;

public class UserGroup {
	Long id;
	String name;
	String ldapName; //for example: cn=papercut,dc=library,dc=campus,dc=westmont,dc=edu
	
	List<DomainUser> users;

	/**
	 * Adds the given user to the group
	 * @param newUser
	 */
	public void addUser(DomainUser newUser){
		if(users == null)
			users = new LinkedList<DomainUser>();
		if(!users.contains(newUser))
			users.add(newUser);
		if(!newUser.getGroups().contains(this))
			newUser.getGroups().add(this);
	}
	
	public void removeUser(DomainUser user){
		if(users == null)
			return;
		if(users.contains(user))
			users.remove(user);
		if(user.getGroups().contains(this))
			user.getGroups().remove(this);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof UserGroup))
			return false;
		UserGroup other = (UserGroup)obj;
		return (other.getId() == id);
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLdapName() {
		return ldapName;
	}
	public void setLdapName(String ldapName) {
		this.ldapName = ldapName;
	}
	public List<DomainUser> getUsers() {
		if(users == null)
			users = new LinkedList<>();
		return users;
	}
	public void setUsers(List<DomainUser> users) {
		this.users = users;
	}
}
