package com.algaworks.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.model.Pessoa;
import com.algaworks.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;

	public Pessoa salvar(Pessoa pessoa) {
		pessoa.getContados().forEach(c -> c.setPessoa(pessoa));
		return pessoaRepository.save(pessoa);
	}

	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
		pessoaSalva.getContados().clear();
		pessoaSalva.getContados().addAll(pessoa.getContados());
		pessoaSalva.getContados().forEach(c -> c.setPessoa(pessoaSalva));
		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo", "contatos");
		pessoaRepository.save(pessoaSalva);
		
		return pessoaSalva;
	}
	
	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
		pessoaSalva.setAtivo(ativo);
		pessoaRepository.save(pessoaSalva);
	}

	public Pessoa buscarPessoaPeloCodigo(Long codigo) {
		Pessoa pessoaSalva = pessoaRepository.findOne(codigo);
		if (pessoaSalva == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return pessoaSalva;
	}
	
}
