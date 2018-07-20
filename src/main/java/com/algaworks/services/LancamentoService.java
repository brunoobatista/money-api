package com.algaworks.services;


import com.algaworks.dto.LancamentoEstatisticaPessoa;
import com.algaworks.services.exception.LancamentoInexistenteException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.algaworks.model.Lancamento;
import com.algaworks.model.Pessoa;
import com.algaworks.repository.LancamentoRepository;
import com.algaworks.repository.PessoaRepository;
import com.algaworks.services.exception.PessoaInativaException;
import com.algaworks.services.exception.PessoaInexistenteException;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
//import com.algaworks.services.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Scheduled(cron = "0 0 6 * * *")
	public void avisarSobreLancamentosVencidos() {
        System.out.println("########## Método sendo executado");
    }

	public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
		List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DT_INICIO", Date.valueOf(inicio));
		parametros.put("DT_FIM", Date.valueOf(fim));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

		InputStream inputStream = this.getClass().getResourceAsStream(
				"/relatorios/lancamentos-por-pessoa.jasper"
		);

		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
				new JRBeanCollectionDataSource(dados));

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

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
