//package demo.app.manualTests;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//
//import demo.app.boundaries.NewUserBoundary;
//import demo.app.boundaries.UserBoundary;
//import demo.app.enums.Role;
//import demo.app.logics.AdminLogic;
//import demo.app.logics.UserLogic;
//
//@Component
//@Profile("manualTests")
//public class UserInitializer implements CommandLineRunner {
//	private UserLogic users;
//	private AdminLogic admin;
//
//	public UserInitializer(UserLogic users, AdminLogic admin) {
//		this.users = users;
//		this.admin = admin;
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		storeUsersInDatabase();
//		this.admin.deleteAllUsers("2024b.yarden.cherry","admin@demo.org");
////		createNewUserBoundary("admin@demo.org", Role.ADMIN, "Admin","https://example.com/avatar2.png");
//		storeUsersInDatabase();
//	}
//
//	private void storeUsersInDatabase() {
//		NewUserBoundary[] usersToStore = new NewUserBoundary[] {
//				createNewUserBoundary("jane@demo.org", Role.SUPERAPP_USER, "Jane Doe",
//						"https://example.com/avatar1.png"),
//				createNewUserBoundary("john@demo.org", Role.SUPERAPP_USER, "John Doe",
//						"https://example.com/avatar2.png"),
//				createNewUserBoundary("admin@demo.org", Role.ADMIN, "Admin",
//						"https://example.com/avatar2.png"),
//				createNewUserBoundary("jo@demo.org", Role.MINIAPP_USER, "Jo Doe",
//						"https://example.com/avatar2.png") };
//
//		for (NewUserBoundary user : usersToStore) {
//			UserBoundary storedUser = this.users.createNewUser(user);
//			System.err.println("Created: " + storedUser);
//		}
//	}
//
//	private NewUserBoundary createNewUserBoundary(String email, Role role, String userName, String avatar) {
//		NewUserBoundary user = new NewUserBoundary();
//		user.setEmail(email);
//		user.setRole(role);
//		user.setUsername(userName);
//		user.setAvatar(avatar);
//		return user;
//	}
//}
