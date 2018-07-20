package com.algaworks.repository.lancamento;

import com.algaworks.dto.LancamentoEstatisticaCategoria;
import com.algaworks.dto.LancamentoEstatisticaDia;
import com.algaworks.dto.LancamentoEstatisticaPessoa;
import com.algaworks.repository.projection.ResumoLancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.algaworks.model.Lancamento;
import com.algaworks.repository.filter.LancamentoFilter;

import java.time.LocalDate;
import java.util.List;

public interface LancamentoRepositoryQuery {

	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim);
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia);
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia);

	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
	
}
