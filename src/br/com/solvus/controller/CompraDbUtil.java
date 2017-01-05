package br.com.solvus.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

import br.com.solvus.jdbc.Compra;
import br.com.solvus.jdbc.Fornecedor;
import br.com.solvus.jdbc.ItemDeCompra;
import br.com.solvus.jdbc.Produto;

public class CompraDbUtil {

	private DataSource dataSource;

	private FornecedorDbUtil fornecedorDbUtil;
	private ItemDeCompraDbUtil itemdecompraDbUtil;
	private ProdutoDbUtil produtoDbUtil;

	public CompraDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
		fornecedorDbUtil = new FornecedorDbUtil(theDataSource);
		produtoDbUtil = new ProdutoDbUtil(theDataSource);
		itemdecompraDbUtil = new ItemDeCompraDbUtil(theDataSource);
	}

	public List<Compra> getCompras() throws Exception {

		ArrayList<Compra> listaCompra = new ArrayList<>();
		Fornecedor fornecedor = null;
		Compra compra = null;

		int idCompraAtual = 0;
		List<ItemDeCompra> listaItemDeCompra = new ArrayList<ItemDeCompra>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {

			myConn = dataSource.getConnection();

			String sql = "select compra.id_compra, data_compra, id_fornecedor, valor_total, id_produto, id_itemdecompra, quantidade, valor_unitario  from compra "
					+ "join itemdecompra as tabelacompraitem on " + "compra.id_compra = tabelacompraitem.id_compra "
					+ "order by data_compra ASC";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			while (myRs.next()) {

				int idCompra = myRs.getInt("id_compra");
				Date dataCompra = myRs.getDate("data_compra");
				int idFornecedor = myRs.getInt("id_fornecedor");
				Double valorTotal = myRs.getDouble("valor_total");
				int idProduto = myRs.getInt("id_produto");
				int idItemDeCompra = myRs.getInt("id_itemdecompra");
				Integer quantidade = myRs.getInt("quantidade");
				Double valorUnitario = myRs.getDouble("valor_unitario");

				fornecedor = fornecedorDbUtil.getFornecedor(idFornecedor);

				Produto produto = produtoDbUtil.getProduto(idProduto);

				ItemDeCompra itemdecompra = new ItemDeCompra(produto, quantidade, valorUnitario);
				itemdecompra.setIdItemDeCompra(idItemDeCompra);

				if (idCompraAtual != idCompra) {
					compra = new Compra(fornecedor, dataCompra);
					compra.setValorTotal(valorTotal);
					compra.setIdCompra(idCompra);
					compra.setFornecedor(fornecedor);
					listaItemDeCompra = new ArrayList<ItemDeCompra>();
					listaItemDeCompra.add(itemdecompra);

					idCompraAtual = idCompra;
					listaCompra.add(compra);
				} else {
					listaItemDeCompra.add(itemdecompra);

				}

				compra.setListaDeItemDeCompra(listaItemDeCompra);

			}

			return listaCompra;
		} finally {
			// close JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {

		try {
			if (myRs != null) {
				myRs.close();
			}

			if (myStmt != null) {
				myStmt.close();
			}

			if (myConn != null) {
				myConn.close(); // doesn't really close it ... just puts back in
								// connection pool
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public Compra getCompra(int idCompra) throws Exception {

		Fornecedor fornecedor = null;
		Compra compra = null;

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		// Integer idCompra= null;

		try {
			// get a connection
			myConn = dataSource.getConnection();

			// idCompra = Integer.parseInt(idCompraString);
			// create sql statement
			String sql = "select * from compra where id_compra=(?)";

			myStmt = myConn.prepareStatement(sql);

			((PreparedStatement) myStmt).setInt(1, idCompra);

			myRs = myStmt.executeQuery(sql);

			while (myRs.next()) {

				Date dataCompra = myRs.getDate("data_compra");
				int idFornecedor = myRs.getInt("id_fornecedor");
				Double valorTotal = myRs.getDouble("valor_total");

				fornecedor = fornecedorDbUtil.getFornecedor(idFornecedor);
				compra = new Compra(fornecedor, dataCompra);
				compra.setValorTotal(valorTotal);
				compra.setIdCompra(idCompra);
				List<ItemDeCompra> listaDeItemDeCompra = new ArrayList<ItemDeCompra>();
				listaDeItemDeCompra = itemdecompraDbUtil.listItemDeCompra(idCompra);
				compra.setListaDeItemDeCompra(listaDeItemDeCompra);
			}

			return compra;
		} finally {
			// close JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

	// public void addProduto(Produto produto) throws Exception {
	//
	// Connection myConn = null;
	// PreparedStatement myStmt = null;
	//
	//
	// if(!checkIfDuplicate(produto.getNomeProduto())){
	//
	//
	// try {
	// // get db connection
	// myConn = dataSource.getConnection();
	//
	// // create sql for insert
	// String sql = "insert into produto (nome_produto) values (?)";
	//
	// myStmt = myConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	//
	// // set the param values for the student
	// myStmt.setString(1, produto.getNomeProduto());
	//
	// // execute sql insert
	// myStmt.execute();
	//
	// try (ResultSet keys = myStmt.getGeneratedKeys()) {
	// keys.next();
	// int id = keys.getInt("id_produto");
	// produto.setIdProduto(id);
	// }
	// } finally {
	// // clean up JDBC objects
	// close(myConn, myStmt, null);
	// }
	// }
	// }
	//
	// public Produto getProduto(String idProdutoString) throws Exception {
	//
	// Produto produto = null;
	//
	// Connection myConn = null;
	// PreparedStatement myStmt = null;
	// ResultSet myRs = null;
	// int idProduto;
	//
	// try {
	// // convert student id to int
	// idProduto = Integer.parseInt(idProdutoString);
	//
	// // get connection to database
	// myConn = dataSource.getConnection();
	//
	// // create sql to get selected student
	// String sql = "select * from produto where id_produto=(?) order by
	// nome_produto";
	//
	// // create prepared statement
	// myStmt = myConn.prepareStatement(sql);
	//
	// // set params
	// myStmt.setInt(1, idProduto);
	//
	// // execute statement
	// myRs = myStmt.executeQuery();
	//
	// // retrieve data from result set row
	// if (myRs.next()) {
	// String nomeProduto = myRs.getString("nome_produto");
	//
	// // use the studentId during construction
	// produto = new Produto(nomeProduto);
	// produto.setIdProduto(idProduto);
	//
	// } else {
	// throw new Exception("Could not find produto id: " + idProduto);
	// }
	//
	// return produto;
	// } finally {
	// // clean up JDBC objects
	// close(myConn, myStmt, myRs);
	// }
	// }
	//
	// public void updateProduto(Produto produto) throws Exception {
	//
	// Connection myConn = null;
	// PreparedStatement myStmt = null;
	//
	// try {
	// // get db connection
	// myConn = dataSource.getConnection();
	//
	// // create SQL update statement
	// String sql = "update produto set nome_produto = (?) where id_produto =
	// (?)";
	//
	// // prepare statement
	// myStmt = myConn.prepareStatement(sql);
	//
	// // set params
	// myStmt.setString(1, produto.getNomeProduto());
	// myStmt.setInt(2, produto.getIdProduto());
	// // execute SQL statement
	// myStmt.execute();
	// } finally {
	// // clean up JDBC objects
	// close(myConn, myStmt, null);
	// }
	// }
	//
	public void deleteCompra(int idCompra) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			// get connection to database
			myConn = dataSource.getConnection();

			// create sql to delete student
			String sql = "delete from compra where id_compra=?";

			// prepare statement
			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, idCompra);

			// execute sql statement
			myStmt.execute();
		} finally {
			// clean up JDBC code
			close(myConn, myStmt, null);
		}
	}

	public void deleteRelationship(int idCompra) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			// get connection to database
			myConn = dataSource.getConnection();

			// create sql to delete student
			String sql = "delete from itemdecompra where id_compra = (?)";

			// prepare statement
			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, idCompra);

			// execute sql statement
			myStmt.execute();
		} finally {
			// clean up JDBC code
			close(myConn, myStmt, null);
		}
	}

	// public boolean hasRelationshipFornecedor(int produtoId) throws
	// SQLException {
	//
	// boolean retorno = false;
	// Connection myConn = null;
	// PreparedStatement myStmt = null;
	//
	// try {
	// // get db connection
	// myConn = dataSource.getConnection();
	//
	// String sql = "select * from fornecedor_produto where
	// fornecedor_produto.id_produto = (?)";
	//
	// myStmt = myConn.prepareStatement(sql);
	//
	// myStmt.setInt(1, produtoId);
	//
	// myStmt.execute();
	//
	// ResultSet resultSet = myStmt.getResultSet();
	//
	// while (resultSet.next()) {
	// retorno = true;
	// }
	//
	// } finally {
	// close(myConn, myStmt, null);
	// }
	//
	// return retorno;
	// }
	//
	// public boolean checkIfDuplicate(String nomeProduto) throws SQLException {
	//
	// boolean retorno = false;
	// Connection myConn = null;
	// PreparedStatement myStmt = null;
	//
	// try {
	// myConn = dataSource.getConnection();
	//
	// String sql = "select * from produto where nome_produto = (?)";
	//
	// myStmt = myConn.prepareStatement(sql);
	//
	// myStmt.setString(1, nomeProduto);
	//
	// myStmt.execute();
	//
	// ResultSet resultSet = myStmt.getResultSet();
	//
	// while (resultSet.next()) {
	// retorno = true;
	// }
	// } finally {
	// close(myConn, myStmt, null);
	// }
	// return retorno;
	// }

	public List<Compra> filterListaCompra(int idFornecedorSelecionado, java.sql.Date dataInicial,
			java.sql.Date dataFinal) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		ArrayList<Compra> listaCompra = new ArrayList<>();
		Fornecedor fornecedor = null;
		Compra compra = null;

		int idCompraAtual = 0;
		List<ItemDeCompra> listaItemDeCompra = new ArrayList<ItemDeCompra>();

		try {

			myConn = dataSource.getConnection();

			String sql = "select * from compra "
					+ "join itemdecompra as tabelacompraitem on compra.id_compra = tabelacompraitem.id_compra "
					+ "where id_fornecedor = (?) " + "and data_compra between (?) and (?)";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, idFornecedorSelecionado);
			myStmt.setDate(2, dataInicial);
			myStmt.setDate(3, dataFinal);

			myRs = myStmt.executeQuery();

				
			while (myRs.next()) {

				int idCompra = myRs.getInt("id_compra");
				Date dataCompra = myRs.getDate("data_compra");
				int idFornecedor = myRs.getInt("id_fornecedor");
				Double valorTotal = myRs.getDouble("valor_total");
				int idProduto = myRs.getInt("id_produto");
				int idItemDeCompra = myRs.getInt("id_itemdecompra");
				Integer quantidade = myRs.getInt("quantidade");
				Double valorUnitario = myRs.getDouble("valor_unitario");

				fornecedor = fornecedorDbUtil.getFornecedor(idFornecedor);

				Produto produto = produtoDbUtil.getProduto(idProduto);

				ItemDeCompra itemdecompra = new ItemDeCompra(produto, quantidade, valorUnitario);
				itemdecompra.setIdItemDeCompra(idItemDeCompra);

				if (idCompraAtual != idCompra) {
					compra = new Compra(fornecedor, dataCompra);
					compra.setValorTotal(valorTotal);
					compra.setIdCompra(idCompra);
					compra.setFornecedor(fornecedor);
					listaItemDeCompra = new ArrayList<ItemDeCompra>();
					listaItemDeCompra.add(itemdecompra);

					idCompraAtual = idCompra;
					listaCompra.add(compra);
				} else {
					listaItemDeCompra.add(itemdecompra);

				}

				compra.setListaDeItemDeCompra(listaItemDeCompra);

			}

			return listaCompra;
		} finally {
			// close JDBC objects
			close(myConn, myStmt, myRs);
		}
	}
}
