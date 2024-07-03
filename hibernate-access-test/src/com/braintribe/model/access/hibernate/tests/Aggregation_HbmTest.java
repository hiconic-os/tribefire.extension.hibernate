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

import org.junit.Test;

import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.HibernateBaseModelTestBase;
import com.braintribe.model.access.hibernate.base.model.simple.BasicEntity;
import com.braintribe.model.access.hibernate.base.model.simple.BasicScalarEntity;

/**
 * This just shows how these tests work.
 * 
 * @see HibernateAccessRecyclingTestBase
 * 
 * @author peter.gazdik
 */
public class Aggregation_HbmTest extends HibernateBaseModelTestBase {

	@Test
	public void groupBySimple() throws Exception {
		bse("A", "MAN", 1);
		bse("B", "MAN", 2);
		bse("C", "WOMAN", 3);
		bse("D", "WOMAN", 4);
		bse("E", "WOMAN", 5);
		session.commit();

		runSelectQuery(from(BasicScalarEntity.T, "be") //
				.select("be", BasicScalarEntity.stringValue) //
				.select().count("be", BasicScalarEntity.name) //
				.select().sum("be", BasicScalarEntity.integerValue) //
				.done() //
		);

		qra.assertContains("MAN", 2L, 3L);
		qra.assertContains("WOMAN", 3L, 12L);
		qra.assertNoMoreResults();
	}

	@Test
	public void simpleGroupByDoneAutomatically() throws Exception {
		BasicScalarEntity a = bse("A", "MAN", 1);
		BasicScalarEntity c = bse("C", "WOMAN", 3);

		be("BSE-1", a, 1);
		be("BSE-2", a, 2);
		be("BSE-3", c, 3);
		be("BSE-4", c, 4);
		be("BSE-5", c, 5);
		session.commit();

		runSelectQuery(from(BasicEntity.T, "be") //
				.select("be", BasicEntity.scalarEntity) //
				.select().count("be", BasicEntity.name) //
				.select().sum("be", BasicEntity.integerValue) //
				.done() //
		);

		qra.assertContains(a, 2L, 3L);
		qra.assertContains(c, 3L, 12L);
		qra.assertNoMoreResults();
	}

	@Test
	public void groupByWithHaving() throws Exception {
		bse("A", "MAN", 1);
		bse("B", "MAN", 2);
		bse("C", "WOMAN", 3);
		bse("D", "WOMAN", 4);
		bse("E", "WOMAN", 5);
		session.commit();

		runSelectQuery(from(BasicScalarEntity.T, "be") //
				.select("be", BasicScalarEntity.stringValue) //
				.select().count("be", BasicScalarEntity.name) //
				.select().sum("be", BasicScalarEntity.integerValue) //
				.having()
					.count("be", BasicScalarEntity.name).ge(3L)
				.done() //
		);

		qra.assertContains("WOMAN", 3L, 12L);
		qra.assertNoMoreResults();
	}

	private BasicScalarEntity bse(String name, String stringValue, int integerValue) {
		BasicScalarEntity bse = createBse(name);
		bse.setStringValue(stringValue);
		bse.setIntegerValue(integerValue);

		return bse;
	}

	private BasicEntity be(String name, BasicScalarEntity bse, int integerValue) {
		BasicEntity be = createBe(name);
		be.setScalarEntity(bse);
		be.setIntegerValue(integerValue);

		return be;
	}

}
