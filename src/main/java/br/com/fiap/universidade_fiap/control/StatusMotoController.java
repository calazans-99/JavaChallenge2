package br.com.fiap.universidade_fiap.control;

import br.com.fiap.universidade_fiap.model.StatusMoto;
import br.com.fiap.universidade_fiap.repository.StatusMotoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/status")
public class StatusMotoController {

    private final StatusMotoRepository statusRepo;

    public StatusMotoController(StatusMotoRepository statusRepo) {
        this.statusRepo = statusRepo;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("statuses", statusRepo.findAll());
        return "status/list";
    }

    @GetMapping("/new")
    public String novo(Model model) {
        model.addAttribute("statusMoto", new StatusMoto());
        return "status/form";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) {
        var st = statusRepo.findById(id).orElseThrow(() -> new RuntimeException("Status não encontrado"));
        model.addAttribute("statusMoto", st);
        return "status/form";
    }

    @PostMapping("/save")
    public String salvar(@ModelAttribute StatusMoto statusMoto) {
        statusRepo.save(statusMoto);
        return "redirect:/status";
    }

    @GetMapping("/delete/{id}")
    public String deletar(@PathVariable Long id) {
        statusRepo.deleteById(id);
        return "redirect:/status";
    }
}