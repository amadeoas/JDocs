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
		<span style="background-color:#ffffff; position: fixed; bottom: 0; width: 100%; border-top: solid #B8B6B6;">
			<p><a href="/">Home</a> <a href="/licence">Licence</a></p>
		</span>
	</body>
</html>