package demo.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.RestClient;

import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.enums.Role;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class ObjectTests {
	
	private String url;
	private int port;
	private RestClient restClient;
	@Value("${spring.application.name}")
	private String superapp;

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
	
	public void addUsers() {
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient
				.post().uri("/users")
				.body(newAdmin)
				.retrieve()
				.body(UserBoundary.class);

		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient
				.post().uri("/users")
				.body(newSuperappUser)
				.retrieve()
				.body(UserBoundary.class);

		NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
		UserBoundary miniappUser = this.restClient
				.post().uri("/users")
				.body(newMiniappUser)
				.retrieve()
				.body(UserBoundary.class);
	}
	
	public void deleteAllAndAddAdmin() {
		NewUserBoundary newAdmin = Utils.createNewUserBoundary("admin_avatar", "adminEmail@demo.or", Role.ADMIN,
				"admin_username");
		UserBoundary newUserAdmin= this.restClient
				.post().uri("/users")
				.body(newAdmin)
				.retrieve()
				.body(UserBoundary.class);
		
		this.restClient.delete()
				.uri("/admin/miniapp?userSuperapp={superapp}&userEmail={adminEmail}", newUserAdmin.getUserId().getSuperapp(), newUserAdmin.getUserId().getEmail())
				.retrieve();

		this.restClient.delete()
				.uri("/admin/objects?userSuperapp={superapp}&userEmail={adminEmail}", newUserAdmin.getUserId().getSuperapp(), newUserAdmin.getUserId().getEmail())
				.retrieve();

		this.restClient.delete()
				.uri("/admin/users?userSuperapp={superapp}&userEmail={adminEmail}", newUserAdmin.getUserId().getSuperapp(), newUserAdmin.getUserId().getEmail())
				.retrieve();
	}

}