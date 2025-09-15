package br.com.fiap.universidade_fiap.controller;

import br.com.fiap.universidade_fiap.model.Patio;
import br.com.fiap.universidade_fiap.service.PatioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/patios")
public class PatioController {

    private final PatioService service;

    public PatioController(PatioService service) { this.service = service; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("patios", service.findAll());
        return "patio/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("patio", new Patio());
        return "patio/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("patio", service.findById(id));
        return "patio/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("patio") Patio patio,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) return "patio/form";
        service.save(patio);
        ra.addFlashAttribute("msg", "Pátio criado com sucesso!");
        return "redirect:/patios";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("patio") Patio patio,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) return "patio/form";
        patio.setId(id);
        service.save(patio);
        ra.addFlashAttribute("msg", "Pátio atualizado com sucesso!");
        return "redirect:/patios";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("msg", "Pátio removido com sucesso!");
        return "redirect:/patios";
    }
}
