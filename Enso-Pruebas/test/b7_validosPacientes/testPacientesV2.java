package b7_validosPacientes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import elementos.Alarma;
import elementos.Paciente;
import subsistemaGestionPacientes.DatosPacientes;
import subsistemaGestionPacientes.Itf6_DatosPacientes;

class testPacientesV2 {
	//En esta clase se prueban los métodos validos del subsistema de pacientes QUE NECESITAN UN PACIENTE DADO DE ALTA
	Itf6_DatosPacientes subsistema;
	String NSSvalido;
	Paciente pacienteRegistrado;
	
	@BeforeEach
	void inicio() {
		subsistema = (Itf6_DatosPacientes)new DatosPacientes();
		NSSvalido = "123456781221";
		//Dar de alta por si no estaba el paciente
		pacienteRegistrado = new Paciente(NSSvalido,"Ourense", "Miguel Martinez", "12-03-1994");
		subsistema.darAlta(pacienteRegistrado);
	}
	
	@AfterEach
	void restaurar() {
		subsistema.eliminar(pacienteRegistrado);
	}
	
	@Test
	@DisplayName("CP_00040: Actualizar paciente con nueva direccion")
	void testActualizarPaciente_040() {
		pacienteRegistrado.setDireccion("Palmeira");
		Boolean resultado = subsistema.actualizar(pacienteRegistrado);
		Paciente pacienteActualizado = subsistema.consultarDatosPaciente(pacienteRegistrado.getnSeguridadSocial());
		assertTrue(resultado); //El metodo devuelve true porque lo actualizo con exito
		assertEquals(pacienteRegistrado, pacienteActualizado); //El paciente recuperado de la BD esta actualizado correctamente
	}
	
	@Test
	@DisplayName("CP_00046: consultar datos de un paciente del sistema")
	void testConsultarPaciente() {	
		Paciente encontrado = subsistema.consultarDatosPaciente(NSSvalido);
		assertEquals(encontrado, pacienteRegistrado);
	}
	
	
	@Test
	@DisplayName("CP_00043: Eliminar paciente registrado en el sistema")
	void testEliminarPaciente_043() {	
		Boolean resultado = subsistema.eliminar(pacienteRegistrado);
		Paciente eliminado = subsistema.consultarDatosPaciente(pacienteRegistrado.getnSeguridadSocial());
		assertTrue(resultado);
		assertNull(eliminado);
	}
	
	
	@Test
	@DisplayName("CP_00057: Actualizar paciente con nuevo nombre")
	void testActualizarPaciente_057() {
		pacienteRegistrado.setDireccion("Palmeira");
		Boolean resultado = subsistema.actualizar(pacienteRegistrado);
		Paciente pacienteActualizado = subsistema.consultarDatosPaciente(pacienteRegistrado.getnSeguridadSocial());
		assertTrue(resultado); //El metodo devuelve true porque lo actualizo con exito
		assertEquals(pacienteRegistrado, pacienteActualizado); //El paciente recuperado de la BD esta actualizado correctamente
	}
	
	@Test
	@DisplayName("CP_00058: Actualizar paciente con nueva fecha nacimiento")
	void testActualizarPaciente_058() {
		pacienteRegistrado.setFechaNacimiento("12-03-1992");
		Boolean resultado = subsistema.actualizar(pacienteRegistrado);
		Paciente pacienteActualizado = subsistema.consultarDatosPaciente(pacienteRegistrado.getnSeguridadSocial());
		assertTrue(resultado); //El metodo devuelve true porque lo actualizo con exito
		assertEquals(pacienteRegistrado, pacienteActualizado); //El paciente recuperado de la BD esta actualizado correctamente
	}
	
	@Test
	@DisplayName("CP_00059: Actualizar paciente con nuevo nombre")
	void testActualizarPaciente_059() {
		pacienteRegistrado.setNombreAp("Miguel Muras");
		Boolean resultado = subsistema.actualizar(pacienteRegistrado);
		Paciente pacienteActualizado = subsistema.consultarDatosPaciente(pacienteRegistrado.getnSeguridadSocial());
		assertTrue(resultado); //El metodo devuelve true porque lo actualizo con exito
		assertEquals(pacienteRegistrado, pacienteActualizado); //El paciente recuperado de la BD esta actualizado correctamente
	}
	
	@Test
	@DisplayName("CP_00060: Actualizar paciente al que no le ha cambiado nada")
	void testActualizarPaciente_060() {
		Boolean resultado = subsistema.actualizar(pacienteRegistrado);
		Paciente pacienteActualizado = subsistema.consultarDatosPaciente(pacienteRegistrado.getnSeguridadSocial());
		assertTrue(resultado); //El metodo devuelve true porque lo actualizo con exito
		assertEquals(pacienteRegistrado, pacienteActualizado); //El paciente recuperado de la BD esta actualizado correctamente
	}
	
	@Test
	@DisplayName("CP_00061: dar alta paciente recien eliminado")
	void testAltaPaciente_061() {
		subsistema.eliminar(pacienteRegistrado);
		Paciente pacienteRegistrar  = new Paciente(NSSvalido,"Ourense", "Miguel Martinez", "12-03-1994");
		Boolean resultado = subsistema.darAlta(pacienteRegistrar); 
		assertTrue(resultado); 
	}

}
