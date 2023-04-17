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


	public JavadocService(@Value("${jdocs.route}") final String route, 
			@Value("${jdocs.repository.type:#{null}}") final String repoType,
			@Value("${jdocs.repository.group:#{null}}") final String groupId,
			@Value("${jdocs.doc.path:#{null}}") final String docPath) {
		this.route = route;
		this.docs = new JavaDocMap(docPath, repoType, docPath);
	}

	public JavaDocMap getDocs() {
		return this.docs;
	}

	public String getRoute() {
		return this.route;
	}

} // end class JavadocService
