package br.com.fiap.universidade_fiap.controller;

import br.com.fiap.universidade_fiap.model.StatusMoto;
import br.com.fiap.universidade_fiap.service.StatusMotoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/status-moto")
public class StatusMotoController {

    private final StatusMotoService service;

    public StatusMotoController(StatusMotoService service) { this.service = service; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("statusList", service.findAll());
        return "statusmoto/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("statusMoto", new StatusMoto());
        return "statusmoto/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("statusMoto", service.findById(id));
        return "statusmoto/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("statusMoto") StatusMoto s,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) return "statusmoto/form";
        service.save(s);
        ra.addFlashAttribute("msg", "Status criado com sucesso!");
        return "redirect:/status-moto";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("statusMoto") StatusMoto s,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) return "statusmoto/form";
        s.setId(id);
        service.save(s);
        ra.addFlashAttribute("msg", "Status atualizado com sucesso!");
        return "redirect:/status-moto";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("msg", "Status removido com sucesso!");
        return "redirect:/status-moto";
    }
}
