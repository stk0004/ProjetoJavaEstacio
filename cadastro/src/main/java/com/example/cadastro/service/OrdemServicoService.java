package com.example.cadastro.service;

import com.example.cadastro.model.OrdemServico;
import com.example.cadastro.repository.FuncionarioRepository;
import com.example.cadastro.repository.OrdemServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class OrdemServicoService {

    private final OrdemServicoRepository ordemServicoRepository;
    private final FuncionarioRepository funcionarioRepository; // Supondo que você tenha um repositório para Funcionário

    // Mapeamento de porcentagens por cargo
    private final Map<String, BigDecimal> porcentagensPorCargo = new HashMap<>();

    // Inicialização das porcentagens para cada cargo
    {
        porcentagensPorCargo.put("Manicure", BigDecimal.valueOf(50));
        porcentagensPorCargo.put("Cabelereiro(a)", BigDecimal.valueOf(40));
        porcentagensPorCargo.put("Depilador(a)", BigDecimal.valueOf(60));
    }

    // Mapeamento de descontos por funcionário
    private final Map<String, BigDecimal> descontosPorFuncionario = new HashMap<>();
    // Inicialização dos descontos (você pode ajustar esses valores conforme necessário)
    {
        descontosPorFuncionario.put("Manicure", BigDecimal.TEN); // Exemplo de desconto fixo de R$ 10,00
        descontosPorFuncionario.put("Cabelereiro(a)", BigDecimal.valueOf(0)); // Exemplo de desconto percentual de 10%
        descontosPorFuncionario.put("Depilador(a)", BigDecimal.ZERO); // Exemplo de nenhum desconto para este cargo
    }

    // Mapeamento de taxas por forma de pagamento
    private final Map<String, BigDecimal> taxasPorFormaPagamento = new HashMap<>();
    // Inicialização das taxas para cada forma de pagamento
    {
        taxasPorFormaPagamento.put("Cartão de Crédito", BigDecimal.valueOf(2.99)); // Taxa de 5% para pagamento com cartão de crédito
        taxasPorFormaPagamento.put("Débito", BigDecimal.valueOf(1.89)); // Taxa de 3% para pagamento com cartão de débito
        taxasPorFormaPagamento.put("Dinheiro", BigDecimal.ZERO); // Nenhuma taxa para pagamento em dinheiro
    }

    @Autowired
    public OrdemServicoService(OrdemServicoRepository ordemServicoRepository, FuncionarioRepository funcionarioRepository) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<OrdemServico> buscarOrdensExecutadas(LocalDate dataInicio, LocalDate dataFim) {
        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(LocalTime.MAX);
        return ordemServicoRepository.findByStatusAndDataEmissaoBetween("executada", inicio, fim);
    }

    public Map<String, BigDecimal> calcularValorPorFuncionario(List<OrdemServico> ordensExecutadas) {
        Map<String, BigDecimal> valorPorFuncionario = new HashMap<>();
        for (OrdemServico ordem : ordensExecutadas) {
            String nomeFuncionario = ordem.getFuncionario().getNome();
            BigDecimal valorOrdem = ordem.getServico().getValor();
            BigDecimal porcentagemCargo = porcentagensPorCargo.getOrDefault(ordem.getFuncionario().getCargo(), BigDecimal.ZERO);
            BigDecimal valorBruto = valorOrdem.multiply(porcentagemCargo).divide(BigDecimal.valueOf(100));
            BigDecimal descontoPorFuncionario = descontosPorFuncionario.getOrDefault(ordem.getFuncionario().getNome(), BigDecimal.ZERO);
            BigDecimal valorLiquido = valorBruto.subtract(descontoPorFuncionario);
            BigDecimal taxaFormaPagamento = taxasPorFormaPagamento.getOrDefault(ordem.getFormaPagamento(), BigDecimal.ZERO);
            BigDecimal descontoFormaPagamento = valorLiquido.multiply(taxaFormaPagamento).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal valorFinal = valorLiquido.subtract(descontoFormaPagamento);
            valorPorFuncionario.put(nomeFuncionario, valorPorFuncionario.getOrDefault(nomeFuncionario, BigDecimal.ZERO).add(valorFinal));
        }
        return valorPorFuncionario;
    }

    public void atualizarStatusOrdens(List<OrdemServico> ordens, String novoStatus) {
        for (OrdemServico ordem : ordens) {
            ordem.setStatus(novoStatus);
            ordemServicoRepository.save(ordem);
        }
    }


    public BigDecimal calcularLucroBruto(List<OrdemServico> ordensExecutadas) {
        BigDecimal lucroBruto = BigDecimal.ZERO;
        for (OrdemServico ordem : ordensExecutadas) {
            lucroBruto = lucroBruto.add(ordem.getServico().getValor());
        }
        return lucroBruto;
    }

    public BigDecimal calcularLucroEmpresario(List<OrdemServico> ordensExecutadas) {
        BigDecimal valorTotalOrdens = BigDecimal.ZERO;
        BigDecimal valorTotalFuncionarios = BigDecimal.ZERO;

        // Calcular o valor total das ordens de serviço
        for (OrdemServico ordem : ordensExecutadas) {
            valorTotalOrdens = valorTotalOrdens.add(ordem.getServico().getValor());
        }

        // Calcular o valor total a ser pago aos funcionários
        Map<String, BigDecimal> valorPorFuncionario = calcularValorPorFuncionario(ordensExecutadas);
        for (BigDecimal valor : valorPorFuncionario.values()) {
            valorTotalFuncionarios = valorTotalFuncionarios.add(valor);
        }

        // Subtrair o valor total a ser pago aos funcionários do valor total das ordens de serviço
        return valorTotalOrdens.subtract(valorTotalFuncionarios);
    }

}



