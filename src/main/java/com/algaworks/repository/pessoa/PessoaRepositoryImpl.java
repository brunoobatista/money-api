package com.algaworks.repository.pessoa;

import com.algaworks.model.Pessoa;
import com.algaworks.model.Pessoa_;
import com.algaworks.repository.filter.PessoaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruno on 10/08/2017.
 */
public class PessoaRepositoryImpl implements PessoaRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Pessoa> criteria = builder.createQuery(Pessoa.class);
        Root<Pessoa> root = criteria.from(Pessoa.class);


        Predicate[] predicates = criarRestricoes(pessoaFilter, builder, root);
        criteria.where(predicates);
        criteria.orderBy(builder.asc(root.get(Pessoa_.codigo)));

        TypedQuery<Pessoa> query = manager.createQuery(criteria);

        adicionarRestricoesDePaginacao(query, pageable);


        return new PageImpl<>(query.getResultList(), pageable, total(pessoaFilter));
    }

    private Predicate[] criarRestricoes(PessoaFilter pessoaFilter, CriteriaBuilder builder,
                                        Root<Pessoa> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(pessoaFilter.getNome())) {
            predicates.add(builder.like(
                    builder.lower(root.get(Pessoa_.nome)), "%" + pessoaFilter.getNome().toLowerCase() + "%"));
        }

        if (pessoaFilter.getAtivo() != null) {
            predicates.add(builder.equal(
               root.get(Pessoa_.ativo), pessoaFilter.getAtivo()
            ));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long total(PessoaFilter pessoaFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Pessoa> root = criteria.from(Pessoa.class);

        Predicate[] predicates = criarRestricoes(pessoaFilter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

}
