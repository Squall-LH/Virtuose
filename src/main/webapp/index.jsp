<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Virtuose</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <link rel="stylesheet" href="css/style.css" />
</head>
<body>
<h2>Virtuose</h2>

<%
if((Boolean)session.getAttribute("no_result")) {
	out.println("<p style=\"color:red\">La recherche précédente n'a pas pu aboutir, veuillez essayer avec d'autres mots-clés et/ou une autre carte.</p>");
}
%>

<p id="description">
Entrez une liste de mot-clés séparés par un espace.<br>
Entrez une carte au format Freemap (.mm).<br><br>

Le résultat de la recherche sera une liste d'arbres classés par ordre décroissant de pertinence.
</p>

<form id="start" enctype="multipart/form-data" action="controller?action=search" method="post">
	<p>
		<label for="keywords">Mots-clés:</label>
		<input id="keywords" name="keywords type="text" required/>
	</p>
	<p>
		<label for="map">Carte:</label>
		<input id="map" name="map" type="file" required/>
	</p>
	<p>
		<input type="submit" value="Envoyer" />
	</p>
</form>


</body>
</html>
