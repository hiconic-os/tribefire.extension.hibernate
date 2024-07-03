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
package org.hibernate.dialect.sane;

import java.sql.Types;

import org.hibernate.dialect.SQLServer2008Dialect;

import com.braintribe.persistence.hibernate.dialects.HibernateDialectMappings;

/**
 * @see #fixVarcharProblem()
 * 
 * @see HibernateDialectMappings#loadDialect(String)
 */
public class SaneSQLServer2008Dialect extends SQLServer2008Dialect {

	private static final int NVARCHAR_MAX_LENGTH = 4000;

	public SaneSQLServer2008Dialect() {
		fixVarcharProblem();
	}

	/**
	 * Fixes a problem that is especially severe for String IDs. The default Hibernate mapping would pick a 'varchar(255)' as it's column type.
	 * However, the actual value provided (via JDBC) would be a 'nvarchar'.
	 * <p>
	 * This means, that for example on "update where id=..." the index wouldn't be used and instead a sequential processing of an entire index
	 * happens. This scan requires allocating a lock on entire table or at least on some page, and can potentially lead to a deadlock, if a similar
	 * update is happening in parallel.
	 * <p>
	 * This was actually happening when we simply tried to create a Resource and assign it a new ResourceSource in parallel (yes, two transactions
	 * which only tried to write new data ended up in a deadlock).
	 */
	private void fixVarcharProblem() {
		registerColumnType(Types.VARCHAR, NVARCHAR_MAX_LENGTH, "nvarchar($l)");
		registerColumnType(Types.VARCHAR, "nvarchar(MAX)");
	}

}
