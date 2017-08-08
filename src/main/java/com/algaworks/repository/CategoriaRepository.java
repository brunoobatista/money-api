package com.algaworks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
