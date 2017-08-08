package com.algaworks.repository;

import com.algaworks.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * Created by Bruno on 02/08/2017.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

   public Optional<Usuario> findByEmail(String email);

}
