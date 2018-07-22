package com.algaworks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.model.Lancamento;
import com.algaworks.repository.lancamento.LancamentoRepositoryQuery;

import java.time.LocalDate;
import java.util.List;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

    List<Lancamento> findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate data);

}
