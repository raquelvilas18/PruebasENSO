package b1.validosSensorizacion;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import subsistemaAlmacenDatos.DatosMedidas;
import subsistemaGestionPacientes.DatosPacientes;

public class test {
	DatosMedidas subsistema;

	@BeforeAll
	public void inicio() {
		subsistema = new DatosMedidas(new DatosPacientes());
	}
	
	@Test
	public void testCP_00001() {
		String resultado = subsistema.leerDatos(null, new ArrayList<>());
		assertEquals(null, resultado, "Falla el CP_00002");	
	}
	

}
