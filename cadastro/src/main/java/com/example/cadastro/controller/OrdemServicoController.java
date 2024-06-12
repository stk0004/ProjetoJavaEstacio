package com.example.cadastro.controller;

import com.example.cadastro.model.Funcionario;
import com.example.cadastro.model.OrdemServico;
import com.example.cadastro.model.Servico;
import com.example.cadastro.repository.ClienteRepository;
import com.example.cadastro.repository.FuncionarioRepository;
import com.example.cadastro.repository.OrdemServicoRepository;
import com.example.cadastro.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
public class OrdemServicoController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @GetMapping("/emitirOrdemServico")
    public String emitirOrdemServico(Model model) {
        // Verificar se há pelo menos uma ordem de serviço
        List<OrdemServico> ordensServico = ordemServicoRepository.findAll();
        if (ordensServico.isEmpty()) {
            // Adicionar uma ordem de serviço padrão se não houver outras
            OrdemServico ordemPadrao = ordemServicoRepository.save(criarOrdemServicoPadrao());
            ordensServico = Collections.singletonList(ordemPadrao);
        }

        model.addAttribute("clientes", clienteRepository.findAll());
        // Atualize para obter apenas tipos de serviço únicos
        Set<String> tiposServicos = servicoRepository.findDistinctTipos();
        model.addAttribute("tiposServicos", tiposServicos);
        model.addAttribute("funcionarios", funcionarioRepository.findAll());
        model.addAttribute("ordensServico", ordensServico);
        model.addAttribute("ordemServico", new OrdemServico());
        return "emitirOrdemServico";
    }

    private OrdemServico criarOrdemServicoPadrao() {
        // Aqui você pode criar uma ordem de serviço padrão com valores irrelevantes
        // Por exemplo:
        OrdemServico ordemPadrao = new OrdemServico();
        ordemPadrao.setCliente(clienteRepository.findById(1L).orElse(null)); // Aqui você pode substituir 1L pelo ID de um cliente padrão
        ordemPadrao.setServico(servicoRepository.findById(1L).orElse(null)); // Aqui você pode substituir 1L pelo ID de um serviço padrão
        ordemPadrao.setFuncionario(funcionarioRepository.findById(1L).orElse(null)); // Aqui você pode substituir 1L pelo ID de um funcionário padrão
        ordemPadrao.setDescricao("Ordem de Serviço Padrão");
        ordemPadrao.setValor(BigDecimal.ZERO); // Valor irrelevante
        ordemPadrao.setDataEmissao(LocalDateTime.now()); // Data de emissão atual
        ordemPadrao.setStatus("pendente");
        ordemPadrao.setFormaPagamento("Forma de Pagamento Padrão");
        ordemPadrao.setOrdemPadrao(true); // Marcar como ordem padrão
        return ordemPadrao;

    }

    @PostMapping("/emitirOrdemServico")
    public String emitirOrdemServico(@ModelAttribute OrdemServico ordemServico) {
        ordemServico.setDescricao(ordemServico.getServico().getDescricao());
        ordemServico.setValor(ordemServico.getServico().getValor());
        ordemServico.setDataEmissao(LocalDateTime.now());
        ordemServico.setStatus("pendente");
        ordemServicoRepository.save(ordemServico);
        return "redirect:/emitirOrdemServico";
    }

    @PostMapping("/deleteOrdemServico")
    public String deleteOrdemServico(@RequestParam List<Long> selectedIds) {
        selectedIds.removeIf(id -> ordemServicoRepository.findById(id).orElseThrow().isOrdemPadrao());
        ordemServicoRepository.deleteAllById(selectedIds);
        return "redirect:/emitirOrdemServico";
    }

    @PostMapping("/executarOrdemServico/{id}")
    public String executarOrdemServico(@PathVariable Long id, @RequestParam String formaPagamento) {
        OrdemServico ordemServico = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço inválida:" + id));
        ordemServico.setStatus("executada");
        ordemServico.setFormaPagamento(formaPagamento);
        ordemServicoRepository.save(ordemServico);
        return "redirect:/emitirOrdemServico";
    }

    @PostMapping("/atualizarFormaPagamento/{id}")
    public String atualizarFormaPagamento(@PathVariable Long id, @RequestParam String formaPagamento) {
        OrdemServico ordemServico = ordemServicoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço inválida:" + id));
        ordemServico.setFormaPagamento(formaPagamento);
        ordemServicoRepository.save(ordemServico);
        return "redirect:/emitirOrdemServico";
    }

    @GetMapping("/buscarDescricoesPorTipo")
    @ResponseBody
    public List<Servico> buscarDescricoesPorTipo(@RequestParam String tipo) {
        return servicoRepository.findByTipo(tipo);
    }

    @GetMapping("/buscarFuncionariosPorTipo")
    @ResponseBody
    public List<Funcionario> buscarFuncionariosPorTipo(@RequestParam String tipoServico) {
        // Mapear o tipo de serviço para o cargo correspondente
        String cargo;
        switch (tipoServico) {
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
        // Buscar funcionários com o cargo correspondente
        return funcionarioRepository.findByCargo(cargo);
    }

}