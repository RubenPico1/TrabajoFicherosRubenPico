<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" errorPage="Error.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h1>TRATAMIENTOS FICHEROS</h1>
	<form action="ServletAcceso" method="post">
		<p>Formato del fichero: 
		<select name="tipo">
		<option value="XLS" >XLS</option>
		<option value="CSV" >CSV</option>
		<option value="JSON" >JSON</option>
		<option value="XML" >XML</option>
		</select>
		</p>
		<hr style="border-color: black;">
		<p>Que quiere hacer con el fichero?</p>
		
		Lectura : <input type="radio" name="Eleccion" value="Lectura" checked/><br>
		Escritura:  <input type="radio" name="Eleccion" value="Escritura"/>		
		<hr style="border-color: black;">
		Dato 1 <input type="text" name="dato1"><br>
		Dato 2 <input type="text" name="dato2"><br>
		Dato 3 <input type="text" name="dato3"><br>
		Dato 4 <input type="text" name="dato4"><br>
		Dato 5 <input type="text" name="dato5"><br>
		Dato 6 <input type="text" name="dato6"><br><br>
		<%if(request.getAttribute("error")!=null && request.getAttribute("error").equals("si")){ %>
		<p style="color: red">(*)Los campos no pueden estar vacios.
		<%} %>
		<input type="submit" value="Enviar">
	</form>
	<%if(application.getAttribute("llegada")!=null && application.getAttribute("llegada").equals("escritura")){//pongo el mensaje si lo que ha hecho es escribir un archivo, ya que si lo ha leido, ya sabra que ha salido bien ya que habra visto la tabla 
		if(application.getAttribute("retorno")!=null && application.getAttribute("retorno").equals("xls")){//tampoco del todo necesario%>
			<p>Se ha creado con exito el archivo xls
		<%}else if(application.getAttribute("retorno")!=null && application.getAttribute("retorno").equals("csv")){ %>
	<p>Se ha creado con exito el archivo csv
		<%}else if(application.getAttribute("retorno")!=null && application.getAttribute("retorno").equals("json")){ %>
		<p>Se ha creado con exito el archivo json
		<%}else if(application.getAttribute("retorno")!=null && application.getAttribute("retorno").equals("xml")){ %>
		<p>Se ha creado con exito el archivo xml
		
	<%}}%>
</body>
</html>