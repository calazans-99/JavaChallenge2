package br.com.fiap.universidade_fiap.control;

import br.com.fiap.universidade_fiap.model.Sensor;
import br.com.fiap.universidade_fiap.repository.SensorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sensors")
public class SensorController {

    private final SensorRepository sensorRepo;

    public SensorController(SensorRepository sensorRepo) {
        this.sensorRepo = sensorRepo;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("sensors", sensorRepo.findAll());
        return "sensor/list";
    }

    @GetMapping("/new")
    public String novo(Model model) {
        model.addAttribute("sensor", new Sensor());
        return "sensor/form";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) {
        var sensor = sensorRepo.findById(id).orElseThrow(() -> new RuntimeException("Sensor não encontrado"));
        model.addAttribute("sensor", sensor);
        return "sensor/form";
    }

    @PostMapping("/save")
    public String salvar(@ModelAttribute Sensor sensor) {
        sensorRepo.save(sensor);
        return "redirect:/sensors";
    }

    @GetMapping("/delete/{id}")
    public String deletar(@PathVariable Long id) {
        sensorRepo.deleteById(id);
        return "redirect:/sensors";
    }
}