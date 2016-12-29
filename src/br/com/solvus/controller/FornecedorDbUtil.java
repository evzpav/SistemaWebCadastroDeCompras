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

import br.com.solvus.jdbc.Fornecedor;
import br.com.solvus.jdbc.Produto;

public class FornecedorDbUtil {

	private DataSource dataSource;

	public FornecedorDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}

	public List<Fornecedor> getFornecedores() throws SQLException {

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			// get a connection
			myConn = dataSource.getConnection();

			// create sql statement
			String sql = "select fornecedor.id_fornecedor,nome_fornecedor, nome_produto, data_contrato, produto.id_produto "
					+ "from fornecedor "
					+ "join fornecedor_produto as fornprod on fornecedor.id_fornecedor = fornprod.id_fornecedor "
					+ "join produto on produto.id_produto = fornprod.id_produto " + "order by id_fornecedor";

			myStmt = myConn.createStatement();

			// execute query
			myRs = myStmt.executeQuery(sql);
			List<Fornecedor> listaDeFornecedores = new ArrayList<Fornecedor>();
			List<Produto> listagemProdutos = new ArrayList<Produto>();

			Fornecedor fornecedor = null;
			int idFornecedorAtual = 0;

			// process result set
			while (myRs.next()) {

				// retrieve data from result set row
				int idFornecedor = myRs.getInt("id_fornecedor");
				String nomeFornecedor = myRs.getString("nome_fornecedor");
				Date dataContrato = myRs.getDate("data_contrato");
				Integer idProduto = myRs.getInt("id_produto");
				String nomeProduto = myRs.getString("nome_produto");
				Produto produto = new Produto(nomeProduto);
				produto.setIdProduto(idProduto);

				// existe um novo fornecedor na linha do banco
				if (idFornecedorAtual != idFornecedor) {
					// adiciona o antigo fornecedor na lista que vai retornar
					if (fornecedor != null) {
						fornecedor.setListagemProdutos(listagemProdutos);
						listaDeFornecedores.add(fornecedor);
					}
					Fornecedor tempFornecedor = new Fornecedor(nomeFornecedor, dataContrato);
					listagemProdutos = new ArrayList<Produto>();
					tempFornecedor.setIdFornecedor(idFornecedor);

					listagemProdutos.add(produto);
					fornecedor = tempFornecedor;
					idFornecedorAtual = idFornecedor;
				} else {
					listagemProdutos.add(produto);
					fornecedor.setListagemProdutos(listagemProdutos);
				}
			}
			if (fornecedor != null) {
				fornecedor.setListagemProdutos(listagemProdutos);
				listaDeFornecedores.add(fornecedor);
			}

			return listaDeFornecedores;

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

	public void addFornecedor(Fornecedor fornecedor) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			// get db connection
			myConn = dataSource.getConnection();

			// create sql for insert
			String sql = "insert into fornecedor (nome_fornecedor, data_contrato) values (?, ?)";

			myStmt = myConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			// set the param values for the student
			myStmt.setString(1, fornecedor.getNomeFornecedor());
			myStmt.setDate(2, new Date(fornecedor.getDataContrato().getTime()));

			// execute sql insert
			myStmt.execute();

			try (ResultSet keys = myStmt.getGeneratedKeys()) {
				keys.next();
				int id = keys.getInt("id_fornecedor");
				fornecedor.setIdFornecedor(id);

			}
			for (Produto produto : fornecedor.getListagemProdutos()) {
				this.saveRelationship(fornecedor, produto);
			}

		} finally {
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}

	}

	public Fornecedor getFornecedor(String idFornecedorString) throws Exception {

		Fornecedor fornecedor = null;

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int idFornecedor;

		try {
			// convert student id to int
			idFornecedor = Integer.parseInt(idFornecedorString);

			// get connection to database
			myConn = dataSource.getConnection();

			// create sql to get selected student
			String sql = "select * from fornecedor where id_fornecedor=?";

			// create prepared statement
			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, idFornecedor);

			// execute statement
			myRs = myStmt.executeQuery();

			// retrieve data from result set row
			if (myRs.next()) {
				String nomeFornecedor = myRs.getString("nome_fornecedor");
				Date dataContrato = myRs.getDate("data_contrato");
				// use the studentId during construction
				fornecedor = new Fornecedor(nomeFornecedor, dataContrato);
				fornecedor.setIdFornecedor(idFornecedor);

			} else {
				throw new Exception("Could not find fornecedor id: " + idFornecedor);
			}

			return fornecedor;
		} finally {
			// clean up JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

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
	public void deleteFornecedor(int idfornecedor) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			// get connection to database
			myConn = dataSource.getConnection();

			// create sql to delete student
			String sql = "delete from fornecedor where id_fornecedor=?";

			// prepare statement
			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, idfornecedor);

			// execute sql statement
			myStmt.execute();
		} finally {
			// clean up JDBC code
			close(myConn, myStmt, null);
		}
	}

	public void deleteRelationship(int idfornecedor) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			// get connection to database
			myConn = dataSource.getConnection();

			// create sql to delete student
			String sql = "delete from fornecedor_produto where id_fornecedor = (?)";

			// prepare statement
			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, idfornecedor);

			// execute sql statement
			myStmt.execute();
		} finally {
			// clean up JDBC code
			close(myConn, myStmt, null);
		}
	}

	public void saveRelationship(Fornecedor fornecedor, Produto produto) throws SQLException {
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			myConn = dataSource.getConnection();

			// create sql to delete student
			String sql = "insert into fornecedor_produto(id_fornecedor, id_produto) values (?,?)";

			// prepare statement
			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, fornecedor.getIdFornecedor());
			myStmt.setInt(2, produto.getIdProduto());

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

	public List<Produto> getProdutosFornecedor(int idFornecedor) throws Exception {

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			// get a connection
			myConn = dataSource.getConnection();

			myStmt = myConn.createStatement();
			// create sql statement
			String sql = "select * from fornecedor_produto"
					+ " join produto on produto.id_produto = fornecedor_produto.id_produto "
					+ "where id_fornecedor = (?)";

			myStmt = myConn.prepareStatement(sql);

			((PreparedStatement) myStmt).setInt(1, idFornecedor);

			myRs = myStmt.executeQuery(sql);

			Produto produto = null;
			List<Produto> listagemProdutos = new ArrayList<Produto>();

			while (myRs.next()) {

				int produtoId = myRs.getInt("id_produto");
				String nomeProduto = myRs.getString("nome_produto");
				produto = new Produto(nomeProduto);
				produto.setIdProduto(produtoId);
				listagemProdutos.add(produto);

			}

			return listagemProdutos;
		} finally {
			// close JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

}
