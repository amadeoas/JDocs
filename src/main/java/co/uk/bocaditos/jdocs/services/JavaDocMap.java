package co.uk.bocaditos.jdocs.services;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;


/**
 * Overall view of the Javadocs available to be served in alphabetical order.
 * 
 * @author aasco
 */
@SuppressWarnings("serial")
public class JavaDocMap extends TreeMap<JavaDocMap.StringName, Set<String>> {

	private static final Logger logger = LogManager.getLogger(JavaDocMap.class);
	
	private final String docPath;
	private final RepoInfo repoInfo;


	public class RepoInfo {

		private final String type;
		private final String groupId;


		public RepoInfo(final String repoType, final String groupId) {
			this.type = repoType;
			this.groupId = groupId;
		}

		public String getType() {
			return type;
		}

		public String getGroupId() {
			return groupId;
		}
		
		public final boolean isGradle() {
			return "gradle".equals(type);
		}

	} // end class RepoInfo


	/**
	 * Wrapper of the name of the name of the library with extra functionality.
	 * 
	 * @author aasco
	 */
	public class StringName implements Comparable<StringName> {

		private String name;


		public StringName(final File libDir) {
			this(libDir.getName());
		}

		public StringName(final String name) {
			this.name = name;
		}

		public final String getName() {
			return this.name;
		}
		
		public final String getDocPath() {
			return JavaDocMap.this.docPath;
		}
		
		public final String getRepoType() {
			return JavaDocMap.this.repoInfo.getType();
		}

		public final String getGroupId() {
			return JavaDocMap.this.repoInfo.getGroupId();
		}
		
		public final boolean hasRepoInfo() {
			return JavaDocMap.this.repoInfo != null;
		}
		
		public final boolean isGradle() {
			return "gradle".equals(JavaDocMap.this.repoInfo.getType());
		}
		
		public final String getApiDoc() {
			final String apiTitle = update(getTitle(), " Model");

			return getDocPath() + "/" + update(apiTitle, " Library").replace(" ", "+");
		}

		public final String getLibDoc() {
			 String libTitle = getTitle();
			 int offset = libTitle.indexOf(" Library");

			 if (offset == -1) {
				 libTitle += "+Library";
			 }

			 return getDocPath() + "/" + libTitle.replace(" ", "+");
		}

		public String getTitle() {
	    	String[] parts = name.split("-");
	    	final StringBuilder buf;

	    	if (parts.length == 1) {
	    		parts = name.split("(?=[A-Z])");
	    		buf = new StringBuilder(name.length() + parts.length - 1);
		    	for (final String part : parts) {
		    		if (buf.length() > 0) {
		    			buf.append(' ')
		    				.append(part);
		    		} else {
		    			buf.append(Character.toUpperCase(part.charAt(0)))
	    					.append(part.substring(1));
		    		}
		    	}
	    	} else {
	    		buf = new StringBuilder(name.length());
		    	for (final String part : parts) {
		    		if (buf.length() > 0) {
		    			buf.append(' ');
		    		}
		    		buf.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
		    	}
	    	}

	    	return buf.toString();
		}

		@Override
		public int hashCode() {
			return this.name.hashCode();
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == null || !(obj instanceof StringName)) {
				return false;
			}

			final StringName name = (StringName) obj;

			return this.name.equals(name.getName());
		}

		@Override
		public String toString() {
			return this.name;
		}

		@Override
		public int compareTo(final StringName name) {
			return this.name.compareTo(name.name);
		}
		
		private String update(String value, final String word) {
			int offset = value.indexOf(word);

			if (offset != -1) {
				value = value.substring(0, offset) + value.substring(offset + word.length());
			}

			return value;
		}

	} // end class StringName


	public JavaDocMap(final String docPath, final String repoType, final String groupId) {
		this.docPath = docPath;
		if (repoType == null || groupId == null || !("gradle".equals(repoType) || "maven".equals(repoType))) {
			this.repoInfo = null;
		} else {
			this.repoInfo = new RepoInfo(repoType, groupId);
		}
		loadDirs();
//		final File[] files = getResourceFolderFiles(f -> f.isDirectory(), "./static/javadoc");
//
//		for (final File file : files) {
//			update(file);
//		}
	}

	public final String getDocPath() {
		return this.docPath;
	}
	
	public final RepoInfo getRepoInfo() {
		return this.repoInfo;
	}

	public final boolean isGradle() {
		return this.repoInfo.isGradle();
	}
	
	public final Set<String> get(final String name) {
		for (final Map.Entry<StringName, Set<String>> n : entrySet()) {
			if (n.getKey().getName().equals(name)) {
				return n.getValue();
			}
		}

		return null;
	}
//
//	private void update(final File libDir) {
//		final File[] files = libDir.listFiles(f -> f.isDirectory());
//		final Set<String> versions = new TreeSet<>();
//
//		for (final File file : files) {
//			versions.add(file.getName());
//		}
//		put(new StringName(libDir), versions);
//	}

	public static File[] getResourceFolderFiles(final FileFilter filter, final String folder) {
		logger.debug("Loading folders undern {}...", folder);
//
//		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
//	    final URL url = loader.getResource(folder);
//	    final String path = url.getPath();
//	    final File[] files = new File(path).listFiles(filter);
		try {
			final File[] files = ResourceUtils.getFile("classpath:" + folder).listFiles(filter);
	
			logger.debug("{} folders from {} were successfully detected", files.length, folder);
	
			return files;
		} catch (final FileNotFoundException fnfe) {
			throw new RuntimeException("Failed to get folders under directory \"" + folder + "\"", fnfe);
		}
	}
	
	public void loadDirs() {
		// Get all the files under this inner resource folder
	    final String scannedPackage = "static/javadoc/*/*/index.html";
	    final PathMatchingResourcePatternResolver scanner = new PathMatchingResourcePatternResolver();

	    try {
			final Resource[] resources = scanner.getResources(scannedPackage);

			if (resources == null || resources.length == 0) {
				throw new RuntimeException("could not find any resources in the scanned package" + scannedPackage);
		    } else {
		        for (final Resource resource : resources) {
		        	logger.info("Resource: {}", resource);
		        	final String path = resource.toString();
		        	int offset = path.indexOf("static/javadoc/") + "static/javadoc/".length();
	
		        	if (offset < "static/javadoc/".length()) {
		        		throw new RuntimeException("Invalid directory " + path);
		        	}

		        	final String[] parts = path.substring(offset).split("/");
		        	Set<String> versions = get(parts[0]);

		        	if (versions == null) {
		        		versions = new TreeSet<>(Collections.reverseOrder());
		        		put(new StringName(parts[0]), versions);
		        	}
		        	versions.add(parts[1]);
		        }
		    }
		} catch (final IOException ioe) {
    		throw new RuntimeException(
    				"Failed to get javadocs from " + scannedPackage + ": " + ioe.getMessage(), 
    				ioe);
		}
	}

} // end class JavaDocMap
