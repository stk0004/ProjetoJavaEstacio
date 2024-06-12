package com.example.cadastro.repository;

import com.example.cadastro.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    List<Funcionario> findByCargo(String cargo);
}
