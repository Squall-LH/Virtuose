<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css/style.css" />
<title>Arbre complet</title>
</head>
<body>
<%String path = "."+(String)session.getAttribute("full_map_path");%>
<applet code="freemind.main.FreeMindApplet.class"
	archive="freemindbrowser.jar" width="100%" height="800px">
	<param name="type" value="application/x-java-applet;version=1.4">
	<param name="scriptable" value="false">
	<param name="modes" value="freemind.modes.browsemode.BrowseMode">
	<param name="browsemode_initial_map"
		value="<%out.print(path);%>">
	<!--          ^ Put the path to your map here, if it starts with a dot, 	
	the file is searched in the filesystem from the path, the html resides in. .  -->
	<param name="initial_mode" value="Browse">
	<param name="selection_method" value="selection_method_direct">
</applet>

</body>
</html>