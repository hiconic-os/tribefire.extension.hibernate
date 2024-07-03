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
package tribefire.extension.hibernate.wire.space;

import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.model.accessdeployment.hibernate.HibernateAccess;
import com.braintribe.model.accessdeployment.hibernate.HibernateEnhancedConnectionPool;
import com.braintribe.model.deployment.database.pool.DatabaseConnectionPool;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.cortex.model.deployment.usersession.cleanup.JdbcCleanupUserSessionsProcessor;
import tribefire.cortex.model.deployment.usersession.service.JdbcUserSessionService;
import tribefire.module.api.DenotationTransformationContext;
import tribefire.module.api.DenotationTransformerRegistry;
import tribefire.module.wire.contract.WebPlatformHardwiredExpertsContract;

@Managed
public class HibernateDenoTransSpace implements WireSpace {

	@Import
	private WebPlatformHardwiredExpertsContract hardwiredExperts;

	public void bind() {
		DenotationTransformerRegistry registry = hardwiredExperts.denotationTransformationRegistry();

		registry.registerStandardMorpher(DatabaseConnectionPool.T, HibernateAccess.T, this::dbConnectionPoolToHibernateAccess);
		registry.registerStandardMorpher(HibernateAccess.T, JdbcUserSessionService.T, this::accessToJdbcUserSessionService);
		registry.registerStandardMorpher(HibernateAccess.T, JdbcCleanupUserSessionsProcessor.T, this::accessToJdbcCleanupUserSessionsProcessor);
	}

	private Maybe<HibernateAccess> dbConnectionPoolToHibernateAccess(DenotationTransformationContext context, DatabaseConnectionPool pool) {
		HibernateAccess hibernateAccess = context.create(HibernateAccess.T);
		hibernateAccess.setConnector(pool);

		return Maybe.complete(hibernateAccess);
	}

	private Maybe<JdbcUserSessionService> accessToJdbcUserSessionService(DenotationTransformationContext context, HibernateAccess access) {
		JdbcUserSessionService service = context.create(JdbcUserSessionService.T);
		service.setConnectionPool(acquireConnectionPoolForAccess(context, access));

		return Maybe.complete(service);
	}

	private Maybe<JdbcCleanupUserSessionsProcessor> accessToJdbcCleanupUserSessionsProcessor(DenotationTransformationContext context,
			HibernateAccess access) {

		JdbcCleanupUserSessionsProcessor service = context.create(JdbcCleanupUserSessionsProcessor.T);
		service.setConnectionPool(acquireConnectionPoolForAccess(context, access));

		return Maybe.complete(service);
	}

	private HibernateEnhancedConnectionPool acquireConnectionPoolForAccess(DenotationTransformationContext context, HibernateAccess access) {
		String id = "connection-pool:hibernate-enhanced/" + access.getExternalId();

		HibernateEnhancedConnectionPool connectionPool = context.findEntityByGlobalId(id);
		if (connectionPool == null) {
			connectionPool = context.create(HibernateEnhancedConnectionPool.T);
			connectionPool.setGlobalId(id);
			connectionPool.setExternalId(id);
			connectionPool.setHibernateComponent(access);
		}
		return connectionPool;
	}

}
