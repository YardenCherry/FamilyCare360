package demo.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.RestClient;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.ObjectBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.enums.Role;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class AdminTests {

	private String url;
	private int port;
	private RestClient restClient;
	@Value("${spring.application.name}")
	private String superapp;
	private UserBoundary[] users = null;
	private ObjectBoundary[] objects = null;
	private MiniAppCommandBoundary[] commands = null;

	@Value("${server.port:8084}")
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		System.err.println("beginning test");
		this.url = "http://localhost:" + port + "/superapp";
		this.restClient = RestClient.create(url);
	}

	@BeforeEach
	public void setup() {
		System.err.println("set up");
		// DELETE database
		deleteAllAndAddAdmin();
	}

	@AfterEach
	public void tearDown() {
		System.err.println("tear down");
		// DELETE database
		deleteAllAndAddAdmin();
	}

//	
//	@Test
//	public void testAdminGetAllObjects()  throws Exception {
//		addObjects();
//		
//		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
//		UserBoundary adminUser = this.restClient
//				.post().uri("/users")
//				.body(newAdmin)
//				.retrieve()
//				.body(UserBoundary.class);
//		
//		ObjectBoundary[] response = this.restClient
//				.get()
//				.uri("/admin/objects?userSuperapp={superapp}&userEmail={adminEmail}&size=5&page=0", superapp, Utils.getAdminEmail())
//				.retrieve()
//				.body(ObjectBoundary[].class);
//
//		assertThat(response)
//		.hasSize(objects.length)
//		.usingRecursiveFieldByFieldElementComparator()
//        .containsAnyElementsOf(Arrays.asList(objects));
//		
//	}
//	
	public void addUsers() {
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdmin).retrieve()
				.body(UserBoundary.class);

		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
		UserBoundary miniappUser = this.restClient.post().uri("/users").body(newMiniappUser).retrieve()
				.body(UserBoundary.class);

		users = new UserBoundary[] { adminUser, superappUser, miniappUser };
	}

	public void addObjects() {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class);

		ObjectBoundary newObject2 = Utils.createNewObject2BySuperapp();
		this.restClient.post().uri("/objects").body(newObject2).retrieve().body(ObjectBoundary.class);

		ObjectBoundary newObject3 = Utils.createNewObject3BySuperapp();
		this.restClient.post().uri("/objects").body(newObject3).retrieve().body(ObjectBoundary.class);

		ObjectBoundary newObject4 = Utils.createNewObject4BySuperapp();
		this.restClient.post().uri("/objects").body(newObject4).retrieve().body(ObjectBoundary.class);

		objects = new ObjectBoundary[] { newObject1, newObject2, newObject3, newObject4 };

	}

	public void deleteAllAndAddAdmin() {
		NewUserBoundary newAdmin = Utils.createNewUserBoundary("admin_avatar", "adminEmail@demo.or", Role.ADMIN,
				"admin_username");
		UserBoundary newUserAdmin = this.restClient.post().uri("/users").body(newAdmin).retrieve()
				.body(UserBoundary.class);

		this.restClient.delete().uri("/admin/miniapp?userSuperapp={superapp}&userEmail={adminEmail}",
				newUserAdmin.getUserId().getSuperapp(), newUserAdmin.getUserId().getEmail()).retrieve();

		this.restClient.delete().uri("/admin/objects?userSuperapp={superapp}&userEmail={adminEmail}",
				newUserAdmin.getUserId().getSuperapp(), newUserAdmin.getUserId().getEmail()).retrieve();

		this.restClient.delete().uri("/admin/users?userSuperapp={superapp}&userEmail={adminEmail}",
				newUserAdmin.getUserId().getSuperapp(), newUserAdmin.getUserId().getEmail()).retrieve();
	}

}