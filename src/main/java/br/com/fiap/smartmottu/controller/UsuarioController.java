package br.com.fiap.smartmottu.controller;

import br.com.fiap.smartmottu.dto.UsuarioRequestDto;
import br.com.fiap.smartmottu.dto.UsuarioResponseDto;
import br.com.fiap.smartmottu.entity.enuns.RoleEnum;
import br.com.fiap.smartmottu.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public String listUsers(Model model, Principal principal) {
        String email = principal.getName();

        UsuarioResponseDto usuarioLogado = service.findByEmail(email);

        if (usuarioLogado.getRole().equals(RoleEnum.ADMIN)) {
            model.addAttribute("users", service.getAll());
        } else {
            model.addAttribute("users", List.of(usuarioLogado));
        }

        return "user-list";
    }


    @GetMapping("/new")
    public String newUsersForm(Model model) {
        model.addAttribute("usuario", new UsuarioRequestDto());
        return "user-form";
    }

    @PostMapping
    public String saveUser(@ModelAttribute("usuario") UsuarioRequestDto dto) {
        service.save(dto);
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        UsuarioResponseDto response = service.getById(id);

        UsuarioRequestDto dto = new UsuarioRequestDto();
        dto.setNome(response.getNome());
        dto.setEmail(response.getEmail());

        model.addAttribute("usuario", dto);
        model.addAttribute("id", id);

        return "user-form";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("usuario") UsuarioRequestDto dto) {
        service.update(id, dto);
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, Principal principal, HttpServletResponse response, HttpServletRequest request) {
        UsuarioResponseDto usuarioLogado = service.findByEmail(principal.getName());
        Long idUsuarioLogado = usuarioLogado.getIdUsuario();

        service.delete(id);

        if (id.equals(idUsuarioLogado)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }

            return "redirect:/logout";
        }

        return "redirect:/users";
    }

}

