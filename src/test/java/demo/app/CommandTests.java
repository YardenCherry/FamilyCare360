package demo.app;

import demo.app.Utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.ObjectBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.entities.MiniAppCommandEntity;
import demo.app.enums.Role;
import demo.app.objects.CommandId;
import jakarta.annotation.PostConstruct;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class CommandTests {

	private String url;
	private int port;
	private RestClient restClient;
	@Value("${spring.application.name}")
	private String superapp;
	private final ObjectMapper objectMapper = new ObjectMapper();

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
	public void testCreateCommandByMiniappAndObjectExiting() throws Exception {
		UserBoundary miniappUser = addMiniAppUser();
		ObjectBoundary targetObject = addObjectBySuperapp();
		MiniAppCommandBoundary miniapp = Utils.createNewCommandByMiniapp(targetObject.getObjectId().getId(),
				miniappUser);
		System.err.println(miniapp);

		Object[] response = this.restClient.post().uri("/miniapp/{miniAppName}", miniapp.getCommandId().getMiniapp())
				.body(miniapp).retrieve().body(Object[].class);

		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdmin).retrieve()
				.body(UserBoundary.class);

		MiniAppCommandBoundary[] mini = this.restClient.get()
				.uri("/admin/miniapp?userSuperapp={superapp}&userEmail={adminEmail}&size=5&page=0",
						adminUser.getUserId().getSuperapp(), adminUser.getUserId().getEmail())
				.retrieve().body(MiniAppCommandBoundary[].class);

		miniapp.getCommandId().setId(mini[0].getCommandId().getId());
		assertThat(response).usingRecursiveComparison().isEqualTo(miniapp);
	}

	@Test
	public void testCreateCommandByMiniappWithNullMiniappNameAndObjectExiting() throws Exception {
		UserBoundary miniappUser = addMiniAppUser();
		miniappUser.getUserId().setSuperapp(null);
		ObjectBoundary targetObject = addObjectBySuperapp();
		MiniAppCommandBoundary miniapp = Utils.createNewCommandByMiniapp(targetObject.getObjectId().getId(),
				miniappUser);
		System.err.println(miniapp);

		assertThatThrownBy(
				() -> this.restClient.post().uri("/miniapp/{miniAppName}", miniapp.getCommandId().getMiniapp())
						.body(miniapp).retrieve().body(Object[].class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(403);
	}

	@Test
	public void testCreateCommandByMiniappWithNullObjectID() throws Exception {
		UserBoundary miniappUser = addMiniAppUser();
		MiniAppCommandBoundary miniapp = Utils.createNewCommandByMiniapp(null, miniappUser);
		System.err.println(miniapp);

		assertThatThrownBy(
				() -> this.restClient.post().uri("/miniapp/{miniAppName}", miniapp.getCommandId().getMiniapp())
						.body(miniapp).retrieve().body(Object[].class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(400);
	}

	@Test
	public void testCreateCommandByMiniappWithNullTargetObject() throws Exception {
		UserBoundary miniappUser = addMiniAppUser();
		ObjectBoundary targetObject = addObjectBySuperapp();
		targetObject = null;
		MiniAppCommandBoundary miniapp = Utils.createNewCommandByMiniapp(null, miniappUser);
		System.err.println(miniapp);

		assertThatThrownBy(
				() -> this.restClient.post().uri("/miniapp/{miniAppName}", miniapp.getCommandId().getMiniapp())
						.body(miniapp).retrieve().body(Object[].class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(400);
	}

	@Test
	public void testCreateCommandByMiniappWithNullCommandID() throws Exception {
		UserBoundary miniappUser = addMiniAppUser();
		ObjectBoundary targetObject = addObjectBySuperapp();
		MiniAppCommandBoundary miniapp = Utils.createNewCommandByMiniapp(targetObject.getObjectId().getId(),
				miniappUser);
		System.err.println(miniapp);
		String miniappName = miniapp.getCommandId().getMiniapp();
		miniapp.setCommandId(null);

		Object[] response = this.restClient.post().uri("/miniapp/{miniAppName}", miniappName).body(miniapp).retrieve()
				.body(Object[].class);

		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdmin).retrieve()
				.body(UserBoundary.class);

		MiniAppCommandBoundary[] mini = this.restClient.get()
				.uri("/admin/miniapp?userSuperapp={superapp}&userEmail={adminEmail}&size=5&page=0",
						adminUser.getUserId().getSuperapp(), adminUser.getUserId().getEmail())
				.retrieve().body(MiniAppCommandBoundary[].class);

		miniapp.setCommandId(new CommandId());
		miniapp.getCommandId().setId(mini[0].getCommandId().getId());
		assertThat(response).usingRecursiveComparison().isEqualTo(miniapp);
	}

	@Test
	public void testCreateCommandByMiniappWithNullInvokedBy() throws Exception {
		UserBoundary miniappUser = addMiniAppUser();
		ObjectBoundary targetObject = addObjectBySuperapp();
		MiniAppCommandBoundary miniapp = Utils.createNewCommandByMiniapp(targetObject.getObjectId().getId(),
				miniappUser);
		System.err.println(miniapp);
		miniapp.setInvokedBy(null);

		assertThatThrownBy(
				() -> this.restClient.post().uri("/miniapp/{miniAppName}", miniapp.getCommandId().getMiniapp())
						.body(miniapp).retrieve().body(Object[].class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(400);
	}

	public UserBoundary addMiniAppUser() {
		NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
		UserBoundary miniappUser = this.restClient.post().uri("/users").body(newMiniappUser).retrieve()
				.body(UserBoundary.class);
		return miniappUser;
	}

	public ObjectBoundary addObjectBySuperapp() {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);

		return object1;
	}

	public ObjectBoundary addObjectNotActiveBySuperapp() {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		newObject1.setActive(false);
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);

		return object1;
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

	@Test
	public void testCreateCommandBySuperappUserResponseWith403() throws Exception {
		NewUserBoundary superappUser = Utils.createNewUserSuperapp();
		ObjectBoundary targetObject = addObjectBySuperapp();
		MiniAppCommandBoundary superapp = Utils.createNewCommandBySuperapp(targetObject.getObjectId().getId());
		System.err.println(superapp);

		assertThatThrownBy(
				() -> this.restClient.post().uri("/miniapp/{miniAppName}", superapp.getCommandId().getMiniapp())
						.body(superapp).retrieve().body(Object[].class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(403);
	}

	@Test
	public void testCreateCommandByAdminUserResponseWith403() throws Exception {
		NewUserBoundary adminUser = Utils.createNewUserAdmin();
		ObjectBoundary targetObject = addObjectBySuperapp();
		MiniAppCommandBoundary admin = Utils.createNewCommandByAdmin(targetObject.getObjectId().getId());
		System.err.println(admin);

		assertThatThrownBy(() -> this.restClient.post().uri("/miniapp/{miniAppName}", admin.getCommandId().getMiniapp())
				.body(admin).retrieve().body(Object[].class)).isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode").extracting("value").isEqualTo(403);
	}

	@Test
	public void testCreateCommandByMiniappWithNonActiveTargetObject() throws Exception {
		UserBoundary miniappUser = addMiniAppUser();
		ObjectBoundary targetObject = addObjectNotActiveBySuperapp();
		MiniAppCommandBoundary miniapp = Utils.createNewCommandByMiniapp(targetObject.getObjectId().getId(),
				miniappUser);
		System.err.println(miniapp);

		assertThatThrownBy(
				() -> this.restClient.post().uri("/miniapp/{miniAppName}", miniapp.getCommandId().getMiniapp())
						.body(miniapp).retrieve().body(Object[].class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(400);
	}

}