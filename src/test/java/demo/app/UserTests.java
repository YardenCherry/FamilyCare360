package demo.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.RestClient;

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

	// @BeforeEach
	public void setup() {
		System.err.println("set up");
		// DELETE database
		this.restClient.delete().retrieve();
	}

	// @AfterEach
	public void tearDown() {
		System.err.println("tear down");
		// DELETE database
		this.restClient.delete().retrieve();
	}

	@Test
	public void contextLoads() {
	}
	
	
}