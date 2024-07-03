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
package com.braintribe.model.accessdeployment.hibernate;

import java.util.Map;

import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.deployment.database.pool.HasDatabaseConnectionPool;
import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.annotation.meta.DeployableComponent;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * A {@link com.braintribe.model.deployment.DeployableComponent} which can provide a Hibernate SessionFactory.
 * <p>
 * The expert interface is com.braintribe.model.access.hibernate.HibernateComponent. 
 */
@Abstract
@DeployableComponent
public interface HibernateComponent extends Deployable, HasDatabaseConnectionPool {

	EntityType<HibernateComponent> T = EntityTypes.T(HibernateComponent.class);

	Map<String, String> getProperties();
	void setProperties(Map<String, String> value);

	String getDefaultSchema();
	void setDefaultSchema(String DefaultSchema);

	String getDefaultCatalog();
	void setDefaultCatalog(String DefaultCatalog);

	HibernateDialect getDialect();
	void setDialect(HibernateDialect hibernateDialect);

}
