package br.com.solvus.jdbc;

public class Produto {

	private String nomeProduto;
	private int idProduto;

	public Produto(String nome) {
		this.nomeProduto = nome;
	
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	
	public void setIdProduto(int id) {
		this.idProduto = id;
	}

	public int getIdProduto() {
		return idProduto;
	}

	@Override
	public String toString() {
		return nomeProduto;
	}

	public void setNome(String nome) {
		this.nomeProduto = nome;
	}

}
