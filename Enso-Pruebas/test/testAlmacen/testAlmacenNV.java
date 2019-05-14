package testAlmacen;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import elementos.Alarma;
import elementos.Estadistico;
import elementos.Medida;
import elementos.Paciente;
import subsistemaAnalisis.DatosAnalisis;
import subsistemaAnalisis.Itf2_DatosInstantaneos;
import subsistemaGestionPacientes.DatosPacientes;

class testAlmacenNV {

	Itf2_DatosInstantaneos subsistema;
	Paciente pacienteRegistrado;
	String NSSvalido;
	String URLvalida;
	@BeforeEach
	void inicio() {
		subsistema = (Itf2_DatosInstantaneos)new DatosAnalisis(new DatosPacientes());
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
	@DisplayName("CP_00009: Generar alarma con medida null")
	void testenviarDatos_009() {
		Alarma respuesta = subsistema.generarAlarma(null, pacienteRegistrado);
		assertNull(respuesta);
	}
	
	@Test
	@DisplayName("CP_00010: Generar alarma con datos normales que no deberian generarla")
	void testGenerarAlarma_010() {
		Float temp = (float) 36.2;
		Float frec = (float)135.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertNull(respuesta);
	}
	
	@Test
	@DisplayName("CP_00013: Generar alarma con datos normales, no deberia saltar")
	void testgenerarAlarma_013() {
		Float temp = (float) 36.5;
		Float frec = (float)145.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertNull(respuesta);
	}
	
	@Test
	@DisplayName("CP_00016: Generar alarma con datos normales, pero paciente nulo")
	void testgenerarAlarma_016() {
		Float temp = (float) 36.5;
		Float frec = (float)145.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma respuesta = subsistema.generarAlarma(medida, null);
		assertNull(respuesta);
	}
	
	@Test
	@DisplayName("CP_00049: Generar alarma con datos normales, temperatura en el limite superior")
	void testgenerarAlarma_049() {
		Float temp = (float) 38.0;
		Float frec = (float)145.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertNull(respuesta);
	}
	
	@Test
	@DisplayName("CP_00050: Generar alarma con datos normales, temperatura en el limite inferior")
	void testgenerarAlarma_050() {
		Float temp = (float) 36.0;
		Float frec = (float)145.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertNull(respuesta);
	}
	
	@Test
	@DisplayName("CP_00053: Generar alarma con datos normales,frecuencia el limite inferior")
	void testgenerarAlarma_053() {
		Float temp = (float) 36.5;
		Float frec = (float)50.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertNull(respuesta);
	}
	
	@Test
	@DisplayName("CP_00055: Generar alarma con datos normales,frecuencia el limite superior")
	void testgenerarAlarma_055() {
		Float temp = (float) 36.5;
		Float frec = (float)132.5;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertNull(respuesta);
	}

	@Test
	@DisplayName("CP_00017: Generar estadistico con  paciente nulo")
	void testGenerarEstadistico_017() {
		Estadistico respuesta = subsistema.generarEstadistico( null);
		assertNull(respuesta);
	}

}
