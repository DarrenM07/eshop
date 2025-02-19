package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EshopApplicationTests {

	@Test
	void contextLoads() {
		// This test ensures the Spring application context loads successfully.
	}

	@Test
	void testMain() {
		// Invoking the main method to increase code coverage.
		EshopApplication.main(new String[]{});
	}
}
