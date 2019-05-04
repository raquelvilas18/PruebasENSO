package opera;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class T05_mock {
	Arit ca;
	Geo cg;

	@BeforeEach
	void setUp() throws Exception {
		ca=Mockito.mock(Arit.class);
		cg=new Geo(ca);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testMult() {
		//Defino comportamientos
		Mockito.when(ca.suma(Mockito.anyInt(), Mockito.anyInt())).thenReturn(5).thenReturn(10).thenReturn(15);
		
		// Act y verificación estática
		assertEquals(15, cg.mult(3, 5));
		
		// Verificación dinámica, ¿Cuántas interacciones hubo con el mock?
		Mockito.verify(ca, Mockito.times(3)).suma(Mockito.anyInt(), Mockito.anyInt());
	}

}
