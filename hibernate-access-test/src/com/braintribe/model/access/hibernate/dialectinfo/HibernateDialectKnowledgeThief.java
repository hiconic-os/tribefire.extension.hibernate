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
package com.braintribe.model.access.hibernate.dialectinfo;

import static com.braintribe.utils.lcd.CollectionTools2.newList;

import java.sql.Types;
import java.util.List;
import java.util.StringJoiner;

import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQL81Dialect;

import com.braintribe.persistence.hibernate.dialects.HibernateDialectMapping;
import com.braintribe.persistence.hibernate.dialects.HibernateDialectMappings;

/**
 * This simply steals some valuable information from Hibernate dialects to use them in pure JDBC applications. The generated code should be placed to
 * jdbc-support, class JdbcDialectMappings.
 * 
 * @author peter.gazdik
 */
public class HibernateDialectKnowledgeThief {

	public static void main(String[] args) {
		new HibernateDialectKnowledgeThief().run();
	}

	List<String> lines = newList();

	Dialect dialect;
	StringJoiner sj;

	private void run() {
		for (HibernateDialectMapping m : HibernateDialectMappings.mapppings()) {
			dialect = resolveDialect(m);

			sj = new StringJoiner(", ", "register(", ");");

			addArgument(m.productRegex); // regex
			addDbVariant(m.variant); // dbVariant
			addArgument(m.dialectType.getName()); // hibernateDialect

			addType(Types.BOOLEAN);
			addType(Types.INTEGER);
			addType(Types.BIGINT);
			addType(Types.FLOAT);
			addType(Types.DOUBLE);
			addType(Types.NUMERIC);
			addType(Types.TIMESTAMP);
			
			addType(Types.NVARCHAR);
			addType(Types.NVARCHAR, 255, 0, 0);
			addType(Types.CLOB);
			addType(Types.BLOB);

			lines.add(sj.toString());
		}

		System.out.println(
				"Following lines should be placed to the static initializer of `com.braintribe.gm:jdbc-support`, class JdbcDialectMappings:");

		lines.forEach(System.out::println);
	}

	private void addType(int typeCode) {
		addArgument(getTypeName(typeCode));
	}

	private String getTypeName(int typeCode) {
		/* Derby/DB2 has weird CLOBs with limit, i.e. the dialect would return clob($l) */
		if (typeCode == Types.CLOB && dialect instanceof DB2Dialect)
			return "clob";

		typeCode = sanitizeType(typeCode);
		String result = dialect.getTypeName(typeCode);

		return result;
	}

	private void addType(int typeCode, int length, int precision, int scale) {
		typeCode = sanitizeType(typeCode);
		addArgument(dialect.getTypeName(typeCode, length, precision, scale));
	}

	private int sanitizeType(int typeCode) {
		// Derby/DB2 does not support nvarchar2 (or other N-things) but allegedly the regular varchar is already unicode
		// PostgreSQL doesn't seem to support nvarchar either
		if (typeCode == Types.NVARCHAR && //
				(dialect instanceof DB2Dialect || dialect instanceof PostgreSQL81Dialect))
			return Types.VARCHAR;

		return typeCode;
	}

	private void addDbVariant(String variant) {
		sj.add("DbVariant." + variant);
	}

	private void addArgument(String arg) {
		sj.add(toJavaSourceString(arg));
	}

	private static String toJavaSourceString(String s) {
		return "\"" + escape(s) + "\"";
	}

	private static String escape(String s) {
		return s.replaceAll("\\\\", "\\\\\\\\") // replace \ with \\
				.replaceAll("\"", "\\\\\""); // replace " with \"
	}

	private static Dialect resolveDialect(HibernateDialectMapping m) {
		try {
			return m.dialectType.getConstructor().newInstance();

		} catch (Exception e) {
			throw new RuntimeException("WTF?", e);
		}
	}

}
