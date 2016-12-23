package br.com.solvus.model;

import java.util.Date;
import java.util.List;

public class Compra {

	private int idCompra;
	private Date dataCompra;
	private List<ItemDeCompra> listaDeItemDeCompra;
	private Fornecedor fornecedor;
	private Double valorTotal;
	
	public Compra (Fornecedor fornecedor,Date dataCompra){
		this.setFornecedor(fornecedor);
		this.dataCompra = dataCompra;
	}
	
	public int getId() {

		return idCompra;
	}

	public void setId(int id) {
		this.idCompra = id;
	}

	public Date getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(Date dataCompra) {
		this.dataCompra = dataCompra;
	}

	public List<ItemDeCompra> getListaDeItemDeCompra() {
		return listaDeItemDeCompra;
	}

	public void setListaDeItemDeCompra(List<ItemDeCompra> listaDeItemDeCompra) {
		this.listaDeItemDeCompra = listaDeItemDeCompra;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	}
