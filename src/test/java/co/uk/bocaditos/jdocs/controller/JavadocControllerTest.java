package co.uk.bocaditos.jdocs.controller;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * JUnit tests for class JavadocController.
 * 
 * @author aasco
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class JavadocControllerTest {
	
	private static final String VERSION = "2.8.1";

	@LocalServerPort
	private int port;
	@Value("${jdocs.route}")
	private String route;


	@Test
	public void indexTest() throws Exception {
		test(null, null);
	}

	@Test
	public void libTest() throws Exception {
		final String NAME = "maven-javadoc-plugin";

		test(NAME, null);
		test(NAME, VERSION);
	}

	private void test(final String name, final String version) throws Exception {
		final String BASE_URL = "http://localhost:" + this.port +  "/";
		final String url = ((name == null) ? BASE_URL 
				: BASE_URL + "lib?name=" + name + (JavadocController.notNull(version) ? "&version=" + version 
					: ""));
		final ResponseEntity<String> entity = new TestRestTemplate()
				.getForEntity(url, String.class);
		final String EXPECTED;
		
		if (name == null) {
			EXPECTED = this.route + "/javadoc/";
		} else if (version == null) {
			EXPECTED = this.route + "/javadoc/" + name + "/" + VERSION + "/index.html";
		} else {
			EXPECTED= "<!DOCTYPE HTML PUBLIC";
		}

		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertTrue("Expected \"" + EXPECTED + "\" but got " + entity.getBody(), 
				entity.getBody().contains(EXPECTED));
	}

} // end class JavadocControllerTest
