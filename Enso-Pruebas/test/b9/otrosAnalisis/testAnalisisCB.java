package b9.otrosAnalisis;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import elementos.Estadistico;
import elementos.Medida;
import elementos.Paciente;
import opera.Arit;
import subsistemaAlmacenDatos.DatosMedidas;
import subsistemaAlmacenDatos.ExcepcionDeFechas;
import subsistemaAlmacenDatos.Itf3_SeriesTemporales;
import subsistemaAnalisis.DatosAnalisis;
import subsistemaAnalisis.Itf2_DatosInstantaneos;
import subsistemaAnalisis.Itf4_Estadisticas;
import subsistemaGestionPacientes.DatosPacientes;
import subsistemaGestionPacientes.Itf6_DatosPacientes;

class testAnalisisCB {

	
	Itf4_Estadisticas subsistema;
	Itf3_SeriesTemporales subsistema2;
	Itf2_DatosInstantaneos subsistemaDatosInstantaneos;
	DatosAnalisis subsistemaAnalisis;
	DatosPacientes subPacientes;
	String NSSvalido;
	String URLvalida;
	Paciente pacienteRegistrado;
	Paciente pacienteRegistrado2;
	
	@BeforeEach
	void inicio() {
		
		subPacientes = new DatosPacientes();
		subsistema = (Itf4_Estadisticas)new DatosAnalisis(subPacientes);
		subsistema2 = new DatosMedidas(subPacientes);
		subsistemaDatosInstantaneos = (Itf2_DatosInstantaneos)new DatosAnalisis(subPacientes);
		NSSvalido = "281234567840";
		URLvalida = "ficheros/"+NSSvalido+"/medidas.csv";
		pacienteRegistrado = new Paciente(NSSvalido, "Santiago de chile5", "Juan Rodriguez Alvarez", "28-07-1998");
		pacienteRegistrado2 = new Paciente("123456781221","Ourense", "Miguel Mart�nez", "12-03-1994");
		subPacientes.darAlta(pacienteRegistrado);
		subPacientes.darAlta(pacienteRegistrado2);
	}
	
	@AfterEach
	void restaurar() {
		subPacientes.eliminar(pacienteRegistrado);
		subPacientes.eliminar(pacienteRegistrado2);
	}
	
	@Test
	@DisplayName("CB_1: comprobar camino 1-2-3-FIN que deber�a lanzar una excepci�n de fechas")
	void testsolicitarEstadistico_001() {
		String fechaInicio="22-03-2019;15:15:15";
		String fechaFin = "22-02-2019;15:15:15";
		Paciente pac = new Paciente("123456781221","Ourense", "Miguel Mart�nez", "12-03-1994");
		subPacientes.darAlta(pac);
		assertThrows(ExcepcionDeFechas.class,()->{subsistema.solicitarEstadisticos(pac, fechaInicio, fechaFin); });
	}
	
	
	//POR HACER HAY QUE METERLE MEDIDAS
	@Test
	@DisplayName("CB_2: comprobar camino 1-2-4-6-7-8-9-12-7-13-FIN que deber�a lanzar una excepci�n de fechas")
	void testsolicitarEstadistico_002() {
		
		String fechaInicio="22-02-2019;15:15:15";
		String fechaFin = "22-03-2019;15:15:15";
		Paciente pac = new Paciente("123456781221","Ourense", "Miguel Mart�nez", "12-03-1994");
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
	
	
	@Test
	@DisplayName("CB_XX: comprobar camino 1-2-3-FIN que deber�a devolver un array vacio, error en la fecha")
	void testsolicitarMedidas_XX() {
		String fechaInicio="22-02-2019;";
		Paciente pac = new Paciente("123456781221","Ourense", "Miguel Mart�nez", "12-03-1994");
		subPacientes.darAlta(pac);
		ArrayList<Medida> aux = subsistema2.solicitarMedidas(pac, fechaInicio);
		assertTrue(aux.isEmpty());
		subPacientes.eliminar(pac);
	}
	
	@Test
	@DisplayName("CB_XX: comprobar camino 1-2-4-5-7-8-10-11-13-14-FIN que deber�a devolver un array vacio, no hay medidas")
	void testsolicitarMedidas_XXX() {
		String fechaInicio="22-02-2019;11:11";
		Paciente pac = new Paciente("123456781221","Ourense", "Miguel Mart�nez", "12-03-1994");
		subPacientes.darAlta(pac);
		ArrayList<Medida> aux = subsistema2.solicitarMedidas(pac, fechaInicio);
		assertTrue(aux.isEmpty());
		subPacientes.eliminar(pac);
	}
	
	@Test
	@DisplayName("CB_XX: comprobar camino 1-2-4-5-7-8-10-11-12-FIN que deber�a devolver un array vacio, fecha posterior a la actual")
	void testsolicitarMedidas_XXXX() {
		String fechaInicio="22-02-2029;11:11";
		Paciente pac = new Paciente("123456781221","Ourense", "Miguel Mart�nez", "12-03-1994");
		subPacientes.darAlta(pac);
		ArrayList<Medida> aux = subsistema2.solicitarMedidas(pac, fechaInicio);
		assertTrue(aux.isEmpty());
		subPacientes.eliminar(pac);
	}
	
	@Test
	@DisplayName("CB_PD: Generar estadístico por el camino 1-2-8-10-11-FIN, devuelve null")
	void testGenerarEstadistico_PD1() {
		pacienteRegistrado2.setMedidas(new ArrayList<>());
		assertNull(subsistemaDatosInstantaneos.generarEstadistico(pacienteRegistrado2));
	}
	
	@Test
	@DisplayName("CB_PD: Generar estadístico por el camino 1-2-3-4-7-2-8-10-11-FIN, devuelve null")
	void testGenerarEstadistico_PD2() {
		ArrayList<Medida> medidas = new ArrayList<>();
		
		// Fechas anteriores a la actual: solo se utilizan las medidas tomadas en la hora actual
		medidas.add(new Medida(36.2f, 80.0f, "30-2-2019;12:34:56"));
		medidas.add(new Medida(36.4f, 85.0f, "30-2-2019;12:39:28"));
		
		pacienteRegistrado2.setMedidas(medidas);
		assertNull(subsistemaDatosInstantaneos.generarEstadistico(pacienteRegistrado2));
	}
	
	@Test
	@DisplayName("CB_PD: Generar estadístico por el camino 1-2-3-4-5-7-2-8-10-11-FIN, devuelve null")
	void testGenerarEstadistico_PD3() {
		ArrayList<Medida> medidas = new ArrayList<>();
		
		// Fechas posteriores a la actual pero también a la hora actual: solo se utilizan las medidas tomadas en la hora actual
		medidas.add(new Medida(36.2f, 80.0f, "30-2-2029;12:34:56"));
		medidas.add(new Medida(36.4f, 85.0f, "30-2-2029;12:39:28"));
		
		pacienteRegistrado2.setMedidas(medidas);
		assertNull(subsistemaDatosInstantaneos.generarEstadistico(pacienteRegistrado2));
	}
	
	@Test
	@DisplayName("CB_PD: Generar estadístico por el camino 1-2-3-4-5-6-7-2-8-9-11-FIN, devuelve ArrayList")
	void testGenerarEstadistico_PD4() {
		ArrayList<Medida> medidas = new ArrayList<>();
		
		// Medidas situadas en la hora actual
		Date fechaActual = new Date();
		String fecha = new SimpleDateFormat("dd-MM-yyyy;kk:mm:ss").format(fechaActual);
		medidas.add(new Medida(36.4f, 85.0f, fecha));
		
		pacienteRegistrado2.setMedidas(medidas);
		assertNotNull(subsistemaDatosInstantaneos.generarEstadistico(pacienteRegistrado2));
	}

}
