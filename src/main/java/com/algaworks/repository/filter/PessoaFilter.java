package com.algaworks.repository.filter;

/**
 * Created by Bruno on 10/08/2017.
 */
public class PessoaFilter {

    private String nome;
    private Boolean ativo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
