package br.com.solvus.model;

import java.util.Date;

public class DadosTabelaAddCompra {

	Itens[] itens;
	
	String dataCompra;
	int idFornecedor;
	double valorTotalCompra;
	
	public String getDataCompra() {
		return dataCompra;
	}
	public void setDataCompra(String dataCompra) {
		this.dataCompra = dataCompra;
	}
	public int getIdFornecedor() {
		return idFornecedor;
	}
	public void setIdFornecedor(int idFornecedor) {
		this.idFornecedor = idFornecedor;
	}
	public double getValorTotalCompra() {
		return valorTotalCompra;
	}
	public void setValorTotalCompra(double valorTotalCompra) {
		this.valorTotalCompra = valorTotalCompra;
	}
	public Itens[] getItens() {
		return itens;
	}
	public void setItens(Itens[] itens) {
		this.itens = itens;
	}
	
	
	
	

}
