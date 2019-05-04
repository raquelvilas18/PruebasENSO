package opera;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class T00_AritTest {

	@Test
	@DisplayName("Pruebo suma CE:positivos enteros")
	void testSuma() {
		// Arrange
		Arit ca = new Arit();
		int actual;
		int esperado = 5;
		
		// Act
		actual=ca.suma(2, 3);
		
		// Assert
		assertEquals(esperado, actual, "La suma de 2 + 3 falla");	
	}

	@Disabled("Todavía sin implementar")
	@Test
	void testResta() {
		fail("Not yet implemented");
	}

}
