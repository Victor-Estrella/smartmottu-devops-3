package br.com.fiap.smartmottu.controller;

import br.com.fiap.smartmottu.dto.MotoRequestDto;
import br.com.fiap.smartmottu.dto.MotoResponseDto;
import br.com.fiap.smartmottu.entity.enuns.StatusEnum;
import br.com.fiap.smartmottu.entity.enuns.TipoMotoEnum;
import br.com.fiap.smartmottu.repository.TipoMotoRepository;
import br.com.fiap.smartmottu.service.MotoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/motos")
public class MotoController {

    @Autowired
    private MotoService service;

    @Autowired
    private TipoMotoRepository tipoMotoRepository;

    @GetMapping
    public String listMotos(Model model) {
        var motos = service.getAll();
        model.addAttribute("motos", motos);
        return "list-moto";
    }

    @GetMapping("/new")
    public String newMotosForm(Model model) {
        model.addAttribute("moto", new MotoRequestDto());
        model.addAttribute("modeloList", TipoMotoEnum.values());
        model.addAttribute("statusList", StatusEnum.values());
        return "form-moto";
    }

    @PostMapping
    public String saveMoto(@Valid @ModelAttribute("moto") MotoRequestDto dto) {
        service.save(dto);
        return "redirect:/motos";
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

        return "form-moto";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("moto") MotoRequestDto dto) {
        service.update(id, dto);
        return "redirect:/motos";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/motos";
    }
}
