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
package com.braintribe.persistence.hibernate.dialects;

import static com.braintribe.persistence.hibernate.dialects.HibernateDialectMapping.mapping;
import static com.braintribe.utils.lcd.CollectionTools2.asList;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.MySQL55Dialect;
import org.hibernate.dialect.MySQL57Dialect;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.Oracle12cDialect;
import org.hibernate.dialect.Oracle9iDialect;
import org.hibernate.dialect.PostgreSQL10Dialect;
import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.dialect.PostgreSQL9Dialect;
import org.hibernate.dialect.SQLServer2005Dialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.dialect.sane.SaneDerbyTenSevenDialect;
import org.hibernate.dialect.sane.SaneSQLServer2008Dialect;
import org.hibernate.dialect.sane.SaneSQLServer2012Dialect;

import com.braintribe.exception.Exceptions;
import com.braintribe.utils.ReflectionTools;

/**
 * @author peter.gazdik
 */
/* This information is also leveraged for our JDBC code, where we extract some information from the dialects (like SQL types names for given DB
 * vendor) - see hibernate-access-test, class HibernateDialectKnowledgeThief */
public class HibernateDialectMappings {

	private static ConcurrentHashMap<String, Class<? extends Dialect>> cache = new ConcurrentHashMap<>();

	@SuppressWarnings("deprecation")
	public static List<HibernateDialectMapping> mapppings() {
		// @formatter:off
		return asList(
			mapping(
					"(?i).*db2/nt.*", 
					"DB2", 
					DB2Dialect.class
			),
			mapping(
					"(?i).*db2.*", 
					"DB2v7_Host", 
					DB2Dialect.class
			),
			mapping(
					"(?i).*microsoft sql server.*Version\\:9\\..*", 
					"mssql", 
					SQLServer2005Dialect.class
			),
			mapping(
					"(?i).*microsoft sql server.*Version\\:10\\..*", 
					"mssql", 
					SaneSQLServer2008Dialect.class
			),
			mapping(
					"(?i).*microsoft sql server.*Version\\:11\\..*", 
					"mssql", 
					SaneSQLServer2012Dialect.class
			),
			mapping(
					"(?i).*microsoft sql server.*Version\\:12\\..*", 
					"mssql", 
					SaneSQLServer2012Dialect.class
			),
			mapping(
					"(?i).*microsoft sql server.*Version\\:13\\..*", 
					"mssql", 
					SaneSQLServer2012Dialect.class
			),
			mapping(
					"(?i).*microsoft sql server.*Version\\:14\\..*", 
					"mssql", 
					SaneSQLServer2012Dialect.class
			),
			mapping(
					"(?i).*microsoft sql server.*Version\\:15\\..*", 
					"mssql", 
					SaneSQLServer2012Dialect.class
			),
			mapping(
					"(?i).*microsoft sql server.*", 
					"mssql", 
					SQLServerDialect.class
			),
			mapping(
					"(?i).*oracle.*12.*", 
					"oracle", 
					Oracle12cDialect.class
			),
			mapping(
					"(?i).*oracle.*1[01].*", 
					"oracle", 
					Oracle10gDialect.class
			),
			mapping(
					"(?i).*oracle.*9.*", 
					"oracle", 
					Oracle9iDialect.class
			),
			mapping(
					"(?i).*oracle.*", 
					"oracle", 
					Oracle12cDialect.class
			),
			mapping(
					"(?i).*mysql.*version:\\s*([89]|\\d\\d)\\..*", // 8 / 9 / 10+ (two digits) 
					"mysql", 
					MySQL8Dialect.class
					),
			mapping(
					"(?i).*mysql.*version:\\s*[67]\\..*", // 6 / 7
					"mysql", 
					MySQL57Dialect.class
					),
			mapping(
					"(?i).*mysql.*version:\\s*5\\.[789]\\..*", // 5.7 / 5.8 / 5.9 
					"mysql", 
					MySQL57Dialect.class
					),
			mapping(
					"(?i).*mysql.*version:\\s*5\\.[56]\\..*", // 5.5 / 5.6 
					"mysql", 
					MySQL55Dialect.class
			),
			mapping(
					"(?i).*mysql.*version:\\s*5\\..*", // 5+ 
					"mysql", 
					MySQL5Dialect.class
			),
			mapping(
					"(?i).*mysql.*", 
					"mysql", 
					MySQLDialect.class
			),
			mapping(
					"(?i).*derby.*version:\\s*(10\\.[7-9]|10\\.\\d\\d).*", // 10.7 - 10.999999   
					"derby",
					SaneDerbyTenSevenDialect.class
			),
			mapping(
					"(?i).*derby.*", 
					"derby", 
					org.hibernate.dialect.DerbyDialect.class
			),
			mapping(
					"(?i)hsql.*", 
					"hsql", 
					HSQLDialect.class
			),
			mapping(
					"(?i)h2.*", 
					"h2", 
					H2Dialect.class
			),
			mapping(
					"(?i)postgre.*version:\\s*\\d\\d.*", // 10+  
					"postgre",
					PostgreSQL10Dialect.class
			),
			mapping(
					"(?i)postgre.*version:\\s*(9\\.[5-9]).*", // 9.5+
					"postgre", 
					PostgreSQL95Dialect.class
			),
			mapping(
					"(?i)postgre.*version:\\s*9\\..*", 
					"postgre", 
					PostgreSQL9Dialect.class
			),
			mapping(
					"(?i)postgre.*version:\\s*8\\.1.*", 
					"postgre", 
					PostgreSQL81Dialect.class
			),
			mapping(
					"(?i)postgre.*version:\\s*8\\.[234].*", 
					"postgre", 
					PostgreSQL82Dialect.class
			),
			mapping(
					"(?i)postgre.*", 
					"postgre", 
					org.hibernate.dialect.PostgreSQLDialect.class
			)
		);
		// @formatter:on
	}

	/**
	 * Loads hibernate {@link Dialect} with given name. It first tries to find a sanitized version of said dialect, like
	 * {@link SaneSQLServer2012Dialect}, which is our extension of given hibernate dialect with some minor tweaks (like different column type
	 * mappings). If no sanitized dialect is found, a default hibernate dialect (in the 'org.hibernate.dialect' package) is looked up.
	 * 
	 * @throws IllegalArgumentException
	 *             if no dialect for given name is found.
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends Dialect> loadDialect(String dialectName) {
		return cache.computeIfAbsent(dialectName, __ -> {
			String saneClassName = "org.hibernate.dialect.sane.Sane" + dialectName;

			Class<?> saneDialectClass = ReflectionTools.getClassOrNull(saneClassName, SaneDerbyTenSevenDialect.class.getClassLoader());
			if (saneDialectClass != null)
				return (Class<? extends Dialect>) saneDialectClass;

			String className = "org.hibernate.dialect." + dialectName;
			try {
				return (Class<? extends Dialect>) Class.forName(className);

			} catch (Exception e) {
				throw Exceptions.unchecked(e, "Failed to load dialect class " + className, IllegalArgumentException::new);
			}

		});
	}

}
