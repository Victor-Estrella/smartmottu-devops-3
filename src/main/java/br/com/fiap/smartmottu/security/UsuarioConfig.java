package br.com.fiap.smartmottu.security;

import br.com.fiap.smartmottu.entity.Usuario;
import br.com.fiap.smartmottu.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsuarioConfig {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Bean
    UserDetailsService gerarUsuario() {
        return username -> {
            Usuario usuario = usuarioRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base dados"));

            return User.builder()
                    .username(usuario.getNome())
                    .password(usuario.getSenha())
                    .roles("USER")
                    .build();
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
