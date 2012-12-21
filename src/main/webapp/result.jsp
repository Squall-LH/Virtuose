<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>js-mindmap demo - JavaScript Mindmap</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>

<ul>
<% out.println(session.getAttribute("map")); %>
</ul>	
</body>
</html>