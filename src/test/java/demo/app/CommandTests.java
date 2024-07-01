package demo.app;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.RestClient;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.ObjectBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.entities.MiniAppCommandEntity;
import demo.app.enums.Role;
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
		UserBoundary miniappUser= addMiniAppUser();
		ObjectBoundary targetObject=addObjectBySuperapp();
		MiniAppCommandBoundary miniapp=Utils.createNewCommandByMiniapp(targetObject.getObjectId().getId(),miniappUser);
		System.err.println(miniapp);

		Object[] response=this.restClient
				.post()
				.uri("/miniapp/{miniAppName}",miniapp.getCommandId().getMiniapp())
				.body(miniapp)
				.retrieve()
				.body(Object[].class);
		
		NewUserBoundary newAdmin = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient
				.post().uri("/users")
				.body(newAdmin)
				.retrieve()
				.body(UserBoundary.class);
		  
		MiniAppCommandBoundary[] mini= this.restClient
				.get()
				.uri("/admin/miniapp?userSuperapp={superapp}&userEmail={adminEmail}&size=5&page=0", adminUser.getUserId().getSuperapp(),adminUser.getUserId().getEmail() )
				.retrieve()
				.body(MiniAppCommandBoundary[].class);
		
		miniapp.getCommandId().setId(mini[0].getCommandId().getId());	
		assertThat(response)
		.usingRecursiveComparison()
		.isEqualTo(miniapp);
	}	
	
	public UserBoundary addMiniAppUser() {
		NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
		UserBoundary miniappUser = this.restClient
				.post().uri("/users")
				.body(newMiniappUser)
				.retrieve()
				.body(UserBoundary.class);
		return miniappUser;
	}
	
	public ObjectBoundary addObjectBySuperapp() {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient
				.post().uri("/users")
				.body(newSuperappUser)
				.retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1=this.restClient
		.post().uri("/objects")
		.body(newObject1)
		.retrieve()
		.body(ObjectBoundary.class);
		
		return object1;
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