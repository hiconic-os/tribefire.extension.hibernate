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
package com.braintribe.model.processing.deployment.hibernate.mapping.render.context;

import java.util.regex.Pattern;

public class EntityMappingContextTools {

	private static final Pattern quotationNeededPattern = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

	/**
	 * Quotes a database identifier with back-ticks, if necessary.
	 * 
	 * @param dbIdentifier
	 *            The database identifier to be quoted
	 */
	public static String quoteIdentifier(String dbIdentifier) {
		if (dbIdentifier == null || (dbIdentifier.startsWith("`") && dbIdentifier.endsWith("`")))
			return dbIdentifier;

		if (!quotationNeededPattern.matcher(dbIdentifier).matches())
			return "`" + dbIdentifier + "`";

		return dbIdentifier;
	}

}
