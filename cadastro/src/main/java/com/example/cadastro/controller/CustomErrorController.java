package com.example.cadastro.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // Redireciona para a página de login em caso de erro
        return "redirect:/usuario/login";
    }

    public String getErrorPath() {
        return "/error";
    }
}

