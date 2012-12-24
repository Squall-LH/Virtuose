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
Entre une liste de mot-clés séparé par un espace.<br>
Entre une carte au format freemap (.mm).<br><br>

Obtient une liste d'arbres correspondants à la requête.
</p>
<form enctype="multipart/form-data" action="controller?action=search" method="post"> 
<p>
Keywords
<input type="text" name="keywords"><br>
Map:
<input type="file" name="map"><br>
<input type="submit">
</p>
</form>
</body>
</html>
