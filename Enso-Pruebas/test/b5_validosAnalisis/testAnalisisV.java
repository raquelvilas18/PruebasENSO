package b5_validosAnalisis;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import elementos.Alarma;
import elementos.Estadistico;
import elementos.Paciente;
import subsistemaAnalisis.DatosAnalisis;
import subsistemaAnalisis.Itf4_Estadisticas;
import subsistemaAnalisis.Itf5_Alarmas;
import subsistemaGestionPacientes.DatosPacientes;

class testAnalisisV {

	Itf4_Estadisticas subsistema;
	Itf5_Alarmas subsistema2;
	String NSSvalidoNoRegistrado = "281234567847";
	String NSSvalidoRegistrado = "281234567840";
	Paciente pacienteRegistrado;
	Paciente pacienteRegistrado2;
	Paciente pacienteNoRegistrado;

	@BeforeEach
	void inicio() {
		subsistema = new DatosAnalisis(new DatosPacientes());
		subsistema2 = new DatosAnalisis(new DatosPacientes());

		pacienteNoRegistrado = new Paciente(NSSvalidoNoRegistrado, "Santiago de chile5", "Juan Rodriguez Alvarez",
				"28-07-1998");
		pacienteRegistrado = new Paciente(NSSvalidoRegistrado, "Santiago de chile5", "Juan Rodriguez Alvarez",
				"28-07-1998");
		pacienteRegistrado2 = new Paciente("123456781221", "Ourense", "Miguel Martínez", "12-03-1994");

		DatosPacientes subsistemaPacientes = new DatosPacientes();
		subsistemaPacientes.darAlta(pacienteRegistrado);
		subsistemaPacientes.darAlta(pacienteRegistrado2);
	}

	@AfterEach
	void limpiar() {
		DatosPacientes subsistemaPacientes = new DatosPacientes();
		subsistemaPacientes.eliminar(pacienteRegistrado);
		subsistemaPacientes.eliminar(pacienteRegistrado2);
	}

	@Test
	@DisplayName("CP_00031: Solicitar estadístico")
	void testSolicitarEstadistico_031() {
		ArrayList<Estadistico> respuesta = null;
		String fechaInicio = "30-2-2019";
		String fechaFin = "30-3-2019";
		respuesta = subsistema.solicitarEstadisticos(pacienteRegistrado, fechaInicio, fechaFin);
		assertNotNull(respuesta);
	}
	
	@Test
	@DisplayName("CP_00035: Ver alarmas de un paciente")
	void testVerAlarmasDeUnPaciente_035() {
		ArrayList<Alarma> respuesta = null;
		respuesta = subsistema2.verAlarmas(pacienteRegistrado);
		assertNotNull(respuesta);
	}
	
}
