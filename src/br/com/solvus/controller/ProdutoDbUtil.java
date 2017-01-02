package br.com.solvus.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import br.com.solvus.jdbc.Produto;

public class ProdutoDbUtil {

	private DataSource dataSource;

	public ProdutoDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}

	public List<Produto> getProdutos() throws Exception {

		List<Produto> produtos = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			// get a connection
			myConn = dataSource.getConnection();

			// create sql statement
			String sql = "select * from produto order by nome_produto";

			myStmt = myConn.createStatement();

			// execute query
			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {

				// retrieve data from result set row
				int idProduto = myRs.getInt("id_produto");
				String nomeProduto = myRs.getString("nome_produto");

				// create new produto object

				Produto tempProduto = new Produto(nomeProduto);
				tempProduto.setIdProduto(idProduto);
				// add it to the list of students
				produtos.add(tempProduto);
			}

			return produtos;
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

	public void addProduto(Produto produto) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

	
		if(!checkIfDuplicate(produto.getNomeProduto())){
			
		
		try {
			// get db connection
			myConn = dataSource.getConnection();

			// create sql for insert
			String sql = "insert into produto (nome_produto) values (?)";

			myStmt = myConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			// set the param values for the student
			myStmt.setString(1, produto.getNomeProduto());

			// execute sql insert
			myStmt.execute();

			try (ResultSet keys = myStmt.getGeneratedKeys()) {
				keys.next();
				int id = keys.getInt("id_produto");
				produto.setIdProduto(id);
			}
		} finally {
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}
		}
	}

	public Produto getProduto(String idProdutoString) throws Exception {

		Produto produto = null;

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int idProduto;

		try {
			// convert student id to int
			idProduto = Integer.parseInt(idProdutoString);

			// get connection to database
			myConn = dataSource.getConnection();

			// create sql to get selected student
			String sql = "select * from produto where id_produto=(?) order by nome_produto";

			// create prepared statement
			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, idProduto);

			// execute statement
			myRs = myStmt.executeQuery();

			// retrieve data from result set row
			if (myRs.next()) {
				String nomeProduto = myRs.getString("nome_produto");

				// use the studentId during construction
				produto = new Produto(nomeProduto);
				produto.setIdProduto(idProduto);

			} else {
				throw new Exception("Could not find produto id: " + idProduto);
			}

			return produto;
		} finally {
			// clean up JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

	public void updateProduto(Produto produto) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			// get db connection
			myConn = dataSource.getConnection();

			// create SQL update statement
			String sql = "update produto set nome_produto = (?) where id_produto = (?)";

			// prepare statement
			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setString(1, produto.getNomeProduto());
			myStmt.setInt(2, produto.getIdProduto());
			// execute SQL statement
			myStmt.execute();
		} finally {
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}
	}

	public void deleteProduto(int produtoId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			// get connection to database
			myConn = dataSource.getConnection();

			// create sql to delete student
			String sql = "delete from produto where id_produto=?";

			// prepare statement
			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, produtoId);

			// execute sql statement
			myStmt.execute();
		} finally {
			// clean up JDBC code
			close(myConn, myStmt, null);
		}
	}

	public boolean hasRelationshipFornecedor(int produtoId) throws SQLException {

		boolean retorno = false;
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			// get db connection
			myConn = dataSource.getConnection();

			String sql = "select * from fornecedor_produto where fornecedor_produto.id_produto = (?)";

			myStmt = myConn.prepareStatement(sql);
			
			myStmt.setInt(1, produtoId);

			myStmt.execute();

			ResultSet resultSet = myStmt.getResultSet();

			while (resultSet.next()) {
				retorno = true;
			}

		} finally {
			close(myConn, myStmt, null);
		}

		return retorno;
	}

	public boolean checkIfDuplicate(String nomeProduto) throws SQLException {

		boolean retorno = false;
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = dataSource.getConnection();
			
			String sql = "select * from produto where nome_produto = (?)";

			myStmt = myConn.prepareStatement(sql);
			
			myStmt.setString(1, nomeProduto);

			myStmt.execute();

			ResultSet resultSet = myStmt.getResultSet();

			while (resultSet.next()) {
				retorno = true;
			}
		} finally {
			close(myConn, myStmt, null);
		}
		return retorno;
	}
}
