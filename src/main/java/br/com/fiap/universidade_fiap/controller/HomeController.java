package br.com.fiap.universidade_fiap.controller;

import br.com.fiap.universidade_fiap.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserRepository userRepo;

    public HomeController(UserRepository userRepo) { this.userRepo = userRepo; }

    @GetMapping("/")
    public String root() { return "redirect:/index"; }

    @GetMapping("/index")
    public String index(@AuthenticationPrincipal UserDetails principal, Model model) {
        if (principal != null) {
            userRepo.findByUsername(principal.getUsername())
                    .ifPresent(u -> model.addAttribute("usuario", u));
        }
        return "index";
    }
}
