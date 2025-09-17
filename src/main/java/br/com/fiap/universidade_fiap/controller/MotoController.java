package br.com.fiap.universidade_fiap.controller;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.service.MotoService;
import br.com.fiap.universidade_fiap.service.PatioService;
import br.com.fiap.universidade_fiap.service.StatusMotoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/motos")
public class MotoController {

    private final MotoService service;
    private final PatioService patioService;
    private final StatusMotoService statusService;

    public MotoController(MotoService service, PatioService patioService, StatusMotoService statusService) {
        this.service = service;
        this.patioService = patioService;
        this.statusService = statusService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("motos", service.findAll());
        return "moto/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("moto", new Moto());
        model.addAttribute("patios", patioService.findAll());
        model.addAttribute("statusList", statusService.findAll());
        return "moto/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("moto", service.findById(id));
        model.addAttribute("patios", patioService.findAll());
        model.addAttribute("statusList", statusService.findAll());
        return "moto/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("moto") Moto moto,
                         BindingResult br,
                         Model model,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("patios", patioService.findAll());
            model.addAttribute("statusList", statusService.findAll());
            return "moto/form";
        }
        service.save(moto);
        ra.addFlashAttribute("msg", "Moto criada com sucesso!");
        return "redirect:/motos";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("moto") Moto moto,
                         BindingResult br,
                         Model model,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("patios", patioService.findAll());
            model.addAttribute("statusList", statusService.findAll());
            return "moto/form";
        }
        moto.setId(id);
        service.save(moto);
        ra.addFlashAttribute("msg", "Moto atualizada com sucesso!");
        return "redirect:/motos";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("msg", "Moto removida com sucesso!");
        return "redirect:/motos";
    }

    // ===== NOVOS ENDPOINTS: ações na lista =====
    @PostMapping("/{id}/ativar")
    public String ativar(@PathVariable Long id, RedirectAttributes ra) {
        service.alterarStatus(id, "ATIVA");
        ra.addFlashAttribute("msg", "Moto ativada!");
        return "redirect:/motos";
    }

    @PostMapping("/{id}/manutencao")
    public String manutencao(@PathVariable Long id, RedirectAttributes ra) {
        service.alterarStatus(id, "EM_MANUTENCAO");
        ra.addFlashAttribute("msg", "Moto enviada para manutenção!");
        return "redirect:/motos";
    }
}
