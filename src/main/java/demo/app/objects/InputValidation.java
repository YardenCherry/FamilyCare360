package demo.app.objects;

import java.util.Objects;

import demo.app.enums.Role;

public class InputValidation {

	public InputValidation() {
	}

	public static boolean isValidEmail(String email) {
		if (email == null)
			return false;
		return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
	}

	public static boolean isValidRole(String role) {
		return Objects.equals(role, Role.ADMIN.toString()) || Objects.equals(role, Role.MINIAPP_USER.toString())
				|| Objects.equals(role, Role.SUPERAPP_USER.toString());
	}

}
