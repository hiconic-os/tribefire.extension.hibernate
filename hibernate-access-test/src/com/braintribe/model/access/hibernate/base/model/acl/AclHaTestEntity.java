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
package com.braintribe.model.access.hibernate.base.model.acl;

import com.braintribe.model.access.hibernate.base.model.HibernateAccessEntity;
import com.braintribe.model.acl.HasAcl;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * HA provides special tests for {@link HasAcl ACL} as these queries are quite important. ACL inspired a special optimization conditions like 
 * {@code .. WHERE 'green' in elements(e.colors) OR 'red' in elements(e.colors)}
 * 
 * @author peter.gazdik
 */
public interface AclHaTestEntity extends HibernateAccessEntity, HasAcl {

	EntityType<AclHaTestEntity> T = EntityTypes.T(AclHaTestEntity.class);

}
