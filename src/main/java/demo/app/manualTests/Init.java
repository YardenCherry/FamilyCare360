package demo.app.manualTests;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.ObjectBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.enums.Role;
import demo.app.logics.AdminLogic;
import demo.app.logics.EnhancedObjectLogic;
import demo.app.logics.UserLogic;
import demo.app.objects.CreatedBy;
import demo.app.objects.Location;
import demo.app.objects.ObjectId;
import demo.app.objects.UserId;

@Component
@Profile("manualTests")
public class Init implements CommandLineRunner {
	private EnhancedObjectLogic objects;
	private UserLogic users;
	private AdminLogic admin;
	private ObjectBoundary[] objectsToStore;
	private UserBoundary[] usersToStore;
	@Value("${spring.application.name}")
	private String superapp;

	public Init(EnhancedObjectLogic objects, UserLogic users, AdminLogic admin) {
		this.objects = objects;
		this.users = users;
		this.admin = admin;
	}

	@Override
	public void run(String... args) throws Exception {
		storeUsersInDatabase();
		this.admin.deleteAllObjects(superapp, "admin@gmail.com");
		this.admin.deleteAllUsers(superapp, "admin@gmail.com");
		storeUsersInDatabase();
		storeBabysittersAndParentsObjectsInDatabase();
		updateUsersInDatabase();
		storeBabysittingEventsInDatabase();
	}

	private void storeBabysittingEventsInDatabase() {
		UserBoundary userParent = users.getSpecificUser(superapp, "noa@gmail.com").get();

		UserBoundary userBabysitter1 = users.getSpecificUser(superapp, "adi@gmail.com").get();

		UserBoundary userBabysitter2 = users.getSpecificUser(superapp, "rotem@gmail.com").get();

		for (UserBoundary user : usersToStore) {
			if (user.getRole() != Role.ADMIN) {
				user.setRole(Role.SUPERAPP_USER);
				user = this.users.updateById(user.getUserId().getSuperapp(), user.getUserId().getEmail(), user).get();
				System.err.println("Updated: " + user);
			}
		}
		objectsToStore = new ObjectBoundary[] {
				createBoundary("1", superapp, "BabysittingEvent", userBabysitter1.getUsername(),
						new Location(0, 0), true,
						createCreatedBy(superapp, userParent.getUserId().getEmail()),
						createBabysittingEventDetails("Event 1 Message", "2024-07-01", userBabysitter1.getUsername(),
								userParent.getUsername(), userParent.getUserId().getEmail())),

				createBoundary("2", superapp, "BabysittingEvent", userBabysitter1.getUsername(),
						new Location(0, 0), true,
						createCreatedBy(superapp, userParent.getUserId().getEmail()),
						createBabysittingEventDetails("Event 2 Message", "2024-07-02", userBabysitter1.getUsername(),
								userParent.getUsername(), userParent.getUserId().getEmail())),

				createBoundary("2", superapp, "BabysittingEvent", userBabysitter1.getUsername(),
						new Location(0, 0), true,
						createCreatedBy(superapp, userParent.getUserId().getEmail()),
						createBabysittingEventDetails("Event 3 Message", "2024-07-03", userBabysitter1.getUsername(),
								userParent.getUsername(), userParent.getUserId().getEmail())),

				createBoundary("2", superapp, "BabysittingEvent", userBabysitter1.getUsername(),
						new Location(0, 0), true,
						createCreatedBy(superapp, userParent.getUserId().getEmail()),
						createBabysittingEventDetails("Event 4 Message", "2024-07-04", userBabysitter1.getUsername(),
								userParent.getUsername(), userParent.getUserId().getEmail())),

				createBoundary("2", superapp, "BabysittingEvent", userBabysitter1.getUsername(),
						new Location(0, 0), true,
						createCreatedBy(superapp, userParent.getUserId().getEmail()),
						createBabysittingEventDetails("Event 5 Message", "2024-07-05", userBabysitter1.getUsername(),
								userParent.getUsername(), userParent.getUserId().getEmail())),

				createBoundary("2", superapp, "BabysittingEvent", userBabysitter1.getUsername(),
						new Location(0, 0), true,
						createCreatedBy(superapp, userParent.getUserId().getEmail()),
						createBabysittingEventDetails("Event 6 Message", "2024-07-06", userBabysitter1.getUsername(),
								userParent.getUsername(), userParent.getUserId().getEmail())),

				createBoundary("2", superapp, "BabysittingEvent", userBabysitter1.getUsername(),
						new Location(0, 0), true,
						createCreatedBy(superapp, userParent.getUserId().getEmail()),
						createBabysittingEventDetails("Event 7 Message", "2024-07-07", userBabysitter1.getUsername(),
								userParent.getUsername(), userParent.getUserId().getEmail())),

				createBoundary("3", superapp, "BabysittingEvent", userBabysitter2.getUsername(),
						new Location(0, 0), true,
						createCreatedBy(superapp, userParent.getUserId().getEmail()),
						createBabysittingEventDetails("Event 8 Message", "2024-07-08", userBabysitter2.getUsername(),
								userParent.getUsername(), userParent.getUserId().getEmail()))

		};

		for (ObjectBoundary boundary : objectsToStore) {
			ObjectBoundary storedBoundary = this.objects.storeInDatabase(boundary);
			System.err.println("Created: " + storedBoundary);
			storedBoundary.getObjectDetails().put("messageId", storedBoundary.getObjectId().getId());
			this.objects.updateById(storedBoundary.getObjectId().getId(), storedBoundary.getObjectId().getSuperapp(),
					storedBoundary.getCreatedBy().getUserId().getSuperapp(),
					storedBoundary.getCreatedBy().getUserId().getEmail(), storedBoundary);
		}

		for (UserBoundary user : usersToStore) {
			if (user.getRole() != Role.ADMIN) {
				user.setRole(Role.MINIAPP_USER);
				user = this.users.updateById(user.getUserId().getSuperapp(), user.getUserId().getEmail(), user).get();
				System.err.println("Updated: " + user);
			}
		}
	}

	private Map<String, Object> createBabysittingEventDetails(String message, String date, String babysitterUid,
			String parentUid, String mailParent) {
		Map<String, Object> objectDetails = new HashMap<>();
		objectDetails.put("messageText", message);
		objectDetails.put("selectedDate", date);
		objectDetails.put("babysitterUid", babysitterUid);
		objectDetails.put("parentUid", parentUid);
		objectDetails.put("mailParent", mailParent);
		objectDetails.put("status", null);
		return objectDetails;
	}

	private void storeBabysittersAndParentsObjectsInDatabase() {
		objectsToStore = new ObjectBoundary[] {
				createBoundary("1", superapp, "Babysitter", "Aa,12345",
						new Location(32.1785837, 34.8810362), true,
						createCreatedBy(superapp, "adi@gmail.com"),
						createBabysitterDetails("מוטה גור 2, רעננה, ישראל", "adi@gmail.com", 32.1785837, true, "4",
								"1993-07-01", 7, "1", "Aa,12345", "0526553008", 25, "adi", "r", 34.8810362)),
				createBoundary("2", superapp, "Babysitter", "Aa!14567",
						new Location(37.4220936, -122.083922), true,
						createCreatedBy(superapp, "rotem@gmail.com"),
						createBabysitterDetails("Unnamed Road, Mountain View, CA 94043, USA", "rotem@gmai.com",
								37.4220936, false, "t", "1986-07-01", 4, "2", "Aa!14567", "0526553002", 60, "rotem",
								"t", -122.083922)),
				createBoundary("3", superapp, "Parent", "Aa,12345", new Location(32.1785796, 34.881024),
						true, createCreatedBy(superapp, "noa@gmail.com"),
						createParentDetails("3", "Aa,12345", 2, "מוטה גור 2, רעננה, ישראל", "noa@gmail.com",
								"0526553001", 32.1785796, "noa", 34.881024)) };

		for (ObjectBoundary boundary : objectsToStore) {
			ObjectBoundary storedBoundary = this.objects.storeInDatabase(boundary);
			System.err.println("Created: " + storedBoundary);
		}
	}

	private ObjectBoundary createBoundary(String id, String superApp, String type, String alias, Location location,
			boolean active, CreatedBy createdBy, Map<String, Object> objectDetails) {
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

	private ObjectId createObjectId(String id, String superApp) {
		ObjectId objectId = new ObjectId();
		objectId.setId(id);
		objectId.setSuperapp(superApp);
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

	private Map<String, Object> createBabysitterDetails(String address, String mail, double latitude, boolean smoke,
			String description, String dateOfBirth, int experience, String uid, String password, String phone,
			int hourlyWage, String name, String maritalStatus, double longitude) {
		Map<String, Object> objectDetails = new HashMap<>();
		objectDetails.put("address", address);
		objectDetails.put("mail", mail);
		objectDetails.put("latitude", latitude);
		objectDetails.put("smoke", smoke);
		objectDetails.put("description", description);
		objectDetails.put("dateOfBirth", dateOfBirth);
		objectDetails.put("experience", experience);
		objectDetails.put("uid", uid);
		objectDetails.put("password", password);
		objectDetails.put("phone", phone);
		objectDetails.put("hourlyWage", hourlyWage);
		objectDetails.put("name", name);
		objectDetails.put("maritalStatus", maritalStatus);
		objectDetails.put("longitude", longitude);
		return objectDetails;
	}

	private Map<String, Object> createParentDetails(String uid, String password, int numberOfChildren, String address,
			String mail, String phone, double latitude, String name, double longitude) {
		Map<String, Object> objectDetails = new HashMap<>();
		objectDetails.put("uid", uid);
		objectDetails.put("password", password);
		objectDetails.put("numberOfChildren", numberOfChildren);
		objectDetails.put("address", address);
		objectDetails.put("mail", mail);
		objectDetails.put("phone", phone);
		objectDetails.put("latitude", latitude);
		objectDetails.put("name", name);
		objectDetails.put("longitude", longitude);
		return objectDetails;
	}

	private void storeUsersInDatabase() {
		usersToStore = new UserBoundary[4];
		int i = 0;
		NewUserBoundary[] newusersToStore = new NewUserBoundary[] {
				createNewUserBoundary("adi@gmail.com", Role.SUPERAPP_USER, "adi@gmail.com", "Aa,12345"),
				createNewUserBoundary("noa@gmail.com", Role.SUPERAPP_USER, "noa@gmail.com", "Aa,12345"),
				createNewUserBoundary("rotem@gmail.com", Role.SUPERAPP_USER, "rotem@gmail.com", "Aa!14567"),
				createNewUserBoundary("admin@gmail.com", Role.ADMIN, "admin@gmail.com", "string") };

		for (NewUserBoundary user : newusersToStore) {
			usersToStore[i] = this.users.createNewUser(user);
			System.err.println("Created: " + usersToStore[i]);
			i++;
		}
	}

	private void updateUsersInDatabase() {

		for (UserBoundary user : usersToStore) {
			for (ObjectBoundary object : objectsToStore) {
				if (user.getUserId().getEmail().equals(object.getCreatedBy().getUserId().getEmail())) {
					object.getObjectDetails().put("uid", object.getObjectId().getId());
					this.objects.updateById(object.getObjectId().getId(), object.getObjectId().getSuperapp(),
							object.getObjectId().getSuperapp(), user.getUserId().getEmail(), object);
					System.err.println("Updated: " + object);
				}
			}
			if (user.getRole() != Role.ADMIN) {
				user.setRole(Role.MINIAPP_USER);
				user.setUsername(getObjectID(user.getUserId().getEmail()));
				user = this.users.updateById(user.getUserId().getSuperapp(), user.getUserId().getEmail(), user).get();
				System.err.println("Updated: " + user);
			}
		}

	}

	private String getObjectID(String email) {
		for (ObjectBoundary object : objectsToStore) {
			if (object.getCreatedBy().getUserId().getEmail().equals(email)) {
				return object.getObjectId().getId();
			}
		}
		return null;
	}

	private NewUserBoundary createNewUserBoundary(String email, Role role, String userName, String avatar) {
		NewUserBoundary user = new NewUserBoundary();
		user.setEmail(email);
		user.setRole(role);
		user.setUsername(userName);
		user.setAvatar(avatar);
		return user;
	}
}
