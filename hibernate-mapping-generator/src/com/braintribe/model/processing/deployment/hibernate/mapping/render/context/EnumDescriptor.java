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

import com.braintribe.model.meta.GmEnumType;
import com.braintribe.model.meta.GmProperty;
import com.braintribe.model.meta.GmType;
import com.braintribe.model.processing.deployment.hibernate.mapping.HbmXmlGenerationContext;

public class EnumDescriptor extends PropertyDescriptor {

	public final String enumSqlType, enumClass;

	protected EnumDescriptor(HbmXmlGenerationContext context, EntityDescriptor descriptor, GmProperty gmProperty,
			PropertyDescriptorMetaData metaData) {

		super(context, descriptor, gmProperty, metaData);

		this.enumSqlType = "12";
		this.enumClass = resolveEnumClass(gmProperty);
	}

	public String getEnumSqlType() {
		return enumSqlType;
	}

	public String getEnumClass() {
		return enumClass;
	}

	private static String resolveEnumClass(GmProperty gmProperty) {
		GmType type = gmProperty.getType();
		if (type.isGmEnum())
			return ((GmEnumType) type).getTypeSignature();
		else
			return null;
	}

	@Override
	public boolean getIsEnumType() {
		return true;
	}
}
