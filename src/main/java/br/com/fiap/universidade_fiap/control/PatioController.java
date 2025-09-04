package br.com.fiap.universidade_fiap.control;

import br.com.fiap.universidade_fiap.model.Patio;
import br.com.fiap.universidade_fiap.repository.PatioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patios")
public class PatioController {

    private final PatioRepository patioRepo;

    public PatioController(PatioRepository patioRepo) {
        this.patioRepo = patioRepo;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("patios", patioRepo.findAll());
        return "patio/list";
    }

    @GetMapping("/new")
    public String novo(Model model) {
        model.addAttribute("patio", new Patio());
        return "patio/form";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) {
        var patio = patioRepo.findById(id).orElseThrow(() -> new RuntimeException("Pátio não encontrado"));
        model.addAttribute("patio", patio);
        return "patio/form";
    }

    @PostMapping("/save")
    public String salvar(@ModelAttribute Patio patio) {
        patioRepo.save(patio);
        return "redirect:/patios";
    }

    @GetMapping("/delete/{id}")
    public String deletar(@PathVariable Long id) {
        patioRepo.deleteById(id);
        return "redirect:/patios";
    }
}