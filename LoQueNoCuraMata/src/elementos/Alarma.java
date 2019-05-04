package elementos;

public class Alarma {
	private String fecha;
	private String tipo;
	private Float valor;
	
	public Alarma(String fecha, String tipo, Float valor) {
		this.fecha = fecha;
		this.tipo = tipo;
		this.valor = valor;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}
}

