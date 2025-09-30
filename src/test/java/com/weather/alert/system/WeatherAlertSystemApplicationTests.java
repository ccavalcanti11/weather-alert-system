package com.weather.alert.system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class WeatherAlertSystemApplicationTests {

	@Test
	void contextLoads() {
		// This test verifies that the Spring Boot application context loads successfully
		// with all configurations and beans properly initialized
	}

}
