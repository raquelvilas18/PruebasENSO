package opera;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class T01_AritFixturesTest {
	// Fistures
	Arit ca;

	@BeforeEach
	void setUp() throws Exception {
		ca = new Arit();
	}

	@AfterEach
	void tearDown() throws Exception {
		ca=null;
	}

	@Test
	void testSuma() {
		// Arrange
		int esperado= 2;
		int real;
		
		//Act
		real=ca.suma(1, 1);
		
		// Assert
		assertEquals(esperado, real, "La suma de 1+1 no ha sido correcta");
	}

	@Test
	void testResta() {
		fail("Not yet implemented");
	}

}
