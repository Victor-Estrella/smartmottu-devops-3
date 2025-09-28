package br.com.fiap.smartmottu.controller;

import br.com.fiap.smartmottu.dto.UsuarioRequestDto;
import br.com.fiap.smartmottu.dto.UsuarioResponseDto;
import br.com.fiap.smartmottu.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public String listUsers(Model model) {
        var responses = service.getAll();
        model.addAttribute("users", responses);
        return "list";
    }

    @GetMapping("/new")
    public String newUsersForm(Model model) {
        model.addAttribute("user", new UsuarioRequestDto());
        return "form";
    }

    @PostMapping
    public String saveUser(@ModelAttribute("user") UsuarioRequestDto dto) {
        service.save(dto);
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        UsuarioResponseDto response = service.getById(id);

        UsuarioRequestDto dto = new UsuarioRequestDto();
        dto.setNome(response.getNome());
        dto.setEmail(response.getEmail());
        dto.setSenha(response.getSenha());

        model.addAttribute("user", dto);
        model.addAttribute("id", id);

        return "form";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("user") UsuarioRequestDto dto) {
        service.update(id, dto);
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/users";
    }
}