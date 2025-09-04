package br.com.fiap.universidade_fiap.control;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import br.com.fiap.universidade_fiap.repository.PatioRepository;
import br.com.fiap.universidade_fiap.repository.StatusMotoRepository;
import br.com.fiap.universidade_fiap.service.MotoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/motos")
public class MotoController {

    private final MotoRepository motoRepo;
    private final PatioRepository patioRepo;
    private final StatusMotoRepository statusRepo;
    private final MotoService motoService;

    public MotoController(MotoRepository motoRepo, PatioRepository patioRepo,
                          StatusMotoRepository statusRepo, MotoService motoService) {
        this.motoRepo = motoRepo;
        this.patioRepo = patioRepo;
        this.statusRepo = statusRepo;
        this.motoService = motoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("motos", motoRepo.findAll());
        return "moto/list";
    }

    @GetMapping("/new")
    public String novo(Model model) {
        model.addAttribute("moto", new Moto());
        model.addAttribute("patios", patioRepo.findAll());
        model.addAttribute("statuses", statusRepo.findAll());
        return "moto/form";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) {
        var moto = motoRepo.findById(id).orElseThrow(() -> new RuntimeException("Moto não encontrada"));
        model.addAttribute("moto", moto);
        model.addAttribute("patios", patioRepo.findAll());
        model.addAttribute("statuses", statusRepo.findAll());
        return "moto/form";
    }

    @PostMapping("/save")
    public String salvar(@ModelAttribute Moto moto) {
        motoService.salvarComValidacao(moto);
        return "redirect:/motos";
    }

    @GetMapping("/delete/{id}")
    public String deletar(@PathVariable Long id) {
        motoRepo.deleteById(id);
        return "redirect:/motos";
    }
}