package com.algaworks.repository.pessoa;

import com.algaworks.model.Pessoa;
import com.algaworks.repository.filter.PessoaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Bruno on 10/08/2017.
 */
public interface PessoaRepositoryQuery {

    public Page<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable);

}
