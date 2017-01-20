package br.com.solvus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import br.com.solvus.model.ItemDeCompra;
import br.com.solvus.model.Produto;

public class ItemDeCompraDbUtil {

	ProdutoDbUtil produtoDbUtil;
	
	private DataSource dataSource;

	
	public ItemDeCompraDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
		produtoDbUtil = new ProdutoDbUtil(theDataSource);
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

	public List<ItemDeCompra> listItemDeCompra(int idCompra) throws Exception {

		List<ItemDeCompra> listaItemDeCompra = new ArrayList<ItemDeCompra>();
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
	//	int idCompra;

		try {
			// convert student id to int
		//	idCompra = Integer.parseInt(idCompraString);

			// get connection to database
			myConn = dataSource.getConnection();

			// create sql to get selected student
			String sql = "select distinct * from itemdecompra where id_compra = (?)";

			// create prepared statement
			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, idCompra);

			// execute statement
			myRs = myStmt.executeQuery();

			// retrieve data from result set row
			if (myRs.next()) {
				
				ItemDeCompra itemdecompra = null;
				int idProduto = myRs.getInt("id_produto");
				int idItemDeCompra = myRs.getInt("id_itemdecompra");
				Integer quantidade = myRs.getInt("quantidade");
				Double valorUnitario = myRs.getDouble("valor_unitario");

				Produto produto = produtoDbUtil.getProduto(idProduto);
				
				itemdecompra = new ItemDeCompra(produto, quantidade, valorUnitario);
				itemdecompra.setIdItemDeCompra(idItemDeCompra);
				listaItemDeCompra.add(itemdecompra);
				

			} else {
				throw new Exception("Could not find produto id: " + idCompra);
			}

			return listaItemDeCompra;
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
