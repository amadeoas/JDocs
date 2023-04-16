package co.uk.bocaditos.jdocs.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;


/**
 * JUnit tests for class JavadocService.
 * 
 * @author aasco
 */
public class JavadocServiceTest {

	@Test
	public void test() {
		final String route = "http://localhost:8080";
		final JavadocService service = new JavadocService(route, null, null, null);

		assertNotNull(service.getDocs());
		assertEquals(route, service.getRoute());
	}

} // end class JavadocServiceTest
