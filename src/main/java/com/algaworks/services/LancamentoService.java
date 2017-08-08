package com.algaworks.services;


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
		Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		
		/*if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}*/
		
		
		if (pessoa == null) {
			throw new PessoaInexistenteException();
		} else if (pessoa.isInativo()) {
			throw new PessoaInativaException();
		}
		return lancamentoRepository.save(lancamento);
	}

}
