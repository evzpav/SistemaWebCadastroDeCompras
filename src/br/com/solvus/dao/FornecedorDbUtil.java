package br.com.solvus.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import br.com.solvus.model.Fornecedor;
import br.com.solvus.model.Produto;

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
			myConn = dataSource.getConnection();

			String sql = "select fornecedor.id_fornecedor,nome_fornecedor, nome_produto, data_contrato, produto.id_produto "
					+ "from fornecedor "
					+ "join fornecedor_produto as fornprod on fornecedor.id_fornecedor = fornprod.id_fornecedor "
					+ "join produto on produto.id_produto = fornprod.id_produto "
					+ "order by nome_fornecedor";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);
			List<Fornecedor> listaDeFornecedores = new ArrayList<Fornecedor>();
			List<Produto> listagemProdutos = new ArrayList<Produto>();

			Fornecedor fornecedor = null;
			int idFornecedorAtual = 0;

			while (myRs.next()) {

				int idFornecedor = myRs.getInt("id_fornecedor");
				String nomeFornecedor = myRs.getString("nome_fornecedor");
				Date dataContrato = myRs.getDate("data_contrato");
				Integer idProduto = myRs.getInt("id_produto");
				String nomeProduto = myRs.getString("nome_produto");
				Produto produto = new Produto(nomeProduto);
				produto.setIdProduto(idProduto);

				if (idFornecedorAtual != idFornecedor) {
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
			close(myConn, myStmt, myRs);

		}
	}

	public List<Fornecedor> getFornecedoresWithCompra() throws SQLException {

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = dataSource.getConnection();

			String sql = "select fornecedor.id_fornecedor,nome_fornecedor, nome_produto, data_contrato, produto.id_produto from fornecedor "
					+ "join fornecedor_produto as fornprod on fornecedor.id_fornecedor = fornprod.id_fornecedor "
					+ "join produto on produto.id_produto = fornprod.id_produto "
					+ "join compra on compra.id_fornecedor = fornecedor.id_fornecedor "
					+ "order by nome_fornecedor";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);
			List<Fornecedor> listaDeFornecedores = new ArrayList<Fornecedor>();
			List<Produto> listagemProdutos = new ArrayList<Produto>();

			Fornecedor fornecedor = null;
			int idFornecedorAtual = 0;

			while (myRs.next()) {

				int idFornecedor = myRs.getInt("id_fornecedor");
				String nomeFornecedor = myRs.getString("nome_fornecedor");
				Date dataContrato = myRs.getDate("data_contrato");
				Integer idProduto = myRs.getInt("id_produto");
				String nomeProduto = myRs.getString("nome_produto");
				Produto produto = new Produto(nomeProduto);
				produto.setIdProduto(idProduto);

				if (idFornecedorAtual != idFornecedor) {
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

	public void addFornecedor(Fornecedor fornecedor) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = dataSource.getConnection();

			String sql = "insert into fornecedor (nome_fornecedor, data_contrato) values (?, ?)";

			myStmt = myConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			myStmt.setString(1, fornecedor.getNomeFornecedor());
			myStmt.setDate(2, new Date(fornecedor.getDataContrato().getTime()));

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
			close(myConn, myStmt, null);
		}

	}

	public Fornecedor getFornecedor(int idFornecedor) throws Exception {

		Fornecedor fornecedor = null;

		Connection myConn = null;
		PreparedStatement myStmt = null;
		PreparedStatement myStmt1 = null;
		ResultSet myRs = null;
		ResultSet myRs1 = null;

		try {

			myConn = dataSource.getConnection();

			String sql = "select * from fornecedor where id_fornecedor=?";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, idFornecedor);

			myRs = myStmt.executeQuery();

			if (myRs.next()) {
				String nomeFornecedor = myRs.getString("nome_fornecedor");
				Date dataContrato = myRs.getDate("data_contrato");

				fornecedor = new Fornecedor(nomeFornecedor, dataContrato);
				fornecedor.setIdFornecedor(idFornecedor);

				// colocar getRelationship

			} else {
				throw new Exception("Could not find fornecedor id: " + idFornecedor);
			}

			String sql1 = "select * from fornecedor_produto"
					+ " join produto on produto.id_produto = fornecedor_produto.id_produto "
					+ "where id_fornecedor = (?)";

			myStmt1 = myConn.prepareStatement(sql1);

			myStmt1.setInt(1, idFornecedor);

			myRs1 = myStmt1.executeQuery();

			myRs1 = myStmt1.getResultSet();

			Produto produto = null;
			List<Produto> listagemProdutos = new ArrayList<Produto>();

			while (myRs1.next()) {
				int idProduto = myRs1.getInt("id_produto");
				String nomeProduto = myRs1.getString("nome_produto");
				produto = new Produto(nomeProduto);
				produto.setIdProduto(idProduto);
				listagemProdutos.add(produto);
			}
			fornecedor.setListagemProdutos(listagemProdutos);

			return fornecedor;

		} finally {
			close(myConn, myStmt, myRs);
		}
	}

	public void updateFornecedor(Fornecedor fornecedor) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = dataSource.getConnection();

			String sql = "update fornecedor set (nome_fornecedor, data_contrato) = (?,?) where id_fornecedor = (?)";

			myStmt = myConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			myStmt.setString(1, fornecedor.getNomeFornecedor());
			myStmt.setDate(2, new Date(fornecedor.getDataContrato().getTime()));
			myStmt.setInt(3, fornecedor.getIdFornecedor());
			myStmt.execute();

			for (Produto produto : fornecedor.getListagemProdutos()) {
				this.saveRelationship(fornecedor, produto);
			}

		} finally {
			close(myConn, myStmt, null);
		}
	}

	public void deleteFornecedor(int idfornecedor) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			myConn = dataSource.getConnection();

			String sql = "delete from fornecedor where id_fornecedor=(?)";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, idfornecedor);

			myStmt.execute();
		} finally {

			close(myConn, myStmt, null);
		}
	}

	public void deleteRelationship(int idfornecedor) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			myConn = dataSource.getConnection();

			String sql = "delete from fornecedor_produto where id_fornecedor = (?)";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, idfornecedor);

			myStmt.execute();
		} finally {

			close(myConn, myStmt, null);
		}
	}

	public void saveRelationship(Fornecedor fornecedor, Produto produto) throws SQLException {
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			myConn = dataSource.getConnection();

			String sql = "insert into fornecedor_produto(id_fornecedor, id_produto) values (?,?)";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, fornecedor.getIdFornecedor());
			myStmt.setInt(2, produto.getIdProduto());

			myStmt.execute();

		} finally {

			close(myConn, myStmt, null);
		}

	}

	public List<Produto> getProdutosFornecedor(int idFornecedor) throws Exception {

		Connection myConn = null;
		ResultSet myRs = null;
		PreparedStatement myStmt = null;

		try {
			myConn = dataSource.getConnection();

			String sql = "select * from fornecedor_produto"
					+ " join produto on produto.id_produto = fornecedor_produto.id_produto "
					+ "where id_fornecedor = (?)";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, idFornecedor);

			myRs = myStmt.executeQuery();

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
			close(myConn, myStmt, myRs);
		}
	}

	public boolean hasRelationshipCompra(int idFornecedor) throws SQLException {
	
		boolean retorno = false;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = dataSource.getConnection();

			String sql = "select * from compra where id_fornecedor = (?)";

			myStmt = myConn.prepareStatement(sql);
			
			myStmt.setInt(1, idFornecedor);

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
	
	public boolean checkIfDuplicate(String nomeFornecedor) throws SQLException {

		
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = dataSource.getConnection();
			
			String sql = "select * from fornecedor where nome_fornecedor = (?)";

			myStmt = myConn.prepareStatement(sql);
			
			myStmt.setString(1, nomeFornecedor);

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
