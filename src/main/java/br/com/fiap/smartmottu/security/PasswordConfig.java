package br.com.fiap.smartmottu.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Suporta hashes {bcrypt} para novos usuários e {noop} para dados seed em desenvolvimento
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}