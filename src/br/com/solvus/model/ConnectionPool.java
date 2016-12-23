package br.com.solvus.model;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.postgresql.jdbc3.Jdbc3PoolingDataSource;

public class ConnectionPool {
	
	private final DataSource dataSource;

	public ConnectionPool() {
		System.out.print("iniciando connection pool...");
		Jdbc3PoolingDataSource pool = new Jdbc3PoolingDataSource();
		pool.setUrl("jdbc:postgresql://localhost:5432/CadatroFornecedores");
		pool.setUser("postgres");
		pool.setPassword("123456");
		dataSource = pool;
		System.out.println(" ok");
	}
	
	public Connection getConnection() {
		System.out.print("adquirindo conexao...");
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(" ok");
		return connection;
	}	
	
	public static ConnectionPool CONNECTIONPOOL = new ConnectionPool();

}
