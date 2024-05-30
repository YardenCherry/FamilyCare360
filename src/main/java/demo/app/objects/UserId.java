package demo.app.objects;

public class UserId {

	private String superapp;
	private String email;

	public UserId() {
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public String getSuperapp() {
		return superapp;
	}

	public void setSuperapp(String superAppName) {
		this.superapp = superAppName;
	}

	@Override
	public String toString() {
		return "UserId {superapp=" + superapp + ",/n email=" + email + "}";
	}

}