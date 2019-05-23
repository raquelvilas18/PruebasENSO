package testAlmacen;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import elementos.Estadistico;
import elementos.Medida;
import elementos.Paciente;
import opera.Arit;
import subsistemaAlmacenDatos.DatosMedidas;
import subsistemaAlmacenDatos.ExcepcionDeFechas;
import subsistemaAlmacenDatos.Itf3_SeriesTemporales;
import subsistemaAnalisis.DatosAnalisis;
import subsistemaAnalisis.Itf2_DatosInstantaneos;
import subsistemaAnalisis.Itf4_Estadisticas;
import subsistemaGestionPacientes.DatosPacientes;
import subsistemaGestionPacientes.Itf6_DatosPacientes;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import elementos.Alarma;
public class testAlmacenCB {

	Itf2_DatosInstantaneos subsistema;
	Itf3_SeriesTemporales almacen;
	Paciente pacienteRegistrado;
	Paciente pacienteRegistrado2;
	String NSSvalido;
	String URLvalida;
	@BeforeEach
	void inicio() {
		subsistema = (Itf2_DatosInstantaneos)new DatosAnalisis(new DatosPacientes());
		almacen = (Itf3_SeriesTemporales) new DatosMedidas(new DatosPacientes());
		NSSvalido = "281234567840";
		URLvalida = "ficheros/"+NSSvalido+"/medidas.csv";
		pacienteRegistrado = new Paciente(NSSvalido, "Santiago de chile5", "Miguel Martinez Muras", "28-07-1998");
		DatosPacientes subsistemaPacientes= new DatosPacientes();
		subsistemaPacientes.darAlta(pacienteRegistrado);
		pacienteRegistrado2 = new Paciente("123456781221","Ourense", "Miguel Mart�nez", "12-03-1994");
		subsistemaPacientes.darAlta(pacienteRegistrado2);
	}
	
	@AfterEach
	void limpiar() {
		DatosPacientes subsistemaPacientes = new DatosPacientes();
		subsistemaPacientes.eliminar(pacienteRegistrado);
		subsistemaPacientes.eliminar(pacienteRegistrado2);
	}
	
	@Test
	@DisplayName("CP_00076: Generar estadístico por el camino 1-2-3-4-7-2-8-10-11-FIN, devuelve null")
	void testGenerarEstadistico_076() {
		ArrayList<Medida> medidas = new ArrayList<>();
		
		// Fechas anteriores a la actual: solo se utilizan las medidas tomadas en la hora actual
		medidas.add(new Medida(36.2f, 80.0f, "30-2-2019;12:34:56"));
		medidas.add(new Medida(36.4f, 85.0f, "30-2-2019;12:39:28"));
		
		pacienteRegistrado2.setMedidas(medidas);
		assertNull(subsistema.generarEstadistico(pacienteRegistrado2));
	}

	@Test
	@DisplayName("CP_00077: Generar estadístico por el camino 1-2-8-10-11-FIN, devuelve null")
	void testGenerarEstadistico_077() {
		pacienteRegistrado2.setMedidas(new ArrayList<>());
		assertNull(subsistema.generarEstadistico(pacienteRegistrado2));
	}
	
	@Test
	@DisplayName("CP_00078: Generar estadístico por el camino 1-2-3-4-5-6-7-2-8-9-11-FIN, devuelve ArrayList")
	void testGenerarEstadistico_078() {
		ArrayList<Medida> medidas = new ArrayList<>();
		
		// Medidas situadas en la hora actual
		Date fechaActual = new Date();
		String fecha = new SimpleDateFormat("dd-MM-yyyy;kk:mm:ss").format(fechaActual);
		medidas.add(new Medida(36.4f, 85.0f, fecha));
		
		pacienteRegistrado2.setMedidas(medidas);
		assertNotNull(subsistema.generarEstadistico(pacienteRegistrado2));
	}
	
	@Test
	@DisplayName("CP_00079: Generar estadístico por el camino 1-2-3-4-5-7-2-8-10-11-FIN, devuelve null")
	void testGenerarEstadistico_079() {
		ArrayList<Medida> medidas = new ArrayList<>();
		
		// Fechas posteriores a la actual pero también a la hora actual: solo se utilizan las medidas tomadas en la hora actual
		medidas.add(new Medida(36.2f, 80.0f, "30-2-2029;12:34:56"));
		medidas.add(new Medida(36.4f, 85.0f, "30-2-2029;12:39:28"));
		
		pacienteRegistrado2.setMedidas(medidas);
		assertNull(subsistema.generarEstadistico(pacienteRegistrado2));
	}
	
	@Test
	@DisplayName("CP_00080: Generar estadístico por el camino 1-2-3-4-5-7-2-3-4-5-6-7-2-8-9-11-FIN, devuelve null")
	void testGenerarEstadistico_080() {
		ArrayList<Medida> medidas = new ArrayList<>();
		
		// Una fecha que no cumple las condiciones y otra que sí
		medidas.add(new Medida(36.4f, 85.0f, "30-2-2029;12:39:28"));
		Date fechaActual = new Date();
		String fecha = new SimpleDateFormat("dd-MM-yyyy;kk:mm:ss").format(fechaActual);
		medidas.add(new Medida(36.4f, 85.0f, fecha));
		
		pacienteRegistrado2.setMedidas(medidas);
		assertNotNull(subsistema.generarEstadistico(pacienteRegistrado2));
	}
	
	@Test
	@DisplayName("CP_00081: Camino: 1-2-3-4-5-6-7-8-F,frecuencia 220 y temperatura 40.5")
	void testgenerarAlarma_081() {
		Float temp = (float) 40.5;
		Float frec = (float)220.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertTrue(respuesta.getTipo().equals("F"), "La alarma no es de tipo frecuencia");
		assertTrue(respuesta.getTipo().equals("T"), "La alarma no es de tipo temperatura");
		assertTrue(respuesta.getValor()==220.0, "La frecuencia no es la esperada");
		assertTrue(respuesta.getValor()==40.5, "La temperatura no es la esperada");
		assertTrue(respuesta.getFecha().equals("12-03-2019"), "La fecha no es la esperada");
	}
	
	@Test
	@DisplayName("CP_00082: Camino: 1-2-3-4-5-6-8-F,frecuencia 47 y temperatura 40.5")
	void testgenerarAlarma_082() {
		Float temp = (float) 40.5;
		Float frec = (float)47.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertTrue(respuesta.getTipo().equals("F"), "La alarma no es de tipo frecuencia");
		assertTrue(respuesta.getTipo().equals("T"), "La alarma no es de tipo temperatura");
		assertTrue(respuesta.getValor()==47.0, "La frecuencia no es la esperada");
		assertTrue(respuesta.getValor()==40.5, "La temperatura no es la esperada");
		assertTrue(respuesta.getFecha().equals("12-03-2019"), "La fecha no es la esperada");
	}
	
	@Test
	@DisplayName("CP_00083: Camino: 1-2-3-5-6-8-F,frecuencia 35.5 y temperatura 220")
	void testgenerarAlarma_083() {
		Float temp = (float) 35.5;
		Float frec = (float)220.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertTrue(respuesta.getTipo().equals("F"), "La alarma no es de tipo frecuencia");
		assertTrue(respuesta.getTipo().equals("T"), "La alarma no es de tipo temperatura");
		assertTrue(respuesta.getValor()==220.0, "La frecuencia no es la esperada");
		assertTrue(respuesta.getValor()==35.5, "La temperatura no es la esperada");
		assertTrue(respuesta.getFecha().equals("12-03-2019"), "La fecha no es la esperada");
	}
	
	@Test
	@DisplayName("CP_00084: Camino: 1-2-3-5-6-8-F,frecuencia 47 y temperatura 35.5")
	void testgenerarAlarma_084() {
		Float temp = (float) 35.5;
		Float frec = (float)47.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertTrue(respuesta.getTipo().equals("F"), "La alarma no es de tipo frecuencia");//Cambiar esto a una respuesta correcta que deberian ser dos alarmas
		assertTrue(respuesta.getTipo().equals("T"), "La alarma no es de tipo temperatura");
		assertTrue(respuesta.getValor()==47.0, "La frecuencia no es la esperada");
		assertTrue(respuesta.getValor()==35.5, "La temperatura no es la esperada");
		assertTrue(respuesta.getFecha().equals("12-03-2019"), "La fecha no es la esperada");
	}
	
	@Test
	@DisplayName("CP_00087: I-2-3-5-6-7-F,frecuencia 60 y temperatura 40.5")
	void testgenerarAlarma_087() {
		Float temp = (float) 40.5;
		Float frec = (float)60.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertTrue(respuesta.getTipo().equals("T"), "La alarma no es de tipo temperatura");
		assertTrue(respuesta.getValor()==40.5, "La temperatura no es la esperada");
		assertTrue(respuesta.getFecha().equals("12-03-2019"), "La fecha no es la esperada");
	}
	
	@Test
	@DisplayName("CP_00088: Camino: I-2-3-4-6-8-F,frecuencia 49 y temperatura 36.5")
	void testgenerarAlarma_088() {
		Float temp = (float) 36.5;
		Float frec = (float)49.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertTrue(respuesta.getTipo().equals("F"), "La alarma no es de tipo frecuencia");
		assertTrue(respuesta.getValor()==49.0, "La frecuencia no es la esperada");
		assertTrue(respuesta.getFecha().equals("12-03-2019"), "La fecha no es la esperada");
	}
	
	@Test
	@DisplayName("CP_00089: I-2-3-5-6-7-8-F,frecuencia 60 y temperatura 35.0")
	void testgenerarAlarma_089() {
		Float temp = (float) 35.0;
		Float frec = (float)60.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertTrue(respuesta.getTipo().equals("T"), "La alarma no es de tipo temperatura");
		assertTrue(respuesta.getValor()==35.0, "La temperatura no es la esperada");
		assertTrue(respuesta.getFecha().equals("12-03-2019"), "La fecha no es la esperada");
	}
	
	@Test
	@DisplayName("CP_00090: Camino: 1-2-3-4-6-7-8-F,frecuencia 220 y temperatura 36.5")
	void testgenerarAlarma_090() {
		Float temp = (float) 36.5;
		Float frec = (float)220.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertTrue(respuesta.getTipo().equals("F"), "La alarma no es de tipo frecuencia");
		assertTrue(respuesta.getValor()==220.0, "La frecuencia no es la esperada");
		assertTrue(respuesta.getFecha().equals("12-03-2019"), "La fecha no es la esperada");
	}
	
}
