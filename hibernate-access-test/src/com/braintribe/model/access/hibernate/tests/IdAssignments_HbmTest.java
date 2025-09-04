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

import static com.braintribe.testing.junit.assertions.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.HibernateBaseModelTestBase;
import com.braintribe.model.access.hibernate.base.model.simple.BasicScalarEntity;
import com.braintribe.model.access.hibernate.base.model.simple.StringIdEntity;
import com.braintribe.model.access.hibernate.base.model.simple.StringIdWithCascadingEntity;
import com.braintribe.model.generic.StandardStringIdentifiable;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.manipulation.basic.normalization.Normalizer;

/**
 * Various test for setting the id property.
 * 
 * @see HibernateAccessRecyclingTestBase
 * 
 * @author peter.gazdik
 */
public class IdAssignments_HbmTest extends HibernateBaseModelTestBase {

	/**
	 * If {@link Normalizer} isn't used in applyManipulation this causes an NPE when computing a hash-code of a reference created internally (because
	 * the code assumes such assignments are stripped by the normalizer).
	 */
	@Test
	public void settingIdToNull() throws Exception {
		BasicScalarEntity bse = createBse("BSE 1");
		bse.setId(null);

		session.commit();

		resetGmSession();

		bse = accessDriver.requireEntityByProperty(BasicScalarEntity.T, BasicScalarEntity.name, "BSE 1");
		assertThat(bse.<Object> getId()).isNotNull();
	}

	@Test
	public void withExplicitId() throws Exception {
		StringIdEntity sie = createSie("SIE-1");
		sie.setId("ONE");

		session.commit();

		resetGmSession();

		sie = accessDriver.requireEntityByProperty(StringIdEntity.T, BasicScalarEntity.name, "SIE-1");
		assertThat(sie.<String> getId()).isEqualTo("ONE");
		assertThat(sie.getPartition()).isNotEmpty().isEqualTo(access.getAccessId());
	}

	/**
	 * This makes sense despite partition not being mapped. We make sure the handling of partition change is handled correctly for the references used
	 * internally.
	 */
	@Test
	public void withExplicitIdAndPartition() throws Exception {
		assertThat(access.getAccessId()).isNotEmpty();

		StringIdEntity sie = createSie("SIE-1");
		sie.setId("ONE");
		sie.setPartition(access.getAccessId());

		session.commit();

		resetGmSession();

		sie = accessDriver.requireEntityByProperty(StringIdEntity.T, BasicScalarEntity.name, "SIE-1");
		assertThat(sie.<String> getId()).isEqualTo("ONE");
		assertThat(sie.getPartition()).isEqualTo(access.getAccessId());
	}

	@Test
	public void autoGeneratesUuidId() throws Exception {
		@SuppressWarnings("unused")
		StringIdEntity sie = createSie("SIE");

		session.commit();
		resetGmSession();

		assertHasId(StringIdEntity.T, "SIE");
	}

	@Test
	public void autoGeneratesUuidId_WithCascading() throws Exception {
		StringIdWithCascadingEntity sieA = createSieWithCascading("SIE-A");
		StringIdWithCascadingEntity sieB = createSieWithCascading("SIE-B");

		sieB.getOthers().add(sieA);
		sieA.getOthers().add(sieB);

		session.commit();
		resetGmSession();

		assertHasId(StringIdWithCascadingEntity.T, "SIE-A");
		assertHasId(StringIdWithCascadingEntity.T, "SIE-B");
	}

	private <T extends StandardStringIdentifiable> void assertHasId(EntityType<T> et, String name) {
		T entity = accessDriver.requireEntityByProperty(et, BasicScalarEntity.name, name);
		assertThat(entity.<String> getId()).isNotNull();
	}

}
