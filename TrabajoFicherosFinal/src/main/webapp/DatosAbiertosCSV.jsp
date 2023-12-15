<%@page import="java.util.ArrayList"%>
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
<h1>Datos CSV</h1>
	<table border="1">
		<%ArrayList<ArrayList<String>> datos = (ArrayList<ArrayList<String>>)application.getAttribute("mapaCSV"); 
		for(ArrayList<String> lista:datos){%>
			<tr>
			<% for(String dato : lista){%>
			<td><%=dato %>,</td>
			<%} %>
			</tr>
			<%} %>				
	</table>
	<form action="Acceso.jsp">
	<input type="submit" name="retorceder" value="retroceder">
</form>
</body>
</html>