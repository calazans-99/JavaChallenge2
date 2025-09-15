package br.com.fiap.universidade_fiap.controller;

import br.com.fiap.universidade_fiap.model.Sensor;
import br.com.fiap.universidade_fiap.service.PatioService;
import br.com.fiap.universidade_fiap.service.SensorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sensores")
public class SensorController {

    private final SensorService service;
    private final PatioService patioService;

    public SensorController(SensorService service, PatioService patioService) {
        this.service = service;
        this.patioService = patioService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("sensores", service.findAll());
        return "sensor/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("sensor", new Sensor());
        model.addAttribute("patios", patioService.findAll());
        return "sensor/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("sensor", service.findById(id));
        model.addAttribute("patios", patioService.findAll());
        return "sensor/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("sensor") Sensor sensor,
                         BindingResult br,
                         Model model,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("patios", patioService.findAll());
            return "sensor/form";
        }
        service.save(sensor);
        ra.addFlashAttribute("msg", "Sensor criado com sucesso!");
        return "redirect:/sensores";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("sensor") Sensor sensor,
                         BindingResult br,
                         Model model,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("patios", patioService.findAll());
            return "sensor/form";
        }
        sensor.setId(id);
        service.save(sensor);
        ra.addFlashAttribute("msg", "Sensor atualizado com sucesso!");
        return "redirect:/sensores";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("msg", "Sensor removido com sucesso!");
        return "redirect:/sensores";
    }
}
