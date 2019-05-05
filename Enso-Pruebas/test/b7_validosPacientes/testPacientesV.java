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
		subsistema.eliminar(pacienteRegistrado);
	}
	
	

	@Test
	@DisplayName("CP_00037: dar de alta paciente valido")
	void testdarAlta_037() {
		pacienteRegistrado = new Paciente(NSSvalido,"Ourense", "Miguel Martinez", "12-03-1994");
		Boolean resultado = subsistema.darAlta(pacienteRegistrado);
		assertTrue(resultado);
	}

	
	@Test
	@DisplayName("CP_00040: Actualizar paciente con nueva direccion")
	void testActualizarPaciente_040() {
		//Dar de alta por si no estaba el paciente
		pacienteRegistrado = new Paciente(NSSvalido,"Ourense", "Miguel Martinez", "12-03-1994");
		subsistema.darAlta(pacienteRegistrado);
		
		
		pacienteRegistrado.setDireccion("Palmeira");
		Boolean resultado = subsistema.actualizar(pacienteRegistrado);
		Paciente pacienteActualizado = subsistema.consultarDatosPaciente(pacienteRegistrado.getnSeguridadSocial());
		assertTrue(resultado); //El metodo devuelve true porque lo actualizo con exito
		assertEquals(pacienteRegistrado, pacienteActualizado); //El paciente recuperado de la BD esta actualizado correctamente
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
	
	
	@Test
	@DisplayName("CP_00057: Actualizar paciente con nuevo nombre")
	void testActualizarPaciente_057() {
		//Dar de alta por si no estaba el paciente
		pacienteRegistrado = new Paciente(NSSvalido,"Ourense", "Miguel Martinez", "12-03-1994");
		subsistema.darAlta(pacienteRegistrado);
		
		
		pacienteRegistrado.setDireccion("Palmeira");
		Boolean resultado = subsistema.actualizar(pacienteRegistrado);
		Paciente pacienteActualizado = subsistema.consultarDatosPaciente(pacienteRegistrado.getnSeguridadSocial());
		assertTrue(resultado); //El metodo devuelve true porque lo actualizo con exito
		assertEquals(pacienteRegistrado, pacienteActualizado); //El paciente recuperado de la BD esta actualizado correctamente
	}
	
	@Test
	@DisplayName("CP_00058: Actualizar paciente con nueva fecha nacimiento")
	void testActualizarPaciente_058() {
		//Dar de alta por si no estaba el paciente
		pacienteRegistrado = new Paciente(NSSvalido,"Ourense", "Miguel Martinez", "12-03-1994");
		subsistema.darAlta(pacienteRegistrado);
		
		pacienteRegistrado.setFechaNacimiento("12-03-1992");
		Boolean resultado = subsistema.actualizar(pacienteRegistrado);
		Paciente pacienteActualizado = subsistema.consultarDatosPaciente(pacienteRegistrado.getnSeguridadSocial());
		assertTrue(resultado); //El metodo devuelve true porque lo actualizo con exito
		assertEquals(pacienteRegistrado, pacienteActualizado); //El paciente recuperado de la BD esta actualizado correctamente
	}
	
	@Test
	@DisplayName("CP_00059: Actualizar paciente con nuevo nombre")
	void testActualizarPaciente_059() {
		//Dar de alta por si no estaba el paciente
		pacienteRegistrado = new Paciente(NSSvalido,"Ourense", "Miguel Martinez", "12-03-1994");
		subsistema.darAlta(pacienteRegistrado);
		
		
		pacienteRegistrado.setNombreAp("Miguel Muras");
		Boolean resultado = subsistema.actualizar(pacienteRegistrado);
		Paciente pacienteActualizado = subsistema.consultarDatosPaciente(pacienteRegistrado.getnSeguridadSocial());
		assertTrue(resultado); //El metodo devuelve true porque lo actualizo con exito
		assertEquals(pacienteRegistrado, pacienteActualizado); //El paciente recuperado de la BD esta actualizado correctamente
	}
	
	@Test
	@DisplayName("CP_00060: Actualizar paciente al que no le ha cambiado nada")
	void testActualizarPaciente_060() {
		//Dar de alta por si no estaba el paciente
		pacienteRegistrado = new Paciente(NSSvalido,"Ourense", "Miguel Martinez", "12-03-1994");
		subsistema.darAlta(pacienteRegistrado);
		
		Boolean resultado = subsistema.actualizar(pacienteRegistrado);
		Paciente pacienteActualizado = subsistema.consultarDatosPaciente(pacienteRegistrado.getnSeguridadSocial());
		assertTrue(resultado); //El metodo devuelve true porque lo actualizo con exito
		assertEquals(pacienteRegistrado, pacienteActualizado); //El paciente recuperado de la BD esta actualizado correctamente
	}
	

}
