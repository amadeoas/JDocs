# UI Javadoc for Libraries
This UI provides access to the Javadoc of some libraries, see image below.

<img width="387" alt="home" src="https://user-images.githubusercontent.com/8356173/230800623-95dbd8c8-b861-442d-9899-6915f5ff8ba1.png" style='border:1px solid #555; display: block; margin-left: auto; margin-right: auto;'>

The UI provides a list of some Javadocs and give access to view them through the UI.

### Usage ###
To see the home page of the UI go to the corresponding URL to see the Java libraries available and their corresponding Javadocs, i.e. for URL http://localhost:8080/. If the UI is running in your computer open the web browser and go to *http://localhost:8080*.

### Development
To add a new Javadoc of a Library to the UI, e.g. **maven-javadoc-plugin-2.8.1-javadoc.jar**:
- <u>unzip</u> the corresponding JAR file into the directory appropriate directory, directory format: 
*src/main/resources/static/javadoc/&lt;lib_name&gt;/&lt;lib_version&gt;*.
Where &lt;lib_name&gt; refers to the name of the library, e.g. **maven-javadoc-plugin**, and &lt;lib_version&gt; to its version, e.g. **2.8.1**.
- <u>test</u> it
- <u>commit</u> it to repository
- <u>build</u>
- <u>deploy</u>

This UI is a Spring Boot application that may be run by itself if the host computer have JRE installed and can be run with the below command.

<div class="warning" style='padding:0.1em; background-color:lightgrey; color:darkblue; border:1px solid #555; display: block; margin-left: auto; margin-right: auto;'>
<span>
	<p style='margin-top:1em; text-align:left'><b>Command</b>:</p>
	<hr />
	<p style='margin-top:1em; text-align:center'>java -jar JDocs-&lt;version&gt;.jar</p>
</span>
</div>
<p></p>
<div class="warning" style='padding:0.1em; background-color:lightgrey; color:darkblue; border:1px solid #555; display: block; margin-left: auto; margin-right: auto;'>
<span>
	<p style='margin-top:1em; text-align:left'><b>Example</b>:</p>
	<hr />
	<p style='margin-left:1em; text-align:center'>java -jar JDocs-1.0.001.jar</p>
</span>
</div>

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.10/gradle-plugin/reference/html/)
* [Apache Freemarker](https://docs.spring.io/spring-boot/docs/2.7.10/reference/htmlsingle/#web.servlet.spring-mvc.template-engines)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.10/reference/htmlsingle/#web)
