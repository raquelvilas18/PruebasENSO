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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

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

@RunWith(MockitoJUnitRunner.class)
class testAlmacenCB {

	Itf4_Estadisticas subsistema;

	DatosAnalisis subsistemaAnalisis;
	DatosPacientes subPacientes;
	Paciente pac;
	Paciente pacienteRegistrado2;
	Itf2_DatosInstantaneos subsistemaDatosInstantaneos;
	Itf3_SeriesTemporales subsistema2;

	@BeforeEach
	void inicio() {
		subPacientes = new DatosPacientes();
		subsistema = (Itf4_Estadisticas) new DatosAnalisis(subPacientes);
		subsistema2 = new DatosMedidas(subPacientes);
		pac = new Paciente("123456781220", "Ourense", "Miguel Mart�nez", "12-03-1994");
		subPacientes.darAlta(pac);
		subsistemaDatosInstantaneos = (Itf2_DatosInstantaneos)new DatosAnalisis(subPacientes);
		pacienteRegistrado2 = new Paciente("123456781221","Ourense", "Miguel Mart�nez", "12-03-1994");
		subPacientes.darAlta(pacienteRegistrado2);

	}

	@AfterEach
	void restaurar() {
		subPacientes.eliminar(pac);
		subPacientes.eliminar(pacienteRegistrado2);
	}

	@Test
	@DisplayName("CP_00076: Generar estadístico por el camino 1-2-3-4-7-2-8-10-11-FIN, devuelve null")
	void testGenerarEstadistico_076() {
		ArrayList<Medida> medidas = new ArrayList<>();
		
		// Fechas anteriores a la actual: solo se utilizan las medidas tomadas en la hora actual
		medidas.add(new Medida(36.2f, 80.0f, "30-2-2019;12:34:56"));
		medidas.add(new Medida(36.4f, 85.0f, "30-2-2019;12:39:28"));
		
		pacienteRegistrado2.setMedidas(medidas);
		assertNull(subsistemaDatosInstantaneos.generarEstadistico(pacienteRegistrado2));
	}

	@Test
	@DisplayName("CP_00077: Generar estadístico por el camino 1-2-8-10-11-FIN, devuelve null")
	void testGenerarEstadistico_077() {
		pacienteRegistrado2.setMedidas(new ArrayList<>());
		assertNull(subsistemaDatosInstantaneos.generarEstadistico(pacienteRegistrado2));
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
		assertNotNull(subsistemaDatosInstantaneos.generarEstadistico(pacienteRegistrado2));
	}
	
	@Test
	@DisplayName("CP_00079: Generar estadístico por el camino 1-2-3-4-5-7-2-8-10-11-FIN, devuelve null")
	void testGenerarEstadistico_079() {
		ArrayList<Medida> medidas = new ArrayList<>();
		
		// Fechas posteriores a la actual pero también a la hora actual: solo se utilizan las medidas tomadas en la hora actual
		medidas.add(new Medida(36.2f, 80.0f, "30-2-2029;12:34:56"));
		medidas.add(new Medida(36.4f, 85.0f, "30-2-2029;12:39:28"));
		
		pacienteRegistrado2.setMedidas(medidas);
		assertNull(subsistemaDatosInstantaneos.generarEstadistico(pacienteRegistrado2));
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
		assertNotNull(subsistemaDatosInstantaneos.generarEstadistico(pacienteRegistrado2));
	}

}
