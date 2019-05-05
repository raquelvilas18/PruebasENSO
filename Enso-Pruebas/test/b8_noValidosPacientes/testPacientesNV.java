package b8_noValidosPacientes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import elementos.Paciente;
import subsistemaAnalisis.DatosAnalisis;
import subsistemaAnalisis.Itf2_DatosInstantaneos;
import subsistemaGestionPacientes.DatosPacientes;
import subsistemaGestionPacientes.Itf6_DatosPacientes;

class testPacientesNV {
	Itf6_DatosPacientes subsistema;
	Paciente pacienteRegistrado;
	String NSSvalido;
	String URLvalida;
	
	@BeforeEach
	void inicio() {
		subsistema = (Itf6_DatosPacientes)new DatosPacientes();
		NSSvalido = "281234567840";
		URLvalida = "ficheros/"+NSSvalido+"/medidas.csv";
		pacienteRegistrado = new Paciente(NSSvalido, "Santiago de chile5", "Juan Rodriguez Alvarez", "28-07-1998");
		subsistema.darAlta(pacienteRegistrado);
	}
	
	@AfterEach
	void restaurar() {
		subsistema.eliminar(pacienteRegistrado);
	}

	@Test
	@DisplayName("CP_00038: Dar de alta a un paciente ya registrado en el sistema")
	void testAltaPaciente_038() {
		Boolean resultado = subsistema.darAlta(pacienteRegistrado);
		assertFalse(resultado);
	}
	
	@Test
	@DisplayName("CP_00039: Dar de alta con parametro nulo")
	void testActualizarPaciente_039() {
		Boolean resultado = subsistema.darAlta(null);
		assertFalse(resultado);
	}
	
	@Test
	@DisplayName("CP_00041: Actualizar paciente no registrado en el sistema")
	void testActualizarPaciente_041() {
		Paciente nuevo = new Paciente("111122223330", "Santiago de chile5", "Juan Rodriguez Alvarez", "28-07-1998");
		Boolean resultado = subsistema.actualizar(nuevo);
		assertFalse(resultado);
	}
	
	@Test
	@DisplayName("CP_00042: Actualizar paciente con parámetro nulo")
	void testActualizarPaciente_042() {
		Boolean resultado = subsistema.actualizar(null);
		assertFalse(resultado);
	}
	
	@Test
	@DisplayName("CP_00044: eliminar paciente no registrado en el sistema")
	void testEliminarPaciente_044() {
		Paciente noRegistrado = new Paciente("111122222219","Ourense", "Miguel Martinez", "12-03-1994");
		Boolean resultado = subsistema.eliminar(noRegistrado);
		assertFalse(resultado);
	}
	
	@Test
	@DisplayName("CP_00045: Actualizar paciente nulo")
	void testEliminarPaciente_045() {
		Boolean resultado = subsistema.eliminar(null);
		assertFalse(resultado);
	}
	
	@Test
	@DisplayName("CP_00047: Consultar datos de paciente introduciendo NSS mal formada")
	void testConsultarPaciente_047() {
		Paciente resultado = subsistema.consultarDatosPaciente("1234");
		assertNull(resultado);
	}
	
	@Test
	@DisplayName("CP_00048: Consultar paciente con un nulo")
	void testConsultarPaciente_048() {
		Paciente resultado = subsistema.consultarDatosPaciente(null);
		assertNull(resultado);
	}
	

}
