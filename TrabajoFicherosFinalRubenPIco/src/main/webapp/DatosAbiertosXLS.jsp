<%@page import="java.util.ArrayList"%>
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
		<td>Dato</td>
	</tr>
	<%if(application.getAttribute("mapaXLS")!=null){
	ArrayList<String> lista = (ArrayList<String>)application.getAttribute("mapaXLS");	

	for(String elemento : lista){ %>
	<tr>
		<td><%=elemento%></td>
	</tr>
<%}} %>		
	</table>
	<form action="Acceso.jsp">
	<input type="submit" name="retorceder" value="retroceder">
</form>
</body>
</html>