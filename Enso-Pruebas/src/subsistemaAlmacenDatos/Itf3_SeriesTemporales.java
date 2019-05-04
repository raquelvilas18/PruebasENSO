package subsistemaAlmacenDatos;
import java.util.ArrayList;

import elementos.Medida;
import elementos.Paciente;

public interface Itf3_SeriesTemporales {
	//Argumentos: paciente sobre el que se solicitan sus medidas y rango de fechas
	//Devolucion: ArrayList con las medidas almacenadas del paciente
	ArrayList<Medida> solicitarMedidas(Paciente paciente, String fechaInicio, String fechaFin);
	//Similar al anterior, pero devuelve medidas desde un momento hasta el actual
	ArrayList<Medida> solicitarMedidas(Paciente paciente, String fechaInicio);
}
