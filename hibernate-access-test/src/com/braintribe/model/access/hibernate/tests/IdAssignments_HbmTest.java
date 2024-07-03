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
import com.braintribe.model.access.hibernate.base.model.simple.BasicScalarEntity;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.manipulation.basic.normalization.Normalizer;

/**
 * Various test for setting the id property.
 * 
 * @see HibernateAccessRecyclingTestBase
 * 
 * @author peter.gazdik
 */
public class IdAssignments_HbmTest extends HibernateAccessRecyclingTestBase {

	@Override
	protected GmMetaModel model() {
		return HibernateAccessRecyclingTestBase.hibernateModels.basic_NoPartition();
	}

	/**
	 * If {@link Normalizer} isn't used in applyManipulation this causes an NPE when computing a hash-code of a reference created internally (because
	 * the code assumes such assignments are stripped by the normalizer).
	 */
	@Test
	public void settingIdToNull() throws Exception {
		BasicScalarEntity bse = session.create(BasicScalarEntity.T);
		bse.setId(null);
		bse.setName("BSE 1");

		session.commit();

		resetGmSession();

		bse = accessDriver.requireEntityByProperty(BasicScalarEntity.T, BasicScalarEntity.name, "BSE 1");
		assertThat(bse.<Object> getId()).isNotNull();
	}

}
