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

import org.hibernate.dialect.SQLServer2012Dialect;

import com.braintribe.persistence.hibernate.dialects.HibernateDialectMappings;

/**
 * @see SaneSQLServer2008Dialect
 * @see HibernateDialectMappings#loadDialect(String)
 */
public class SaneSQLServer2012Dialect extends SQLServer2012Dialect {

	private static final int NVARCHAR_MAX_LENGTH = 4000;

	public SaneSQLServer2012Dialect() {
		fixVarcharProblem();
	}

	private void fixVarcharProblem() {
		registerColumnType(Types.VARCHAR, NVARCHAR_MAX_LENGTH, "nvarchar($l)");
		registerColumnType(Types.VARCHAR, "nvarchar(MAX)");
	}

}
