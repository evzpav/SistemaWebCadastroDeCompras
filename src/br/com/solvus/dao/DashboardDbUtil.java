package br.com.solvus.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import br.com.solvus.model.GraphFornecedorDTO;
import br.com.solvus.model.GraphProdutoDTO;
import br.com.solvus.model.GraphVolumeMensalProdutoDTO;
import br.com.solvus.model.TableTopFornecedoresDTO;
import br.com.solvus.util.ConvertDate;

public class DashboardDbUtil {

	private DataSource dataSource;



	public DashboardDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
		
	}

//	public static void main(String[] args) {
//
//		Integer[] arrayIdFornecedores = {23,45};
//		Integer idProduto = null;
//
//		Date dataInicial = ConvertDate.convertStringToDate("01/01/1999");
//		Date dataFinal = ConvertDate.convertStringToDate("01/01/2018");
//
//		java.sql.Date dataInicialSql = ConvertDate.convertDateToSqlDate(dataInicial);
//		java.sql.Date dataFinalSql = ConvertDate.convertDateToSqlDate(dataFinal);
//
//		String sqlFornecedores = " where 1=1 ";
//	
//
//		String idFornecedores = "";
//
//		if (arrayIdFornecedores.length > 0) {
//
//			for (Integer idFornecedor : arrayIdFornecedores) {
//
//				idFornecedores += idFornecedor + ",";
//			}
//			idFornecedores = idFornecedores.substring(0, idFornecedores.length() - 1);
//
//			sqlFornecedores += " and fornecedor.id_fornecedor in (" + idFornecedores + ") ";
//
//		}
//
//		if (dataInicialSql != null) {
//			sqlFornecedores += "and data_compra > '" + dataInicialSql + "' ";
//
//		}
//
//		if (dataFinalSql != null) {
//			sqlFornecedores += "and data_compra < '" + dataFinalSql + "' ";
//
//		}
//
//		if (idProduto != null) {
//			sqlFornecedores += "and id_produto=" + idProduto;
//
//		}
//
//		System.out.println(sqlFornecedores);
//
//	}
	
	
	public List<GraphFornecedorDTO> filtrarGraficoPercentualFornecedores(Integer[] arrayIdFornecedores, Integer idProduto, Date dataInicial, Date dataFinal) throws SQLException {
		
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		String sqlFornecedores = " where 1=1 ";
	
		String idFornecedores = "";
		
		List<GraphFornecedorDTO> listOfItensGraphFornecedorDTO = new ArrayList<GraphFornecedorDTO>();

		
		if (arrayIdFornecedores != null && arrayIdFornecedores.length > 0) {

			for (Integer idFornecedor : arrayIdFornecedores) {

				idFornecedores += idFornecedor + ",";
			}
			idFornecedores = idFornecedores.substring(0, idFornecedores.length() - 1);

			sqlFornecedores += " and fornecedor.id_fornecedor in (" + idFornecedores + ")";
			
		}else{
			sqlFornecedores = "";
			
		}


		if (dataInicial != null) {
			java.sql.Date dataInicialSql = ConvertDate.convertDateToSqlDate(dataInicial);
			sqlFornecedores += " and data_compra > '" + dataInicialSql + "' ";

		}

		if (dataFinal != null) {
			java.sql.Date dataFinalSql = ConvertDate.convertDateToSqlDate(dataFinal);
			sqlFornecedores += " and data_compra < '" + dataFinalSql + "' ";
		}

		if (idProduto != null) {
			sqlFornecedores += " and id_produto=" + idProduto;

		}

	
			myConn = dataSource.getConnection();

			String sql = "select nome_fornecedor, sum(quantidade) from itemdecompra "
					+ "join compra on itemdecompra.id_compra = compra.id_compra "
					+ "join fornecedor on compra.id_fornecedor =fornecedor.id_fornecedor"
					+ sqlFornecedores 
					+" group by fornecedor.id_fornecedor ";
			
			
			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);


				while (myRs.next()) {
	
					String nomeFornecedor = myRs.getString("nome_fornecedor");
					Double quantidade = myRs.getDouble("sum");
			
				GraphFornecedorDTO graphFornecedorDTO =  new GraphFornecedorDTO(nomeFornecedor, quantidade);
				listOfItensGraphFornecedorDTO.add(graphFornecedorDTO);
				System.out.println("dentro do dao: "+nomeFornecedor+" "+quantidade );
				}
				
			return listOfItensGraphFornecedorDTO;
	
			
	}

	public List<GraphProdutoDTO> filtrarGraficoPercentualProduto(Integer[] arrayIdFornecedores, Integer idProduto,	Date dataInicial, Date dataFinal) throws SQLException {
	
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		String sqlProdutos = " where 1=1 ";
	
		String idFornecedores = "";
		
		List<GraphProdutoDTO> listLinesGraphProdutoDTO = new ArrayList<GraphProdutoDTO>();

		
		if (arrayIdFornecedores != null && arrayIdFornecedores.length > 0) {

			for (Integer idFornecedor : arrayIdFornecedores) {

				idFornecedores += idFornecedor + ",";
			}
			idFornecedores = idFornecedores.substring(0, idFornecedores.length() - 1);

			sqlProdutos += " and compra.id_fornecedor in (" + idFornecedores + ")";
			
		}else{
			sqlProdutos = "";
			
		}


		if (dataInicial != null) {
			java.sql.Date dataInicialSql = ConvertDate.convertDateToSqlDate(dataInicial);
			sqlProdutos += " and data_compra > '" + dataInicialSql + "' ";

		}

		if (dataFinal != null) {
			java.sql.Date dataFinalSql = ConvertDate.convertDateToSqlDate(dataFinal);
			sqlProdutos += " and data_compra < '" + dataFinalSql + "' ";
		}

		if (idProduto != null) {
			sqlProdutos += " and produto.id_produto=" + idProduto;

		}

	
			myConn = dataSource.getConnection();

			String sql = "select produto.nome_produto, sum(quantidade) from itemdecompra "
					+ " join compra on itemdecompra.id_compra = compra.id_compra "
					+ " join produto on produto.id_produto = itemdecompra.id_produto " 
					+sqlProdutos
					+ " group by  produto.id_produto ";
			
			System.out.println(sql);
			
			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);


				while (myRs.next()) {
	
					String nomeProduto = myRs.getString("nome_produto");
					Double quantidade = myRs.getDouble("sum");
			
				GraphProdutoDTO graphProdutoDTO =  new GraphProdutoDTO(nomeProduto, quantidade);
				listLinesGraphProdutoDTO.add(graphProdutoDTO);
				
				System.out.println("dentro do dao: "+nomeProduto+" "+quantidade );
				}
				
			return listLinesGraphProdutoDTO;

	}

	public List<GraphVolumeMensalProdutoDTO> filtrarGraficoBarraVolumeMensalProduto(Integer[] arrayIdFornecedores,Integer idProduto, Date dataInicial, Date dataFinal) throws SQLException {
	
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		String sqlVolumeMensal = " where 1=1 ";
	
		String idFornecedores = "";
		
		List<GraphVolumeMensalProdutoDTO> listLinesGraphVolumeMensalDTO = new ArrayList<GraphVolumeMensalProdutoDTO>();

		
		if (arrayIdFornecedores != null && arrayIdFornecedores.length > 0) {

			for (Integer idFornecedor : arrayIdFornecedores) {

				idFornecedores += idFornecedor + ",";
			}
			idFornecedores = idFornecedores.substring(0, idFornecedores.length() - 1);

			sqlVolumeMensal += " and compra.id_fornecedor in (" + idFornecedores + ")";
			
		}else{
			sqlVolumeMensal = "";
			
		}


		if (dataInicial != null) {
			java.sql.Date dataInicialSql = ConvertDate.convertDateToSqlDate(dataInicial);
			sqlVolumeMensal += " and data_compra > '" + dataInicialSql + "' ";

		}

		if (dataFinal != null) {
			java.sql.Date dataFinalSql = ConvertDate.convertDateToSqlDate(dataFinal);
			sqlVolumeMensal += " and data_compra < '" + dataFinalSql + "' ";
		}

		if (idProduto != null) {
			sqlVolumeMensal += " and id_produto=" + idProduto;

		}

	
			myConn = dataSource.getConnection();

			String sql = "select sum(quantidade), to_char(data_compra, 'Mon/YY') as mes,  "
					+ "to_char(data_compra, 'YY') as ano, "
					+ "to_char(data_compra, 'MM') mesorder  "
					+ " from itemdecompra join compra on itemdecompra.id_compra = compra.id_compra "
					+ sqlVolumeMensal
					+ " group by mes, ano, mesorder "
					+ " order by ano, mesorder";
								
		
			
			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);


				while (myRs.next()) {
	
					String mes = myRs.getString("mes");
					Double sumQuantity = myRs.getDouble("sum");
			
				GraphVolumeMensalProdutoDTO graphVolumeMensalDTO =  new GraphVolumeMensalProdutoDTO(mes, sumQuantity);
				listLinesGraphVolumeMensalDTO.add(graphVolumeMensalDTO);
				
				System.out.println("dentro do dao volume: "+mes+" "+sumQuantity );
				}
				
			return listLinesGraphVolumeMensalDTO;
	}

	public List<TableTopFornecedoresDTO> filtrarTabelaTopFornecedores(Integer[] arrayIdFornecedores, Integer idProduto,	Date dataInicial, Date dataFinal) throws SQLException {
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		String sqlTabelaTopFornecedores = " where 1=1 ";
	
		String idFornecedores = "";
		
		List<TableTopFornecedoresDTO> listLinesTabelaTopFornecedoresDTO = new ArrayList<TableTopFornecedoresDTO>();

		
		if (arrayIdFornecedores != null && arrayIdFornecedores.length > 0) {

			for (Integer idFornecedor : arrayIdFornecedores) {

				idFornecedores += idFornecedor + ",";
			}
			idFornecedores = idFornecedores.substring(0, idFornecedores.length() - 1);

			sqlTabelaTopFornecedores += " and compra.id_fornecedor in (" + idFornecedores + ")";
			
		}else{
			sqlTabelaTopFornecedores = "";
			
		}


		if (dataInicial != null) {
			java.sql.Date dataInicialSql = ConvertDate.convertDateToSqlDate(dataInicial);
			sqlTabelaTopFornecedores += " and data_compra > '" + dataInicialSql + "' ";

		}

		if (dataFinal != null) {
			java.sql.Date dataFinalSql = ConvertDate.convertDateToSqlDate(dataFinal);
			sqlTabelaTopFornecedores += " and data_compra < '" + dataFinalSql + "' ";
		}

		if (idProduto != null) {
			sqlTabelaTopFornecedores += " and id_produto=" + idProduto;

		}

	
			myConn = dataSource.getConnection();

			String sql = "select fornecedor.id_fornecedor, nome_fornecedor, round(sum(valor_total)) as total_compra from compra "
					+ " join fornecedor on compra.id_fornecedor = fornecedor.id_fornecedor "
					+ " join itemdecompra on compra.id_compra=itemdecompra.id_compra "
					+ sqlTabelaTopFornecedores
					+ " group by fornecedor.id_fornecedor "
					+ " order by total_compra desc "
					+ " limit 5";
		
			
			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);


				while (myRs.next()) {
	
					String nomeFornecedor = myRs.getString("nome_fornecedor");
					Double totalCompra = myRs.getDouble("total_compra");
			
				TableTopFornecedoresDTO tabelaTopFornecedoresDTO =  new TableTopFornecedoresDTO(nomeFornecedor, totalCompra);
				listLinesTabelaTopFornecedoresDTO.add(tabelaTopFornecedoresDTO);
				
				System.out.println("dentro do dao tabela top: "+nomeFornecedor+" "+totalCompra );
				}
				
			return listLinesTabelaTopFornecedoresDTO;
	}

	
}