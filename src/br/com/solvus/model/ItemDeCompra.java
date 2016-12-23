package br.com.solvus.model;

public class ItemDeCompra {

	private int idItemDeCompra;
	private Integer quantidade;
	private Double valorUnitario;
	private Produto produto;

	public ItemDeCompra(Produto produto, int quantidade, double valorUnitario) {
		super();
		this.setProduto(produto);
		this.quantidade = quantidade;
		this.valorUnitario = valorUnitario;
	}
	
	public ItemDeCompra(){
		
	}

	public int getIdItemDeCompra() {
		return idItemDeCompra;
	}

	public void setIdItemDeCompra(int idItemDeCompra) {
		this.idItemDeCompra = idItemDeCompra;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public Double getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}


}
