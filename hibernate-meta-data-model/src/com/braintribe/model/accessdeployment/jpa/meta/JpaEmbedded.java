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
package com.braintribe.model.accessdeployment.jpa.meta;

import java.util.Map;

import com.braintribe.model.accessdeployment.hibernate.meta.EntityMapping;
import com.braintribe.model.accessdeployment.hibernate.meta.PropertyMapping;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * Mapping for a property whose type is an embeddable entity type.
 * 
 * @see EntityMapping#getIsEmbeddable()
 */
public interface JpaEmbedded extends JpaPropertyMapping {

	EntityType<JpaEmbedded> T = EntityTypes.T(JpaEmbedded.class);

	/**
	 * For now, all the properties of the embedded entity type have to be mapped here. Also, only
	 * {@link PropertyMapping} is supported as a map value right now.
	 * 
	 * This can however inconvenient if we want to use the same embeddable type multiple times, but map one of it's
	 * properties differently while others stay the same - this forces us to define the whole map again. So in the
	 * future, the In the future, also want to support {@link JpaPropertyMapping}s on the embeddable type itself, to
	 * serve as a default.
	 * 
	 * Also, currently there is no support for default values of column name and type, so in case PropertyMapping is
	 * used, both have to be specified explicitly.
	 */
	Map<String, JpaPropertyMapping> getEmbeddedPropertyMappings();
	void setEmbeddedPropertyMappings(Map<String, JpaPropertyMapping> embeddedPropertyMappings);

}
