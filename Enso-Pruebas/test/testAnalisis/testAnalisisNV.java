package testAnalisis;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import elementos.*;
import subsistemaAlmacenDatos.ExcepcionDeFechas;

import java.util.*;
import subsistemaAnalisis.DatosAnalisis;
import subsistemaAnalisis.Itf4_Estadisticas;
import subsistemaAnalisis.Itf5_Alarmas;
import subsistemaGestionPacientes.DatosPacientes;

class testAnalisisNV {

	Itf4_Estadisticas subsistema;
	Itf5_Alarmas subsistema2;
	String NSSvalidoNoRegistrado = "281234567847";
	String NSSvalidoRegistrado = "281234567840";
	Paciente pacienteRegistrado;
	Paciente pacienteNoRegistrado;

	@BeforeEach
	void inicio() {
		subsistema = new DatosAnalisis(new DatosPacientes());
		subsistema2 = new DatosAnalisis(new DatosPacientes());

		pacienteNoRegistrado = new Paciente(NSSvalidoNoRegistrado, "Santiago de chile5", "Juan Rodriguez Alvarez",
				"28-07-1998");
		pacienteRegistrado = new Paciente(NSSvalidoRegistrado, "Santiago de chile5", "Juan Rodriguez Alvarez",
				"28-07-1998");

		DatosPacientes subsistemaPacientes = new DatosPacientes();
		subsistemaPacientes.darAlta(pacienteRegistrado);
	}

	@AfterEach
	void limpiar() {
		DatosPacientes subsistemaPacientes = new DatosPacientes();
		subsistemaPacientes.eliminar(pacienteRegistrado);
	}

	@Test
	@DisplayName("CP_00032: Solicitar estadistico ->comprobar camino 1-2-3-FIN que deber�a lanzar una excepci�n de fechas")
	void testsolicitarEstadistico_001() {
		String fechaInicio="22-03-2019;15:15:15";
		String fechaFin = "22-02-2019;15:15:15";
		Paciente pac = new Paciente("123456781221","Ourense", "Miguel Mart�nez", "12-03-1994");
		DatosPacientes subsistemaPacientes = new DatosPacientes();
		subsistemaPacientes.darAlta(pac);
		assertThrows(ExcepcionDeFechas.class,()->{subsistema.solicitarEstadisticos(pac, fechaInicio, fechaFin); });
		
		subsistemaPacientes.eliminar(pac);
	
	}

	@Test
	@DisplayName("CP_00033: Solicitar estad�stico")
	void solicitarEstadistico_33() {
		ArrayList<Estadistico> respuesta = null;
		String fechaInicio = "30-2-2019";
		String fechaFin = "23-15-2019";
		respuesta = subsistema.solicitarEstadisticos(pacienteRegistrado, fechaInicio, fechaFin);
		assertNull(respuesta);
	}

	@Test
	@DisplayName("CP_00034: Solicitar estad�stico")
	void solicitarEstadistico_34() {
		ArrayList<Estadistico> respuesta = null;
		String fechaInicio = "22-2-2019";
		String fechaFin = "30-1-2019";
		respuesta = subsistema.solicitarEstadisticos(pacienteRegistrado, fechaInicio, fechaFin);
		assertNull(respuesta);
	}

	@Test
	@DisplayName("CP_00036: Ver alarmas de un paciente")
	void verAlarmas_36() {
		ArrayList<Alarma> respuesta = null;
		respuesta = subsistema2.verAlarmas(pacienteNoRegistrado);
		assertNull(respuesta);
	}

}
