<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import="java.util.List"
	%>
<!DOCTYPE html>
<html>
<head>
<title>Liste de résultats</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="css/style.css" />
</head>
<body>
	<h2>Résultat</h2>

	<%
		String path = "./tmp/" + session.getAttribute("map");
	%>
	<h3><% out.println((String)session.getAttribute("current_title")); %></h3>
	<applet code="freemind.main.FreeMindApplet.class"
		archive="freemindbrowser.jar" width="100%" height="500px">
		<param name="type" value="application/x-java-applet;version=1.4">
		<param name="scriptable" value="false">
		<param name="modes" value="freemind.modes.browsemode.BrowseMode">
		<param name="browsemode_initial_map" value="<%out.print(path);%>">
		<!--          ^ Put the path to your map here, if it starts with a dot, 	
	the file is searched in the filesystem from the path, the html resides in. .  -->
		<param name="initial_mode" value="Browse">
		<param name="selection_method" value="selection_method_direct">
	</applet>

	<div id="options">
		<p class="show">
			<a href="#options">Afficher plus de résultats</a>
		</p>
		<p class="hide">
			<a href="#">Masquer les résultats</a>
		</p>
		<p>
		<ol>
			<%
				List<String> title = (List) session.getAttribute("title");
				String ids = (String)session.getAttribute("ids");
				int i = 0;
				for (String t : title) {
					if(!ids.equals(String.valueOf(i))) {
						out.println("<li><a href=\"controller?action=show&id="+i+"\">"+t+"</a></li>");
					}
					i++;
				}
			%>
		</ol>
	</div>

	<p>
		<%
			out.println("<a id=\"carte\" href=\"controller?action=full_map\">Dézoom</a>");
		%>
	</p>

</body>
</html>
