package co.uk.bocaditos.jdocs.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * .
 * 
 * @author aasco
 */
@Service
public class JavadocService {

	private final String route;
	private final JavaDocMap docs;


	public JavadocService(@Value("${jdocs.route}") final String route) {
		this.route = route;
		this.docs = new JavaDocMap();
	}

	public JavaDocMap getDocs() {
		return this.docs;
	}

	public String getRoute() {
		return this.route;
	}

} // end class JavadocService
