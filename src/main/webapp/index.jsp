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

<p id="description">
Entre une liste de mot-clés séparés par un espace.<br>
Entre une carte au format freemap (.mm).<br><br>

Le résultat de la recherche sera une liste d'arbres classés par ordre décroissant de pertinance.
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
