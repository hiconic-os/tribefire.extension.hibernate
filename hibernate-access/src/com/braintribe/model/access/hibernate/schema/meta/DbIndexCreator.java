package com.braintribe.model.access.hibernate.schema.meta;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import com.braintribe.cfg.Required;
import com.braintribe.logging.Logger;
import com.braintribe.model.processing.deployment.hibernate.mapping.index.IndexDescriptor;
import com.braintribe.util.jdbc.JdbcTools;
import com.braintribe.util.jdbc.dialect.JdbcDialect;

/**
 * @author peter.gazdik
 */
public class DbIndexCreator {

	private static final Logger log = Logger.getLogger(DbUpdateStatementExecutor.class);

	private String contextDescription;
	private DataSource dataSource;
	private List<IndexDescriptor> indexDescriptors;

	private boolean supportsIfNotExists;

	// @formatter:off
	/** Context description for logging purposes, e.g. HibernateAccess my.access.id */
	@Required public void setContextDescription(String contextDescription) { this.contextDescription = contextDescription; }
	@Required public void setIndexDescriptors(List<IndexDescriptor> indexDescriptors) { this.indexDescriptors = indexDescriptors; }
	// @formatter:on

	@Required
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.supportsIfNotExists = supportsIfNotExists();
	}

	/** Whether or not the DB supports "create index if not exists" */
	private boolean supportsIfNotExists() {
		JdbcDialect jdbcDialect = JdbcDialect.detectDialect(dataSource);
		switch (jdbcDialect.knownDbVariant()) {
			case H2:
			case postgre:
				return true;
			default:
				return false;
		}
	}

	public void createIndices() {
		String ifNotExists = supportsIfNotExists ? "if not exists " : "";

		JdbcTools.withStatement(dataSource, () -> "Ensuring indices f " + contextDescription, dbStatement -> {
			for (IndexDescriptor iDesc : indexDescriptors)
				ensure(dbStatement, iDesc, "create index " + ifNotExists + //
						iDesc.getIndexName() + " on " +  iDesc.getTableName() + "(" + iDesc.getColumnName() + ")");
		});
	}

	private void ensure(Statement dbStatement, IndexDescriptor iDesc, String sql) {
		try {
			log.debug(() -> "Ensuring index via '" + sql + "' for " + contextDescription);
			dbStatement.executeUpdate(sql);

		} catch (SQLException e) {
			// For now we don't care that much about DBs that don't support "ifNotExists"
			if (supportsIfNotExists)
				log.warn("Error while ensureing index " + iDesc.getIndexName() + " for entity: " + iDesc.getEntityTypeSignature() + ", property: "
						+ iDesc.getPropertyName() + " [" + contextDescription + "]", e);
		}
	}

}
