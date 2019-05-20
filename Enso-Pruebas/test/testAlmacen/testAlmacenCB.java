package testAlmacen;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import elementos.Alarma;
import elementos.Estadistico;
import elementos.Medida;
import elementos.Paciente;
import subsistemaAlmacenDatos.DatosMedidas;
import subsistemaAlmacenDatos.Itf3_SeriesTemporales;
import subsistemaAnalisis.DatosAnalisis;
import subsistemaAnalisis.Itf2_DatosInstantaneos;
import subsistemaGestionPacientes.DatosPacientes;

public class testAlmacenCB {

	Itf2_DatosInstantaneos subsistema;
	Itf3_SeriesTemporales almacen;
	Paciente pacienteRegistrado;
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
	}
	
	@AfterEach
	void limpiar() {
		DatosPacientes subsistemaPacientes = new DatosPacientes();
		subsistemaPacientes.eliminar(pacienteRegistrado);
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
