package com.example.cadastro.controller;

import com.example.cadastro.model.Servico;
import com.example.cadastro.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/servico")
public class ServicoController {

    @Autowired
    private ServicoRepository servicoRepository;

    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        Servico servico = new Servico();
        model.addAttribute("servico", servico);
        return "cadastro_servico";
    }

    @PostMapping("/salvar")
    public String salvarServico(@ModelAttribute("servico") Servico servico) {
        determinarCargo(servico);
        servicoRepository.save(servico);
        return "redirect:/servico/listar";
    }

    @GetMapping("/listar")
    public String listarServicos(Model model) {
        List<Servico> servicos = servicoRepository.findAll();
        model.addAttribute("servicos", servicos);
        return "lista_servicos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        Optional<Servico> servico = servicoRepository.findById(id);
        if (servico.isPresent()) {
            model.addAttribute("servico", servico.get());
            return "editar_servico";
        }
        return "redirect:/servico/listar";
    }

    @PostMapping("/atualizar")
    public String atualizarServico(@ModelAttribute("servico") Servico servico) {
        determinarCargo(servico);
        servicoRepository.save(servico);
        return "redirect:/servico/listar";
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirServico(@PathVariable Long id) {
        Optional<Servico> servicoOptional = servicoRepository.findById(id);
        if (servicoOptional.isPresent()) {
            servicoRepository.delete(servicoOptional.get());
            return ResponseEntity.ok("Serviço excluído com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    private void determinarCargo(Servico servico) {
        String cargo;
        switch (servico.getTipo()) {
            case "Manicure":
                cargo = "Manicure";
                break;
            case "Cabelo":
                cargo = "Cabelereiro(a)";
                break;
            case "Depilação":
                cargo = "Depilador(a)";
                break;
            default:
                cargo = "";
                break;
        }
        servico.setCargo(cargo);
    }
}
