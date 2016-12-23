package br.com.solvus.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDao implements DAO<Fornecedor> {

	private final Connection connection;

	public FornecedorDao(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void save(Fornecedor fornecedor) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(
				"insert into fornecedor(nome_fornecedor, data_contrato) values (?,?)",
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, fornecedor.getNome());
			stmt.setDate(2, new Date(fornecedor.getDataContrato().getTime()));
			stmt.execute();

			try (ResultSet keys = stmt.getGeneratedKeys()) {
				keys.next();
				int id = keys.getInt("id_fornecedor");
				fornecedor.setId(id);

			}
			for (Produto produto : fornecedor.getListagemProdutos()) {
				this.saveRelationship(fornecedor, produto);
			}
		}

	}

	public void saveRelationship(Fornecedor fornecedor, Produto produto) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(
				"insert into fornecedor_produto(id_fornecedor, id_produto) values (?,?)",
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, fornecedor.getId());
			stmt.setInt(2, produto.getId());
			stmt.execute();

		}
	}

	@Override
	public void update(Fornecedor object) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Fornecedor findById(Integer idFornecedor) throws SQLException {
		String sql = "select * from fornecedor where id_fornecedor = (?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, idFornecedor);
			stmt.execute();

			ResultSet resultSet = stmt.getResultSet();
			Fornecedor fornecedor = null;
			while (resultSet.next()) {
				String nomeFornecedor = resultSet.getString("nome_fornecedor");
				Date dateContrato = resultSet.getDate("data_contrato");
				int id = resultSet.getInt("id_fornecedor");
				fornecedor = new Fornecedor(nomeFornecedor, dateContrato);
				fornecedor.setId(id);
			}

			String sql1 = "select * from fornecedor_produto"
					+ " join produto on produto.id_produto = fornecedor_produto.id_produto "
					+ "where id_fornecedor = (?)";
			try (PreparedStatement pstmt = connection.prepareStatement(sql1)) {
				pstmt.setInt(1, idFornecedor);
				pstmt.execute();

				ResultSet resultSet1 = pstmt.getResultSet();
				Produto produto = null;
				List<Produto> listagemProdutos = new ArrayList<Produto>();

				while (resultSet1.next()) {
					int produtoId = resultSet1.getInt("id_produto");
					String nomeProduto = resultSet1.getString("nome_produto");
					produto = new Produto(nomeProduto);
					produto.setId(produtoId);
					listagemProdutos.add(produto);
				}
				
				fornecedor.setListagemProdutos(listagemProdutos);
				return fornecedor;
			}
		}
	}

	@Override
	public List<Fornecedor> list() throws SQLException {
		String sql = "select * from fornecedor";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.execute();

			ResultSet resultSet = stmt.getResultSet();
			ArrayList<Fornecedor> fornecedores = new ArrayList<>();
			while (resultSet.next()) {
				String nome = resultSet.getString("nome_fornecedor");
				int id = resultSet.getInt("id_fornecedor");
				Date dataContrato = resultSet.getDate("data_contrato");
				Fornecedor f = new Fornecedor(nome, dataContrato);
				f.setId(id);
				fornecedores.add(f);
			}
			return fornecedores;
		}
	}

	public List<Fornecedor> listWithRelationship() throws SQLException {
		String sql = "select fornecedor.id_fornecedor,nome_fornecedor, nome_produto, data_contrato, produto.id_produto "
				+ "from fornecedor "
				+ "join fornecedor_produto as fornprod on fornecedor.id_fornecedor = fornprod.id_fornecedor "
				+ "join produto on produto.id_produto = fornprod.id_produto " + "order by id_fornecedor";
		List<Fornecedor> listaDeFornecedores = new ArrayList<Fornecedor>();
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.execute();

			ResultSet resultSet = stmt.getResultSet();
			List<Produto> listaDeProdutos = new ArrayList<Produto>();

			Fornecedor fornecedor = null;
			int idFornecedorAtual = 0;
			while (resultSet.next()) {

				Integer id = resultSet.getInt("id_fornecedor");

				String nomeFornecedor = resultSet.getString("nome_fornecedor");
				String nomeProduto = resultSet.getString("nome_produto");
				Integer idProduto = resultSet.getInt("id_produto");
				Date dataContrato = resultSet.getDate("data_contrato");
				Produto produto = new Produto(nomeProduto);
				produto.setId(idProduto);

				// existe um novo fornecedor na linha do banco
				if (idFornecedorAtual != id) {
					// adiciona o antigo fornecedor na lista que vai retornar
					if (fornecedor != null) {
						fornecedor.setListagemProdutos(listaDeProdutos);
						listaDeFornecedores.add(fornecedor);
					}
					Fornecedor f = new Fornecedor(nomeFornecedor, dataContrato);
					listaDeProdutos = new ArrayList<Produto>();
					f.setId(id);
					
					listaDeProdutos.add(produto);
					fornecedor = f;
					idFornecedorAtual = id;
				} else {
					listaDeProdutos.add(produto);
					fornecedor.setListagemProdutos(listaDeProdutos);
				}
			}
			if (fornecedor != null) {
				fornecedor.setListagemProdutos(listaDeProdutos);
				listaDeFornecedores.add(fornecedor);
			}

		}

		return listaDeFornecedores;
	}

	@Override
	public void deleteById(Integer idFornecedor) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement("delete from fornecedor where id_fornecedor = (?)",
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, idFornecedor);
			stmt.execute();
		}

	}

	public boolean checkIfDuplicate(String nomeFornecedor) throws SQLException {
		String sql = "select * from fornecedor where nome_fornecedor = (?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, nomeFornecedor);
			stmt.execute();

			ResultSet resultSet = stmt.getResultSet();

			while (resultSet.next()) {
				return true;

			}
		}
		return false;
	}

	public void deleteRelationship(int idFornecedor) throws SQLException {

		try (PreparedStatement stmt = connection.prepareStatement(
				"delete from fornecedor_produto where id_fornecedor = (?)", Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, idFornecedor);
			stmt.execute();

		}
	}

}
