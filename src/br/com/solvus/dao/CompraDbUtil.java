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

import br.com.solvus.model.Compra;
import br.com.solvus.model.Fornecedor;
import br.com.solvus.model.ItemDeCompra;
import br.com.solvus.model.Produto;

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

	public Compra getCompra(int idCompra) throws Exception {

		Fornecedor fornecedor = null;
		Compra compra = null;

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = dataSource.getConnection();

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
			close(myConn, myStmt, myRs);
		}
	}

	public void addCompra(Compra compra) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = dataSource.getConnection();

			String sql = "insert into compra(id_fornecedor, data_compra, valor_total) values (?,?,?)";

			myStmt = myConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			myStmt.setInt(1, compra.getFornecedor().getIdFornecedor());
			myStmt.setDate(2, new Date(compra.getDataCompra().getTime()));
			myStmt.setDouble(3, compra.getValorTotal());

			myStmt.execute();

			try (ResultSet keys = myStmt.getGeneratedKeys()) {
				keys.next();
				int id = keys.getInt("id_compra");
				compra.setIdCompra(id);

			}
		} finally {
			close(myConn, myStmt, null);
		}

	}

	public void addItemDeCompra(Compra compra) throws SQLException {
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = dataSource.getConnection();

			String sql = "insert into itemdecompra(id_produto, quantidade, valor_unitario) values (?,?,?)";

			myStmt = myConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			for (ItemDeCompra itemdecompra : compra.getListaDeItemDeCompra()) {
				myStmt.setInt(1, itemdecompra.getProduto().getIdProduto());
				myStmt.setInt(2, itemdecompra.getQuantidade());
				myStmt.setDouble(3, itemdecompra.getValorUnitario());
				myStmt.execute();

				try (ResultSet keys = myStmt.getGeneratedKeys()) {
					keys.next();
					int idItemDeCompra = keys.getInt("id_itemdecompra");
					itemdecompra.setIdItemDeCompra(idItemDeCompra);
				}
			}
		} finally {
			close(myConn, myStmt, null);
		}

	}

	public void saveRelationship(Compra compra) throws SQLException {
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = dataSource.getConnection();

			String sql = "update itemdecompra set id_compra = (?) where id_itemdecompra = (?)";
			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, compra.getIdCompra());

			for (ItemDeCompra itemDeCompra : compra.getListaDeItemDeCompra()) {
				myStmt.setInt(2, itemDeCompra.getIdItemDeCompra());
				myStmt.execute();
			}
		} finally {
			close(myConn, myStmt, null);
		}

	}

	public void deleteCompra(int idCompra) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			myConn = dataSource.getConnection();

			String sql = "delete from compra where id_compra=?";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, idCompra);

			myStmt.execute();
		} finally {
			close(myConn, myStmt, null);
		}
	}

	public void deleteRelationship(int idCompra) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			myConn = dataSource.getConnection();

			String sql = "delete from itemdecompra where id_compra = (?)";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, idCompra);

			myStmt.execute();
		} finally {
			close(myConn, myStmt, null);
		}
	}

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
			close(myConn, myStmt, myRs);
		}
	}
	
	
	public List<Compra> filterListaCompra(int idFornecedorSelecionado) throws Exception {

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

			String sql = "select * from compra join itemdecompra as tabelacompraitem on compra.id_compra = tabelacompraitem.id_compra where id_fornecedor = (?) ";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, idFornecedorSelecionado);
			
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
			close(myConn, myStmt, myRs);
		}
	}

	public boolean isDataCompraValid(Compra compra) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		boolean isValid = true;

		java.util.Date dataCompra = compra.getDataCompra();
		java.sql.Date dataCompraSql = new java.sql.Date(dataCompra.getTime());

		int idFornecedor = compra.getFornecedor().getIdFornecedor();

		try {

			myConn = dataSource.getConnection();

			String sql = "select * from fornecedor where id_fornecedor = (?) and data_contrato > (?)";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, idFornecedor);
			myStmt.setDate(2, dataCompraSql);
			myStmt.execute();
			
			ResultSet resultSet = myStmt.getResultSet();

			if (resultSet.next()) {
				isValid =  false;
			}
			
		} finally {
			close(myConn, myStmt, null);
		}

		
		return isValid;
		
	}
}
