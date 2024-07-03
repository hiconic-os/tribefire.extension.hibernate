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

import static com.braintribe.utils.lcd.CollectionTools2.newMap;

import java.util.Map;

import com.braintribe.model.accessdeployment.hibernate.meta.EntityMapping;
import com.braintribe.model.accessdeployment.jpa.meta.JpaPropertyMapping;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.meta.GmProperty;
import com.braintribe.model.processing.deployment.hibernate.mapping.HbmXmlGenerationContext;

public class MappingMetaDataResolver {

	private final HbmXmlGenerationContext context;
	private Map<String, EntityMapping> entityMappingCache;
	private Map<String, JpaPropertyMapping> propertyMappingCache;
	private boolean enableCache = false;

	// TODO remove this stupid cache
	public MappingMetaDataResolver(HbmXmlGenerationContext context, boolean enableCache) {
		this.context = context;
		this.enableCache = enableCache;
		if (this.enableCache) {
			this.entityMappingCache = newMap();
			this.propertyMappingCache = newMap();
		}
	}

	public EntityMapping getEntityMapping(GmEntityType gmEntityType) {
		EntityMapping entityMapping = null;

		if (enableCache) {

			String typeSignature = gmEntityType.getTypeSignature();
			entityMapping = entityMappingCache.get(typeSignature);

			if (entityMapping == null && !entityMappingCache.containsKey(typeSignature)) {
				entityMapping = resolveEntityMapping(gmEntityType);
				entityMappingCache.put(typeSignature, entityMapping); // nulls also kept
			}

		} else {
			entityMapping = resolveEntityMapping(gmEntityType);
		}

		return entityMapping;
	}

	public JpaPropertyMapping getPropertyMapping(GmEntityType gmEntityType, GmProperty gmProperty) {
		if (!enableCache)
			return resolvePropertyMapping(gmEntityType, gmProperty);

		String propertySignature = gmEntityType.getTypeSignature() + ":" + gmProperty.getName();

		JpaPropertyMapping result = propertyMappingCache.get(propertySignature);

		if (result == null && !propertyMappingCache.containsKey(propertySignature)) {
			result = resolvePropertyMapping(gmEntityType, gmProperty);
			propertyMappingCache.put(propertySignature, result); // nulls also kept
		}

		return result;
	}

	private EntityMapping resolveEntityMapping(GmEntityType gmEntityType) {
		return context.entityMd(gmEntityType).meta(EntityMapping.T).exclusive();
	}

	private JpaPropertyMapping resolvePropertyMapping(GmEntityType gmEntityType, GmProperty gmProperty) {
		return context.propertyMd(gmEntityType, gmProperty).meta(JpaPropertyMapping.T).exclusive();
	}


}
