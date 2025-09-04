package br.com.fiap.universidade_fiap.control;

import br.com.fiap.universidade_fiap.model.User;
import br.com.fiap.universidade_fiap.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public UserController(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "user/list";
    }

    @GetMapping("/new")
    public String novo(Model model) {
        model.addAttribute("user", new User());
        return "user/form";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) {
        var user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        model.addAttribute("user", user);
        return "user/form";
    }

    @PostMapping("/save")
    public String salvar(@ModelAttribute User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deletar(@PathVariable Long id) {
        userRepo.deleteById(id);
        return "redirect:/users";
    }
}