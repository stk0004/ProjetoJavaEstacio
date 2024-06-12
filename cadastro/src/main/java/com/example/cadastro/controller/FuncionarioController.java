package com.example.cadastro.controller;

import com.example.cadastro.model.Funcionario;
import com.example.cadastro.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class FuncionarioController {


    @Autowired
    private FuncionarioRepository funcionarioRepository;


    @GetMapping("/funcionario/cadastro")
    public String mostrarFormularioCadastroFuncionario(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        return "cadastro_funcionario";
    }

    @PostMapping("/funcionario/salvar")
    public String salvarFuncionario(Funcionario funcionario) {
        funcionarioRepository.save(funcionario);
        return "redirect:/funcionario/sucesso";
    }

    @GetMapping("/funcionario/sucesso")
    public String mostrarPaginaSucessoFuncionario() {
        return "sucesso_funcionario";
    }

}


