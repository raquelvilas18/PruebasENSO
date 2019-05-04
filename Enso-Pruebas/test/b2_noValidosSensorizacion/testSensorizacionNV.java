package b2_noValidosSensorizacion;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import elementos.Paciente;
import subsistemaAlmacenDatos.DatosMedidas;
import subsistemaAlmacenDatos.Itf1_DatosSimulados;
import subsistemaGestionPacientes.DatosPacientes;

class testSensorizacionNV {

	Itf1_DatosSimulados subsistema;
	Paciente pacienteRegistrado;
	String NSSvalido;
	String URLvalida;
	@BeforeEach
	void inicio() {
		subsistema = (Itf1_DatosSimulados)new DatosMedidas(new DatosPacientes());
		NSSvalido = "281234567840";
		URLvalida = "ficheros/"+NSSvalido+"/medidas.csv";
		pacienteRegistrado = new Paciente(NSSvalido, "Santiago de chile5", "Juan Rodriguez Alvarez", "28-07-1998");
		DatosPacientes subsistemaPacientes= new DatosPacientes();
		subsistemaPacientes.darAlta(pacienteRegistrado);
	}
	
	@AfterEach
	void limpiar() {
		DatosPacientes subsistemaPacientes = new DatosPacientes();
		subsistemaPacientes.eliminar(pacienteRegistrado);
	}
	
	@Test
	@DisplayName("CP_00002: Leer datos del paciente con URL nula")
	void testLeerDatosPacientes_002() {
		String resultado = subsistema.leerDatos(null, new ArrayList<>());
		assertNull(resultado);
	}
	
	@Test
	@DisplayName("CP_00003: Leer datos del paciente con URL nula")
	void testLeerDatosPacientes_003() {
		String resultado = subsistema.leerDatos(NSSvalido, null);
		assertNull(resultado);
	}
	
	@Test
	@DisplayName("CP_00004: Leer datos del paciente con URL no valida y arraylist no valido")
	void testLeerDatosPacientes_004() {	
		String resultado = subsistema.leerDatos("ficheros//medidas.csv", null);
		assertNull(resultado);
	}
	
	@Test
	@DisplayName("CP_00006: Enviar datos de los sensores con arrayList no valido")
	void testenviarDatos_006() {
		Boolean respuesta = subsistema.enviarDatos( null, NSSvalido);
		assertFalse(respuesta);
	}
	
	@Test
	@DisplayName("CP_00007: Enviar datos de los sensores con arrayList no valido y NSS no valido")
	void testenviarDatos_007() {
		Boolean respuesta = subsistema.enviarDatos( null, null);
		assertFalse(respuesta);
	}

	@Test
	@DisplayName("CP_00008: Enviar datos de los sensores con  NSS no valido")
	void testenviarDatos_008() {
		Boolean respuesta = subsistema.enviarDatos( new ArrayList<>(), "12123456700");
		assertFalse(respuesta);
	}
	
	

}
