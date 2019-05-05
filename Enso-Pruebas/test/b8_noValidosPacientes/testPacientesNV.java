package b8_noValidosPacientes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import elementos.Paciente;
import subsistemaAnalisis.DatosAnalisis;
import subsistemaAnalisis.Itf2_DatosInstantaneos;
import subsistemaGestionPacientes.DatosPacientes;
import subsistemaGestionPacientes.Itf6_DatosPacientes;

class testPacientesNV {
	Itf6_DatosPacientes subsistema;
	Paciente pacienteRegistrado;
	String NSSvalido;
	String URLvalida;
	
	@BeforeEach
	void inicio() {
		subsistema = (Itf6_DatosPacientes)new DatosPacientes();
		NSSvalido = "281234567840";
		URLvalida = "ficheros/"+NSSvalido+"/medidas.csv";
		pacienteRegistrado = new Paciente(NSSvalido, "Santiago de chile5", "Juan Rodriguez Alvarez", "28-07-1998");
		DatosPacientes subsistemaPacientes= new DatosPacientes();
		subsistemaPacientes.darAlta(pacienteRegistrado);
	}
	
	@AfterEach
	void restaurar() {
		subsistema.eliminar(pacienteRegistrado);
	}

}
