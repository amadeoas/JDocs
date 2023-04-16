package co.uk.bocaditos.jdocs.services;

import static org.junit.Assert.assertNotEquals;
import org.junit.Test;


/**
 * JUnit tests for class JavaDocMap.
 * 
 * @author aasco
 */
public class JavaDocMapTest {

	@Test
	public void test() {
		final JavaDocMap docs = new JavaDocMap(null, null, null);

		assertNotEquals(0, docs.size());
	}

} // end class JavaDocMapTest
