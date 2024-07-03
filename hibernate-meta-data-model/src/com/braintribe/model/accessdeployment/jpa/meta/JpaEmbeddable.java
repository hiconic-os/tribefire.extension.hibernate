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

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.data.EntityTypeMetaData;
import com.braintribe.model.meta.data.ExplicitPredicate;

/**
 * Specifies that a given entity type is embeddable, i.e. this type cannot be mapped to a table and all the properties
 * of that type are embedded within the owner entity's table. The mapping for these embedded properties must either be
 * provided by {@link JpaEmbedded} meta-data, or an xml snippet.
 */
public interface JpaEmbeddable extends EntityTypeMetaData, ExplicitPredicate {

	EntityType<JpaEmbeddable> T = EntityTypes.T(JpaEmbeddable.class);

}
