package testAnalisis;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import elementos.Paciente;
import opera.Arit;
import subsistemaAlmacenDatos.ExcepcionDeFechas;
import subsistemaAnalisis.DatosAnalisis;
import subsistemaAnalisis.Itf4_Estadisticas;
import subsistemaGestionPacientes.DatosPacientes;
import subsistemaGestionPacientes.Itf6_DatosPacientes;

@RunWith(MockitoJUnitRunner.class)
class testAnalisisCB {

	@Mock
	DatosPacientes iDatosPacientes;

	@InjectMocks
	DatosAnalisis mock;

	Itf4_Estadisticas subsistema;

	DatosAnalisis subsistemaAnalisis;
	DatosPacientes subPacientes;
	Paciente pac;

	@BeforeEach
	void inicio() {
		subPacientes = new DatosPacientes();
		subsistema = (Itf4_Estadisticas) new DatosAnalisis(subPacientes);

		pac = new Paciente("123456781220", "Ourense", "Miguel Martínez", "12-03-1994");
		subPacientes.darAlta(pac);

	}

	@AfterEach
	void restaurar() {
		File miDir = new File(".");
		String path = miDir.getAbsolutePath().substring(0, miDir.getAbsolutePath().length() - 1);
		String url = "ficheros\\123456781220";
		this.bDirectorio(new File(path + url));
	}


	@Test
	@DisplayName("CP_00032: Solicitar estadistico ->comprobar camino 1-2-3-FIN que debería lanzar una excepción de fechas")
	void testsolicitarEstadistico_001() {
		String fechaInicio = "25-03-2019;15:15:15";
		String fechaFin = "22-02-2019;15:15:15";

		ArrayList<Estadistico> resultado = subsistema.solicitarEstadisticos(pac, fechaInicio, fechaFin);
		assertTrue(resultado.isEmpty());
		subPacientes.eliminar(pac);
	}

	@Test
	@DisplayName("CB_2: comprobar camino 1-2-4-6-7-8-9-12-7-13-FIN generar un arrayList con un único estadístico")
	void testsolicitarEstadistico_002() {

		String fechaInicio = "22-02-2019;15:15:15";
		String fechaFin = "22-03-2019;15:15:15";

		FileWriter fichero;
		try {
			fichero = new FileWriter("ficheros\\" + pac.getnSeguridadSocial() + "\\Estadistico.csv", true);
			PrintWriter pw = new PrintWriter(fichero);
			pw.write("01-03-2019;15:15:15;T;36.7;36.1;36.4;33;34;35;36\n");
			pw.close();

			// Generar estadístico esperado con los datos pasados:
			HashMap<String, Float> datosT = new HashMap<>();
			datosT.put("t_maximo", (float) 36.7);
			datosT.put("t_minimo", (float) 36.1);
			datosT.put("t_media", (float) 36.4);
			datosT.put("t_primerCuartil", (float) 33);
			datosT.put("t_segundoCuartil", (float) 34);
			datosT.put("t_tercerCuartil", (float) 35);
			datosT.put("t_nAlarmas", (float) 36);

			// HashMap<String, Float> datos, String fecha
			Estadistico estadisticoCorrecto = new Estadistico(datosT, "01-03-2019;15:15:15");

			// MOCKEAR EL METODO PARA QUE DEVUELVA UN PACIENTE CON ESTADISTICOS VACIO NO
			// NULO
			pac.setEstadisticos(new ArrayList<>());
			iDatosPacientes = Mockito.mock(DatosPacientes.class);
			Mockito.when(iDatosPacientes.consultarDatosPaciente(Mockito.any())).thenReturn(pac);
			mock = new DatosAnalisis(iDatosPacientes);
			ArrayList<Estadistico> resultado = mock.solicitarEstadisticos(pac, fechaInicio, fechaFin);
			Paciente sistema = subPacientes.consultarDatosPaciente(pac.getnSeguridadSocial());

			assertEquals(resultado.size(), 1);
			assertEquals(resultado, sistema.getEstadisticos());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("CB_4: comprobar camino 1-2-4-5-6-7-8-9-10-11-12-7-13-FIN  generar un arrayList con un único estadístico")
	void testsolicitarEstadistico_004() {

		String fechaInicio = "22-02-2019;15:15:15";
		String fechaFin = "22-03-2019;15:15:15";

		FileWriter fichero;
		try {
			fichero = new FileWriter("ficheros\\" + pac.getnSeguridadSocial() + "\\Estadistico.csv", true);
			PrintWriter pw = new PrintWriter(fichero);
			pw.write("01-03-2019;15:15:15;T;36.7;36.1;36.4;33;34;35;36\n");
			pw.close();
			fichero.close();

			// Generar estadístico esperado con los datos pasados:
			HashMap<String, Float> datosT = new HashMap<>();
			datosT.put("t_maximo", (float) 36.7);
			datosT.put("t_minimo", (float) 36.1);
			datosT.put("t_media", (float) 36.4);
			datosT.put("t_primerCuartil", (float) 33);
			datosT.put("t_segundoCuartil", (float) 34);
			datosT.put("t_tercerCuartil", (float) 35);
			datosT.put("t_nAlarmas", (float) 36);

			// HashMap<String, Float> datos, String fecha
			Estadistico estadisticoCorrecto = new Estadistico(datosT, "01-03-2019;15:15:15");

			// MOCKEAR EL METODO PARA QUE DEVUELVA UN PACIENTE CON ESTADISTICOS  NULL
			pac.setEstadisticos(null);
			iDatosPacientes = Mockito.mock(DatosPacientes.class);
			Mockito.when(iDatosPacientes.consultarDatosPaciente(Mockito.any())).thenReturn(pac);
			mock = new DatosAnalisis(iDatosPacientes);
			ArrayList<Estadistico> resultado = mock.solicitarEstadisticos(pac, fechaInicio, fechaFin);
			Paciente sistema = subPacientes.consultarDatosPaciente(pac.getnSeguridadSocial());

			assertEquals(resultado.size(), 1);
			assertEquals(resultado, sistema.getEstadisticos());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("CB_3: comprobar camino . 1-2-4-5-7-8-9-10-12-7-13-FIN generar un arrayList vacio (paciente con una sola medida asociada posterior al intervalo)")
	void testsolicitarEstadistico_003() {

		String fechaInicio = "22-02-2019;15:15:15";
		String fechaFin = "22-03-2019;15:15:15";

		// Creacion del paciente con la medida dada
		HashMap<String, Float> datosT = new HashMap<>();
		datosT.put("t_maximo", (float) 56.7);
		datosT.put("t_minimo", (float) 36.1);
		datosT.put("t_media", (float) 36.4);
		datosT.put("t_primerCuartil", (float) 33);
		datosT.put("t_segundoCuartil", (float) 34);
		datosT.put("t_tercerCuartil", (float) 35);
		datosT.put("t_nAlarmas", (float) 36);

		// HashMap<String, Float> datos, String fecha
		Estadistico estadisticoCorrecto = new Estadistico(datosT, "03-09-2019;15:15:15");
		ArrayList<Estadistico> estadistico = new ArrayList<>();
		estadistico.add(estadisticoCorrecto);
		pac.setEstadisticos(estadistico);

		iDatosPacientes = Mockito.mock(DatosPacientes.class);
		Mockito.when(iDatosPacientes.consultarDatosPaciente(Mockito.any())).thenReturn(pac);
		mock = new DatosAnalisis(iDatosPacientes);
		ArrayList<Estadistico> resultado = mock.solicitarEstadisticos(pac, fechaInicio, fechaFin);
		assertTrue(resultado.isEmpty());

	}

	@Test
	@DisplayName("CB_5: comprobar camino 1-2-4-5-7-8-9-12-7-8-9-10-12-7-8-9-10-11-12-7-13-FIN  generar un arrayList con un único estadístico")
	void testsolicitarEstadistico_005() {

		String fechaInicio = "22-02-2019;15:15:15";
		String fechaFin = "22-03-2019;15:15:15";

		ArrayList<Estadistico> estadisticosClient = new ArrayList<>();
		// Generar estadístico esperado con los datos pasados:
		HashMap<String, Float> datosT = new HashMap<>();
		datosT.put("t_maximo", (float) 36.7);
		datosT.put("t_minimo", (float) 36.1);
		datosT.put("t_media", (float) 36.4);
		datosT.put("t_primerCuartil", (float) 33);
		datosT.put("t_segundoCuartil", (float) 34);
		datosT.put("t_tercerCuartil", (float) 35);
		datosT.put("t_nAlarmas", (float) 36);

		Estadistico est1 = new Estadistico(datosT, "01-01-2019;15:15:15"); // fecha antes de fecha inicio
		Estadistico est2 = new Estadistico(datosT, "01-09-2019;15:15:15"); // fecha despues de fecha fin
		Estadistico est3 = new Estadistico(datosT, "01-03-2019;15:15:15"); // fecha dentro del intervalo

		estadisticosClient.add(est1);
		estadisticosClient.add(est2);
		estadisticosClient.add(est3);
		pac.setEstadisticos(estadisticosClient);

		Estadistico estadisticoCorrecto = new Estadistico(datosT, "01-03-2019;15:15:15");
		ArrayList<Estadistico> resultadoEsperado = new ArrayList<>();
		resultadoEsperado.add(estadisticoCorrecto);

		iDatosPacientes = Mockito.mock(DatosPacientes.class);
		Mockito.when(iDatosPacientes.consultarDatosPaciente(Mockito.any())).thenReturn(pac);
		mock = new DatosAnalisis(iDatosPacientes);
		ArrayList<Estadistico> resultado = mock.solicitarEstadisticos(pac, fechaInicio, fechaFin);

		assertEquals(1, resultado.size());
		assertEquals(resultado.get(0), resultadoEsperado.get(0));

	}

	@Test
	@DisplayName("CB_6: comprobar camino 1-2-4-5-7-8-9-12-7-8-9-10-12-7-8-9-10-11-12-7-13-FIN  generar un arrayList con un único estadístico")
	void testsolicitarEstadistico_006() {

		String fechaInicio = "22-02-2019;15:15:15";
		String fechaFin = "22-03-2019;15:15:15";

		ArrayList<Estadistico> estadisticosClient = new ArrayList<>();
		// Generar estadístico esperado con los datos pasados:
		HashMap<String, Float> datosT = new HashMap<>();
		datosT.put("t_maximo", (float) 36.7);
		datosT.put("t_minimo", (float) 36.1);
		datosT.put("t_media", (float) 36.4);
		datosT.put("t_primerCuartil", (float) 33);
		datosT.put("t_segundoCuartil", (float) 34);
		datosT.put("t_tercerCuartil", (float) 35);
		datosT.put("t_nAlarmas", (float) 36);

		Estadistico est1 = new Estadistico(datosT, "01-03-2019;15:15:15"); // fecha antes de fecha inicio
		Estadistico est2 = new Estadistico(datosT, "02-03-2019;15:15:15"); // fecha despues de fecha fin
		Estadistico est3 = new Estadistico(datosT, "03-03-2019;15:15:15"); // fecha dentro del intervalo

		estadisticosClient.add(est1);
		estadisticosClient.add(est2);
		estadisticosClient.add(est3);
		pac.setEstadisticos(estadisticosClient);

		iDatosPacientes = Mockito.mock(DatosPacientes.class);
		Mockito.when(iDatosPacientes.consultarDatosPaciente(Mockito.any())).thenReturn(pac);
		mock = new DatosAnalisis(iDatosPacientes);
		ArrayList<Estadistico> resultado = mock.solicitarEstadisticos(pac, fechaInicio, fechaFin);

		assertEquals(3, resultado.size());
		assertEquals(resultado, estadisticosClient);
	}
	
	
	void bDirectorio(File borrar) {
		if (borrar.isDirectory()) {
			try {
				for (File listFile : borrar.listFiles()) {
					if (listFile.isFile()) {
						listFile.delete();
						listFile.deleteOnExit();
					} else {
						if (listFile.isDirectory()) {
							bDirectorio(listFile);
							listFile.delete();
							listFile.deleteOnExit();
						}
					}
				}
			} catch (NullPointerException e) {
				System.out.println(e);
			}
		}
		borrar.delete();
		borrar.deleteOnExit();
	}

}
