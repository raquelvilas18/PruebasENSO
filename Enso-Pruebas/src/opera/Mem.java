package opera;

public class Mem {
	int m;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + m;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mem other = (Mem) obj;
		if (m != other.m)
			return false;
		return true;
	}

	public Mem() {
		m=0;
	}
	
	void mS(int m) {
		this.m=m;
	}

}
