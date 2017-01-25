package br.com.solvus.model;

import java.util.Date;

public class FiltroDashboard {
	
	private Integer [] arrayIdFornecedoresSelecionados;
	private Integer idProduto;
	private Date dataInicial;
	private Date dataFinal;
	
	
	
	public Integer getIdProduto() {
		return idProduto;
	}
	public void setidProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}
	public Date getDataInicial() {
		return dataInicial;
	}
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	public Date getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	public Integer [] getArrayIdFornecedoresSelecionados() {
		return arrayIdFornecedoresSelecionados;
	}
	public void setArrayIdFornecedoresSelecionados(Integer [] arrayIdFornecedoresSelecionados) {
		this.arrayIdFornecedoresSelecionados = arrayIdFornecedoresSelecionados;
	}

}
