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

import org.junit.Ignore;
import org.junit.Test;

import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.deployment.hibernate.mapping.HbmXmlGeneratingService;

/**
 * Uses older version of hbm mappings.
 * 
 * @see ScalarCollections_HbmTest
 * 
 * @author peter.gazdik
 */
// This fails with Hibernate 6 because of the Collection<Enum> problem
@Ignore
public class ScalarCollections_v1_HbmTest extends ScalarCollections_HbmTest {

	@Override
	protected int mappingVersion() {
		return HbmXmlGeneratingService.MAPPING_VERSION_1;
	}

	@Override
	protected GmMetaModel model() {
		return hibernateModels.scalarCollections_v1();
	}
	
	@Override
	@Test
	public void storesAndLoadsScalarCollections() throws Exception {
		super.run_storesAndLoadsScalarCollections();
	}

}
