package subsistemaAnalisis;
import elementos.Alarma;
import elementos.Estadistico;
import elementos.Medida;
import elementos.Paciente;

public interface Itf2_DatosInstantaneos {
	//Argumentos: recibe una medida enviada desde un sensor, Paciente correspondiente
	//Devolucion: alarma creada si procede (null en caso contrario)
	Alarma generarAlarma(Medida medida, Paciente paciente);
	//Argumentos: Paciente (si el sistema detecta una hora en punto en la lectura, se ejecuta este método)
	//Devolucion: estadistico creado asociado a un paciente
	Estadistico generarEstadistico(Paciente paciente);
}
