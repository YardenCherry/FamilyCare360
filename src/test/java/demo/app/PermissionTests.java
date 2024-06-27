package demo.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.UserBoundary;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class PermissionTests {
	private String url;
	private int port;
	private RestClient restClient;

	private UserBoundary[] users;

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

		users = new UserBoundary[] { adminUser, superappUser, miniappUser };
	}

	@AfterEach
	public void cleanup() {
		this.restClient.delete()
				.uri("/admin/miniapp?userSuperapp={superapp}&userEmail={adminEmail}", superapp, Utils.getAdminEmail())
				.retrieve();

		this.restClient.delete()
				.uri("/admin/objects?userSuperapp={superapp}&userEmail={adminEmail}", superapp, Utils.getAdminEmail())
				.retrieve();

		this.restClient.delete()
				.uri("/admin/users?userSuperapp={superapp}&userEmail={adminEmail}", superapp, Utils.getAdminEmail())
				.retrieve();
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testAdminGetAllUsers() throws Exception {
		UserBoundary[] response = this.restClient
				.get()
				.uri("/admin/users?userSuperapp={superapp}&userEmail={adminEmail}&size=5&page=0", superapp, Utils.getAdminEmail())
				.retrieve()
				.body(UserBoundary[].class);

		assertThat(response)
		.hasSize(users.length)
		.usingRecursiveFieldByFieldElementComparator()
        .containsAnyElementsOf(Arrays.asList(users));
	}
	
	@Test
	public void testSuperappGetAllUsers() throws Exception {

		assertThatThrownBy(() -> this.restClient
				.get()
				.uri("/admin/users?userSuperapp={superapp}&userEmail={adminEmail}&size=5&page=0", superapp, Utils.getSuperappUserEmail())
				.retrieve()
				.body(UserBoundary[].class))
			.isInstanceOf(HttpStatusCodeException.class)
			.extracting("statusCode")
			.extracting("value")
			.isEqualTo(403);
	}

	@Test
	public void testMiniappGetAllUsers() throws Exception {

		assertThatThrownBy(() -> this.restClient
				.get()
				.uri("/admin/users?userSuperapp={superapp}&userEmail={adminEmail}&size=5&page=0", superapp, Utils.getMiniappUserEmail())
				.retrieve()
				.body(UserBoundary[].class))
			.isInstanceOf(HttpStatusCodeException.class)
			.extracting("statusCode")
			.extracting("value")
			.isEqualTo(403);
	}
	
	@Test
	public void testAdminDeleteAllUsers() throws Exception {
		this.restClient
				.delete()
				.uri("/admin/users?userSuperapp={superapp}&userEmail={adminEmail}", superapp, Utils.getAdminEmail())
				.retrieve();
	}
	
	@Test
	public void testSuperappDeleteAllUsers() throws Exception {

		assertThatThrownBy(() -> this.restClient
				.delete()
				.uri("/admin/users?userSuperapp={superapp}&userEmail={adminEmail}", superapp, Utils.getSuperappUserEmail())
				.retrieve()
				.body(UserBoundary[].class))
			.isInstanceOf(HttpStatusCodeException.class)
			.extracting("statusCode")
			.extracting("value")
			.isEqualTo(403);
	}

	@Test
	public void testMiniappDeleteAllUsers() throws Exception {

		assertThatThrownBy(() -> this.restClient
				.delete()
				.uri("/admin/users?userSuperapp={superapp}&userEmail={adminEmail}", superapp, Utils.getMiniappUserEmail())
				.retrieve()
				.body(UserBoundary[].class))
			.isInstanceOf(HttpStatusCodeException.class)
			.extracting("statusCode")
			.extracting("value")
			.isEqualTo(403);
	}
}