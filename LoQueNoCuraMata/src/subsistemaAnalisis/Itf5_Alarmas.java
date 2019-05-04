package subsistemaAnalisis;
import java.util.ArrayList;

import elementos.Alarma;
import elementos.Paciente;

public interface Itf5_Alarmas {
	//Argumentos: paciente sobre el que se solicitan sus alertas
	//Devolucion: ArrayList con las alarmas almacenadas del paciente
	ArrayList<Alarma> verAlarmas(Paciente paciente);
}
