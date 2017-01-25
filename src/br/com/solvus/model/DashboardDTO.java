package br.com.solvus.model;

import java.util.List;

public class DashboardDTO {

	
	private List <GraphFornecedorDTO> listLinesGraphFornecedorDTO;
	private List <GraphProdutoDTO> listLinesGraphProdutoDTO;
	private List <GraphVolumeMensalProdutoDTO> listLinesGraphVolumeMensalProdutoDTO;
	private List <TableTopFornecedoresDTO> listLinesTableTopFornecedorDTO;
	
	
	public DashboardDTO(List<GraphFornecedorDTO> listLinesGraphFornecedorDTO,
			List<GraphProdutoDTO> listLinesGraphProdutoDTO, List<GraphVolumeMensalProdutoDTO> listLinesGraphVolumeProdutoDTO,
			List<TableTopFornecedoresDTO> listLinesTableTopFornecedorDTO) {
		super();
		this.listLinesGraphFornecedorDTO = listLinesGraphFornecedorDTO;
		this.listLinesGraphProdutoDTO = listLinesGraphProdutoDTO;
		this.listLinesGraphVolumeMensalProdutoDTO = listLinesGraphVolumeProdutoDTO;
		this.listLinesTableTopFornecedorDTO = listLinesTableTopFornecedorDTO;
	}
	
}
