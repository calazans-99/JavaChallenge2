package br.com.fiap.universidade_fiap.controller;

import br.com.fiap.universidade_fiap.model.StatusMoto;
import br.com.fiap.universidade_fiap.service.StatusMotoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/status")
public class StatusController {

    private final StatusMotoService statusService;

    public StatusController(StatusMotoService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("statusList", statusService.findAll());
        return "statusmoto/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("statusMoto", new StatusMoto());
        return "statusmoto/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("statusMoto") StatusMoto statusMoto,
                         BindingResult result) {
        if (result.hasErrors()) return "statusmoto/form";
        statusService.save(statusMoto);
        return "redirect:/status";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("statusMoto", statusService.findById(id));
        return "statusmoto/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("statusMoto") StatusMoto statusMoto,
                         BindingResult result) {
        if (result.hasErrors()) return "statusmoto/form";
        statusMoto.setId(id);
        statusService.save(statusMoto);
        return "redirect:/status";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        statusService.delete(id);
        return "redirect:/status";
    }
}
