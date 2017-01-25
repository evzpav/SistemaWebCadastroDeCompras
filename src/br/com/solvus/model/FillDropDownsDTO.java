package br.com.solvus.model;

import java.util.List;

public class FillDropDownsDTO {

	
	private List<Fornecedor> fornecedores;
	private List<Produto> produtos;
	
	public FillDropDownsDTO(List<Fornecedor> fornecedores, List<Produto> produtos) {
		super();
		this.fornecedores = fornecedores;
		this.produtos = produtos;
	}
	
}
