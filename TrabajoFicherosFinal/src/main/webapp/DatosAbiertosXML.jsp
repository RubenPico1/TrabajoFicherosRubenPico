<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h1>Datos XML</h1>
<table border="1">
	<tr>
		<td>Elemento</td>
		<td>Dato</td>
	</tr>
	<%if(application.getAttribute("mapaXML")!=null){
	HashMap<String, String> mapa = (HashMap<String,String>)application.getAttribute("mapaXML");//no puede ser nulo porque se comprueba arriba	

	for(String elemento : mapa.keySet()){ %>
	<tr>
		<td><%=elemento%></td>
		<td><%= mapa.get(elemento) %></td>
	</tr>
<%}} %>		
	</table>
	<form action="Acceso.jsp">
	<input type="submit" name="retorceder" value="retroceder">
</form>
</body>
</html>