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
package test.models.hierarchy;

import com.braintribe.model.generic.StandardIdentifiable;
import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.processing.deployment.hibernate.mapping.HbmEntityTypeMapBuilder;

/**
 * The hierarchy with {@link Base}, {@link Left} and {@link Right} should collapse into a single table, even if this type is not mapped, as long as
 * it's sub-type {@link BaseReferer} is.
 * <p>
 * There was a bug that this would not be the case, because this {@link #getBase()} base property would not be considered when electing top level
 * entities in {@link HbmEntityTypeMapBuilder#categorizeEntities()}.
 */
@Abstract
public interface AbstractBaseReferer extends StandardIdentifiable {

	EntityType<AbstractBaseReferer> T = EntityTypes.T(AbstractBaseReferer.class);

	Base getBase();
	void setBase(Base base);

}
