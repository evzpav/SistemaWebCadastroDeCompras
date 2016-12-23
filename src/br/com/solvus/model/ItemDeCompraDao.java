package br.com.solvus.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemDeCompraDao implements DAO<ItemDeCompra> {

	private final Connection connection;
	private ProdutoDao produtoDao;

	public ItemDeCompraDao(Connection connection) {
		this.connection = connection;
		produtoDao = new ProdutoDao(connection);
	}

	public void save(ItemDeCompra itemdecompra, Compra compra) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(
				"insert into itemdecompra(id_produto, quantidade, valor_unitario) values (?,?,?)",
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, itemdecompra.getProduto().getId());
			stmt.setInt(2, itemdecompra.getQuantidade());
			stmt.setDouble(3, itemdecompra.getValorUnitario());
			stmt.execute();

			try (ResultSet keys = stmt.getGeneratedKeys()) {
				keys.next();
				int idItemDeCompra = keys.getInt("id_itemdecompra");
				itemdecompra.setIdItemDeCompra(idItemDeCompra);
			}

			// for (Integer itemdecompra1 : compra.getListaDeItemDeCompra()) {
			// this.saveRelationship(itemdecompra1, compra);
			// }

		}

	}

	public void saveRelationship(List<ItemDeCompra> listaDeItemDeCompraAdicionadosNaTabela, Compra compra)
			throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(
				"update itemdecompra set id_compra = (?) " + "where id_itemdecompra = (?)",
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, compra.getId());
			for (ItemDeCompra itemDeCompra : listaDeItemDeCompraAdicionadosNaTabela) {
				stmt.setInt(2, itemDeCompra.getIdItemDeCompra());
				stmt.execute();
			}

		}
	}

	@Override
	public ItemDeCompra findById(Integer idItemDeCompra) throws SQLException {
		String sql = "select * from itemdecompra where id_itemdecompra = (?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, idItemDeCompra);
			stmt.execute();

			ResultSet resultSet = stmt.getResultSet();
			ItemDeCompra itemdecompra = null;
			Produto produto = null;
			while (resultSet.next()) {
				int idProduto = resultSet.getInt("id_produto");
				int quantidade = resultSet.getInt("quantidade");
				double valorUnitario = resultSet.getDouble("valor_unitario");
				produto = produtoDao.findById(idProduto);
				itemdecompra = new ItemDeCompra(produto, quantidade, valorUnitario);
				itemdecompra.setIdItemDeCompra(idItemDeCompra);
			}

			return itemdecompra;
		}
	}

	@Override
	public void deleteById(Integer idItemDeCompra) throws SQLException {
		String sql = "delete from itemdecompra where id_itemdecompra = (?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, idItemDeCompra);
			stmt.execute();
		}

	}

	public List<ItemDeCompra> list(int idCompra) throws SQLException {
		String sql = "select distinct * from itemdecompra where id_compra = (?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, idCompra);
			stmt.execute();

			List<ItemDeCompra> listaItemDeCompra = new ArrayList<ItemDeCompra>();
			ResultSet resultSet = stmt.getResultSet();

			while (resultSet.next()) {
				Produto produto = null;
				ItemDeCompra itemdecompra = null;
				int idProduto = resultSet.getInt("id_produto");
				int idItemDeCompra = resultSet.getInt("id_itemdecompra");
				Integer quantidade = resultSet.getInt("quantidade");
				Double valorUnitario = resultSet.getDouble("valor_unitario");
				produto = produtoDao.findById(idProduto);
				itemdecompra = new ItemDeCompra(produto, quantidade, valorUnitario);
				itemdecompra.setIdItemDeCompra(idItemDeCompra);
				listaItemDeCompra.add(itemdecompra);

			}
			return listaItemDeCompra;
		}
	}

	@Override
	public void update(ItemDeCompra itemdecompra) throws SQLException {
		try (PreparedStatement stmt = connection
				.prepareStatement("update itemdecompra set (id_produto,quantidade,valor_unitario) = (?,?,?)"
						+ " where id_itemdecompra = (?)", Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, itemdecompra.getProduto().getId());
			stmt.setInt(2, itemdecompra.getQuantidade());
			stmt.setDouble(3, itemdecompra.getValorUnitario());
			stmt.setInt(4, itemdecompra.getIdItemDeCompra());
			stmt.execute();

		}

	}

	@Override
	public void save(ItemDeCompra itemdecompra) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(
				"insert into itemdecompra(id_produto, quantidade, valor_unitario) values (?,?,?)",
				Statement.RETURN_GENERATED_KEYS)) {
			int idProduto = produtoDao.findIdProduto(itemdecompra.getProduto());

			stmt.setInt(1, idProduto);
			stmt.setInt(2, itemdecompra.getQuantidade());
			stmt.setDouble(3, itemdecompra.getValorUnitario());
			stmt.execute();

			try (ResultSet keys = stmt.getGeneratedKeys()) {
				keys.next();
				int idItemDeCompra = keys.getInt("id_itemdecompra");
				itemdecompra.setIdItemDeCompra(idItemDeCompra);
			}

		}

	}

	@Override
	public List<ItemDeCompra> list() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean produtoHasRelationshipCompra(int idProduto) throws SQLException {

		boolean retorno = false;
		try (PreparedStatement stmt = connection.prepareStatement(
				"select * from itemdecompra where id_produto = (?)",
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, idProduto);
			stmt.execute();

			ResultSet resultSet = stmt.getResultSet();

			while (resultSet.next()) {
				retorno = true;
			}
		}
		return retorno;
	}
	
}
