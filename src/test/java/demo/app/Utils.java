package demo.app;

import java.util.Date;
import java.util.Map;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.ObjectBoundary;
import demo.app.enums.Role;
import demo.app.objects.CommandId;
import demo.app.objects.CreatedBy;
import demo.app.objects.Location;
import demo.app.objects.ObjectId;
import demo.app.objects.TargetObject;
import demo.app.objects.UserId;

public class Utils {
	private static String adminEmail = "admin@gmail.com";
	private static String miniappUserEmail = "mini@gmail.com";
	private static String superappUserEmail = "super@gmail.com";

	public static NewUserBoundary createNewUserAdmin() {
		return createNewUserBoundary("admin_avatar", adminEmail, Role.ADMIN,
				"admin_username");
	}
	
	public static NewUserBoundary createNewUserSuperapp() {
		return createNewUserBoundary("super_avatar", superappUserEmail,
				Role.SUPERAPP_USER, "super_username");
	}
	
	public static NewUserBoundary createNewUserMiniapp() {
		return createNewUserBoundary("mini_avatar", miniappUserEmail, Role.MINIAPP_USER,
				"mini_username");
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

	private static MiniAppCommandBoundary createCommandBoundary(String superapp, String miniapp, String id, String Command,
			String idObject, String email, Map<String, Object> commandsAttributess) {
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
		return miniappUserEmail;
	}

	public static void setMiniappUserEmail(String miniappUserEmail) {
		Utils.miniappUserEmail = miniappUserEmail;
	}

	public static String getSuperappUserEmail() {
		return superappUserEmail;
	}

	public static void setSuperappUserEmail(String superappUserEmail) {
		Utils.superappUserEmail = superappUserEmail;
	}
	

}
