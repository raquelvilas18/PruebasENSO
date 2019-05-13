package elementos;

import java.util.HashMap;

public class Estadistico {
	private HashMap<String,Float> datos;
	private String fecha;
	
	//Formato de la fecha: dd-mm-aa;hh:mm:ss
	public Estadistico(HashMap<String, Float> datos, String fecha) {
		this.setDatos(datos);
		this.setFecha(fecha);
	}

	public HashMap<String, Float> getDatos() {
		return datos;
	}

	public void setDatos(HashMap<String, Float> datos) {
		this.datos = datos;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	@Override
	public boolean equals(Object otro) {
		if(otro == null)return false;
		Estadistico otroE = (Estadistico) otro;
		return (otroE.datos.equals(this.datos) && otroE.fecha.equals(this.fecha));
	}
	
}
