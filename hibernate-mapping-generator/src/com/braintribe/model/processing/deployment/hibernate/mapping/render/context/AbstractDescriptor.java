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

import com.braintribe.model.processing.deployment.hibernate.mapping.HbmXmlGenerationContext;
import com.braintribe.utils.lcd.CommonTools;

/**
 * 
 */
abstract class AbstractDescriptor {

	protected final HbmXmlGenerationContext context;

	public String tag;

	protected AbstractDescriptor(HbmXmlGenerationContext context) {
		this.context = context;
	}

	protected static String simpleName(String fullName) {
		int pos = fullName.lastIndexOf(".");

		if (pos < 0)
			return fullName;
		else
			return fullName.substring(pos + 1);
	}

	protected static String capitalize(String s) {
		return CommonTools.capitalize(s);
	}

	public String getTag() {
		return tag;
	}

	/**
	 * <p>
	 * Applies the table prefix as given by {@link HbmXmlGenerationContext#tablePrefix} to table names configured via
	 * metadata or hints.
	 * 
	 * @param hintedTableName
	 *            Table name configured via metadata or hints.
	 * @return The table name possibly prefixed.
	 */
	protected String prefixTableName(String hintedTableName) {
		String p = context.tablePrefix;
		return p != null ? p + hintedTableName : hintedTableName;
	}

}
