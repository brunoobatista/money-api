package com.algaworks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.model.Lancamento;
import com.algaworks.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

}
