package co.uk.bocaditos.jdocs.controller;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JavadocControllerTest {

	private static final String UPDATE_FRAME_BASE = "<frameset rows=\"30%,*,24px\" title=\"";
	private static final String UPDATE_FRAME 
			= UPDATE_FRAME_BASE + "Left frames\" onLoad=\"top.loadFrames()\">";
	private static final String MENU_FRAME 
			= "<frame src=\"/javadoc/menu-frame.html\" name=\"menuFrame\" title=\"Menu\">";
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

	@Test
	public void checkJavadoc1() throws IOException {
		// Check for uncompressed javadocs, i.e. jar
		final File[] jars = new File("src/main/resources/static/javadoc")
				.listFiles(f -> f.getName().endsWith("-javadoc.jar"));
		final StringBuilder errors = new StringBuilder();
		int numErrors = 0;

		for (final File jar : jars) {
			final String filename = jar.getName().substring(0, jar.getName().length() - "-javadoc.jar".length());
			int offset = filename.lastIndexOf("-");
			final String name = filename.substring(0, offset);
			final String version = filename.substring(offset + 1);
			File destDir = new File(jar.getParentFile(), name);

			// Create directories
			if (!destDir.exists()) {
				// Make javadoc dir
				assertTrue("Failed to create directory " + destDir.getAbsolutePath(), destDir.mkdir());
			}
			destDir = new File(destDir, version);
			if (destDir.exists()) {
				errors.append("Directory ")
					.append(destDir.getAbsolutePath())
					.append(" already exist\n");
			}
			if (!destDir.mkdir()) {
				errors.append("Failed to create directory ")
					.append(destDir.getAbsolutePath());
			}

			unzip(jar, destDir);
	        assertTrue(jar.delete());
		}
		assertEquals("Failures installing new javadocs: " + errors.toString(), 0, numErrors);
		assertEquals("There were " + jars.length + " javadoc JAR files that were installed. Re-run test to " 
					+ "succeed", 
				0, jars.length);
	}

	@Test
	public void checkJavadoc2() throws IOException {
		// Check already uncompressed javadoc
		final File[] libs = new File("src/main/resources/static/javadoc").listFiles(f -> f.isDirectory());
		final StringBuilder errors = new StringBuilder();
		int numErrors = 0;

		for (final File lib : libs) {
			final File[] versions = lib.listFiles(f -> f.isDirectory());

			for (final File version : versions) {
				final File[] files = version.listFiles(f -> f.getName().equals("index.html"));
				final List<String> lines = Files.readAllLines(files[0].toPath(), StandardCharsets.UTF_8);
				int lineNum = -1;
				boolean notFound = true;

				while (++lineNum < lines.size()) {
					String line = lines.get(lineNum).toLowerCase();

					if (line.startsWith(UPDATE_FRAME_BASE)) {
						// Already set up
						notFound = false;

						break;
					} else if (line.startsWith("<frameset rows=\"30%,70%\"")) {
						// Not yet setup
						boolean updated = false;

						lines.set(lineNum, UPDATE_FRAME);
						while (++lineNum < lines.size()) {
							line = lines.get(lineNum).toLowerCase();
							if (line.equals("</frameset>")) {
								lines.add(lineNum, MENU_FRAME);
								updated = true;

								break;
							}
						}

						if (updated) {
							// Update the main.html file
							try (final FileWriter writer = new FileWriter(files[0])) {
								for (final String l : lines) {
									writer.write(l);
									writer.write("\n");
								}
							}
						} else {
							errors.append("main.html format is incompatible for library ")
								.append(updated)
								.append(lib.getName())
								.append(" and version ")
								.append(version.getName())
								.append("\n");
							++numErrors;
						}
						notFound = false;

						break;
					}
				}

				if (notFound) {
					errors.append("Missing identifying string in file ")
						.append(files[0].getAbsolutePath());
					++numErrors;
				}
			}
		}
		assertEquals("Found " + numErrors + " errors: " + errors.toString(), 0, numErrors);
	}

	public static File newFile(final File destDir, final ZipEntry zipEntry) throws IOException {
	    final File destFile = new File(destDir, zipEntry.getName());
	    final String destDirPath = destDir.getCanonicalPath();
	    final String destFilePath = destFile.getCanonicalPath();

	    if (!destFilePath.startsWith(destDirPath + File.separator)) {
	        throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
	    }

	    return destFile;
	}
	
	private static void unzip(final File jar, final File destDir) throws IOException {
		// Unzip javadoc
        try (final ZipInputStream zis = new ZipInputStream(new FileInputStream(jar))) {
			final byte[] buffer = new byte[1024];
	        ZipEntry zipEntry = zis.getNextEntry();

	        while (zipEntry != null) {
	            final File newFile = newFile(destDir, zipEntry);

	            if (zipEntry.isDirectory()) {
	                if (!newFile.isDirectory() && !newFile.mkdirs()) {
	                    throw new IOException("Failed to create directory " + newFile);
	                }
	            } else {
	                // Fix for Windows-created archives
	                final File parent = newFile.getParentFile();

	                if (!parent.isDirectory() && !parent.mkdirs()) {
	                    throw new IOException("Failed to create directory " + parent);
	                }

	                // write file content
	                final FileOutputStream fos = new FileOutputStream(newFile);
	                int len;

	                while ((len = zis.read(buffer)) > 0) {
	                    fos.write(buffer, 0, len);
	                }
	                fos.close();
	            }
	            zipEntry = zis.getNextEntry();
	        }
	        zis.closeEntry();
        }
	}

} // end class JavadocControllerTest
