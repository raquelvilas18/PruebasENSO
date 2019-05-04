package noInterfazGrafica;

import elementos.Paciente;
import subsistemaAlmacenDatos.DatosMedidas;
import subsistemaAlmacenDatos.Itf1_DatosSimulados;
import subsistemaAlmacenDatos.Itf3_SeriesTemporales;
import subsistemaAnalisis.DatosAnalisis;
import subsistemaAnalisis.Itf4_Estadisticas;
import subsistemaAnalisis.Itf5_Alarmas;
import subsistemaGestionPacientes.DatosPacientes;
import subsistemaGestionPacientes.Itf6_DatosPacientes;
import subsistemaSensorizacion.DatosSensores;

import java.util.ArrayList;

import elementos.Alarma;
import elementos.Estadistico;
import elementos.Medida;

public class Main {

	public static void main(String[] args) {
		DatosSensores datosSensores = new DatosSensores();		
		Itf6_DatosPacientes itfPacientes;
		DatosPacientes datosPacientes = new DatosPacientes();
		itfPacientes = (Itf6_DatosPacientes) datosPacientes;
		
		Itf1_DatosSimulados i1 = (Itf1_DatosSimulados) new DatosMedidas(datosPacientes);
		Itf3_SeriesTemporales itfSeriesTemporales = (Itf3_SeriesTemporales) new DatosMedidas(datosPacientes);
		Itf4_Estadisticas itfEstadisticas = (Itf4_Estadisticas) new DatosAnalisis(datosPacientes);
		Itf5_Alarmas itfAlarmas = (Itf5_Alarmas) new DatosAnalisis(datosPacientes);
		ArrayList<Medida> medidas = new ArrayList<>();
		datosSensores.simularDatosSensores(50, "1290192019");
		Paciente paciente = new Paciente("1290192019", "avda castelao", "carlos rial", "20-02-1998");
		itfPacientes.darAlta(paciente);
	
		i1.leerDatos(".\\ficheros\\" + paciente.getnSeguridadSocial() + "\\datosSimulados.csv", medidas);


		i1.enviarDatos(medidas, paciente.getnSeguridadSocial());
		for(Estadistico e : itfEstadisticas.solicitarEstadisticos(paciente, "28-04-2019;21:00:00", "28-04-2019;23:00:00"))
			System.out.println(e.getFecha());

		Paciente paciente2 = new Paciente("100000000000", "avda", "jose guerra", "03-09-1998");
		itfPacientes.darAlta(paciente2);

		ArrayList<Medida> medidas2 = new ArrayList<>();
		medidas2.add(new Medida(36.1f,151.4f,"28-04-2019;22:30:00"));
		i1.enviarDatos(medidas2, paciente.getnSeguridadSocial());
		paciente2.setDireccion("qweqwe");
		itfPacientes.actualizar(paciente2);
		for(Medida m : itfSeriesTemporales.solicitarMedidas(paciente, "27-04-2019;22:30:00")){
			System.out.println(m.getTemperatura());
		}
		for(Alarma m : itfAlarmas.verAlarmas(paciente)){
			System.out.println(m.getValor() + m.getTipo());
		}
		
	    itfPacientes.eliminar(paciente2);
	}

}
