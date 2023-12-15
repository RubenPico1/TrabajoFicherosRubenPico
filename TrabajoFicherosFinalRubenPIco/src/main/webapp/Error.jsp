<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<p style="color: red">TIPO DE ERROR <%= application.getAttribute("error")!=null?application.getAttribute("error"):"Error desconocido" %>
	
	<form action="Acceso.jsp">
	<input type="submit" name="retorceder" value="retroceder">
</form>
</body>
</html>