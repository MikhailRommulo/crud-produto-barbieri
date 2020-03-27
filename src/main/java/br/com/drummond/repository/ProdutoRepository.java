package br.com.drummond.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.drummond.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer>{

}
