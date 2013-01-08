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

<p>
Entre une liste de mot-clés séparés par un espace.<br>
Entre une carte au format freemap (.mm).<br><br>

Le résultat de la recherche sera une liste d'arbres classés par ordre décroissant de pertinance.
</p>
<form enctype="multipart/form-data" action="controller?action=search" method="post"> 
<p>
Keywords
<input type="text" name="keywords" required><br>
Map:
<input type="file" name="map" required><br>
<input type="submit">
</p>
</form>
</body>
</html>
