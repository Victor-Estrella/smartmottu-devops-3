package br.com.fiap.smartmottu.service;

import br.com.fiap.smartmottu.dto.UsuarioRequestDto;
import br.com.fiap.smartmottu.dto.UsuarioResponseDto;
import br.com.fiap.smartmottu.entity.Usuario;
import br.com.fiap.smartmottu.exception.NotFoundException;
import br.com.fiap.smartmottu.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;


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

    public void delete( @PathVariable Long id) throws RuntimeException {
        if (!repository.existsById(id)) {
            throw new RuntimeException((Throwable) NotFoundException.forUser(id));
        }

        repository.deleteById(id);
    }

    public UsuarioResponseDto save(@Valid @RequestBody UsuarioRequestDto filter) {
        Usuario usuario = Usuario.builder()
                .nome(filter.getNome())
                .email(filter.getEmail())
                .senha(filter.getSenha())
                .build();

        Usuario saved = repository.save(usuario);

        return UsuarioResponseDto.from(saved);
    }

    public UsuarioResponseDto update(Long id, UsuarioRequestDto dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(NotFoundException.forUser(id));

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());

        return UsuarioResponseDto.from(repository.save(usuario));
    }

    public Usuario findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(NotFoundException.forLogin());
    }



}
