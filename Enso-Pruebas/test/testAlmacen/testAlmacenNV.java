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

class testAlmacenNV {

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
		Alarma respuesta = subsistema.generarAlarma(null, pacienteRegistrado);
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
	@DisplayName("CP_00081: Generar alarma con datos normales,frecuencia 220 y temperatura 40.5")
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
	@DisplayName("CP_00082: Generar alarma con datos normales,frecuencia 47 y temperatura 40.5")
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
	@DisplayName("CP_00083: Generar alarma con datos normales,frecuencia 35.5 y temperatura 220")
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
	@DisplayName("CP_00084: Generar alarma con datos normales,frecuencia 47 y temperatura 35.5")
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
	@DisplayName("CP_00087: Generar alarma con datos normales,frecuencia 60 y temperatura 40.5")
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
	@DisplayName("CP_00088: Generar alarma con datos normales,frecuencia 49 y temperatura 36.5")
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
	@DisplayName("CP_00089: Generar alarma con datos normales,frecuencia 60 y temperatura 35.0")
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
	@DisplayName("CP_00090: Generar alarma con datos normales,frecuencia 220 y temperatura 36.5")
	void testgenerarAlarma_090() {
		Float temp = (float) 36.5;
		Float frec = (float)220.0;
		Medida medida = new Medida(temp, frec, "12-03-2019");
		Alarma respuesta = subsistema.generarAlarma(medida, pacienteRegistrado);
		assertTrue(respuesta.getTipo().equals("F"), "La alarma no es de tipo frecuencia");
		assertTrue(respuesta.getValor()==220.0, "La frecuencia no es la esperada");
		assertTrue(respuesta.getFecha().equals("12-03-2019"), "La fecha no es la esperada");
	}

	@Test
	@DisplayName("CP_00017: Generar estadistico con  paciente nulo")
	void testGenerarEstadistico_017() {
		Estadistico respuesta = subsistema.generarEstadistico( null);
		assertNull(respuesta);
	}
	
	@Test
	@DisplayName("CP_00020: Solicitar medidas entre dos fechas con paciente eliminado")
	void testSolicitarMedidasEntreDosFechas_020() {
		String fechaInicio = "30-2-2019";
		String fechaFin = "30-3-2019";
		
		// Eliminar paciente
		DatosPacientes subsistemaPacientes= new DatosPacientes();
		subsistemaPacientes.eliminar(pacienteRegistrado);
		
		assertNull(almacen.solicitarMedidas(pacienteRegistrado, fechaInicio, fechaFin));
	}
	
	@Test
	@DisplayName("CP_00021: Solicitar medidas entre dos fechas con fecha de inicio nula")
	void testSolicitarMedidasEntreDosFechas_021() {
		String fechaInicio = null;
		String fechaFin = "30-3-2019";
		assertNull(almacen.solicitarMedidas(pacienteRegistrado, fechaInicio, fechaFin));
	}
	
	@Test
	@DisplayName("CP_00022: Solicitar medidas entre dos fechas con fecha de inicio más tarde que la fecha de fin")
	void testSolicitarMedidasEntreDosFechas_022() {
		String fechaInicio = "30-3-2019";
		String fechaFin = "30-2-2019";
		assertNull(almacen.solicitarMedidas(pacienteRegistrado, fechaInicio, fechaFin));
	}
	
	@Test
	@DisplayName("CP_00023: Solicitar medidas entre dos fechas con paciente nulo y fecha de inicio inválida")
	void testSolicitarMedidasEntreDosFechas_023() {
		String fechaInicio = "16/15/2019";
		String fechaFin = "30-2-2019";
		assertNull(almacen.solicitarMedidas(null, fechaInicio, fechaFin));
	}
	
	@Test
	@DisplayName("CP_00024: Solicitar medidas entre dos fechas con paciente nulo y fechas equivalentes")
	void testSolicitarMedidasEntreDosFechas_024() {
		String fechaInicio = "30-2-2019";
		String fechaFin = "30-2-2019";
		assertNull(almacen.solicitarMedidas(null, fechaInicio, fechaFin));
	}
	
	@Test
	@DisplayName("CP_00025: Solicitar medidas entre dos fechas con fechas nulas")
	void testSolicitarMedidasEntreDosFechas_025() {
		String fechaInicio = null;
		String fechaFin = null;
		assertNull(almacen.solicitarMedidas(pacienteRegistrado, fechaInicio, fechaFin));
	}
	
	@Test
	@DisplayName("CP_00026: Solicitar medidas entre dos fechas con paciente y fechas nulos")
	void testSolicitarMedidasEntreDosFechas_026() {
		String fechaInicio = null;
		String fechaFin = null;
		assertNull(almacen.solicitarMedidas(null, fechaInicio, fechaFin));
	}
	
	@Test
	@DisplayName("CP_00028: Solicitar medidas desde una fecha con paciente nulo")
	void testSolicitarMedidasDesdeUnaFecha_028() {
		String fechaInicio = "30-2-2019";
		assertNull(almacen.solicitarMedidas(null, fechaInicio));
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "12-12-678", "34-03-2019" })
	void testSolicitarMedidasDesdeUnaFecha_029(String fecha) {
		assertNull(almacen.solicitarMedidas(pacienteRegistrado, fecha));
	}
	
	@Test
	@DisplayName("CP_00030: Solicitar medidas desde una fecha con paciente nunca registrado y fecha nula")
	void testSolicitarMedidasDesdeUnaFecha_030() {
		String fechaInicio = null;
		Paciente pacienteNuncaRegistrado = pacienteRegistrado = new Paciente("581234537840", "Santiago de Compostela", "Mario Rodriguez Alvarez", "27-06-1978");
		assertNull(almacen.solicitarMedidas(pacienteNuncaRegistrado, fechaInicio));
	}
	
	@Test
	@DisplayName("CP_00062: Generar estadístico con paciente sin medidas")
	void testGenerarEstadistico_062() {
		pacienteRegistrado.setMedidas(new ArrayList<>());
		Estadistico respuesta = subsistema.generarEstadistico(pacienteRegistrado);
		
		// Debe ser nulo porque el paciente no tiene medidas, por lo tanto no se instancia el Estadistico
		assertNull(respuesta);
	}

}
