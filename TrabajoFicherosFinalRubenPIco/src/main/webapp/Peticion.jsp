<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
¡<html>
<head>
    <title>Seleccionar Archivo</title>
</head>
<body>
<%!String tipo = "";// String tipo = (String)getServletContext().getAttribute("retorno");%>
<script type="text/javascript">
    function seleccionarArchivo() {
        var input = document.createElement("input");
        input.type = "file";

        input.accept = ".";<%=tipo%>

        input.addEventListener("change", function(event) {
            var archivoSeleccionado = event.target.files[0];

            enviarArchivoAlServidor(archivoSeleccionado);
        });

        input.click();
    }

    function enviarArchivoAlServidor(archivo) {
        var formData = new FormData();
        formData.append("archivo", archivo);

        fetch("TuServlet", {
            method: "GET",
            body: formData
        })
        .then(response => response.text())
        .then(data => {

        	console.log(data);
        })
        .catch(error => {
            console.error("Error al enviar el archivo:", error);
        });
    }
</script>

<h2>Seleccione el archivo</h2>

<button onclick="seleccionarArchivo()">Seleccionar Archivo</button>

<form action="ServletAcceso" method="get" enctype="multipart/form-data">
    <input type="submit" name="volver"> 
</form> 
</body>
</html>