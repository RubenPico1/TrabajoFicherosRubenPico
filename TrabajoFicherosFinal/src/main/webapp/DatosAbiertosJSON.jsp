<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" errorPage="Error.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h1>Datos JSON</h1>
<table border="1">
	<tr>
		<td>Clave</td>
		<td>Dato</td>
	</tr>
	<%if(application.getAttribute("mapaJSON")!=null){
	HashMap<String, String> mapa = (HashMap<String,String>)application.getAttribute("mapaJSON");//no puede ser nulo porque se comprueba arriba	

	for(String clave : mapa.keySet()){ %>
	<tr>
		<td><%=clave%></td>
		<td><%= mapa.get(clave) %></td>
	</tr>
<%}} %>
<form action="Acceso.jsp">
	<input type="submit" name="retorceder" value="retroceder">
</form>
</table>
</body>
</html>