package demo.app.manualTests;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.ObjectBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.enums.Role;
import demo.app.logics.AdminLogic;
import demo.app.logics.ObjectLogic;
import demo.app.logics.UserLogic;
import demo.app.objects.CreatedBy;
import demo.app.objects.Location;
import demo.app.objects.ObjectId;
import demo.app.objects.UserId;

@Component
@Profile("manualTests")
public class Initializer implements CommandLineRunner {
	private ObjectLogic objects;
	private UserLogic users;
	private AdminLogic admin;

	public Initializer(ObjectLogic objects, UserLogic users, AdminLogic admin) {
		this.objects = objects;
		this.users = users;
		this.admin = admin;
	}

	@Override
	public void run(String... args) throws Exception {
		storeUsersInDatabase();
		this.admin.deleteAllObjects("2024b.yarden.cherry", "admin@demo.org");
		this.admin.deleteAllUsers("2024b.yarden.cherry", "admin@demo.org");

		storeUsersInDatabase();
		storeObjectsInDatabase();
	}

	private void storeObjectsInDatabase() {
		ObjectBoundary[] objectsToStore = new ObjectBoundary[] {
				createBoundary("1", "2024b.yarden.cherry", "dummyType", "demo instance",
						new Location(32.115139, 34.817804), createCreatedBy("2024b.yarden.cherry", "jane@demo.org"),
						createObjectDetails()),
				createBoundary("2", "2024b.yarden.cherry", "dummyType", "demo instance",
						new Location(32.115139, 34.817804), createCreatedBy("2024b.yarden.cherry", "jane@demo.org"),
						createObjectDetails()),
				createBoundary("3", "2024b.yarden.cherry", "dummyType", "demo instance",
						new Location(32.115139, 34.817804), createCreatedBy("2024b.yarden.cherry", "john@demo.org"),
						createObjectDetails()),
				createBoundary("4", "2024b.yarden.cherry", "dummy", "demo instance", new Location(32.115139, 34.817804),
						createCreatedBy("2024b.yarden.cherry", "john@demo.org"), createObjectDetails()),
				createBoundary("5", "2024b.yarden.cherry", "dummy", "demo instance", null,
						createCreatedBy("2024b.yarden.cherry", "jane@demo.org"), createObjectDetails()) };

		for (ObjectBoundary boundary : objectsToStore) {
			ObjectBoundary storedBoundary = this.objects.storeInDatabase(boundary);
			System.err.println("Created: " + storedBoundary);
		}
	}

	private ObjectBoundary createBoundary(String id, String superApp, String type, String alias, Location location,
			CreatedBy createdBy, Map<String, Object> objectDetails) {
		ObjectBoundary boundary = new ObjectBoundary();
		boundary.setObjectId(createObjectId(id, superApp));
		boundary.setType(type);
		boundary.setAlias(alias);
		boundary.setLocation(location);
		boundary.setActive(true);
		boundary.setCreationTimestamp(new Date());
		boundary.setCreatedBy(createdBy);
		boundary.setObjectDetails(objectDetails);
		return boundary;
	}

	private ObjectId createObjectId(String id, String superApp) {
		ObjectId objectId = new ObjectId();
		objectId.setId(id);
		objectId.setSuperApp(superApp);
		return objectId;
	}

	private CreatedBy createCreatedBy(String superapp, String email) {
		CreatedBy createdBy = new CreatedBy();
		UserId userId = new UserId();
		userId.setEmail(email);
		userId.setSuperapp(superapp);
		createdBy.setUserId(userId);
		return createdBy;
	}

	private Map<String, Object> createObjectDetails() {
		Map<String, Object> objectDetails = new HashMap<>();
		objectDetails.put("key1", "can be set to any value you wish");
		objectDetails.put("key2", "you can also name the attributes any name you like");
		objectDetails.put("key3", 42.00);
		objectDetails.put("key4", true);
		objectDetails.put("key5",
				Arrays.asList("Array", "of", 1, 2.0, false, Map.of("type", "user defined"), "values"));
		return objectDetails;
	}

	private void storeUsersInDatabase() {
		NewUserBoundary[] usersToStore = new NewUserBoundary[] {
				createNewUserBoundary("jane@demo.org", Role.SUPERAPP_USER, "Jane Doe",
						"https://example.com/avatar1.png"),
				createNewUserBoundary("john@demo.org", Role.SUPERAPP_USER, "John Doe",
						"https://example.com/avatar2.png"),
				createNewUserBoundary("jo@demo.org", Role.MINIAPP_USER, "John Doe", "https://example.com/avatar2.png"),
				createNewUserBoundary("admin@demo.org", Role.ADMIN, "Admin", "https://example.com/avatar2.png")

		};

		for (NewUserBoundary user : usersToStore) {
			UserBoundary storedUser = this.users.createNewUser(user);
			System.err.println("Created: " + storedUser);
		}
	}

	private NewUserBoundary createNewUserBoundary(String email, Role role, String userName, String avatar) {
		NewUserBoundary user = new NewUserBoundary();
		user.setEmail(email);
		user.setRole(role);
		user.setUserName(userName);
		user.setAvatar(avatar);
		return user;
	}
}
