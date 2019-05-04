package b3_validosAlmacen;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import elementos.Alarma;
import elementos.Estadistico;
import elementos.Medida;
import elementos.Paciente;
import subsistemaAlmacenDatos.DatosMedidas;
import subsistemaAlmacenDatos.Itf1_DatosSimulados;
import subsistemaAnalisis.DatosAnalisis;
import subsistemaAnalisis.Itf2_DatosInstantaneos;
import subsistemaGestionPacientes.DatosPacientes;
import subsistemaSensorizacion.DatosSensores;

class testAlmacenV {

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
	@DisplayName("CP_00011: Generar alarma con datos de hipotermia")
	void testgenerarAlarma_011() {
		Float temp = (float) 34;
		Float frec = (float)135.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma alarmaEsperada = new Alarma("12-03-2019", "T", temp);
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertSame(respuesta.getFecha(), alarmaEsperada.getFecha());
		assertSame(respuesta.getTipo(), alarmaEsperada.getTipo());
		assertSame(respuesta.getValor(), alarmaEsperada.getValor());
	}
	
	@Test
	@DisplayName("CP_00012: Generar alarma con datos de fiebre")
	void testgenerarAlarma_012() {
		Float temp = (float) 39.2;
		Float frec = (float)135.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma alarmaEsperada = new Alarma("12-03-2019", "T", temp);
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertSame(respuesta.getFecha(), alarmaEsperada.getFecha());
		assertSame(respuesta.getTipo(), alarmaEsperada.getTipo());
		assertSame(respuesta.getValor(), alarmaEsperada.getValor());
	}
	
	@Test
	@DisplayName("CP_00014: Generar alarma por frecuencia muy baja")
	void testgenerarAlarma_014() {
		Float temp = (float) 36.5;
		Float frec = (float)49.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma alarmaEsperada = new Alarma("12-03-2019", "F", frec);
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertSame(respuesta.getFecha(), alarmaEsperada.getFecha());
		assertSame(respuesta.getTipo(), alarmaEsperada.getTipo());
		assertSame(respuesta.getValor(), alarmaEsperada.getValor());
	}

	@Test
	@DisplayName("CP_00015: Generar alarma por frecuencia muy alta")
	void testgenerarAlarma_015() {
		Float temp = (float) 36.5;
		Float frec = (float)235.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma alarmaEsperada = new Alarma("12-03-2019", "F", frec);
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertSame(respuesta.getFecha(), alarmaEsperada.getFecha());
		assertSame(respuesta.getTipo(), alarmaEsperada.getTipo());
		assertSame(respuesta.getValor(), alarmaEsperada.getValor());
	}
	
	@Test
	@DisplayName("CP_00051: Generar alarma por temperatura justo inferior al limite")
	void testgenerarAlarma_051() {
		Float temp = (float) 35.99;
		Float frec = (float)235.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma alarmaEsperada = new Alarma("12-03-2019", "T", temp);
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertSame(respuesta.getFecha(), alarmaEsperada.getFecha());
		assertSame(respuesta.getTipo(), alarmaEsperada.getTipo());
		assertSame(respuesta.getValor(), alarmaEsperada.getValor());
	}
	
	@Test
	@DisplayName("CP_00052: Generar alarma por temperatura justo superior al limite")
	void testgenerarAlarma_052() {
		Float temp = (float) 38.01;
		Float frec = (float)235.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma alarmaEsperada = new Alarma("12-03-2019", "T", temp);
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertSame(respuesta.getFecha(), alarmaEsperada.getFecha());
		assertSame(respuesta.getTipo(), alarmaEsperada.getTipo());
		assertSame(respuesta.getValor(), alarmaEsperada.getValor());
	}
	
	@Test
	@DisplayName("CP_00054: Generar alarma por frecuencia justo inferior al limite")
	void testgenerarAlarma_054() {
		Float temp = (float) 36.5;
		Float frec = (float)49.99;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma alarmaEsperada = new Alarma("12-03-2019", "F", frec);
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertSame(respuesta.getFecha(), alarmaEsperada.getFecha());
		assertSame(respuesta.getTipo(), alarmaEsperada.getTipo());
		assertSame(respuesta.getValor(), alarmaEsperada.getValor());
	}
	
	@Test
	@DisplayName("CP_00056: Generar alarma por frecuencia justo superior al limite")
	void testgenerarAlarma_056() {
		Float temp = (float) 36.5;
		Float frec = (float)132.5;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma alarmaEsperada = new Alarma("12-03-2019", "F", frec);
		Paciente pacientePrueba = new Paciente("1234567899", "OurenseGalicia", "Maria Mar Alvarez", "01-01-1964");

		Alarma respuesta = subsistema.generarAlarma(medida, pacientePrueba);
		assertSame(respuesta.getFecha(), alarmaEsperada.getFecha());
		assertSame(respuesta.getTipo(), alarmaEsperada.getTipo());
		assertSame(respuesta.getValor(), alarmaEsperada.getValor());
	}
	
	@Test
	@DisplayName("CP_00018: Generar estadistico valido, con paciente registrado")
	void testGenerarEstadistico_017() {
		DatosSensores generador = new DatosSensores();
		generador.simularDatosSensores(2, pacienteRegistrado.getnSeguridadSocial());
		Estadistico respuesta = subsistema.generarEstadistico( pacienteRegistrado);
		for(String dato :respuesta.getDatos().keySet()) {
			System.out.println(dato +"----" + respuesta.getDatos().get(dato));
		}
		//assertNull(respuesta);
	}
}
