package demo.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.checkerframework.checker.lock.qual.NewObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.ObjectBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.enums.Role;
import demo.app.objects.Location;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class ObjectTests {
	
	private String url;
	private int port;
	private RestClient restClient;
	@Value("${spring.application.name}")
	private String superapp;
	private ObjectBoundary[] objects=null;

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
	public void testSuperAppCreateObject()  throws Exception {
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
		
		assertThat(this.restClient
				.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}", object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),object1.getCreatedBy().getUserId().getSuperapp(),object1.getCreatedBy().getUserId().getEmail())
				.retrieve()
				.body(ObjectBoundary.class))
		.usingRecursiveComparison()
		.isEqualTo(object1);
	}
	
	@Test
	public void testSuperAppCreateObjectWithNullAlias()  throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient
				.post().uri("/users")
				.body(newSuperappUser)
				.retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		newObject1.setAlias(null);
		assertThatThrownBy(() -> this.restClient
				.post().uri("/objects")
				.body(newObject1)
				.retrieve()
				.body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode")
				.extracting("value")
				.isEqualTo(400);

	}
	
	@Test
	public void testSuperAppCreateObjectWithEmptyAlias()  throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient
				.post().uri("/users")
				.body(newSuperappUser)
				.retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		newObject1.setAlias("");
		assertThatThrownBy(() -> this.restClient
				.post().uri("/objects")
				.body(newObject1)
				.retrieve()
				.body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode")
				.extracting("value")
				.isEqualTo(400);

	}
	
	@Test
	public void testSuperAppCreateObjectWithEmptyCreatedByDetails() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient
				.post().uri("/users")
				.body(newSuperappUser)
				.retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		newObject1.getCreatedBy().getUserId().setSuperapp("");
		newObject1.getCreatedBy().getUserId().setEmail("");
		assertThatThrownBy(() -> this.restClient
				.post().uri("/objects")
				.body(newObject1)
				.retrieve()
				.body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode")
				.extracting("value")
				.isEqualTo(403);
	}
	
	@Test
	public void testSuperAppCreateObjectWithNullType()  throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient
				.post().uri("/users")
				.body(newSuperappUser)
				.retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		newObject1.setType(null);
		assertThatThrownBy(() -> this.restClient
				.post().uri("/objects")
				.body(newObject1)
				.retrieve()
				.body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode")
				.extracting("value")
				.isEqualTo(400);
	}
	
	@Test
	public void testSuperAppCreateObjectWithEmptyType()  throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient
				.post().uri("/users")
				.body(newSuperappUser)
				.retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		newObject1.setType("");
		assertThatThrownBy(() -> this.restClient
				.post().uri("/objects")
				.body(newObject1)
				.retrieve()
				.body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode")
				.extracting("value")
				.isEqualTo(400);
	}
	
	@Test
	public void testUpdateObjectTypeToDatabase() throws Exception{
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
		object1.setType("newType");

		this.restClient
		.put()
		.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),object1.getCreatedBy().getUserId().getSuperapp(),object1.getCreatedBy().getUserId().getEmail())
		.body(object1)
		.retrieve();

		assertThat(this.restClient
				.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),object1.getCreatedBy().getUserId().getSuperapp(),object1.getCreatedBy().getUserId().getEmail())
				.retrieve()
				.body(ObjectBoundary.class))
		.usingRecursiveComparison()
		.isEqualTo(object1);
		
	}
	
	@Test
	public void testUpdateObjectAliasToDatabase() throws Exception{
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
		object1.setAlias("newAlias");

		this.restClient
		.put()
		.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),object1.getCreatedBy().getUserId().getSuperapp(),object1.getCreatedBy().getUserId().getEmail())
		.body(object1)
		.retrieve();

		assertThat(this.restClient
				.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),object1.getCreatedBy().getUserId().getSuperapp(),object1.getCreatedBy().getUserId().getEmail())
				.retrieve()
				.body(ObjectBoundary.class))
		.usingRecursiveComparison()
		.isEqualTo(object1);
		
	}
	
	@Test
	public void testUpdateObjectLocationToDatabase() throws Exception{
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
		System.out.println(object1);
		
		object1.setLocation(new Location(39.625,32.665));
		System.out.println(object1);

		this.restClient
		.put()
		.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),object1.getCreatedBy().getUserId().getSuperapp(),object1.getCreatedBy().getUserId().getEmail())
		.body(object1)
		.retrieve();
		System.out.println(object1);

		assertThat(this.restClient
				.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),object1.getCreatedBy().getUserId().getSuperapp(),object1.getCreatedBy().getUserId().getEmail())
				.retrieve()
				.body(ObjectBoundary.class))
		.usingRecursiveComparison()
		.isEqualTo(object1);
		
	}
	

	
	@Test
	public void testSuperAppGetAllObjects()  throws Exception {
		UserBoundary superappUser = addObjects();
		
		ObjectBoundary[] response = this.restClient
				.get()
				.uri("/objects?userSuperapp={userSuperapp}&userEmail={userEmail}&size=5&page=0", superappUser.getUserId().getSuperapp(), superappUser.getUserId().getEmail())
				.retrieve()
				.body(ObjectBoundary[].class);

		assertThat(response)
		.hasSize(objects.length)
		.usingRecursiveFieldByFieldElementComparator()
        .containsAnyElementsOf(Arrays.asList(objects));
	}
	
	@Test
	public void testSuperGetObjectById()  throws Exception {
		UserBoundary superappUser = addObjects();
		
		ObjectBoundary[] response = this.restClient
				.get()
				.uri("/objects?userSuperapp={userSuperapp}&userEmail={userEmail}&size=5&page=0", superappUser.getUserId().getSuperapp(), superappUser.getUserId().getEmail())
				.retrieve()
				.body(ObjectBoundary[].class);

		assertThat(response)
		.hasSize(objects.length)
		.usingRecursiveFieldByFieldElementComparator()
        .containsAnyElementsOf(Arrays.asList(objects));
	}
	
	public UserBoundary addObjects() {
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
		
		ObjectBoundary newObject2 = Utils.createNewObject2BySuperapp();
		ObjectBoundary object2=this.restClient
		.post().uri("/objects")
		.body(newObject2)
		.retrieve()
		.body(ObjectBoundary.class);
		
		ObjectBoundary newObject3 = Utils.createNewObject3BySuperapp();
		ObjectBoundary object3= this.restClient
		.post().uri("/objects")
		.body(newObject3)
		.retrieve()
		.body(ObjectBoundary.class);
		
		ObjectBoundary newObject4 = Utils.createNewObject4BySuperapp();
		ObjectBoundary object4=this.restClient
		.post().uri("/objects")
		.body(newObject4)
		.retrieve()
		.body(ObjectBoundary.class);
		
		objects= new ObjectBoundary[] {object1, object2, object3, object4};
		return superappUser;
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