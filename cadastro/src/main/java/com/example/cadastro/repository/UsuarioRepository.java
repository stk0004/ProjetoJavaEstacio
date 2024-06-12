package com.example.cadastro.repository;

import com.example.cadastro.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsernameAndPassword(String username, String password);
    Usuario findByUsername(String username);
    Usuario findByEmail(String email);
}
