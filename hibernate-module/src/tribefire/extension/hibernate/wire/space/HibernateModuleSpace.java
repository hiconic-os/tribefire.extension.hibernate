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

import com.braintribe.model.accessdeployment.hibernate.HibernateAccess;
import com.braintribe.model.accessdeployment.hibernate.selector.HibernateDbVendorSelector;
import com.braintribe.model.accessdeployment.hibernate.selector.HibernateDialectSelector;
import com.braintribe.model.deployment.database.pool.DatabaseConnectionPool;
import com.braintribe.model.processing.deployment.api.binding.DenotationBindingBuilder;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.extension.hibernate.meta.experts.HibernateDbVendorSelectorExpert;
import tribefire.extension.hibernate.meta.experts.HibernateDialectSelectorExpert;
import tribefire.module.api.DenotationMorpher;
import tribefire.module.api.InitializerBindingBuilder;
import tribefire.module.wire.contract.TribefireModuleContract;
import tribefire.module.wire.contract.TribefireWebPlatformContract;
import tribefire.module.wire.contract.WebPlatformHardwiredExpertsContract;

/**
 * This module binds the basic components of the standard Hibernate extension, namely:
 * 
 * <ul>
 * <li>Experts for {@link HibernateDialectSelector} and {@link HibernateDbVendorSelector}.
 * <li>{@link DenotationMorpher} that converts {@link DatabaseConnectionPool} to {@link HibernateAccess}
 * <li>TODO HibernateAccess deployment expert
 * </ul>
 */
@Managed
public class HibernateModuleSpace implements TribefireModuleContract {

	// @formatter:off
	@Import private TribefireWebPlatformContract tfPlatform;
	@Import private WebPlatformHardwiredExpertsContract hardwiredExperts;

	@Import private HibernateDeployablesSpace hibernateDeployables;
	@Import private HibernateDenoTransSpace hibernateDenoTrans;
	@Import private HibernateInitializerSpace hibernateInitializer;
	// @formatter:on

	@Override
	public void bindHardwired() {
		hibernateDenoTrans.bind();

		hardwiredExperts.bindMetaDataSelectorExpert(HibernateDialectSelector.T, new HibernateDialectSelectorExpert());
		hardwiredExperts.bindMetaDataSelectorExpert(HibernateDbVendorSelector.T, new HibernateDbVendorSelectorExpert());
	}

	@Override
	public void bindDeployables(DenotationBindingBuilder bindings) {
		hibernateDeployables.bindDeployables(bindings);
	}

	@Override
	public void bindInitializers(InitializerBindingBuilder bindings) {
		hibernateInitializer.bindInitializers(bindings);
	}

}
