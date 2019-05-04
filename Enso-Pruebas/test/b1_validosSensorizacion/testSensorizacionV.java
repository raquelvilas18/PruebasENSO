package b1_validosSensorizacion;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.*;

import elementos.Paciente;
import subsistemaAlmacenDatos.DatosMedidas;
import subsistemaGestionPacientes.DatosPacientes;

class testSensorizacionV {
	DatosMedidas subsistema;
	Paciente pacienteRegistrado;
	String NSSvalido;
	String URLvalida;
	@BeforeEach
	void inicio() {
		subsistema = new DatosMedidas(new DatosPacientes());
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
	@DisplayName("CP_00001: Leer datos del paciente con parametros validos")
	void testLeerDatosPacientes_001() {
		String respuesta = subsistema.leerDatos(URLvalida, new ArrayList<>());
		assertEquals(NSSvalido, respuesta, "Leer datos del paciente no devuelve el NSS correcto");
	}

}
