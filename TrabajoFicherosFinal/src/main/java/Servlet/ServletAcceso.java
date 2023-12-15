package Servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.Desktop;
import java.awt.image.DirectColorModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.http.HttpResponse.ResponseInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.commons.fileupload.FileUploadException;


/**
 * Servlet implementation class ServletAcceso
 */
public class ServletAcceso extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ServletAcceso() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent((javax.servlet.http.HttpServletRequest) request);
		String ruta="";
        if (isMultipart) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                List<FileItem> items = upload.parseRequest((javax.servlet.http.HttpServletRequest) request);

                for (FileItem item : items) {//puede coger mas de un archivo... aunque solo leo el primero
                    if (!item.isFormField()) {//es un archivo
                    	byte[] contenido = item.get();//para pasarlo a un tipo file
                    	File tempo = new File("relleno");//solo es para crear el archivo
                    	FileUtils.writeByteArrayToFile(tempo, contenido);//tranformo de forma cutre el fileitem en un file
                    	
                    	if(getServletConfig().getServletContext().getAttribute("retorno").equals("xls")) {
                    		ArrayList<String> lista = new ArrayList<String>();

                    		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(tempo));
                    		HSSFWorkbook wb = new HSSFWorkbook(fs);
                    	    HSSFSheet sheet = wb.getSheetAt(0);
                    	    HSSFRow row;
                    	    HSSFCell cell;

                    	    int rows; 
                    	    rows = sheet.getPhysicalNumberOfRows();

                    	    int cols = 0;
                    	    int tmp = 0;
                    	    
                    	    for(int i = 0; i < 10 || i < rows; i++) {
                    	        row = sheet.getRow(i);
                    	        if(row != null) {
                    	            tmp = sheet.getRow(i).getPhysicalNumberOfCells();//por si no estan ordenados 
                    	            if(tmp > cols) cols = tmp;
                    	        }
                    	    }
                    	    for(int r = 0; r < rows; r++) {
                    	        row = sheet.getRow(r);
                    	        if(row != null) {
                    	            for(int c = 0; c < cols; c++) {
                    	                cell = row.getCell((short)c);
                    	                if(cell != null) {
                    	                	lista.add(cell.getStringCellValue());//solo coge una fila(es decir que no diferencio filas y columnas en el resultado(si se quisiese hacer seria cogiendo el row de el primero for, metiendolo en el mapa, y luego dependiendo del numero colocarlos como sea en el jsp.)
                    	                }
                    	            }
                    	        }
                    	    }
                    	    request.getServletContext().setAttribute("mapaXLS", lista);  		
                    		ruta = "DatosAbiertosXLS.jsp";
                    	}else if(getServletConfig().getServletContext().getAttribute("retorno").equals("csv")) {
                    		ArrayList<ArrayList<String>> datoss = new ArrayList<ArrayList<String>>();
                            try (CSVReader csvReader = new CSVReader(new FileReader("book.csv"));) {//poner el file elegido
                                String[] values = null;
                                try {
                    				while ((values = csvReader.readNext()) != null) {
                    				}
                    				ArrayList<String> temps = new ArrayList<String>();
                    				 for(String d : Arrays.asList(values)) {
                    		        		temps.add(d);
                    		        	}
                    				 datoss.add(temps);
                    			} catch (CsvValidationException | IOException e) {
                    				// TODO Auto-generated catch block
                    				getServletConfig().getServletContext().setAttribute("error", "Error al leer el CSV");
                    				ruta = "Error.jsp";//si llega aqui es que hay fallado y sobreescribe el path
                    				RequestDispatcher disp = request.getRequestDispatcher(ruta);//esto se puede quitar
                    				disp.forward(request, response);
                    			}
                            }
                            request.getServletContext().setAttribute("mapaCSV", datoss);//no mapa
                    		ruta = "DatosAbiertosCSV.jsp";
                    	}else if(getServletConfig().getServletContext().getAttribute("retorno").equals("json")) {
                    		JSONParser parser = new JSONParser();
                    		//File file = new File("falso.json");//poner el file que se recibe o si no, poner aqui la ruta, en caso de que en vez de con el buscador, se le pida la ruta sin mas/solucion cutre, pero que funciona por el path
                    		HashMap<String, String> mapa = new HashMap<String, String>();//supondre que en el archivo hay solo tipos simples
                    		try {
                    			Object path = parser.parse(new FileReader(tempo.getAbsolutePath()));//esto no tiene sentido, pero bueno
                    			JSONObject objeto = new JSONObject(path.toString());//creo el objeto que tiene todos los datos del json
                    			Set<String> claves = objeto.keySet();//saco las claves 
                    			for(String clave : claves) {
                    				String temp = (String)objeto.getString(clave);
                    				mapa.put(clave, temp);//creo el mapa con las claves y sus valores
                    			}
                    			
                    			
                    		} catch (IOException | ParseException e) {
                    			getServletConfig().getServletContext().setAttribute("error", "Error al leer el JSON");
                				ruta = "Error.jsp";//si llega aqui es que hay fallado y sobreescribe el path
                				RequestDispatcher disp = request.getRequestDispatcher(ruta);//esto se puede quitar
                				disp.forward(request, response);
                    		}
                    		request.getServletContext().setAttribute("mapaJSON", mapa);
                			ruta = "DatosAbiertosJSON.jsp";
                    	}else if(getServletConfig().getServletContext().getAttribute("retorno").equals("xml")) {//lo mismo, no es necesario, pero por si acaso
                    		HashMap<String, String> mapa = new HashMap<String,String>();
                    	    XMLStreamReader xr = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(tempo));
                    	    
                    	    while(xr.hasNext()) {

                    	        int e = xr.next();
                    	        if (e == XMLStreamReader.START_ELEMENT) {
                    	            String nombre = xr.getLocalName();
                    	            xr.next();
                    	            String dato = null;
                    	            try {
                    	                dato = xr.getText();
                    	                mapa.put(nombre, dato);//se podria hacer para que funcionase bien incluso si se meten etiquetas con el mismo nombre  extrayendolos de los atributos, pero no termina de funcionar
                    	            } catch (IllegalStateException exep) {
                    	            	getServletConfig().getServletContext().setAttribute("error", "Error al leer el XML");
                        				ruta = "Error.jsp";//si llega aqui es que hay fallado y sobreescribe el path
                        				RequestDispatcher disp = request.getRequestDispatcher(ruta);//esto se puede quitar
                        				disp.forward(request, response);
                    	            }
                    	        } 
                    	    }
                    	    request.getServletContext().setAttribute("mapaXML", mapa);
                    	    
                    	    /*String nombre = "";
                    	    String valor = "";
                    	    String nombreattr = "";
                    	    while(xr.hasNext()) {
                    	        int e = xr.next();
                    	        switch (e)
                    	        {
                    	            case XMLStreamReader.START_ELEMENT:
                    	            {
                    	            	nombre = xr.getLocalName();
                    	                final int attributeCount = xr.getAttributeCount();
                    	                if(attributeCount > 0)
                    	                {
                    	                	nombreattr = xr.getAttributeName(0).getLocalPart();
                    	                    final String valora = xr.getAttributeValue(0);
                    	                    mapa.put(nombre, valora);
                    	                }
                    	                break;
                    	            }
                    	            case XMLStreamReader.CHARACTERS:
                    	            {
                    	            	valor = xr.getText();
                    	                break;
                    	            }
                    	        }
                    	    }
                    	    request.getServletContext().setAttribute("mapaXML", mapa);*/
                    	    
                    		ruta = "DatosAbiertosXML.jsp";
                    	}              
                    }
                }
            } catch (Exception e) {
            	getServletConfig().getServletContext().setAttribute("error", "Error en el archivo");
				ruta = "Error.jsp";//si llega aqui es que hay fallado y sobreescribe el path
				RequestDispatcher disp = request.getRequestDispatcher(ruta);
				disp.forward(request, response);
            }
        }
        RequestDispatcher disp = request.getRequestDispatcher(ruta);
		disp.forward(request, response);
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tipo = request.getParameter("tipo");
		if(request.getParameter("Eleccion").equals("Escritura")) {
			if(request.getParameter("dato1").isBlank() || request.getParameter("dato2").isBlank()|| request.getParameter("dato3").isBlank() ||request.getParameter("dato4").isBlank()||request.getParameter("dato5").isBlank()||request.getParameter("dato6").isBlank()) {
				request.setAttribute("error", "si");
				RequestDispatcher dis = request.getRequestDispatcher("Acceso.jsp");
				dis.forward(request, response);
			}else {//escritura correcto
				getServletConfig().getServletContext().setAttribute("llegada", "escritura");
				if(tipo.equals("XLS")) {// el XLS
					getServletConfig().getServletContext().setAttribute("retorno", "xls");
					Workbook wb = new HSSFWorkbook();
					CreationHelper createHelper = wb.getCreationHelper();
					
					Sheet sheet = wb.createSheet("si");
					Row row = sheet.createRow((short)0);
					for(int i = 0; i<6; i++) {
						Cell cell = row.createCell(i);//tiene que empezar por cero
						cell.setCellValue("cabezera");//esto es solo para poner una cabezera
					}
					
					Row row1 = sheet.createRow((short)1);
					for(int i = 0; i<6; i++) {
						Cell cell = row1.createCell(i);//tiene que empezar por cero
						int temp = i+1;
						cell.setCellValue(request.getParameter("dato"+temp));//esto lo guarda en una linea, se le puede poner una primera fila si se quiere con las cabezeras
					}
					FileOutputStream fos = new FileOutputStream("Documentos\\Archivo.xls");
					wb.write(fos);
					fos.close();
					
					if (Desktop.isDesktopSupported()) {
		                Desktop desktop = Desktop.getDesktop();

		                desktop.open(new File("Documentos\\Archivo.xls"));
		            } else {
		            	getServletConfig().getServletContext().setAttribute("error", "Error en la apertura del archivo XLS");
        				RequestDispatcher disp = request.getRequestDispatcher("Error.jsp");
        				disp.forward(request, response);
		            }
					
					
				}else if (tipo.equals("CSV")) {// el CSV
					getServletConfig().getServletContext().setAttribute("retorno", "csv");
					File fi = new File("Documents\\Archivo.csv");
					FileWriter outputfile = new FileWriter(fi);
					CSVWriter writer = new CSVWriter(outputfile);
					
					String[] header = {"Clave", "Dato"};//las claves(headers)
			        writer.writeNext(header);
			        
			        Enumeration<String> mob = request.getParameterNames();
			        int cont = 1;
			        while(mob.hasMoreElements()) {
			        	String temp = mob.nextElement();
			        	if(!temp.equals("Eleccion")&&!temp.equals("tipo")) {
			        		String[] data = { "Dato "+cont, request.getParameter(temp) };//podria usar temp pero prefiero con mayusculas y espacio 
				            writer.writeNext(data);
				            cont++;
			        	}
			        }
			        writer.close(); 
			        
			        if (Desktop.isDesktopSupported()) {
		                Desktop desktop = Desktop.getDesktop();

		                desktop.open(fi);
		            } else {
		            	getServletConfig().getServletContext().setAttribute("error", "Error en la apertura del archivo CSV");
        				RequestDispatcher disp = request.getRequestDispatcher("Error.jsp");
        				disp.forward(request, response);
		            }
		                
				}else if (tipo.equals("JSON")) {// el JSON
					getServletConfig().getServletContext().setAttribute("retorno", "json");
					JSONObject ob = new JSONObject();
					for(int i =0; i<6;i++) {//algo cutre, podria recuperar los nombres de los parametros, pero se que tiene que ser seis pues...
					ob.put("Clave" + i, request.getParameter("dato"+i));//coge los datos, y como en la interfaz no puedes diferenciar claves, las pongo yo.
					}
					FileWriter fw = new FileWriter("Documentos\\Archivo.json", false);//le das nombre, podria pedirlo, o pedir la ruta.
					fw.write(ob.toString());//escribe el json
					fw.close();
					
					if (Desktop.isDesktopSupported()) {
		                Desktop desktop = Desktop.getDesktop();

		                desktop.open(new File("Documentos\\Archivo.json"));
		            } else {
		            	getServletConfig().getServletContext().setAttribute("error", "Error en la apertura del archivo JSON");
        				RequestDispatcher disp = request.getRequestDispatcher("Error.jsp");
        				disp.forward(request, response);
		            }
				}else if (tipo.equals("XML")) {//innecesario,   el XML
					getServletConfig().getServletContext().setAttribute("retorno", "xml");
					Document documento;
			        Element elemento = null;
			        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			        
			        try {
			        	
						DocumentBuilder db = dbf.newDocumentBuilder();
						documento = db.newDocument();
						
						Element rootEle = documento.createElement("Datos");//el<> principal
						
						Enumeration<String> en = request.getParameterNames();
						while(en.hasMoreElements()) {
							String temp = en.nextElement();
							if(!temp.equals("tipo")&&!temp.equals("Eleccion")) {//coger todos los parametros de datos
								elemento = documento.createElement(temp);//<dato1>
								elemento.appendChild(documento.createTextNode(request.getParameter(temp)));//<valor dato1>
						        rootEle.appendChild(elemento);
							}
						}
						documento.appendChild(rootEle);
						
						Transformer tr;
						try {
							tr = TransformerFactory.newInstance().newTransformer();
							tr.setOutputProperty(OutputKeys.INDENT, "yes");
				            tr.setOutputProperty(OutputKeys.METHOD, "xml");
				            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				            tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
				            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				            try {
								tr.transform(new DOMSource(documento), new StreamResult(new FileOutputStream("Documentos\\Archivo.csv")));
							} catch (FileNotFoundException | TransformerException e) {
								getServletConfig().getServletContext().setAttribute("error", "Error al escribir el XML");
	            				RequestDispatcher disp = request.getRequestDispatcher("Error.jsp");
	            				disp.forward(request, response);
							}
						} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
							getServletConfig().getServletContext().setAttribute("error", "Error al escribir el XML");//no concreto cual
            				RequestDispatcher disp = request.getRequestDispatcher("Error.jsp");
            				disp.forward(request, response);
						}

						
					} catch (ParserConfigurationException e) {
						getServletConfig().getServletContext().setAttribute("error", "Error al escribir el XML");
        				RequestDispatcher disp = request.getRequestDispatcher("Error.jsp");
        				disp.forward(request, response);
					}
			        if (Desktop.isDesktopSupported()) {
		                Desktop desktop = Desktop.getDesktop();

		                desktop.open(new File("Documentos\\Archivo.xml"));
		            } else {
		            	getServletConfig().getServletContext().setAttribute("error", "Error en la apertura del archivo CSV");
        				RequestDispatcher disp = request.getRequestDispatcher("Error.jsp");
        				disp.forward(request, response);
		            }
				}
				RequestDispatcher disp = request.getRequestDispatcher("Acceso.jsp");
				disp.forward(request, response);
			}
			
			}else if(request.getParameter("Eleccion").equals("Lectura")) {//innecesario porque solo hay dos opciones
			getServletConfig().getServletContext().setAttribute("llegada", "lectura");//no necesario
			
			if(tipo.equals("XLS")) {
				getServletConfig().getServletContext().setAttribute("retorno", "xls");//se puede hacer con el nombre en vez de ir uno a uno, pero luego lo uso
			}else if (tipo.equals("CSV")) {
				getServletConfig().getServletContext().setAttribute("retorno", "csv");//en caso de repeticion se sobreescribe.
			}else if (tipo.equals("JSON")) {
				getServletConfig().getServletContext().setAttribute("retorno", "json");//como solo llega hasta aqui una vez por acceso, tambien uso este para el mensaje de acceso
			}else if (tipo.equals("XML")) {
				getServletConfig().getServletContext().setAttribute("retorno", "xml");
			}
			RequestDispatcher dis = request.getRequestDispatcher("Peticion.jsp");
			dis.forward(request, response);dis.forward(request, response);
		}
	}
}


	
	
