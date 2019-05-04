package subsistemaGestionPacientes;
import elementos.Paciente;

public interface Itf6_DatosPacientes {
	//Argumentos: datos encapsulados en la clase paciente
	//Devolucion: true si funcionó, false si no 
	boolean darAlta(Paciente paciente);
	boolean actualizar(Paciente paciente);
	boolean eliminar(Paciente paciente);
	//Argumentos: String con el numero de seguridad social
	//Devolucion: datos del paciente
	Paciente consultarDatosPaciente(String NSS);
}
