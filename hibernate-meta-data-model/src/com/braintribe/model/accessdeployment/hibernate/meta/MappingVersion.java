// ============================================================================
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
package com.braintribe.model.accessdeployment.hibernate.meta;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.data.ModelMetaData;

/** Specifies the mapping version. */
public interface MappingVersion extends ModelMetaData {

	EntityType<MappingVersion> T = EntityTypes.T(MappingVersion.class);

	/**
	 * Original versions with some issues:
	 * <ul>
	 * <li>Collections of Enums have values stored as binary, not strings
	 * <li>Collections of Dates have values stored as date only, rather than timestamp (i.e. time is missing)
	 * </ul>
	 */
	int MAPPING_VERSION_1 = 1;

	/**
	 * Changes from v1:
	 * <ul>
	 * <li>Collections of Enums have values stored as strings
	 * <li>Collections of Dates are proper timestamps (i.e. including time, not just Date)
	 * </ul>
	 */
	int MAPPING_VERSION_2 = 2;

	/**
	 * Adds indices (for now).
	 */
	int MAPPING_VERSION_3 = 3;

	
	int getVersion();
	void setVersion(int version);

}
