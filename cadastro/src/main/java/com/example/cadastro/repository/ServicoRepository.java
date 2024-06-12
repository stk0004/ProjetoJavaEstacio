package com.example.cadastro.repository;

import com.example.cadastro.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    @Query("SELECT DISTINCT s.tipo FROM Servico s")
    Set<String> findDistinctTipos();
    List<Servico> findByTipo(String tipo);
}
