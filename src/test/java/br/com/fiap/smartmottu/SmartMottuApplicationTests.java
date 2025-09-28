package br.com.fiap.smartmottu;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(exclude = SecurityAutoConfiguration.class)
@ActiveProfiles("test")
class SmartMottuApplicationTests {

	@Test
	void contextLoads() {
	}

}
