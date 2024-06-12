package com.example.cadastro.controller;

import com.example.cadastro.model.ClientePessoaJuridica;
import com.example.cadastro.repository.ClientePessoaJuridicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClientePessoaJuridicaController {

    @Autowired
    private ClientePessoaJuridicaRepository clientePessoaJuridicaRepository;

    @GetMapping("/cliente/pessoa-juridica/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("cliente", new ClientePessoaJuridica());
        return "cadastro_cliente_pj";
    }

    @PostMapping("/cliente/pessoa-juridica/salvar")
    public String salvarCliente(ClientePessoaJuridica cliente) {
        clientePessoaJuridicaRepository.save(cliente);
        return "redirect:/cliente/sucesso";
    }
}
