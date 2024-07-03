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
package com.braintribe.persistence.hibernate;

import java.sql.Connection;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;

import com.braintribe.exception.Exceptions;
import com.braintribe.utils.lcd.LazyInitialization;

/**
 * A  {@link Connection JDBC connection} {@link Supplier} that ensures the schema is created by Hibernate before connection is used.
 */
public class HibernateJdbcConnectionSupplier implements Supplier<Connection> {

	private final SessionFactory sessionFactory;
	private final DataSource dataSource;
	private final LazyInitialization schemaUpdate = new LazyInitialization(this::updateSchema);

	public HibernateJdbcConnectionSupplier(SessionFactory sessionFactory, DataSource dataSource) {
		this.sessionFactory = sessionFactory;
		this.dataSource = dataSource;
	}

	@Override
	public Connection get() {
		schemaUpdate.run();

		try {
			return dataSource.getConnection();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	private void updateSchema() {
		sessionFactory.openStatelessSession().close();
	}
}