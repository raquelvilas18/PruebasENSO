package opera;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class T04_parametrizados {
	Arit ca;

	@BeforeEach
	void setUp() throws Exception {
		ca=new Arit();
	}

	@AfterEach
	void tearDown() throws Exception {
		ca=null;
	}

	@ParameterizedTest
	@CsvSource({"8,5,3","5,3,2","3,2,1"})
	void testResta(int min, int sus, int esperado) {
		assertEquals(esperado, ca.resta(min, sus));
	}
	
	@ParameterizedTest
	@CsvSource({"1,2,3","2,3,5","3,5,8"})
	void testSuma(int s1, int s2, int esperado) {
		assertEquals(esperado, ca.suma(s1, s2));
	}

}
