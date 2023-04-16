<#include "/common.ftl"/>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<title>Libraries Javadocs</title>
		<link rel="stylesheet" href="main.css">
	</head>
	<body>
		<h1>Libraries Javadoc<@buildJDocs /></h1>
<#list docs as name, versions>
		<details>
  			<summary>${name.getTitle()} </a><@buildVersions name=name route=route /><@buildLibDoc name=name /><@buildApiDoc name=name /></summary>
			<ul>
    <#list versions as version>
				<li><a href="${route}/javadoc/${name.getName()}/${version}/index.html">${version}</a> <@buildCopy name=name version=version /></li>
    </#list>
			</ul>
		</details>
</#list>
		<span style="background-color:#ffffff; position: fixed; bottom: 0; width: 100%; border-top: solid #B8B6B6;">
			<p><span style="color:#C8C6C6">Home</span> <a href="/licence">Licence</a></p>
		</span>
	</body>
</html>