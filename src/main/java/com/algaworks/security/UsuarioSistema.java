package com.algaworks.security;

import com.algaworks.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by Bruno on 09/08/2017.
 */
public class UsuarioSistema extends User {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    public UsuarioSistema(Collection<? extends GrantedAuthority> authorities, Usuario usuario) {
        super(usuario.getEmail(), usuario.getSenha(), authorities);
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

}
