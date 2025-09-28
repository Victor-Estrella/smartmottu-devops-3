package br.com.fiap.smartmottu.service;

import br.com.fiap.smartmottu.dto.UsuarioRequestDto;
import br.com.fiap.smartmottu.dto.UsuarioResponseDto;
import br.com.fiap.smartmottu.entity.Usuario;
import br.com.fiap.smartmottu.exception.NotFoundException;
import br.com.fiap.smartmottu.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<UsuarioResponseDto> getAll() {
        return repository.findAll()
                .stream()
                .map(UsuarioResponseDto::from)
                .toList();
    }


    public UsuarioResponseDto getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(UsuarioResponseDto::from)
                .orElseThrow(NotFoundException.forUser(id));
    }

    public void delete(@PathVariable Long id) throws RuntimeException {
        if (!repository.existsById(id)) {
            throw new RuntimeException((Throwable) NotFoundException.forUser(id));
        }

        repository.deleteById(id);
    }

    public UsuarioResponseDto save(@Valid @RequestBody UsuarioRequestDto filter) {
        Usuario usuario = Usuario.builder()
                .nome(filter.getNome())
                .email(filter.getEmail())
                .senha(passwordEncoder.encode(filter.getSenha()))
                .build();

        Usuario saved = repository.save(usuario);

        return UsuarioResponseDto.from(saved);
    }

    public UsuarioResponseDto update(Long id, UsuarioRequestDto dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(NotFoundException.forUser(id));

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        return UsuarioResponseDto.from(repository.save(usuario));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> user = repository.findByEmail(username);

        if (user.isPresent()) {
            Usuario usuario = user.get();
            return User.builder()
                    .username(usuario.getEmail())
                    .password(usuario.getSenha())
                    .roles("USER")
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}