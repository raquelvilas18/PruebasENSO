package opera;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class T07_mockInjectEnOrden {
	Arit ca;
	
	@InjectMocks
	Geo cg;

	@BeforeEach
	void setUp() throws Exception {
		ca=Mockito.mock(Arit.class);
		MockitoAnnotations.initMocks(this);
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
		InOrder enOrden=Mockito.inOrder(ca);
		enOrden.verify(ca).suma( 0, 5);
		enOrden.verify(ca).suma( 5, 5);
		enOrden.verify(ca).suma(10, 5);
		
		Mockito.verify(ca, Mockito.times(3)).suma(Mockito.anyInt(), Mockito.anyInt());	}

}
