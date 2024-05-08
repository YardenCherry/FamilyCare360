package demo;

public class NameBoundary {
	private String firstName;
	private String lastName;

	public NameBoundary() {
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

	@Override
	public String toString() {
		return "NameBoundary [firstName=" + firstName + ", lastName=" + lastName + "]";
	}

}
