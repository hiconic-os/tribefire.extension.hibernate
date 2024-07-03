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

import static com.braintribe.model.query.OrderingDirection.ascending;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.HibernateBaseModelTestBase;
import com.braintribe.model.access.hibernate.base.model.simple.BasicScalarEntity;
import com.braintribe.model.processing.query.fluent.SelectQueryBuilder;
import com.braintribe.model.query.SelectQuery;

/**
 * @see HibernateAccessRecyclingTestBase
 * 
 * @author peter.gazdik
 */
public class Pagination_HbmTest extends HibernateBaseModelTestBase {

	@Test
	public void simplePagination() throws Exception {

		createBse("BSE-1");
		createBse("BSE-2");
		createBse("BSE-3");
		createBse("BSE-4");

		session.commit();

		resetGmSession();

//		assertContainsForLimitOffset(1, 0, "BSE-1");
//		assertContainsForLimitOffset(1, 1, "BSE-2");
//		assertContainsForLimitOffset(2, 0, "BSE-1", "BSE-2");
		assertContainsForLimitOffset(0, 2, "BSE-3", "BSE-4");
//		assertContainsForLimitOffset(-5, 2, "BSE-3", "BSE-4");
	}

	private void assertContainsForLimitOffset(int limit, int offset, String... expectedNames) {
		List<String> names = queryPaginated(limit, offset);

		assertThat(names).containsExactly(expectedNames);
	}

	private List<String> queryPaginated(int limit, int offset) {
		return session.query().select(query(limit, offset)).list();
	}

	private SelectQuery query(int limit, int offset) {
		return new SelectQueryBuilder() //
				.select("e", "name") //
				.from(BasicScalarEntity.T, "e") //
				.orderBy(ascending).property("e", "name") //
				.paging(limit, offset) //
				.done();
	}

}
