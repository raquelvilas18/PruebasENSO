package opera;

public class Geo implements ItfGeo {
	Arit ca;

	public Geo(Arit ca) {
		super();
		this.ca = ca;
	}

	public Geo() {
		super();
		this.ca=new Arit();
	}

	@Override
	public int mult(int mdr, int mdo) {
		int result=0;
		
		for(int i=0; i<mdr; i++) {
			result=ca.suma(result, mdo);
		}
		return result;
	}

	@Override
	public int div(int ddo, int dsr) throws DivByCero {
		if(dsr==0)
			throw new DivByCero();
		return 0;
	}

}
