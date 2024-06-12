package com.example.cadastro.controller;

import com.example.cadastro.model.Usuario;
import com.example.cadastro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/principal")
    public String paginaPrincipal() {
        return "principal";
    }

    @GetMapping("/login")
    public String mostrarPaginaLogin() {
        return "login";
    }

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public String fazerLogin(@RequestParam String username, @RequestParam String password, Model model) {
        Usuario usuario = usuarioRepository.findByUsernameAndPassword(username, password);
        if (usuario != null) {
            return "redirect:/principal";
        } else {
            model.addAttribute("mensagem", "Usuário ou Senha incorretos!");
            return "login";
        }
    }
}
