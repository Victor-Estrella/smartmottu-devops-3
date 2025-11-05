package br.com.fiap.smartmottu.controller;

import br.com.fiap.smartmottu.dto.MotoRequestDto;
import br.com.fiap.smartmottu.dto.MotoResponseDto;
import br.com.fiap.smartmottu.entity.enuns.StatusEnum;
import br.com.fiap.smartmottu.entity.enuns.TipoMotoEnum;
import br.com.fiap.smartmottu.service.MotoService;
import br.com.fiap.smartmottu.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/motos")
public class MotoController {

    @Autowired
    private MotoService service;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listMotos(Model model, Principal principal) {
        var motos = service.getAll();
        model.addAttribute("motos", motos);
        return "list-moto";
    }


    @GetMapping("/new")
    public String newMotosForm(Model model) {
        model.addAttribute("moto", new MotoRequestDto());

        model.addAttribute("statusList", StatusEnum.values());
        model.addAttribute("modeloList", TipoMotoEnum.values());
        return "form-moto";
    }


    @PostMapping
    public String saveMoto(@Valid @ModelAttribute("moto") MotoRequestDto dto) {
        service.save(dto);
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        MotoResponseDto response = service.getById(id);

        MotoRequestDto dto = new MotoRequestDto();
        dto.setNmChassi(response.getNmChassi());
        dto.setPlaca(response.getPlaca());
        dto.setUnidade(response.getUnidade());
        dto.setStatusId(response.getStatusId());
        dto.setModeloId(response.getModeloId());

        model.addAttribute("moto", dto);
        model.addAttribute("id", id);

        model.addAttribute("statusList", StatusEnum.values());
        model.addAttribute("modeloList", TipoMotoEnum.values());

        return "form-moto";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("moto") MotoRequestDto dto) {
        service.update(id, dto);
        return "redirect:/";
    }

//    @DeleteMapping("/{id}")
//    public String deleteMoto(@PathVariable Long id) {
//        try {
//            service.delete(id);
//            return "redirect:/motos";
//
//        } catch (RuntimeException e) {
//
//            // Opção 1: Redirecionar para a lista com uma mensagem de erro
//            return "redirect:/motos?error=" + e.getMessage();
//
//            // Opção 2: Redirecionar para uma página de erro dedicada
//            // return "erro-exclusao";
//        }
//    }
}
