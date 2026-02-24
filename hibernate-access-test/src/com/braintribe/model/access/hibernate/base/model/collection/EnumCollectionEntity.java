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
package com.braintribe.model.access.hibernate.base.model.collection;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface EnumCollectionEntity extends GenericEntity {

	EntityType<EnumCollectionEntity> T = EntityTypes.T(EnumCollectionEntity.class);

	String getName();
	void setName(String name);

	Set<ScalarEnum> getEnumSet();
	void setEnumSet(Set<ScalarEnum> enumSet);

	List<ScalarEnum> getEnumList();
	void setEnumList(List<ScalarEnum> enumList);
	
	Map<ScalarEnum, String> getEnumKeyMap();
	void setEnumKeyMap(Map<ScalarEnum, String> enumKeyMap);

	Map<String, ScalarEnum> getEnumValueMap();
	void setEnumValueMap(Map<String, ScalarEnum> enumValueMap);
}
