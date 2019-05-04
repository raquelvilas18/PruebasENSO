package subsistemaAlmacenDatos;
import java.util.ArrayList;

import elementos.Medida;

public interface Itf1_DatosSimulados {
	//Argumentos: URLArchivo, Arraylist vacio que se irá llenando con los datos leidos.
	//Devolucion: String que indica el NSeguridadSocial del paciente que tiene esas medidas.
	String leerDatos(String URLArchivo, ArrayList<Medida> medidas);
	//Argumentos: ArrayList con las medidas tomadas, NSS del paciente
	//Devolucion: boolean que indica si funcionó o no
	boolean enviarDatos(ArrayList<Medida> medidas, String NSS);
}
