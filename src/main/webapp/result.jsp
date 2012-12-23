<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.Map"
    %>
   <!DOCTYPE html>
<html>
<head>
  <title>js-mindmap demo - JavaScript Mindmap</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  </head>
<body>

<ul>
<% Map<String,String> map = (Map)session.getAttribute("listMap");
int i = 0;
for (Map.Entry<String, String> entry : map.entrySet()) {
    String key = entry.getKey();
    String value = entry.getValue();
    out.println("<li><a href=\"controller?action=show&id="+i+"\">"+key+"</a></li>");
    i++;
}
%>

</ul>	
</body>
</html>