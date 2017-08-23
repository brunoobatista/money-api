package com.algaworks.services;


import com.algaworks.services.exception.LancamentoInexistenteException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.model.Lancamento;
import com.algaworks.model.Pessoa;
import com.algaworks.repository.LancamentoRepository;
import com.algaworks.repository.PessoaRepository;
import com.algaworks.services.exception.PessoaInativaException;
import com.algaworks.services.exception.PessoaInexistenteException;
//import com.algaworks.services.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	

	public Lancamento salvar(Lancamento lancamento) {
		if (lancamento.getPessoa().getCodigo() == null) {
			throw new PessoaInexistenteException();
		}
		/*if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}*/

		validarPessoa(lancamento);

		return lancamentoRepository.save(lancamento);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
		if ( (lancamento.getPessoa() != null) && (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) ) {
			validarPessoa(lancamento);
		}

		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

		return lancamentoRepository.save(lancamentoSalvo);
	}

	private Lancamento buscarLancamentoExistente(Long codigo) {
		Lancamento lancamentoSalvo = lancamentoRepository.findOne(codigo);
		if (lancamentoSalvo == null) {
			throw new LancamentoInexistenteException();
		}
		return lancamentoSalvo;
	}

	private void validarPessoa(Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		if (pessoa == null) {
			throw new PessoaInexistenteException();
		} else if (pessoa.isInativo()) {
			throw new PessoaInativaException();
		}
	}

}
