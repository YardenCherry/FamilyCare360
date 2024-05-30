package demo.app.entities;

import demo.app.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS")
public class UserEntity {

	@Id
	private String id;
	private String userName;
	private Role role;
	private String avatar;

	public UserEntity() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserEntity{" + "id='" + id + '\'' + ", userName='" + userName + '\'' + ", role=" + role + ", avatar='"
				+ avatar + '\'' + '}';
	}
}
