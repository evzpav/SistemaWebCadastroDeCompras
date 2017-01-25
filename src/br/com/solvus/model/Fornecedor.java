package br.com.solvus.model;

import java.util.Date;
import java.util.List;


public class Fornecedor {

	private  String nomeFornecedor;
	private  Date dataContrato;	
	private int idFornecedor;
	private List <Produto> listagemProdutos;
	private List <Integer> listagemIdProdutos;
	private Double totalEmCompras;
	
	
	public Fornecedor(String nome, Date dataContrato) {
		this.nomeFornecedor = nome;
		this.dataContrato = dataContrato;
	
	}
	

	
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}
	
	public void setIdFornecedor(int id) {
		this.idFornecedor = id;
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

	public int getIdFornecedor() {
		return idFornecedor;
	}


	public void setNomeFornecedor(String nome) {
		this.nomeFornecedor = nome;
	}


	public void setDataContrato(Date dataContrato) {
		this.dataContrato = dataContrato;
	}

	@Override
	public String toString() {
		return nomeFornecedor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idFornecedor;
		result = prime * result + ((nomeFornecedor == null) ? 0 : nomeFornecedor.hashCode());
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
		if (idFornecedor != other.idFornecedor)
			return false;
		if (nomeFornecedor == null) {
			if (other.nomeFornecedor != null)
				return false;
		} else if (!nomeFornecedor.equals(other.nomeFornecedor))
			return false;
		return true;
	}



	public List <Integer> getListagemIdProdutos() {
		return listagemIdProdutos;
	}



	public void setListagemIdProdutos(List <Integer> listagemIdProdutos) {
		this.listagemIdProdutos = listagemIdProdutos;
	}



	public Double getTotalEmCompras() {
		return totalEmCompras;
	}



	public void setTotalEmCompras(Double totalEmCompras) {
		this.totalEmCompras = totalEmCompras;
	}



	
	
	

}
