// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
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
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.OracleDialect;
import org.hibernate.dialect.SQLServerDialect;

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
					"(?i).*microsoft sql server.*", 
					"mssql", 
					SQLServerDialect.class
			),
			mapping(
					"(?i).*oracle.*", 
					"oracle", 
					OracleDialect.class
			),
			mapping(
					"(?i).*mysql.*", 
					"mysql", 
					MySQLDialect.class
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
					"(?i)postgre.*", 
					"postgre", 
					org.hibernate.dialect.PostgreSQLDialect.class
			)
		);
		// @formatter:on
	}

	/**
	 * Loads hibernate {@link Dialect} with given name. It first tries to find a sanitized version of said dialect, like
	 * SaneSQLServerDialect, which is our extension of given hibernate dialect with some minor tweaks (like different column type
	 * mappings). If no sanitized dialect is found, a default hibernate dialect (in the 'org.hibernate.dialect' package) is looked up.
	 * 
	 * @throws IllegalArgumentException
	 *             if no dialect for given name is found.
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends Dialect> loadDialect(String dialectName) {
		return cache.computeIfAbsent(dialectName, __ -> {
			String saneClassName = "org.hibernate.dialect.sane.Sane" + dialectName;

			Class<?> saneDialectClass = ReflectionTools.getClassOrNull(saneClassName, HibernateDialectMappings.class.getClassLoader());
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
