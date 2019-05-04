package opera;

public class DivByCero extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DivByCero() {
		super("División por Cero");
	}

	public DivByCero(String arg0) {
		super(arg0);
	}

}
