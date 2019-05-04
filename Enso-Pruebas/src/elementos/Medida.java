package elementos;

public class Medida {
	private Float temperatura;
	private Float ritmo;
	private String fecha;
	
	public Medida(Float temperatura, Float ritmo, String fecha) {
		this.temperatura = temperatura;
		this.ritmo = ritmo;
		this.fecha = fecha;
	}

	public Float getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(Float temperatura) {
		this.temperatura = temperatura;
	}

	public Float getRitmo() {
		return ritmo;
	}

	public void setRitmo(Float ritmo) {
		this.ritmo = ritmo;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
}
