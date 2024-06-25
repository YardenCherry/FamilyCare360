package demo.app.boundaries;

import demo.app.enums.Role;

public class NewUserBoundary {

	private String email;
	private Role role;
	private String username;
	private String avatar;

	public NewUserBoundary() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Role getRole() {
		return role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "NewUserBoundary{" + "email='" + email + '\'' + ", role=" + role + ", username='" + username + '\''
				+ ", avatar='" + avatar + '\'' + '}';
	}
}
