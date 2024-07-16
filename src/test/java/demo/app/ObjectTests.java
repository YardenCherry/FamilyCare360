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
import demo.app.objects.ObjectId;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class ObjectTests {

	private String url;
	private int port;
	private RestClient restClient;
	@Value("${spring.application.name}")
	private String superapp;
	private ObjectBoundary[] objects = null;

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
    public void testGetObjectsByType() throws Exception {
	  NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);
	  	ObjectBoundary object1 =Utils.createNewObjectBySuperapp();
        ObjectBoundary object2 =Utils.createNewObjectBySuperapp();
        ObjectBoundary object3 =Utils.createNewObjectBySuperapp();
        String type = "testType";

        object1.setType(type);
        object2.setType(type);
        object3.setType("OtherType");
        
        ObjectBoundary ob1 = this.restClient.post().uri("/objects").body(object1).retrieve()
				.body(ObjectBoundary.class);
        ObjectBoundary ob2 = this.restClient.post().uri("/objects").body(object2).retrieve()
				.body(ObjectBoundary.class);
        ObjectBoundary ob3 = this.restClient.post().uri("/objects").body(object3).retrieve()
				.body(ObjectBoundary.class);
        ObjectBoundary[] objects1 = {ob1,ob2};
		ObjectBoundary[] response = this.restClient.get()
				.uri("/objects/search/byType/{type}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getType(), superappUser.getUserId().getSuperapp(),superappUser.getUserId().getEmail())
				.retrieve().body(ObjectBoundary[].class);

	assertThat(response).hasSize(objects1.length).usingRecursiveFieldByFieldElementComparator()
				.containsAnyElementsOf(Arrays.asList(objects1));  
        
    }
	
	@Test
    public void testAdminGetObjectsByType() throws Exception {
	  NewUserBoundary newAdminUser = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdminUser).retrieve()
				.body(UserBoundary.class);
		
	NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
			UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
					.body(UserBoundary.class);
			
	  	ObjectBoundary object1 =Utils.createNewObjectBySuperapp();
        ObjectBoundary object2 =Utils.createNewObjectBySuperapp();
        ObjectBoundary object3 =Utils.createNewObjectBySuperapp();
        String type = "testType";

        object1.setType(type);
        object2.setType(type);
        object3.setType("OtherType");
        
        ObjectBoundary ob1 = this.restClient.post().uri("/objects").body(object1).retrieve()
				.body(ObjectBoundary.class);
        ObjectBoundary ob2 = this.restClient.post().uri("/objects").body(object2).retrieve()
				.body(ObjectBoundary.class);
        ObjectBoundary ob3 = this.restClient.post().uri("/objects").body(object3).retrieve()
				.body(ObjectBoundary.class);
        ObjectBoundary[] objects1 = {ob1,ob2};

		assertThatThrownBy(() -> this.restClient.get()
				.uri("/objects/search/byType/{type}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getType(), adminUser.getUserId().getSuperapp(),adminUser.getUserId().getEmail())
				.retrieve().body(ObjectBoundary[].class))
		.isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode").extracting("value").isEqualTo(403);
    }
	
	@Test
	public void testAdminGetObjectsByAlias() throws Exception {
	  NewUserBoundary newAdminUser = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdminUser).retrieve()
				.body(UserBoundary.class);
		
	NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
			UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
					.body(UserBoundary.class);
			
	  	ObjectBoundary object1 =Utils.createNewObjectBySuperapp();
	    ObjectBoundary object2 =Utils.createNewObjectBySuperapp();
	    ObjectBoundary object3 =Utils.createNewObjectBySuperapp();
	    String alias = "testAlias";

	    object1.setAlias(alias);
	    object2.setAlias(alias);
	    object3.setAlias("OtherAlias");
	    
	    ObjectBoundary ob1 = this.restClient.post().uri("/objects").body(object1).retrieve()
				.body(ObjectBoundary.class);
	    ObjectBoundary ob2 = this.restClient.post().uri("/objects").body(object2).retrieve()
				.body(ObjectBoundary.class);
	    ObjectBoundary ob3 = this.restClient.post().uri("/objects").body(object3).retrieve()
				.body(ObjectBoundary.class);
	    ObjectBoundary[] objects1 = {ob1,ob2};

		assertThatThrownBy(() -> this.restClient.get()
				.uri("/objects/search/byAlias/{alias}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						alias, adminUser.getUserId().getSuperapp(),adminUser.getUserId().getEmail())
				.retrieve().body(ObjectBoundary[].class))
		.isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode").extracting("value").isEqualTo(403);
	}
	
	@Test
	public void testSuperAppCreateObject() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);

		assertThat(this.restClient.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getCreatedBy().getUserId().getEmail())
				.retrieve().body(ObjectBoundary.class)).usingRecursiveComparison().isEqualTo(object1);
	}

	@Test
	public void testAdminCreateObject() throws Exception {
		NewUserBoundary newAdminUser = Utils.createNewUserAdmin();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newAdminUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectByAdmin();

		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(403);
	}

	@Test
	public void testMiniappCreateObject() throws Exception {
		NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
		UserBoundary miniappUser = this.restClient.post().uri("/users").body(newMiniappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectByMiniApp();

		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(403);
	}

	@Test
	public void testSuperAppCreateObjectWithNullAlias() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		newObject1.setAlias(null);
		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(400);

	}

	@Test
	public void testAdminCreateObjectWithNullAlias() throws Exception {
		NewUserBoundary newAdminUser = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdminUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectByAdmin();
		newObject1.setAlias(null);
		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(403);

	}

	@Test
	public void testMiniappCreateObjectWithNullAlias() throws Exception {
		NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
		UserBoundary miniappUser = this.restClient.post().uri("/users").body(newMiniappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectByMiniApp();
		newObject1.setAlias(null);
		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(403);
	}

	@Test
	public void testSuperAppCreateObjectWithEmptyAlias() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		newObject1.setAlias("");
		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(400);

	}

	@Test
	public void testAdminCreateObjectWithEmptyAlias() throws Exception {
		NewUserBoundary newAdminUser = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdminUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectByAdmin();
		newObject1.setAlias("");
		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(403);

	}

	@Test
	public void testMiniappCreateObjectWithEmptyAlias() throws Exception {
		NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
		UserBoundary miniappUser = this.restClient.post().uri("/users").body(newMiniappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectByMiniApp();
		newObject1.setAlias("");
		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(403);
	}

	@Test
	public void testSuperAppCreateObjectWithEmptyCreatedByDetails() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		newObject1.getCreatedBy().getUserId().setSuperapp("");
		newObject1.getCreatedBy().getUserId().setEmail("");
		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(403);
	}

	@Test
	public void testAdminCreateObjectWithEmptyCreatedByDetails() throws Exception {
		NewUserBoundary newAdminUser = Utils.createNewUserSuperapp();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdminUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectByAdmin();
		newObject1.getCreatedBy().getUserId().setSuperapp("");
		newObject1.getCreatedBy().getUserId().setEmail("");
		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(403);
	}

	@Test
	public void testAMiniappCreateObjectWithEmptyCreatedByDetails() throws Exception {
		NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
		UserBoundary miniappUser = this.restClient.post().uri("/users").body(newMiniappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectByMiniApp();
		newObject1.getCreatedBy().getUserId().setSuperapp("");
		newObject1.getCreatedBy().getUserId().setEmail("");
		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(403);
	}

	@Test
	public void testSuperAppCreateObjectWithNullType() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		newObject1.setType(null);
		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(400);
	}

	@Test
	public void testAdminCreateObjectWithNullType() throws Exception {
		NewUserBoundary newAdminUser = Utils.createNewUserSuperapp();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdminUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectByAdmin();
		newObject1.setType(null);
		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(403);
	}

	@Test
	public void testMiniappCreateObjectWithNullType() throws Exception {
		NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
		UserBoundary miniappUser = this.restClient.post().uri("/users").body(newMiniappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectByMiniApp();
		newObject1.setType(null);
		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(403);
	}

	@Test
	public void testSuperAppCreateObjectWithEmptyType() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		newObject1.setType("");
		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(400);
	}

	@Test
	public void testAdminCreateObjectWithEmptyType() throws Exception {
		NewUserBoundary newAdminUser = Utils.createNewUserAdmin();
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdminUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectByAdmin();
		newObject1.setType("");
		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(403);
	}

	@Test
	public void testMiniappCreateObjectWithEmptyType() throws Exception {
		NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
		UserBoundary miniappUser = this.restClient.post().uri("/users").body(newMiniappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectByMiniApp();
		newObject1.setType("");
		assertThatThrownBy(
				() -> this.restClient.post().uri("/objects").body(newObject1).retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("value")
				.isEqualTo(403);
	}

	@Test
	public void testSuperappUpdateObjectTypeToDatabase() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);
		object1.setType("newType");

		this.restClient.put()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getCreatedBy().getUserId().getEmail())
				.body(object1).retrieve();

		assertThat(this.restClient.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getCreatedBy().getUserId().getEmail())
				.retrieve().body(ObjectBoundary.class)).usingRecursiveComparison().isEqualTo(object1);

	}

	@Test
	public void testAdminUpdateObjectTypeToDatabase() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		NewUserBoundary newAdminUser = Utils.createNewUserAdmin();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdminUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);
		object1.setType("newType");

		assertThatThrownBy(() -> this.restClient.put()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), adminUser.getUserId().getEmail())
				.body(object1).retrieve().body(ObjectBoundary.class)).isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode").extracting("value").isEqualTo(403);

	}

	@Test
	public void testMiniappUpdateObjectTypeToDatabase() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);
		UserBoundary miniappUser = this.restClient.post().uri("/users").body(newMiniappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);
		object1.setType("newType");

		assertThatThrownBy(() -> this.restClient.put()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), miniappUser.getUserId().getEmail())
				.body(object1).retrieve().body(ObjectBoundary.class)).isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode").extracting("value").isEqualTo(403);
	}

	@Test
	public void testSuperappUpdateObjectAliasToDatabase() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);
		object1.setAlias("newAlias");

		this.restClient.put()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getCreatedBy().getUserId().getEmail())
				.body(object1).retrieve();

		assertThat(this.restClient.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getCreatedBy().getUserId().getEmail())
				.retrieve().body(ObjectBoundary.class)).usingRecursiveComparison().isEqualTo(object1);

	}

	@Test
	public void testAdminUpdateObjectAliasToDatabase() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		NewUserBoundary newAdminUser = Utils.createNewUserAdmin();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdminUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);
		object1.setAlias("newAlias");

		assertThatThrownBy(() -> this.restClient.put()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), adminUser.getUserId().getEmail())
				.body(object1).retrieve().body(ObjectBoundary.class)).isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode").extracting("value").isEqualTo(403);
	}

	@Test
	public void testMiniappUpdateObjectAliasToDatabase() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);
		UserBoundary miniappUser = this.restClient.post().uri("/users").body(newMiniappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);
		object1.setAlias("newAlias");

		assertThatThrownBy(() -> this.restClient.put()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), miniappUser.getUserId().getEmail())
				.body(object1).retrieve().body(ObjectBoundary.class)).isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode").extracting("value").isEqualTo(403);
	}

	@Test
	public void testSuperappUpdateObjectIdAndSuperappToDatabase() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);
		ObjectId objectId1 = object1.getObjectId();
		objectId1.setId("11");
		objectId1.setSuperapp("2024.demo");
		object1.setObjectId(objectId1);

		assertThatThrownBy(() -> this.restClient.put()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getCreatedBy().getUserId().getEmail())
				.body(object1).retrieve().body(ObjectBoundary.class));

	}

	@Test
	public void testAdminUpdateObjectIdAndSuperappToDatabase() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		NewUserBoundary newAdminUser = Utils.createNewUserAdmin();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdminUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);
		ObjectId objectId1 = object1.getObjectId();
		objectId1.setId("11");
		objectId1.setSuperapp("2024.demo");
		object1.setObjectId(objectId1);

		assertThatThrownBy(() -> this.restClient.put()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), adminUser.getUserId().getEmail())
				.body(object1).retrieve().body(ObjectBoundary.class)).isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode").extracting("value").isEqualTo(403);
	}

	@Test
	public void testMiniappUpdateObjectIdAndSuperappToDatabase() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);
		UserBoundary miniappUser = this.restClient.post().uri("/users").body(newMiniappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);
		ObjectId objectId1 = object1.getObjectId();
		objectId1.setId("11");
		objectId1.setSuperapp("2024.demo");
		object1.setObjectId(objectId1);

		assertThatThrownBy(() -> this.restClient.put()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), miniappUser.getUserId().getEmail())
				.body(object1).retrieve().body(ObjectBoundary.class)).isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode").extracting("value").isEqualTo(403);
	}

	@Test
	public void testSuperappUpdateObjectLocationToDatabase() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);

		object1.setLocation(new Location(39.625, 32.665));

		this.restClient.put()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getCreatedBy().getUserId().getEmail())
				.body(object1).retrieve();

		assertThat(this.restClient.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getCreatedBy().getUserId().getEmail())
				.retrieve().body(ObjectBoundary.class)).usingRecursiveComparison().isEqualTo(object1);

	}

	@Test
	public void testAdminUpdateObjectLocationToDatabase() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		NewUserBoundary newAdminUser = Utils.createNewUserAdmin();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdminUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);

		object1.setLocation(new Location(39.625, 32.665));

		assertThatThrownBy(() -> this.restClient.put()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), adminUser.getUserId().getEmail())
				.body(object1).retrieve().body(ObjectBoundary.class)).isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode").extracting("value").isEqualTo(403);

	}

	@Test
	public void testMiniappUpdateObjectLocationToDatabase() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);
		UserBoundary miniappUser = this.restClient.post().uri("/users").body(newMiniappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);

		object1.setLocation(new Location(39.625, 32.665));

		assertThatThrownBy(() -> this.restClient.put()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), miniappUser.getUserId().getEmail())
				.body(object1).retrieve().body(ObjectBoundary.class)).isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode").extracting("value").isEqualTo(403);

	}

	@Test
	public void testSuperappTryToSearchAndFindAnActiveObject() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);

		assertThat(this.restClient.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getCreatedBy().getUserId().getEmail())
				.retrieve().body(ObjectBoundary.class)).usingRecursiveComparison().isEqualTo(object1);
	}

	@Test
	public void testSuperappTryToSearchAndFindAnNotActiveObject() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		newObject1.setActive(false);
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);

		assertThat(this.restClient.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getCreatedBy().getUserId().getEmail())
				.retrieve().body(ObjectBoundary.class)).usingRecursiveComparison().isEqualTo(object1);
	}

	@Test
	public void testAdminTryToSearchAndFindAnNotActiveObject() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		NewUserBoundary newAdminUser = Utils.createNewUserAdmin();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdminUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		newObject1.setActive(false);
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);

		assertThatThrownBy(() -> this.restClient.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						adminUser.getUserId().getSuperapp(), adminUser.getUserId().getEmail())
				.retrieve().body(ObjectBoundary.class)).isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode").extracting("value").isEqualTo(403);

	}

	@Test
	public void testMiniappTryToSearchAndFindAnNotActiveObject() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);
		UserBoundary miniappUser = this.restClient.post().uri("/users").body(newMiniappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		newObject1.setActive(false);

		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);

		assertThatThrownBy(() -> this.restClient.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getObjectId().getSuperapp(), object1.getObjectId().getId(),
						miniappUser.getUserId().getSuperapp(), miniappUser.getUserId().getEmail())
				.retrieve().body(ObjectBoundary.class)).isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode").extracting("value").isEqualTo(404);

	}

	@Test
	public void testAdminTryToSearchAndFindAnActiveObject() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		NewUserBoundary newAdminUser = Utils.createNewUserAdmin();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);
		UserBoundary adminUser = this.restClient.post().uri("/users").body(newAdminUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);

		assertThatThrownBy(() -> this.restClient.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getObjectId().getId(), object1.getCreatedBy().getUserId().getSuperapp(),
						adminUser.getUserId().getEmail())
				.retrieve().body(ObjectBoundary.class)).isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode").extracting("value").isEqualTo(403);
	}

	@Test
	public void testMiniappTryToSearchAndFindAnActiveObject() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		NewUserBoundary newMiniappUser = Utils.createNewUserAdmin();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);
		UserBoundary miniappUser = this.restClient.post().uri("/users").body(newMiniappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);

		assertThatThrownBy(() -> this.restClient.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getCreatedBy().getUserId().getSuperapp(), object1.getObjectId().getId(),
						object1.getObjectId().getId(), object1.getCreatedBy().getUserId().getSuperapp(),
						miniappUser.getUserId().getEmail())
				.retrieve().body(ObjectBoundary.class)).isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode").extracting("value").isEqualTo(403);
	}

	
	@Test
	public void testRetrieveObjectsByLocation() throws Exception {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		newObject1.setLocation(new Location(5,2));
		ObjectBoundary objectNotWithinDistance = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);
		
		ObjectBoundary newObject2 = Utils.createNewObjectBySuperapp();
		newObject2.setLocation(new Location(-1,-2));
		ObjectBoundary objectNotWithinDistance2 = this.restClient.post().uri("/objects").body(newObject2).retrieve()
				.body(ObjectBoundary.class);
		
		ObjectBoundary newObject3 = Utils.createNewObjectBySuperapp();
		newObject3.setLocation(new Location(0.5, 0));
		ObjectBoundary objectWithinDistance = this.restClient.post().uri("/objects").body(newObject3).retrieve()
				.body(ObjectBoundary.class);
		
		ObjectBoundary newObject4 = Utils.createNewObjectBySuperapp();
		newObject4.setLocation(new Location(0.5, 0.5));
		ObjectBoundary objectWithinDistance2 = this.restClient.post().uri("/objects").body(newObject4).retrieve()
				.body(ObjectBoundary.class);
		
		ObjectBoundary[] objects1 = {objectWithinDistance2,objectWithinDistance};

		ObjectBoundary[] response =this.restClient
				.get()
				.uri("/objects/search/byLocation/{lat}/{lng}/{distance}?units={units}&userSuperapp={userSuperapp}&userEmail={userEmail}&size=5&page=0",
			        0, 0, 2, "NEUTRAL",
			        superappUser.getUserId().getSuperapp(),
			        superappUser.getUserId().getEmail())
				.retrieve().body(ObjectBoundary[].class);
		
		assertThat(response).hasSize(objects1.length)
				.usingRecursiveFieldByFieldElementComparator()
				.containsAnyElementsOf(Arrays.asList(objects1));  		
	}
	
	
	
	@Test
    public void testGetObjectsByAlias() throws Exception {
	  NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);
	  	ObjectBoundary object1 =Utils.createNewObjectBySuperapp();
        ObjectBoundary object2 =Utils.createNewObjectBySuperapp();
        ObjectBoundary object3 =Utils.createNewObjectBySuperapp();
        String alias = "testAlias";

        object1.setAlias(alias);
        object2.setAlias(alias);
        object3.setAlias("OtherAlias");
        
        ObjectBoundary ob1 = this.restClient.post().uri("/objects").body(object1).retrieve()
				.body(ObjectBoundary.class);
        ObjectBoundary ob2 = this.restClient.post().uri("/objects").body(object2).retrieve()
				.body(ObjectBoundary.class);
        ObjectBoundary ob3 = this.restClient.post().uri("/objects").body(object3).retrieve()
				.body(ObjectBoundary.class);
        ObjectBoundary[] objects1 = {ob1,ob2};
		ObjectBoundary[] response = this.restClient.get()
				.uri("/objects/search/byAlias/{alias}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getAlias(), superappUser.getUserId().getSuperapp(),superappUser.getUserId().getEmail())
				.retrieve().body(ObjectBoundary[].class);

	assertThat(response).hasSize(objects1.length).usingRecursiveFieldByFieldElementComparator()
				.containsAnyElementsOf(Arrays.asList(objects1));
	}
	
	@Test
    public void testGetObjectsByAliasPattern() throws Exception {
		 NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
			UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
					.body(UserBoundary.class);
		  	ObjectBoundary object1 =Utils.createNewObjectBySuperapp();
	        ObjectBoundary object2 =Utils.createNewObjectBySuperapp();
	        ObjectBoundary object3 =Utils.createNewObjectBySuperapp();
	        String alias = "testAlias";

	        object1.setAlias(alias);
	        object2.setAlias(alias);
	        object3.setAlias("OtherAlias");
	        
	        ObjectBoundary ob1 = this.restClient.post().uri("/objects").body(object1).retrieve()
					.body(ObjectBoundary.class);
	        ObjectBoundary ob2 = this.restClient.post().uri("/objects").body(object2).retrieve()
					.body(ObjectBoundary.class);
	        ObjectBoundary ob3 = this.restClient.post().uri("/objects").body(object3).retrieve()
					.body(ObjectBoundary.class);
	        ObjectBoundary[] objects1 = {ob1,ob2};
	        
			ObjectBoundary[] response = this.restClient.get()
					.uri("/objects/search/byAliasPattern/{pattern}?userSuperapp={userSuperapp}&userEmail={userEmail}&page=0&size=5",
							"est", superappUser.getUserId().getSuperapp(),superappUser.getUserId().getEmail())
					.retrieve().body(ObjectBoundary[].class);

		assertThat(response).hasSize(objects1.length).usingRecursiveFieldByFieldElementComparator()
					.containsAnyElementsOf(Arrays.asList(objects1));
		}
	
	@Test
    public void testMiniAppGetObjectsByType() throws Exception {
	  NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);
		 NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
			UserBoundary miniAppUser = this.restClient.post().uri("/users").body(newMiniappUser).retrieve()
					.body(UserBoundary.class);
	  	ObjectBoundary object1 =Utils.createNewObjectBySuperapp();
        ObjectBoundary object2 =Utils.createNewObjectBySuperapp();
        ObjectBoundary object3 =Utils.createNewObjectBySuperapp();
        String type = "testType";

        object1.setType(type);
        object2.setType(type);
        object3.setType("OtherType");
        
        ObjectBoundary ob1 = this.restClient.post().uri("/objects").body(object1).retrieve()
				.body(ObjectBoundary.class);
        ObjectBoundary ob2 = this.restClient.post().uri("/objects").body(object2).retrieve()
				.body(ObjectBoundary.class);
        ObjectBoundary ob3 = this.restClient.post().uri("/objects").body(object3).retrieve()
				.body(ObjectBoundary.class);
        ObjectBoundary[] objects1 = {ob1,ob2};
		ObjectBoundary[] response = this.restClient.get()
				.uri("/objects/search/byType/{type}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getType(), miniAppUser.getUserId().getSuperapp(),miniAppUser.getUserId().getEmail())
				.retrieve().body(ObjectBoundary[].class);

	assertThat(response).hasSize(objects1.length).usingRecursiveFieldByFieldElementComparator()
				.containsAnyElementsOf(Arrays.asList(objects1));  
        
    }
	
	

	


@Test
public void testMiniAppGetObjectsByAlias() throws Exception {
  NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
	UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
			.body(UserBoundary.class);
	 NewUserBoundary newMiniappUser = Utils.createNewUserMiniapp();
		UserBoundary miniAppUser = this.restClient.post().uri("/users").body(newMiniappUser).retrieve()
				.body(UserBoundary.class);
  	ObjectBoundary object1 =Utils.createNewObjectBySuperapp();
    ObjectBoundary object2 =Utils.createNewObjectBySuperapp();
    ObjectBoundary object3 =Utils.createNewObjectBySuperapp();
    String alias = "testAlias";

    object1.setAlias(alias);
    object2.setAlias(alias);
    object3.setAlias("OtherAlias");
    
    ObjectBoundary ob1 = this.restClient.post().uri("/objects").body(object1).retrieve()
			.body(ObjectBoundary.class);
    ObjectBoundary ob2 = this.restClient.post().uri("/objects").body(object2).retrieve()
			.body(ObjectBoundary.class);
    ObjectBoundary ob3 = this.restClient.post().uri("/objects").body(object3).retrieve()
			.body(ObjectBoundary.class);
    ObjectBoundary[] objects1 = {ob1,ob2};
	ObjectBoundary[] response = this.restClient.get()
			.uri("/objects/search/byAlias/{alias}?userSuperapp={userSuperapp}&userEmail={userEmail}",
					alias, miniAppUser.getUserId().getSuperapp(),miniAppUser.getUserId().getEmail())
			.retrieve().body(ObjectBoundary[].class);

assertThat(response).hasSize(objects1.length).usingRecursiveFieldByFieldElementComparator()
			.containsAnyElementsOf(Arrays.asList(objects1));  
    
}


	
	@Test
	public void testSuperAppGetAllObjects() throws Exception {
		UserBoundary superappUser = addObjects();

		ObjectBoundary[] response = this.restClient.get()
				.uri("/objects?userSuperapp={userSuperapp}&userEmail={userEmail}&size=5&page=0",
						superappUser.getUserId().getSuperapp(), superappUser.getUserId().getEmail())
				.retrieve().body(ObjectBoundary[].class);

		assertThat(response).hasSize(objects.length).usingRecursiveFieldByFieldElementComparator()
				.containsAnyElementsOf(Arrays.asList(objects));
	}

	@Test
	public void testSuperGetObjectById() throws Exception {
		UserBoundary superappUser = addObjects();
		System.out.println(objects[0]);

		ObjectBoundary response = this.restClient.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}&size=5&page=0",
						objects[0].getObjectId().getSuperapp(), objects[0].getObjectId().getId(),
						superappUser.getUserId().getSuperapp(), superappUser.getUserId().getEmail())
				.retrieve().body(ObjectBoundary.class);

		assertThat(response).usingRecursiveComparison().isEqualTo(objects[0]);
	}
	

	public UserBoundary addObjects() {
		NewUserBoundary newSuperappUser = Utils.createNewUserSuperapp();
		UserBoundary superappUser = this.restClient.post().uri("/users").body(newSuperappUser).retrieve()
				.body(UserBoundary.class);

		ObjectBoundary newObject1 = Utils.createNewObjectBySuperapp();
		ObjectBoundary object1 = this.restClient.post().uri("/objects").body(newObject1).retrieve()
				.body(ObjectBoundary.class);

		ObjectBoundary newObject2 = Utils.createNewObject2BySuperapp();
		ObjectBoundary object2 = this.restClient.post().uri("/objects").body(newObject2).retrieve()
				.body(ObjectBoundary.class);

		ObjectBoundary newObject3 = Utils.createNewObject3BySuperapp();
		ObjectBoundary object3 = this.restClient.post().uri("/objects").body(newObject3).retrieve()
				.body(ObjectBoundary.class);

		ObjectBoundary newObject4 = Utils.createNewObject4BySuperapp();
		ObjectBoundary object4 = this.restClient.post().uri("/objects").body(newObject4).retrieve()
				.body(ObjectBoundary.class);

		objects = new ObjectBoundary[] { object1, object2, object3, object4 };
		System.out.println(objects[0]);
		return superappUser;
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