package com.example.cadastro.controller;


import com.example.cadastro.model.Usuario;
import com.example.cadastro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro";
    }

    @PostMapping("/salvar")
    public String salvarUsuario(@RequestParam String username, @RequestParam String email, @RequestParam String password, Model model) {
        // Verificar se o usuário ou e-mail já existem
        Usuario existingUser = usuarioRepository.findByUsername(username);
        Usuario existingEmail = usuarioRepository.findByEmail(email);

        if (existingUser != null) {
            model.addAttribute("mensagem", "Nome de usuário já existe");
            return "cadastro";
        }

        if (existingEmail != null) {
            model.addAttribute("mensagem", "E-mail já está cadastrado");
            return "cadastro";
        }

        // Se não existir, salvar o novo usuário
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuarioRepository.save(usuario);
        return "redirect:/usuario/sucesso";
    }

    @GetMapping("/sucesso")
    public String mostrarPaginaSucesso() {
        return "sucesso";
    }

    @GetMapping("/login")
    public String mostrarPaginaLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String fazerLogin(@RequestParam String username, @RequestParam String password, Model model) {
        Usuario usuario = usuarioRepository.findByUsernameAndPassword(username, password);
        if (usuario != null) {
            return "redirect:/principal";
        } else {
            model.addAttribute("mensagem", "Usuário ou senha incorretos");
            return "login";
        }
    }
}
