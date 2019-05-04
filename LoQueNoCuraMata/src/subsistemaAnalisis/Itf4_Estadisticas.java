package subsistemaAnalisis;
import java.util.ArrayList;

import elementos.Estadistico;
import elementos.Paciente;

public interface Itf4_Estadisticas {
	//Argumentos: paciente sobre el que se solicitan sus estadisticos y rango de fechas
	//Devolucion: ArrayList con los estadisticos almacenados del paciente
	ArrayList<Estadistico> solicitarEstadisticos(Paciente paciente, String fechaInicio, String fechaFin);
}
