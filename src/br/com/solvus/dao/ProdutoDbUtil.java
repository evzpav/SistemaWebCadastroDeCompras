package br.com.solvus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import br.com.solvus.model.Produto;

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
			myConn = dataSource.getConnection();

			String sql = "select * from produto order by nome_produto";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			while (myRs.next()) {

				int idProduto = myRs.getInt("id_produto");
				String nomeProduto = myRs.getString("nome_produto");


				Produto tempProduto = new Produto(nomeProduto);
				tempProduto.setIdProduto(idProduto);
				produtos.add(tempProduto);
			}

			return produtos;
		} finally {
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
				myConn.close(); 
					
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
			myConn = dataSource.getConnection();

			String sql = "insert into produto (nome_produto) values (?)";

			myStmt = myConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			myStmt.setString(1, produto.getNomeProduto());

			myStmt.execute();

			try (ResultSet keys = myStmt.getGeneratedKeys()) {
				keys.next();
				int id = keys.getInt("id_produto");
				produto.setIdProduto(id);
			}
		} finally {
			close(myConn, myStmt, null);
		}
		}
	}

	public Produto getProduto(int idProduto) throws Exception {

		Produto produto = null;

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {

			myConn = dataSource.getConnection();

			String sql = "select * from produto where id_produto=(?) order by nome_produto";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, idProduto);

			myRs = myStmt.executeQuery();

			if (myRs.next()) {
				String nomeProduto = myRs.getString("nome_produto");

				produto = new Produto(nomeProduto);
				produto.setIdProduto(idProduto);

			} else {
				throw new Exception("Could not find produto id: " + idProduto);
			}

			return produto;
		} finally {
			close(myConn, myStmt, myRs);
		}
	}

	public void updateProduto(Produto produto) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = dataSource.getConnection();

			String sql = "update produto set nome_produto = (?) where id_produto = (?)";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setString(1, produto.getNomeProduto());
			myStmt.setInt(2, produto.getIdProduto());
			myStmt.execute();
		} finally {
			close(myConn, myStmt, null);
		}
	}

	public void deleteProduto(int produtoId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			myConn = dataSource.getConnection();

			String sql = "delete from produto where id_produto=?";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, produtoId);

			myStmt.execute();
			
		} finally {
			close(myConn, myStmt, null);
		}
	}

	public boolean hasRelationshipFornecedor(int idProduto) throws SQLException {

		boolean retorno = false;
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = dataSource.getConnection();

			String sql = "select * from fornecedor_produto where fornecedor_produto.id_produto = (?)";

			myStmt = myConn.prepareStatement(sql);
			
			myStmt.setInt(1, idProduto);

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
				return true;
			}
		} finally {
			close(myConn, myStmt, null);
		}
		return false;
	}
}
