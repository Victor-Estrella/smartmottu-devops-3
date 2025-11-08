package br.com.fiap.smartmottu.service;

import br.com.fiap.smartmottu.dto.UsuarioRequestDto;
import br.com.fiap.smartmottu.dto.UsuarioResponseDto;
import br.com.fiap.smartmottu.entity.Usuario;
import br.com.fiap.smartmottu.entity.enuns.RoleEnum;
import br.com.fiap.smartmottu.exception.NotFoundException;
import br.com.fiap.smartmottu.repository.AluguelRepository;
import br.com.fiap.smartmottu.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AluguelRepository aluguelRepository;


    public List<UsuarioResponseDto> getAll() {
        return repository.findAll()
                .stream()
                .map(usuario -> {
                    boolean temAluguel = aluguelRepository.existsByUsuario_IdUsuario(usuario.getIdUsuario());

                    return UsuarioResponseDto.from(usuario, temAluguel);

                })

                .toList();
    }

    public UsuarioResponseDto getById(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(NotFoundException.forUser(id));

        boolean temAluguel = aluguelRepository.existsByUsuario_IdUsuario(usuario.getIdUsuario());

        return UsuarioResponseDto.from(usuario, temAluguel);
    }

    public void delete(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(NotFoundException.forUser(id));

        boolean temAluguel = aluguelRepository.existsByUsuario_IdUsuario(usuario.getIdUsuario());

        if (temAluguel) {
            throw new RuntimeException("Usuário não pode ser excluído pois possui aluguéis vinculados.");
        }

        repository.delete(usuario);
    }

    public UsuarioResponseDto save(@Valid UsuarioRequestDto filter) {
        Usuario usuario = Usuario.builder()

                .nome(filter.getNome())
                .email(filter.getEmail())
                .senha(passwordEncoder.encode(filter.getSenha()))
                .role(RoleEnum.USER)
                .build();

        Usuario saved = repository.save(usuario);

        return UsuarioResponseDto.from(saved);
    }

    public UsuarioResponseDto update(Long id, UsuarioRequestDto dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(NotFoundException.forUser(id));

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        usuario.setRole(RoleEnum.USER);

        return UsuarioResponseDto.from(repository.save(usuario));
    }


    public UsuarioResponseDto findByEmail(String email) {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(NotFoundException.forLogin());
        return UsuarioResponseDto.from(usuario);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> user = repository.findByEmail(username);

        if (user.isPresent()) {
            Usuario usuario = user.get();
            return User.builder()
                    .username(usuario.getEmail())
                    .password(usuario.getSenha())
                    .roles(usuario.getRole().name())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
