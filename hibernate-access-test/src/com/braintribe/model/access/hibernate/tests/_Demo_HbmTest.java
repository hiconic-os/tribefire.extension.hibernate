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
package com.braintribe.model.access.hibernate.tests;

import static com.braintribe.testing.junit.assertions.gm.assertj.core.api.GmAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.model.simple.BasicScalarEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmMetaModel;

/**
 * This just shows how these tests work.
 * 
 * @see HibernateAccessRecyclingTestBase
 * 
 * @author peter.gazdik
 */
public class _Demo_HbmTest extends HibernateAccessRecyclingTestBase {

	@Override
	protected GmMetaModel model() {
		return HibernateAccessRecyclingTestBase.hibernateModels.basic_NoPartition();
	}

	@Test
	public void showRunningMultipleTests() throws Exception {
		// Test #1 - assume empty DB, create some data and do the asserts
		createSingleInstanceInEmptyDb();

		// reset test - this is otherwise done automatically before each test method
		rollbackTransaction(); // @After first test
		prepareAccess(); // @Before next test

		// Test #2 - assume empty DB again, create some data and do the asserts
		createSingleInstanceInEmptyDb();
	}

	private void createSingleInstanceInEmptyDb() {
		assertInstances(BasicScalarEntity.T, 0);

		BasicScalarEntity entity = accessDriver.acquireEntity(BasicScalarEntity.T, BasicScalarEntity.name, "BSE 1");
		assertThat(entity).isNotNull().hasId();

		assertInstances(BasicScalarEntity.T, 1);
	}

	private void assertInstances(EntityType<?> entityType, int expectedCount) {
		List<?> results = session.query().select(from(entityType, "e").select("e", "id").done()).list();

		assertThat(results).hasSize(expectedCount);
	}

}
