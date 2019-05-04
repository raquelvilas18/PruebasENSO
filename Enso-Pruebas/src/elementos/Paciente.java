package elementos;

import java.util.ArrayList;

public class Paciente {
	private String nSeguridadSocial;
	private String direccion;
	private String nombreAp;
	private String fechaNacimiento;
	private ArrayList<Estadistico> estadisticos;
	private ArrayList<Alarma> alarmas;
	private ArrayList<Medida> medidas;
	
	public Paciente() {
		this.estadisticos = new ArrayList<>();
		this.alarmas = new ArrayList<>();
		this.medidas = new ArrayList<>();
	}
	
	public Paciente(String nSeguridadSocial, String direccion, String nombreAp, String fechaNacimiento) {
		this.setnSeguridadSocial(nSeguridadSocial);
		this.setDireccion(direccion);
		this.setNombreAp(nombreAp);
		this.setFechaNacimiento(fechaNacimiento);
		this.estadisticos = new ArrayList<>();
		this.alarmas = new ArrayList<>();
		this.medidas = new ArrayList<>();
	}
	
	public Paciente(String nSeguridadSocial, String direccion, String nombreAp, String fechaNacimiento,
			ArrayList<Estadistico> estadisticos, ArrayList<Alarma> alarmas, ArrayList<Medida> medidas) {
		this.setnSeguridadSocial(nSeguridadSocial);
		this.setDireccion(direccion);
		this.setNombreAp(nombreAp);
		this.setFechaNacimiento(fechaNacimiento);
		this.setEstadisticos(estadisticos);
		this.setAlarmas(alarmas);
		this.setMedidas(medidas);
	}

	public String getnSeguridadSocial() {
		return nSeguridadSocial;
	}

	public void setnSeguridadSocial(String nSeguridadSocial) {
		this.nSeguridadSocial = nSeguridadSocial;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNombreAp() {
		return nombreAp;
	}

	public void setNombreAp(String nombreAp) {
		this.nombreAp = nombreAp;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public ArrayList<Estadistico> getEstadisticos() {
		return estadisticos;
	}

	public void setEstadisticos(ArrayList<Estadistico> estadisticos) {
		this.estadisticos = estadisticos;
	}

	public ArrayList<Alarma> getAlarmas() {
		return alarmas;
	}

	public void setAlarmas(ArrayList<Alarma> alarmas) {
		this.alarmas = alarmas;
	}

	public ArrayList<Medida> getMedidas() {
		return medidas;
	}

	public void setMedidas(ArrayList<Medida> medidas) {
		this.medidas = medidas;
	}
}
