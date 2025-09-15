// src/main/java/br/com/fiap/universidade_fiap/controller/LoginController.java
package br.com.fiap.universidade_fiap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "login"; // templates/login.html
    }
}
