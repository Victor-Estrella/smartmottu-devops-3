package br.com.fiap.smartmottu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.rsocket.RSocketSecurityAutoConfiguration;

@SpringBootApplication(exclude = {RSocketSecurityAutoConfiguration.class})
public class SafehubApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafehubApplication.class, args);
	}
}