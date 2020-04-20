package br.com.drummond.resources;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.drummond.model.Produto;
import br.com.drummond.repository.ProdutoRepository;

@RestController
@RequestMapping("/produtos")
public class CRUDProdutosResources {
	
	@Autowired
	ProdutoRepository produtoRepository;
	
	@GetMapping
	public ResponseEntity<?> pegarProdutos(){
		List<Produto> produtos = produtoRepository.findAll();
		if(produtos.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Não existem produtos registrados");
		}else {
			return ResponseEntity.ok(produtos);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> pegarProduto(@PathVariable int id) {
		Optional<Produto> produto = produtoRepository.findById(id);
		if(produto.isPresent()) {
			return ResponseEntity.ok(produto.get());
		}else {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Não existe produto com esse código");
		}
	}
	
	@PostMapping
	public ResponseEntity<Produto> cadastrarProduto(@Valid @RequestBody Produto produto) {
		Produto produtoParaCadastrar = produtoRepository.save(produto);
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoParaCadastrar);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Produto> alterarProduto(@PathVariable Integer id, @Valid @RequestBody Produto produto) {
		Optional<Produto> produtoExistente = produtoRepository.findById(id);
		if(produtoExistente.isPresent()) {
			if(produto.getDescricao() != null) {
				produtoExistente.get().setDescricao(produto.getDescricao());
			}
			if(produto.getPreco() > 0) {
				produtoExistente.get().setPreco(produto.getPreco());
			}
			if(produto.getSaldo() > 0) {
				produtoExistente.get().setSaldo(produto.getSaldo());
			}
			if(produto.getUnidade() != null) {
				produtoExistente.get().setUnidade(produto.getUnidade());
			}
			return ResponseEntity.ok(produtoRepository.save(produtoExistente.get()));
		} else {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Produto não encontrado");
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> apagarProduto(@PathVariable Integer id) {
		Optional<Produto> produtoExistente = produtoRepository.findById(id);
		if(produtoExistente.isPresent()) {
			produtoRepository.deleteById(produtoExistente.get().getCodigo());
			return ResponseEntity.status(HttpStatus.OK).body("O produto "+ produtoExistente.get().getDescricao() +" foi apagado");
		} else {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Produto não encontrado");
		}
	}
	
}
