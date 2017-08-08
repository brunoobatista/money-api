package com.algaworks.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.event.RecursoCriadoEvent;

@Component
public class RecursoCriadoLestener implements ApplicationListener<RecursoCriadoEvent> {

	@Override
	public void onApplicationEvent(RecursoCriadoEvent recrusoCriadoEvent) {
		HttpServletResponse response = recrusoCriadoEvent.getResponse();
		Long codigo = recrusoCriadoEvent.getCodigo();
		
		adicionarHeaderLocation(response, codigo);

	}

	private void adicionarHeaderLocation(HttpServletResponse response, Long codigo) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				.buildAndExpand(codigo).toUri();
		response.setHeader("Location", uri.toASCIIString());
	}

}
