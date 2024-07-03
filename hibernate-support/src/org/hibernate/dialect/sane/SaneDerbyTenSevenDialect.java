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

import org.hibernate.dialect.DerbyTenSevenDialect;

import com.braintribe.persistence.hibernate.dialects.HibernateDialectMappings;

/**
 * This is an extension of the {@link org.hibernate.dialect.DerbyTenSevenDialect} Hibernate dialect that fixes a problem with CLOBs. Use this dialect
 * to prevent CLOBs to be limited to 255 characters.
 * 
 * @see HibernateDialectMappings#loadDialect(String)
 */
public class SaneDerbyTenSevenDialect extends DerbyTenSevenDialect {

	public SaneDerbyTenSevenDialect() {
		registerColumnType(Types.CLOB, "clob");
	}

}
