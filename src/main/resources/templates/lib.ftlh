<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<title>Libraries Javadocs</title>
		<link rel="stylesheet" href="static/main.css">
	</head>
	<body>
		<h1>${name.getTitle()} Library Javadoc - ${name.getName()}</h1>
		<div class="content">
			<ul>
    <#list versions as version>
				<li><a href="${route}/javadoc/${name.getName()}/${version}/index.html">${version}</a></li>
    </#list>
			</ul>
		</div>
		<hr />
		<a href="${route}/">Home</a>
	</body>
</html>