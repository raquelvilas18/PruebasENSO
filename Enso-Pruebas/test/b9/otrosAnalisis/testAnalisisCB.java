package b9.otrosAnalisis;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import elementos.Estadistico;
import elementos.Paciente;
import opera.Arit;
import subsistemaAlmacenDatos.ExcepcionDeFechas;
import subsistemaAnalisis.DatosAnalisis;
import subsistemaAnalisis.Itf4_Estadisticas;
import subsistemaGestionPacientes.DatosPacientes;
import subsistemaGestionPacientes.Itf6_DatosPacientes;

class testAnalisisCB {

	Itf4_Estadisticas subsistema;
	DatosAnalisis subsistemaAnalisis;
	DatosPacientes subPacientes;
	String NSSvalido;
	String URLvalida;
	Paciente pacienteRegistrado;
	
	@BeforeEach
	void inicio() {
		
		subPacientes = new DatosPacientes();
		subsistema = (Itf4_Estadisticas)new DatosAnalisis(subPacientes);
		NSSvalido = "281234567840";
		URLvalida = "ficheros/"+NSSvalido+"/medidas.csv";
		pacienteRegistrado = new Paciente(NSSvalido, "Santiago de chile5", "Juan Rodriguez Alvarez", "28-07-1998");
		subPacientes.darAlta(pacienteRegistrado);
	}
	
	@AfterEach
	void restaurar() {
		subPacientes.eliminar(pacienteRegistrado);
	}
	
	@Test
	@DisplayName("CB_1: comprobar camino 1-2-3-FIN que debería lanzar una excepción de fechas")
	void testsolicitarEstadistico_001() {
		String fechaInicio="22-03-2019;15:15:15";
		String fechaFin = "22-02-2019;15:15:15";
		Paciente pac = new Paciente("123456781221","Ourense", "Miguel Martínez", "12-03-1994");
		subPacientes.darAlta(pac);
		assertThrows(ExcepcionDeFechas.class,()->{subsistema.solicitarEstadisticos(pac, fechaInicio, fechaFin); });
	}
	
	
	//POR HACER HAY QUE METERLE MEDIDAS
	@Test
	@DisplayName("CB_2: comprobar camino 1-2-4-6-7-8-9-12-7-13-FIN que debería lanzar una excepción de fechas")
	void testsolicitarEstadistico_002() {
		
		String fechaInicio="22-02-2019;15:15:15";
		String fechaFin = "22-03-2019;15:15:15";
		Paciente pac = new Paciente("123456781221","Ourense", "Miguel Martínez", "12-03-1994");
		pac.setEstadisticos(new ArrayList<>());
		subPacientes.darAlta(pac);
		
		FileWriter fichero;
		try {
			fichero = new FileWriter("ficheros/123456781221/Estadistico.csv",true);
	        PrintWriter pw = new PrintWriter(fichero);
			pw.write("01-03-2019;15:15:15;T;36.7;36.1;36.4;33;34;35;36\n");
			pw.close();
			
			ArrayList<Estadistico> resultado = subsistema.solicitarEstadisticos(pac, fechaInicio, fechaFin);
			Paciente sistema = subPacientes.consultarDatosPaciente("123456781221");
			//assertEquals(resultado.size(), 1);
			assertSame(resultado, sistema.getEstadisticos());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
