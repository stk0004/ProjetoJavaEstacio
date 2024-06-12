package com.example.cadastro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.cadastro.model.ClientePessoaFisica;
import com.example.cadastro.repository.ClientePessoaFisicaRepository;

@Controller
public class ClientePessoaFisicaController {

    @Autowired
    private ClientePessoaFisicaRepository clientePessoaFisicaRepository;

    @GetMapping("/cliente/pessoa-fisica/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("cliente", new ClientePessoaFisica());
        return "cadastro_cliente_pf";
    }

    @PostMapping("/cliente/pessoa-fisica/salvar")
    public String salvarCliente(ClientePessoaFisica cliente) {
        clientePessoaFisicaRepository.save(cliente);
        return "redirect:/cliente/sucesso";
    }
}
