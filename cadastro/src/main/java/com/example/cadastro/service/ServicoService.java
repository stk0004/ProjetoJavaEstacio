package com.example.cadastro.service;

import com.example.cadastro.model.Servico;
import com.example.cadastro.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public List<Servico> findByTipo(String tipo) {
        return servicoRepository.findByTipo(tipo);
    }
}
