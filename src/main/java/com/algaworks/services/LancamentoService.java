package com.algaworks.services;


import com.algaworks.dto.LancamentoEstatisticaPessoa;
import com.algaworks.mail.Mailer;
import com.algaworks.model.Usuario;
import com.algaworks.repository.UsuarioRepository;
import com.algaworks.services.exception.LancamentoInexistenteException;
import com.algaworks.storage.S3;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
//import com.algaworks.services.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

	private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";

	private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class);
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private Mailer mailer;

	@Autowired
	private S3 s3;

	//@Scheduled(fixedDelay = 1000 * 60 * 30)
	@Scheduled(cron = "0 0 6 * * *")
	public void avisarSobreLancamentosVencidos() {
		if (logger.isDebugEnabled()) {
			logger.debug("Preparando envio de e-mails.");
		}
		List<Lancamento> vencidos = lancamentoRepository
				.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());

		if (vencidos.isEmpty()) {
			logger.info("Sem lançamentos vencidos");
			return;
		}

		logger.info("Existem {} lançamentos vencidos.", vencidos.size());

		List<Usuario> usuarios = usuarioRepository
				.findByPermissoesDescricao(DESTINATARIOS);

		if (usuarios.isEmpty()) {
			logger.warn("Existem vencimentos, porém não existe destinatários.");
			return;
		}

		mailer.avisarSobreLancamentosVencidos(vencidos, usuarios);
		logger.info("Envio de email de aviso concluído.");
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

		if (StringUtils.hasText(lancamento.getAnexo())) {
			s3.salvar(lancamento.getAnexo());
		}

		return lancamentoRepository.save(lancamento);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
		if ( (lancamento.getPessoa() != null) && (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) ) {
			validarPessoa(lancamento);
		}

		if (StringUtils.isEmpty(lancamento.getAnexo()) &&
				StringUtils.hasText(lancamentoSalvo.getAnexo())) {
			s3.remover(lancamentoSalvo.getAnexo());
		} else if (StringUtils.hasText(lancamento.getAnexo()) &&
				!lancamento.getAnexo().equals(lancamentoSalvo.getAnexo())) {
			s3.substituir(lancamentoSalvo.getAnexo(), lancamento.getAnexo());
		}

		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

		return lancamentoRepository.save(lancamentoSalvo);
	}

	private Lancamento buscarLancamentoExistente(Long codigo) {
		Optional<Lancamento> lancamentoSalvo = lancamentoRepository.findById(codigo);
		if (!lancamentoSalvo.isPresent()) {
			throw new LancamentoInexistenteException();
		}
		return lancamentoSalvo.get();
	}

	private void validarPessoa(Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.getOne(lancamento.getPessoa().getCodigo());
		if (pessoa == null) {
			throw new PessoaInexistenteException();
		} else if (pessoa.isInativo()) {
			throw new PessoaInativaException();
		}
	}

}
