package br.com.solvus.model;

import java.util.Date;
import java.util.List;

public class Fornecedor {

	private  String nome;
	private  Date dataContrato;	
	private int id;
	private List <Produto> listagemProdutos;

	
	
	public Fornecedor(String nome, Date dataContrato) {
		this.nome = nome;
		this.dataContrato = dataContrato;
	
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setId(int id) {
		this.id = id;
	}


	public Date getDataContrato() {
		return dataContrato;
	}

	public List<Produto> getListagemProdutos() {
		return listagemProdutos;
	}

	public void setListagemProdutos(List<Produto> listagemProdutos) {
		this.listagemProdutos = listagemProdutos;
	}

	public int getId() {
		return id;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public void setDataContrato(Date dataContrato) {
		this.dataContrato = dataContrato;
	}

	@Override
	public String toString() {
		return nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fornecedor other = (Fornecedor) obj;
		if (id != other.id)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	
	
	

}
