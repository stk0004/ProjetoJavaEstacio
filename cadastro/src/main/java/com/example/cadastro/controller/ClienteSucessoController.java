package com.example.cadastro.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClienteSucessoController {

    @GetMapping("/cliente/sucesso")
    public String mostrarPaginaSucesso() {
        return "sucesso_cliente";
    }
}


