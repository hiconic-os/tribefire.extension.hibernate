package tribefire.extension.hibernate;

import java.util.UUID;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

public interface DataSources {
	static DataSource newDataSource() {
		String key = "hibernate-support-test-db-" + UUID.randomUUID().toString();

		String url = "jdbc:h2:mem:" + key + ";DB_CLOSE_DELAY=-1";
		
		HikariDataSource result = new HikariDataSource();

		result.setDriverClassName("org.h2.Driver");
		result.setJdbcUrl(url);

		return result;
	}
}
