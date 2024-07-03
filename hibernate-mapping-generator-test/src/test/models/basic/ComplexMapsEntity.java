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
package test.models.basic;

import java.util.Map;

import com.braintribe.model.generic.StandardIdentifiable;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;


public interface ComplexMapsEntity extends StandardIdentifiable {

	final EntityType<ComplexMapsEntity> T = EntityTypes.T(ComplexMapsEntity.class);

	Map<String, SimpleEntity> getStringToEntityMap();
	void setStringToEntityMap(Map<String, SimpleEntity> value);

	Map<SimpleEntity, String> getEntityToStringMap();
	void setEntityToStringMap(Map<SimpleEntity, String> value);

	Map<SimpleEntity, SimpleEntity> getEntityToEntityMap();
	void setEntityToEntityMap(Map<SimpleEntity, SimpleEntity> value);

}
