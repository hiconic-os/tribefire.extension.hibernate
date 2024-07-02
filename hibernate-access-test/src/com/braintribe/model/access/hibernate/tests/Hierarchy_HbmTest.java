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

import org.junit.Test;

import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.HibernateBaseModelTestBase;
import com.braintribe.model.access.hibernate.base.model.simple.HierarchyBase;
import com.braintribe.model.access.hibernate.base.model.simple.HierarchySubA;
import com.braintribe.model.access.hibernate.base.model.simple.HierarchySubB;
import com.braintribe.model.processing.query.fluent.SelectQueryBuilder;
import com.braintribe.model.query.SelectQuery;

/**
 * This is more a test for a bug in hibernate.
 * 
 * @see "https://jira.braintribe.com/browse/COREHA-23"
 * 
 * @see HibernateAccessRecyclingTestBase
 * 
 * @author peter.gazdik
 */
public class Hierarchy_HbmTest extends HibernateBaseModelTestBase {

	@Test
	public void selectTypeSignature() throws Exception {
		createSubA("AA");
		createSubB("BB");

		session.commit();
		
		SelectQuery query = new SelectQueryBuilder() //
				.select().entitySignature().entity("e") //
				.select("e", "name") //
				.from(HierarchyBase.T, "e") //
				.done();

		runSelectQuery(query);

		qra.assertContains(HierarchySubA.T.getTypeSignature(), "AA");
		qra.assertContains(HierarchySubB.T.getTypeSignature(), "BB");
		qra.assertNoMoreResults();
	}

	@Test
	public void typeSignatureCondition() throws Exception {
		createSubA("AA");
		createSubB("BB");

		session.commit();
		
		SelectQuery query = new SelectQueryBuilder() //
				.select().entitySignature().entity("e") //
				.select("e", "name") //
				.from(HierarchyBase.T, "e") //
				.where() //
					.entitySignature("e").like("*A")
				.done();

		runSelectQuery(query);

		qra.assertContains(HierarchySubA.T.getTypeSignature(), "AA");
		qra.assertNoMoreResults();
	}

}
