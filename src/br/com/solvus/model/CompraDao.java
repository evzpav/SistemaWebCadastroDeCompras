package br.com.solvus.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompraDao implements DAO<Compra> {

	private final Connection connection;
	private FornecedorDao fornecedorDao;

	public CompraDao(Connection connection) {
		this.connection = connection;
		fornecedorDao = new FornecedorDao(connection);
	}

	@Override
	public void save(Compra compra) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(
				"insert into compra(id_fornecedor, data_compra, valor_total) values (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, compra.getFornecedor().getId());
			stmt.setDate(2, new Date(compra.getDataCompra().getTime()));
			stmt.setDouble(3,  compra.getValorTotal());
			stmt.execute();

			try (ResultSet keys = stmt.getGeneratedKeys()) {
				keys.next();
				int id = keys.getInt("id_compra");
				compra.setId(id);
				
			}

		}

	}

	@Override
	public void update(Compra compra) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(
				"update compra set (id_fornecedor, data_compra) = (?,?) where id_compra = (?)",
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, compra.getFornecedor().getId());
			stmt.setDate(2, new Date(compra.getDataCompra().getTime()));
			stmt.setInt(3, compra.getId());
			stmt.execute();

		}

	}

	@Override
	public Compra findById(Integer idCompra) throws SQLException {
		Compra compra = null;
		String sql = "select * from compra where id_compra = (?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, idCompra);
			stmt.execute();

			Fornecedor fornecedor = null;
			ResultSet resultSet = stmt.getResultSet();
			while (resultSet.next()) {
				Date dataCompra = resultSet.getDate("data_compra");
				int idFornecedor = resultSet.getInt("id_fornecedor");
				fornecedor = fornecedorDao.findById(idFornecedor);
				compra = new Compra(fornecedor, dataCompra);
				compra.setId(idCompra);
			}
		}
		return compra;
	}



	@Override
	public void deleteById(Integer idCompra) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement("delete from compra where id_compra = (?)",
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, idCompra);
			stmt.execute();
		}

	}

	public void deleteRelationship(int idCompra) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement("delete from itemdecompra where id_compra = (?)",
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, idCompra);
			stmt.execute();
		}

	}

	@Override
	public List<Compra> list() throws SQLException {
		String sql = "select * from compra";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.execute();

			ResultSet resultSet = stmt.getResultSet();
			ArrayList<Compra> listaCompra = new ArrayList<>();
			Fornecedor fornecedor = null;
			Compra compra = null;
			while (resultSet.next()) {
				int idCompra = resultSet.getInt("id_compra");
				int idFornecedor = resultSet.getInt("id_fornecedor");
				Date dataCompra = resultSet.getDate("data_compra");
				fornecedor = fornecedorDao.findById(idFornecedor);
				compra = new Compra(fornecedor, dataCompra);
				Double valorTotal = resultSet.getDouble("valor_total");
				compra.setValorTotal(valorTotal);
	//			compra.setListaDeItemDeCompra(listaDeItemDeCompra);
				compra.setId(idCompra);
				listaCompra.add(compra);
			}
			return listaCompra;
		}
	}

	public List<Compra> filtrarListaCompra(Fornecedor fornecedorSelecionadoNoCombo, java.sql.Date dataInicial,
			java.sql.Date dataFinal) throws SQLException {
		String sql = "select * from compra where id_fornecedor = (?) and data_compra between (?) and (?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, fornecedorSelecionadoNoCombo.getId());
			stmt.setDate(2, dataInicial);
			stmt.setDate(3, dataFinal);
			stmt.execute();

			ResultSet resultSet = stmt.getResultSet();
			ArrayList<Compra> listaCompra = new ArrayList<>();
			Fornecedor fornecedor = null;
			Compra compra = null;
			while (resultSet.next()) {
				int idCompra = resultSet.getInt("id_compra");
				int idFornecedor = resultSet.getInt("id_fornecedor");
				Date dataCompra = resultSet.getDate("data_compra");
				Double valorTotal = resultSet.getDouble("valor_total");
				fornecedor = fornecedorDao.findById(idFornecedor);
				compra = new Compra(fornecedor, dataCompra);
				compra.setValorTotal(valorTotal);
				listaCompra.add(compra);
				compra.setId(idCompra);
			}
			return listaCompra;

		}

	}

	public List<Compra> filtrarPorFornecedorApenas(Fornecedor fornecedorSelecionadoNoCombo) throws SQLException {
		String sql = "select * from compra where id_fornecedor = (?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, fornecedorSelecionadoNoCombo.getId());
			stmt.execute();

			ResultSet resultSet = stmt.getResultSet();
			ArrayList<Compra> listaCompra = new ArrayList<>();
			Fornecedor fornecedor = null;
			Compra compra = null;
			while (resultSet.next()) {
				int idCompra = resultSet.getInt("id_compra");
				int idFornecedor = resultSet.getInt("id_fornecedor");
				Date dataCompra = resultSet.getDate("data_compra");
				Double valorTotal = resultSet.getDouble("valor_total");
				fornecedor = fornecedorDao.findById(idFornecedor);
				compra = new Compra(fornecedor, dataCompra);
				compra.setValorTotal(valorTotal);
				listaCompra.add(compra);
				compra.setId(idCompra);
			}
			return listaCompra;
		}
	}
	

	
	public boolean fornecedorHasRelationshipCompra(int idFornecedor) throws SQLException {

		boolean retorno = false;
		try (PreparedStatement stmt = connection.prepareStatement(
				"select * from compra where id_fornecedor = (?)",
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, idFornecedor);
			stmt.execute();

			ResultSet resultSet = stmt.getResultSet();

			while (resultSet.next()) {
				retorno = true;
			}
		}
		return retorno;
	}
}
