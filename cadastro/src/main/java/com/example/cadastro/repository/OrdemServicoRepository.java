package com.example.cadastro.repository;

import com.example.cadastro.model.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
     List<OrdemServico> findByStatusAndDataEmissaoBetween(String status, LocalDateTime dataInicio, LocalDateTime dataFim);
}