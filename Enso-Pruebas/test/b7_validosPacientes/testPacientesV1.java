package b7_validosPacientes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import elementos.Paciente;
import subsistemaGestionPacientes.DatosPacientes;
import subsistemaGestionPacientes.Itf6_DatosPacientes;

class testPacientesV1 {
	//Prueba de los metodos validos de Pacientes que NO necesitan un paciente dado de alta en el sistema
	Itf6_DatosPacientes subsistema;
	String NSSvalido;
	Paciente pacienteRegistrado;
	
	@BeforeEach
	void inicio() {
		subsistema = (Itf6_DatosPacientes)new DatosPacientes();
		NSSvalido = "123456781221";
	}
	
	@AfterEach
	void restaurar() {
		subsistema.eliminar(pacienteRegistrado);
	}
	

	@Test
	@DisplayName("CP_00037: dar de alta paciente valido")
	void testdarAlta_037() {
		pacienteRegistrado = new Paciente(NSSvalido,"Ourense", "Miguel Martinez", "12-03-1994");
		Boolean resultado = subsistema.darAlta(pacienteRegistrado);
		assertTrue(resultado);
	}

}
