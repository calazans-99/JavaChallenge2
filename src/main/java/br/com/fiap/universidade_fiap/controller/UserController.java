package br.com.fiap.universidade_fiap.controller;

import br.com.fiap.universidade_fiap.model.User;
import br.com.fiap.universidade_fiap.repository.FuncaoRepository;
import br.com.fiap.universidade_fiap.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UserController {

    private final UserService service;
    private final FuncaoRepository funcaoRepo;

    public UserController(UserService service, FuncaoRepository funcaoRepo) {
        this.service = service;
        this.funcaoRepo = funcaoRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("usuarios", service.findAll());
        return "usuario/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("usuario", new User());
        model.addAttribute("funcoes", funcaoRepo.findAll());
        return "usuario/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", service.findById(id));
        model.addAttribute("funcoes", funcaoRepo.findAll());
        return "usuario/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("usuario") User usuario,
                         BindingResult br,
                         Model model,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("funcoes", funcaoRepo.findAll());
            return "usuario/form";
        }
        service.create(usuario); // encode de senha dentro do service
        ra.addFlashAttribute("msg", "Usuário criado com sucesso!");
        return "redirect:/usuarios";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("usuario") User usuario,
                         BindingResult br,
                         @RequestParam(value = "alterarSenha", required = false) Boolean alterarSenha,
                         Model model,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("funcoes", funcaoRepo.findAll());
            return "usuario/form";
        }
        boolean trocaSenha = Boolean.TRUE.equals(alterarSenha)
                && usuario.getSenha() != null && !usuario.getSenha().isBlank();

        service.update(id, usuario, trocaSenha);
        ra.addFlashAttribute("msg", "Usuário atualizado com sucesso!");
        return "redirect:/usuarios";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("msg", "Usuário removido com sucesso!");
        return "redirect:/usuarios";
    }
}
