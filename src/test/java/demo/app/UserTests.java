package demo.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.enums.Role;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class UserTests {

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

	@Test
	public void testCreateNewUserToDatabase() throws Exception {
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdmin).retrieve()
				.body(UserBoundary.class);

		assertThat(this.restClient.get()
				.uri("/users/login/{superapp}/{email}", adminUser.getUserId().getSuperapp(),
						adminUser.getUserId().getEmail())
				.retrieve().body(UserBoundary.class)).usingRecursiveComparison().isEqualTo(adminUser);
	}

	@Test
	public void testCreateNewUserWithAnExistingEmailToDatabase() throws Exception {
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdmin).retrieve()
				.body(UserBoundary.class);

		assertThatThrownBy(
				() -> this.restClient.post().uri("/users").body(newAdmin).retrieve().body(UserBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(400);
	}

	@Test
	public void testCreateNewUserWithNullAvatar() throws Exception {
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		newAdmin.setAvatar(null);
		assertThatThrownBy(
				() -> this.restClient.post().uri("/users").body(newAdmin).retrieve().body(UserBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(400);
	}

	@Test
	public void testCreateNewUserWithEmptyAvatar() throws Exception {
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		newAdmin.setAvatar("");
		assertThatThrownBy(
				() -> this.restClient.post().uri("/users").body(newAdmin).retrieve().body(UserBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(400);
	}

	@Test
	public void testCreateNewUserWithNullUserName() throws Exception {
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		newAdmin.setUsername(null);
		assertThatThrownBy(
				() -> this.restClient.post().uri("/users").body(newAdmin).retrieve().body(UserBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(400);
	}

	@Test
	public void testCreateNewUserWithEmptyUserName() throws Exception {
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		newAdmin.setUsername("");
		assertThatThrownBy(
				() -> this.restClient.post().uri("/users").body(newAdmin).retrieve().body(UserBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(400);
	}

	@Test
	public void testCreateNewUserWithNullEmail() throws Exception {
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		newAdmin.setEmail(null);
		assertThatThrownBy(
				() -> this.restClient.post().uri("/users").body(newAdmin).retrieve().body(UserBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(400);
	}

	@Test
	public void testCreateNewUserWithEmptyEmail() throws Exception {
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		newAdmin.setEmail("");
		assertThatThrownBy(
				() -> this.restClient.post().uri("/users").body(newAdmin).retrieve().body(UserBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(400);
	}

//	public void testCreateNewUserWithInvalidRole() throws Exception{
//		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
//		newAdmin.setRole(Role.ADMI);
//		newAdmin.setEmail("admin");
//		assertThatThrownBy(() -> this.restClient
//				.post().uri("/users")
//				.body(newAdmin)
//				.retrieve()
//				.body(UserBoundary.class))
//				.isInstanceOf(HttpStatusCodeException.class)
//				.extracting("statusCode")
//				.extracting("value")
//				.isEqualTo(400);
//	}

	@Test
	public void testCreateNewUserWithInvalidEmail() throws Exception {
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		newAdmin.setEmail("admin");
		assertThatThrownBy(
				() -> this.restClient.post().uri("/users").body(newAdmin).retrieve().body(UserBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(400);
	}

	@Test
	public void testLoginUserToDatabase() throws Exception {
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdmin).retrieve()
				.body(UserBoundary.class);

		NewUserBoundary newSuperapp = Utils.createNewUserSuperapp();
		this.restClient.post().uri("/users").body(newSuperapp).retrieve().body(UserBoundary.class);

		NewUserBoundary newMiniApp = Utils.createNewUserMiniapp();
		this.restClient.post().uri("/users").body(newMiniApp).retrieve().body(UserBoundary.class);

		assertThat(this.restClient.get()
				.uri("/users/login/{superapp}/{email}", adminUser.getUserId().getSuperapp(),
						adminUser.getUserId().getEmail())
				.retrieve().body(UserBoundary.class)).usingRecursiveComparison().isEqualTo(adminUser);

	}

	@Test
	public void testLoginUserNonExistingToDatabase() throws Exception {
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdmin).retrieve()
				.body(UserBoundary.class);

		NewUserBoundary newSuperapp = Utils.createNewUserSuperapp();
		this.restClient.post().uri("/users").body(newSuperapp).retrieve().body(UserBoundary.class);

		NewUserBoundary newMiniApp = Utils.createNewUserMiniapp();
		this.restClient.post().uri("/users").body(newMiniApp).retrieve().body(UserBoundary.class);

		assertThatThrownBy(() -> this.restClient.get()
				.uri("/users/login/{superapp}/{email}", adminUser.getUserId().getSuperapp(), "noa@demo.org").retrieve()
				.body(UserBoundary.class)).isInstanceOf(HttpStatusCodeException.class).extracting("statusCode")
				.extracting("value").isEqualTo(404);
	}

	@Test
	public void testUpdateAvatarUserToDatabase() throws Exception {
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdmin).retrieve()
				.body(UserBoundary.class);

		NewUserBoundary newSuperapp = Utils.createNewUserSuperapp();
		this.restClient.post().uri("/users").body(newSuperapp).retrieve().body(UserBoundary.class);

		Map<String, Object> update = new HashMap<>();
		update.put("avatar", "new");

		this.restClient.put()
				.uri("/users/{superapp}/{email}", adminUser.getUserId().getSuperapp(), adminUser.getUserId().getEmail())
				.body(update).retrieve().body(UserBoundary.class);

		adminUser.setAvatar("new");

		assertThat(this.restClient.get()
				.uri("/users/login/{superapp}/{email}", adminUser.getUserId().getSuperapp(),
						adminUser.getUserId().getEmail())
				.retrieve().body(UserBoundary.class)).usingRecursiveComparison().isEqualTo(adminUser);

	}

	@Test
	public void testUpdateNameUserToDatabase() throws Exception {
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdmin).retrieve()
				.body(UserBoundary.class);

		NewUserBoundary newSuperapp = Utils.createNewUserSuperapp();
		this.restClient.post().uri("/users").body(newSuperapp).retrieve().body(UserBoundary.class);

		Map<String, Object> update = new HashMap<>();
		update.put("username", "new");

		this.restClient.put()
				.uri("/users/{superapp}/{email}", adminUser.getUserId().getSuperapp(), adminUser.getUserId().getEmail())
				.body(update).retrieve().body(UserBoundary.class);

		adminUser.setUsername("new");

		assertThat(this.restClient.get()
				.uri("/users/login/{superapp}/{email}", adminUser.getUserId().getSuperapp(),
						adminUser.getUserId().getEmail())
				.retrieve().body(UserBoundary.class)).usingRecursiveComparison().isEqualTo(adminUser);
	}

	@Test
	public void testUpdateRoleUserToDatabase() throws Exception {
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdmin).retrieve()
				.body(UserBoundary.class);

		NewUserBoundary newSuperapp = Utils.createNewUserSuperapp();
		this.restClient.post().uri("/users").body(newSuperapp).retrieve().body(UserBoundary.class);

		Map<String, Object> update = new HashMap<>();
		update.put("role", Role.MINIAPP_USER);

		this.restClient.put()
				.uri("/users/{superapp}/{email}", adminUser.getUserId().getSuperapp(), adminUser.getUserId().getEmail())
				.body(update).retrieve().body(UserBoundary.class);

		adminUser.setRole(Role.MINIAPP_USER);

		assertThat(this.restClient.get()
				.uri("/users/login/{superapp}/{email}", adminUser.getUserId().getSuperapp(),
						adminUser.getUserId().getEmail())
				.retrieve().body(UserBoundary.class)).usingRecursiveComparison().isEqualTo(adminUser);

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