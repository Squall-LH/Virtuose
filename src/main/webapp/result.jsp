<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.List"
    %>
<!DOCTYPE html>
<html>
<head>
  <title>Liste de résultats</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <link rel="stylesheet" href="css/style.css" />
</head>
<body>

<ol>
<% List<String> title = (List)session.getAttribute("title");
List<String> content = (List)session.getAttribute("content");
int i = 0;
for(String t : title) {
    out.println("<li><a href=\"controller?action=show&id="+i+"\">"+t+"</a></li>");
	i++;
}
%>

</ol>	
</body>
</html>