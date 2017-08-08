package com.algaworks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
