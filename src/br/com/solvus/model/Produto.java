package br.com.solvus.model;

public class Produto {

	private String nomeProduto;
	private int idProduto;
	private  boolean checked;
	
	
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

	public void setNomeProduto(String nome) {
		this.nomeProduto = nome;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	
	
	

}
