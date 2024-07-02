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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package tribefire.extension.hibernate.edr2cc.wire.space;

import com.braintribe.model.accessdeployment.hibernate.HibernateAccess;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.extension.hibernate.edr2cc.denotrans.HibernateAccessEdr2ccEnricher;
import tribefire.module.api.DenotationEnrichmentResult;
import tribefire.module.api.DenotationTransformationContext;
import tribefire.module.wire.contract.TribefireModuleContract;
import tribefire.module.wire.contract.TribefireWebPlatformContract;
import tribefire.module.wire.contract.WebPlatformHardwiredExpertsContract;

/**
 * Configures proper hibernate mappings for hibernate-based system accesses.
 * 
 * @see HibernateAccessEdr2ccEnricher
 * 
 * @author peter.gazdik
 */
@Managed
public class HibernateAccessesEdr2ccModuleSpace implements TribefireModuleContract {

	@Import
	private TribefireWebPlatformContract tfPlatform;

	@Import
	private WebPlatformHardwiredExpertsContract hardwiredExperts;

	//
	// Hardwired deployables
	//

	@Override
	public void bindHardwired() {
		tfPlatform.hardwiredExperts().denotationTransformationRegistry() //
				.registerEnricher("HibernateAccessesEdr2ccEnricher", HibernateAccess.T, this::enrichSystemAccess);
	}

	private DenotationEnrichmentResult<HibernateAccess> enrichSystemAccess(//
			DenotationTransformationContext context, HibernateAccess access) {

		return new HibernateAccessEdr2ccEnricher(context, access, tfPlatform.modelApi()::newMetaDataEditor).run();
	}


}
