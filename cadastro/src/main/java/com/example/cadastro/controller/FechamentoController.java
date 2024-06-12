package com.example.cadastro.controller;

import com.example.cadastro.model.OrdemServico;
import com.example.cadastro.service.OrdemServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FechamentoController {

    private final OrdemServicoService ordemServicoService;

    @Autowired
    public FechamentoController(OrdemServicoService ordemServicoService) {
        this.ordemServicoService = ordemServicoService;

    }

    @GetMapping("/fechamento")
    public String exibirFechamentoForm(Model model) {
        return "fechamento";
    }

    @GetMapping("/gerarFechamento")
    public String gerarFechamento(
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Model model) {
        List<OrdemServico> ordensExecutadas = ordemServicoService.buscarOrdensExecutadas(dataInicio, dataFim);
        ordemServicoService.atualizarStatusOrdens(ordensExecutadas, "fechada");
        // Calcular o lucro bruto, valor por funcionário e lucro do empresário
        BigDecimal lucroBruto = ordemServicoService.calcularLucroBruto(ordensExecutadas).setScale(2, RoundingMode.HALF_UP);
        Map<String, BigDecimal> valorPorFuncionario = ordemServicoService.calcularValorPorFuncionario(ordensExecutadas);
        BigDecimal lucroEmpresario = ordemServicoService.calcularLucroEmpresario(ordensExecutadas).setScale(2, RoundingMode.HALF_UP);

        // Arredondar o valor para cada funcionário
        Map<String, BigDecimal> valorPorFuncionarioArredondado = new HashMap<>();
        for (Map.Entry<String, BigDecimal> entry : valorPorFuncionario.entrySet()) {
            BigDecimal valorArredondado = entry.getValue().setScale(2, RoundingMode.HALF_UP);
            valorPorFuncionarioArredondado.put(entry.getKey(), valorArredondado);
        }

        // Adicionar os valores arredondados ao modelo
        model.addAttribute("ordensExecutadas", ordensExecutadas);
        model.addAttribute("lucroBruto", lucroBruto);
        model.addAttribute("valorPorFuncionario", valorPorFuncionarioArredondado); // Usar os valores arredondados
        model.addAttribute("lucroEmpresario", lucroEmpresario);

        return "relatorioFechamento";
    }
}
