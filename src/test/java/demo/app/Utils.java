package demo.app;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.ObjectBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.enums.Role;
import demo.app.objects.CommandId;
import demo.app.objects.CreatedBy;
import demo.app.objects.Location;
import demo.app.objects.ObjectId;
import demo.app.objects.TargetObject;
import demo.app.objects.UserId;

public class Utils {
	private static String adminEmail = "admin@gmail.com";
	private static String miniappEmail = "mini@gmail.com";
	private static String superappEmail = "super@gmail.com";
	@Value("${spring.application.name}")
	private static String superapp;

	public static NewUserBoundary createNewUserAdmin() {
		return createNewUserBoundary("admin_avatar", adminEmail, Role.ADMIN, "admin_username");
	}

	public static NewUserBoundary createNewUserSuperapp() {
		return createNewUserBoundary("super_avatar", superappEmail, Role.SUPERAPP_USER, "super_username");
	}

	public static NewUserBoundary createNewUserMiniapp() {
		return createNewUserBoundary("mini_avatar", miniappEmail, Role.MINIAPP_USER, "mini_username");
	}

	public static ObjectBoundary createNewObjectByAdmin() {
		return createObjectBoundary("1", superapp, "type", "alias", new Location(0, 0), true,
				createCreatedBy(superapp, adminEmail), createObjectDetails());
	}

	public static ObjectBoundary createNewObjectBySuperapp() {
		return createObjectBoundary("1", superapp, "type", "alias", new Location(10, 0), true,
				createCreatedBy(superapp, superappEmail), createObjectDetails());
	}

	public static ObjectBoundary createNewObject3BySuperapp() {
		return createObjectBoundary("1", superapp, "type1", "alias", new Location(10, 0), true,
				createCreatedBy(superapp, superappEmail), createObjectDetails());
	}

	public static ObjectBoundary createNewObject2BySuperapp() {
		return createObjectBoundary("1", superapp, "type1", "alias1", new Location(0, 10), true,
				createCreatedBy(superapp, superappEmail), createObjectDetails());
	}

	public static ObjectBoundary createNewObject4BySuperapp() {
		return createObjectBoundary("1", superapp, "type", "alias1", new Location(10, 10), true,
				createCreatedBy(superapp, superappEmail), createObjectDetails());
	}

	public static ObjectBoundary createNewObjectByMiniApp() {
		return createObjectBoundary("1", superapp, "type", "alias", new Location(0, 0), true,
				createCreatedBy(superapp, miniappEmail), createObjectDetails());
	}

	public static MiniAppCommandBoundary createNewCommandByAdmin(String objectId) {
		return createCommandBoundary(superapp, "miniapp", "1", "command", objectId, adminEmail, createObjectDetails());
	}

	public static MiniAppCommandBoundary createNewCommandBySuperapp(String objectId) {
		return createCommandBoundary(superapp, "miniapp", "1", "command", objectId, superappEmail,
				createObjectDetails());
	}

	public static MiniAppCommandBoundary createNewCommandByMiniapp(String objectId, UserBoundary miniappUser) {
		superapp = miniappUser.getUserId().getSuperapp();
		return createCommandBoundary(miniappUser.getUserId().getSuperapp(), "miniapp", "1", "command", objectId,
				miniappEmail, createObjectDetails());

	}

	public static NewUserBoundary createNewUserBoundary(String avatar, String email, Role role, String username) {
		NewUserBoundary newUser = new NewUserBoundary();
		newUser.setAvatar(avatar);
		newUser.setEmail(email);
		newUser.setRole(role);
		newUser.setUsername(username);
		return newUser;
	}

	private static CreatedBy createCreatedBy(String superapp, String email) {
		CreatedBy createdBy = new CreatedBy();
		UserId userId = new UserId();
		userId.setEmail(email);
		userId.setSuperapp(superapp);
		createdBy.setUserId(userId);
		return createdBy;
	}

	private static ObjectId createObjectId(String id, String superApp) {
		ObjectId objectId = new ObjectId();
		objectId.setId(id);
		objectId.setSuperapp(superApp);
		return objectId;
	}

	private static CommandId createCommandId(String superapp, String miniapp, String id) {
		CommandId commandId = new CommandId();
		commandId.setSuperapp(superapp);
		commandId.setMiniapp(miniapp);
		commandId.setId(id);
		return commandId;
	}

	private static ObjectBoundary createObjectBoundary(String id, String superApp, String type, String alias,
			Location location, boolean active, CreatedBy createdBy, Map<String, Object> objectDetails) {
		ObjectBoundary boundary = new ObjectBoundary();
		boundary.setObjectId(createObjectId(id, superApp));
		boundary.setType(type);
		boundary.setAlias(alias);
		boundary.setLocation(location);
		boundary.setActive(active);
		boundary.setCreationTimestamp(new Date());
		boundary.setCreatedBy(createdBy);
		boundary.setObjectDetails(objectDetails);
		return boundary;
	}

	private static MiniAppCommandBoundary createCommandBoundary(String superapp, String miniapp, String id,
			String Command, String idObject, String email, Map<String, Object> commandsAttributess) {
		MiniAppCommandBoundary commandBoundary = new MiniAppCommandBoundary();
		commandBoundary.setCommandId(createCommandId(superapp, miniapp, id));
		commandBoundary.setCommand(Command);
		TargetObject targetObject = new TargetObject();
		targetObject.setObjectId(createObjectId(idObject, superapp));
		commandBoundary.setTargetObject(targetObject);
		commandBoundary.setInvocationTimeStamp(new Date());
		commandBoundary.setInvokedBy(createCreatedBy(superapp, email));
		commandBoundary.setCommandAttributes(commandsAttributess);

		return commandBoundary;
	}

	public static String getAdminEmail() {
		return adminEmail;
	}

	public static void setAdminEmail(String adminEmail) {
		Utils.adminEmail = adminEmail;
	}

	public static String getMiniappUserEmail() {
		return miniappEmail;
	}

	public static void setMiniappUserEmail(String miniappUserEmail) {
		Utils.miniappEmail = miniappUserEmail;
	}

	public static String getSuperappUserEmail() {
		return superappEmail;
	}

	public static void setSuperappUserEmail(String superappUserEmail) {
		Utils.superappEmail = superappUserEmail;
	}

	private static Map<String, Object> createObjectDetails() {
		Map<String, Object> objectDetails = new HashMap<>();
		objectDetails.put("key1", "can be set to any value you wish");
		objectDetails.put("key2", "you can also name the attributes any name you like");
		objectDetails.put("key3", 42.00);
		objectDetails.put("key4", true);
		objectDetails.put("key5",
				Arrays.asList("Array", "of", 1, 2.0, false, Map.of("type", "user defined"), "values"));
		return objectDetails;
	}

}
