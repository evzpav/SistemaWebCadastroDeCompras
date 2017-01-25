package br.com.solvus.model;

import java.util.Date;

public class GraphVolumeMensalProdutoDTO {

	
	private String mes;
	private Double sumQuantity;
	
	public GraphVolumeMensalProdutoDTO(String mes, Double sumQuantity) {
		super();
		this.mes = mes;
		this.sumQuantity = sumQuantity;
	}
}
