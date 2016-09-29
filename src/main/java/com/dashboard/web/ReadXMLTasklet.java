package com.dashboard.web;



import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.dashboard.model.Aplicacion;
import com.dashboard.model.Cliente;
import com.dashboard.model.Servidor;
import com.dashboard.service.ConexionDAO;
import com.dashboard.service.XMLReader;
import com.utils.Utilidades;


@Component("readXMLTasklet")
public class ReadXMLTasklet implements Tasklet {
	
	
	
		
	@Resource
	ConexionDAO conexion;
	
	
	public void insertUpdateInfo() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException{
		Date fechaActual = Calendar.getInstance(TimeZone.getTimeZone("America/Caracas")).getTime();
		String timestamp = Utilidades.DATE_FORMAT.format(fechaActual);
		XMLReader  xmlReader = new XMLReader();
		Servidor servidor = xmlReader.getXML();
	List<Aplicacion> listaAplicaciones =	servidor.getListaAplicaciones();
		for (Aplicacion aplicacion : listaAplicaciones) {
			
			conexion.insertUpdateAplicacion(aplicacion, timestamp);
			List<Cliente> listaClientes = aplicacion.getListaClientes();
			if(listaClientes != null)
			for (Cliente cliente : listaClientes) {
				cliente.setIdAplicacion(aplicacion.getNombre());
				conexion.insertUpdateCliente(cliente, timestamp);
			}
		}
	}
	
	

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1)
			throws Exception {

		insertUpdateInfo();
		
		
		return RepeatStatus.FINISHED;
	}

}
