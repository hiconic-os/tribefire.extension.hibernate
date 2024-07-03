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
package com.braintribe.model.access.hibernate.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.wire.space.HibernateModelsSpace;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.meta.GmMetaModel;

/**
 * Basic tests for entity that is fully woven, checking that Hibernate was given the right class-loader.
 * 
 * @see HibernateAccessRecyclingTestBase
 * 
 * @author peter.gazdik
 */
public class Own_CL_HbmTest extends HibernateAccessRecyclingTestBase {

	private static final String NON_CP_INSTANCE_NAME = "NonCpEntity";

	@Override
	protected GmMetaModel model() {
		return HibernateAccessRecyclingTestBase.hibernateModels.nonClasspath();
	}

	@Test
	public void storesAndLoadsScalarEntity() throws Exception {
		prepareNonCpEntity();

		EntityType<?> et = GMF.getTypeReflection().getEntityType(HibernateModelsSpace.NON_CP_ENTITY_SIG);

		GenericEntity e = accessDriver.requireEntityByProperty(et, "name", NON_CP_INSTANCE_NAME);
		assertThat(e).isNotNull();
	}

	private void prepareNonCpEntity() {
		EntityType<?> et = GMF.getTypeReflection().getEntityType(HibernateModelsSpace.NON_CP_ENTITY_SIG);
		Property nameProperty = et.getProperty("name");

		GenericEntity nonCpEntity = session.create(et);
		nameProperty.set(nonCpEntity, NON_CP_INSTANCE_NAME);

		session.commit();

		resetGmSession();
	}
}
