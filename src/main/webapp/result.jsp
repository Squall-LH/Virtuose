<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>js-mindmap demo - JavaScript Mindmap</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <link rel="stylesheet" type="text/css" href="css/js-mindmap.css" />
  <link href="css/style.css" type="text/css" rel="stylesheet"/>

  <!-- jQuery -->
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js" type="text/javascript"></script>
  <!-- UI, for draggable nodes -->
  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.15/jquery-ui.min.js"></script>

  <!-- Raphael for SVG support (won't work on android) -->
  <script type="text/javascript" src="js/raphael-min.js"></script>

  <!-- Mindmap -->
  <script type="text/javascript" src="js/js-mindmap.js"></script>

  <!-- Kick everything off -->
  <script src="js/script.js" type="text/javascript"></script>

</head>
<body>
<p>keywords:
<% out.println(session.getAttribute("keywords")); %>
</p>
<ul>
<% out.println(session.getAttribute("map")); %>
</ul>	
</body>
</html>