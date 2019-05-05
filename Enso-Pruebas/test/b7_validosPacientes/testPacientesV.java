package b7_validosPacientes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import elementos.Alarma;
import elementos.Paciente;
import subsistemaGestionPacientes.DatosPacientes;
import subsistemaGestionPacientes.Itf6_DatosPacientes;

class testPacientesV {
	Itf6_DatosPacientes subsistema;
	String NSSvalido;
	Paciente pacienteRegistrado;
	String URLvalida;
	
	@BeforeEach
	void inicio() {
		subsistema = (Itf6_DatosPacientes)new DatosPacientes();
		NSSvalido = "123456781221";
	}
	
	@AfterEach
	void restaurar() {
		
	}
	
	

	@Test
	@DisplayName("CP_00037: dar de alta paciente valido")
	void testdarAlta_037() {
		pacienteRegistrado = new Paciente(NSSvalido,"Ourense", "Miguel Martinez", "12-03-1994");
		Boolean resultado = subsistema.darAlta(pacienteRegistrado);
		assertTrue(resultado);
	}

	
	@Test
	@DisplayName("CP_00040: Actualizar paciente con parametros validos (SOLO VALIDO SI CP_00037 pasa)")
	void testActualizarPaciente_040() {
		pacienteRegistrado.setDireccion("Palmeira");
		Boolean resultado = subsistema.actualizar(pacienteRegistrado);
		assertTrue(resultado);
		Paciente pacienteActualizado = subsistema.consultarDatosPaciente(pacienteRegistrado.getnSeguridadSocial());
		assertEquals(pacienteRegistrado, pacienteActualizado);
	}
	
	@Test
	@Disabled
	@DisplayName("CP_00043: Generar alarma con medida null")
	void testenviarDatos_043() {

	}
	
	@Test
	@Disabled
	@DisplayName("CP_00046: Generar alarma con medida null")
	void testenviarDatos_046() {

	}
}
