// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.model.access.hibernate.schema.meta;

import java.io.File;
import java.util.List;

import javax.sql.DataSource;

import com.braintribe.cfg.Required;
import com.braintribe.exception.Exceptions;
import com.braintribe.logging.Logger;
import com.braintribe.logging.Logger.LogLevel;
import com.braintribe.model.accessdeployment.hibernate.meta.DbUpdateStatement;
import com.braintribe.model.processing.deployment.hibernate.schema.meta.DbUpdateStatementGenerator;
import com.braintribe.util.jdbc.JdbcTools;
import com.braintribe.utils.logging.LogLevels;

/**
 * @author peter.gazdik
 */
public class DbUpdateStatementExecutor {

	private String contextDescription;
	private DataSource dataSource;
	private File mappingDirectory;

	private static final Logger log = Logger.getLogger(DbUpdateStatementExecutor.class);

	// @formatter:off
	/** Context description for logging purposes, e.g. HibernateAccess my.access.id */
	@Required public void setContextDescription(String contextDescription) { this.contextDescription = contextDescription; }
	@Required public void setDataSource(DataSource dataSource) { this.dataSource = dataSource; }
	@Required public void setMappingDirectory(File mappingDirectory) { this.mappingDirectory = mappingDirectory; }
	// @formatter:on

	public void runBeforeSchemaCreationTasks() {
		runTasks(true);
	}

	public void runAfterSchemaCreationTasks() {
		runTasks(false);
	}

	private void runTasks(boolean before) {
		List<DbUpdateStatement> statements = DbUpdateStatementGenerator.readDbUpdateStatements(mappingDirectory, before);

		for (DbUpdateStatement statement : statements)
			run(statement);
	}

	private void run(DbUpdateStatement statement) {
		try {
			JdbcTools.withStatement(dataSource, () -> "Executing: " + statement, dbStatement -> {
				String sql = statement.getExpression();
				log.debug(() -> "Executing DbUpdateStemeent '" + sql + "' for " + contextDescription);

				dbStatement.execute(sql);
			});

		} catch (RuntimeException e) {
			if (statement.getStopOnError())
				throw Exceptions.contextualize(e, "Error while attempting to execute statement: " + statement);

			log.log(level(statement), "Error while attempting to execute statement: " + statement + " for " + contextDescription, e);
		}
	}

	private LogLevel level(DbUpdateStatement statement) {
		return LogLevels.convert(statement.getLogLevelOnError(), Logger.LogLevel.WARN);
	}
}
