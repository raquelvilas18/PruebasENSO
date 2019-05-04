package opera;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class T03_otrosTest {
	Geo gc;

	@BeforeEach
	void setUp() throws Exception {
		gc = new Geo();
	}

	@AfterEach
	void tearDown() throws Exception {
		gc=null;
	}

	@Tag("Basico")
	@Test
	void testDiv() {
		Throwable exc = assertThrows(DivByCero.class, ()->{gc.div(5,0);});
		assertEquals("División por Cero",exc.getMessage());
	}
	
	@Tag("Tiempo")
	@Nested
	@DisplayName("Pruebas de Rendimiento")
	class Detiempo {
	@Test
	@DisplayName("Prueba rendimiento total")
	void porTiempo() {
		assertTimeout(Duration.ofSeconds(1), ()->{Thread.sleep(2000);});
	}
	
	@Test
	@DisplayName("Prueba rendimiento con límite")
	void porTiempoStop() {
		assertTimeoutPreemptively(Duration.ofSeconds(1), ()->{Thread.sleep(2000);});
	}
	}
	
	@Tag("multiple")
	@Test
	@DisplayName("Prueba Múltíple")
	void pruebaMultiple() {
		assertAll("Prueba de multiples cosas",
			       () -> assertTrue(true,"Esperaba que esto fuera True"),
			       () -> assertTrue(false,"Esperaba que esto fuera True"),
			       () -> assertEquals(1, 1),
			       () -> assertEquals(1, 2),
			       () -> assertEquals(5, 2));

	}
	
}
