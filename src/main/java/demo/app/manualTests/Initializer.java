//package demo.app.manualTests;
//
//import java.util.Arrays;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//
//import demo.app.boundaries.MiniAppCommandBoundary;
//import demo.app.boundaries.NewUserBoundary;
//import demo.app.boundaries.ObjectBoundary;
//import demo.app.boundaries.UserBoundary;
//import demo.app.enums.Role;
//import demo.app.logics.AdminLogic;
//import demo.app.logics.CommandLogic;
//import demo.app.logics.EnhancedObjectLogic;
//import demo.app.logics.UserLogic;
//import demo.app.objects.CommandId;
//import demo.app.objects.CreatedBy;
//import demo.app.objects.Location;
//import demo.app.objects.ObjectId;
//import demo.app.objects.TargetObject;
//import demo.app.objects.UserId;
//
//@Component
//@Profile("manualTests")
//public class Initializer implements CommandLineRunner {
//	private EnhancedObjectLogic objects;
//	private UserLogic users;
//	private AdminLogic admin;
//	private CommandLogic commands;
//
//	public Initializer(EnhancedObjectLogic objects, UserLogic users, AdminLogic admin, CommandLogic commands) {
//		this.objects = objects;
//		this.users = users;
//		this.admin = admin;
//		this.commands = commands;
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		storeUsersInDatabase();
//		this.admin.deleteAllObjects("2024b.yarden.cherry", "admin@demo.org");
//		this.admin.deleteAllUsers("2024b.yarden.cherry", "admin@demo.org");
//		storeUsersInDatabase();
//		storeObjectsInDatabase();
//		storeCommandsInDatabase();
//	}
//
//	private void storeCommandsInDatabase() {
//		MiniAppCommandBoundary[] commandsStore = new MiniAppCommandBoundary[] {
//				createCommand("2024b.yarden.cherry", "dummyApp", "1", "get All Babysitters",
//						this.objects.getAll(5, 0, "2024b.yarden.cherry", "superapp1@demo.org").get(0).getObjectId()
//								.getId(),
//						"miniapp@demo.org", createObjectDetails()),
//				createCommand(
//						"2024b.yarden.cherry", "dummy", "", "get All Parents", this.objects
//								.getAll(5, 0, "2024b.yarden.cherry", "superapp1@demo.org").get(0).getObjectId().getId(),
//						"miniapp@demo.org", createObjectDetails()), };
//		for (MiniAppCommandBoundary boundary : commandsStore) {
//			Object storedBoundary = this.commands.storeInDatabase(boundary.getCommandId().getMiniapp(), boundary);
//			System.err.println("Created: " + storedBoundary);
//		}
//
//	}
//
//	private MiniAppCommandBoundary createCommand(String superapp, String miniapp, String id, String Command,
//			String idObject, String email, Map<String, Object> commandsAttributess) {
//		MiniAppCommandBoundary commandBoundary = new MiniAppCommandBoundary();
//		commandBoundary.setCommandId(createCommandId(superapp, miniapp, id));
//		commandBoundary.setCommand(Command);
//		TargetObject targetObject = new TargetObject();
//		targetObject.setObjectId(createObjectId(idObject, superapp));
//		commandBoundary.setTargetObject(targetObject);
//		commandBoundary.setInvocationTimeStamp(new Date());
//		commandBoundary.setInvokedBy(createCreatedBy(superapp, email));
//		commandBoundary.setCommandAttributes(commandsAttributess);
//
//		return commandBoundary;
//	}
//
//	private CommandId createCommandId(String superapp, String miniapp, String id) {
//		CommandId commandId = new CommandId();
//		commandId.setSuperapp(superapp);
//		commandId.setMiniapp(miniapp);
//		commandId.setId(id);
//		return commandId;
//	}
//
//	private void storeObjectsInDatabase() {
//		ObjectBoundary[] objectsToStore = new ObjectBoundary[] {
//				createBoundary("1", "2024b.yarden.cherry", "dummyType", "demo", new Location(32.115139, 34.817804),
//						true, createCreatedBy("2024b.yarden.cherry", "superapp1@demo.org"), createObjectDetails()),
//				createBoundary("2", "2024b.yarden.cherry", "dummyType", "demo instance",
//						new Location(32.115139, 34.817804), true,
//						createCreatedBy("2024b.yarden.cherry", "superapp2@demo.org"), createObjectDetails()),
//				createBoundary("3", "2024b.yarden.cherry", "dummyType", "demo instance",
//						new Location(32.115139, 34.817804), true,
//						createCreatedBy("2024b.yarden.cherry", "superapp1@demo.org"), createObjectDetails()),
//				createBoundary("4", "2024b.yarden.cherry", "dummy", "demo instance", new Location(32.115139, 34.817804),
//						true, createCreatedBy("2024b.yarden.cherry", "superapp2@demo.org"), createObjectDetails()),
//				createBoundary("5", "2024b.yarden.cherry", "dummy", "demo instance", null, false,
//						createCreatedBy("2024b.yarden.cherry", "superapp1@demo.org"), createObjectDetails()) };
//
//		for (ObjectBoundary boundary : objectsToStore) {
//			ObjectBoundary storedBoundary = this.objects.storeInDatabase(boundary);
//			System.err.println("Created: " + storedBoundary);
//		}
//	}
//
//	private ObjectBoundary createBoundary(String id, String superApp, String type, String alias, Location location,
//			boolean active, CreatedBy createdBy, Map<String, Object> objectDetails) {
//		ObjectBoundary boundary = new ObjectBoundary();
//		boundary.setObjectId(createObjectId(id, superApp));
//		boundary.setType(type);
//		boundary.setAlias(alias);
//		boundary.setLocation(location);
//		boundary.setActive(active);
//		boundary.setCreationTimestamp(new Date());
//		boundary.setCreatedBy(createdBy);
//		boundary.setObjectDetails(objectDetails);
//		return boundary;
//	}
//
//	private ObjectId createObjectId(String id, String superApp) {
//		ObjectId objectId = new ObjectId();
//		objectId.setId(id);
//		objectId.setSuperapp(superApp);
//		return objectId;
//	}
//
//	private CreatedBy createCreatedBy(String superapp, String email) {
//		CreatedBy createdBy = new CreatedBy();
//		UserId userId = new UserId();
//		userId.setEmail(email);
//		userId.setSuperapp(superapp);
//		createdBy.setUserId(userId);
//		return createdBy;
//	}
//
//	private Map<String, Object> createObjectDetails() {
//		Map<String, Object> objectDetails = new HashMap<>();
//		objectDetails.put("key1", "can be set to any value you wish");
//		objectDetails.put("key2", "you can also name the attributes any name you like");
//		objectDetails.put("key3", 42.00);
//		objectDetails.put("key4", true);
//		objectDetails.put("key5",
//				Arrays.asList("Array", "of", 1, 2.0, false, Map.of("type", "user defined"), "values"));
//		return objectDetails;
//	}
//
//	private void storeUsersInDatabase() {
//		NewUserBoundary[] usersToStore = new NewUserBoundary[] {
//				createNewUserBoundary("superapp1@demo.org", Role.SUPERAPP_USER, "Jane Doe",
//						"https://example.com/avatar1.png"),
//				createNewUserBoundary("superapp2@demo.org", Role.SUPERAPP_USER, "John Doe",
//						"https://example.com/avatar2.png"),
//				createNewUserBoundary("miniapp@demo.org", Role.MINIAPP_USER, "John Doe",
//						"https://example.com/avatar2.png"),
//				createNewUserBoundary("admin@demo.org", Role.ADMIN, "Admin", "https://example.com/avatar2.png")
//
//		};
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
