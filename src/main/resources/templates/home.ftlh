<!--<#import "/spring.ftl" as spring/>-->
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<title>Libraries Javadocs</title>
		<link rel="stylesheet" href="static/main.css">
	</head>
	<body>
		<h1>Libraries Javadoc</h1>
<#list docs as name, versions>
		<details>
  			<summary>${name.getTitle()} - <a href="${route}/lib?name=${name.getName()}">${name.getName()}</a></summary>
			<ul>
    <#list versions as version>
				<li><a href="${route}/javadoc/${name.getName()}/${version}/index.html">${version}</a></li>
    </#list>
			</ul>
		</details>
</#list>
	</body>
</html>