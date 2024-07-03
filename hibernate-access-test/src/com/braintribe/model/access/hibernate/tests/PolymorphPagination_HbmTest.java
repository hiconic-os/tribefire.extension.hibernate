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

import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.HibernateBaseModelTestBase;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.processing.query.fluent.SelectQueryBuilder;
import com.braintribe.model.query.SelectQuery;
import com.braintribe.testing.category.KnownIssue;

/**
 * This is more a test for a bug in hibernate.
 * 
 * @see "https://jira.braintribe.com/browse/COREHA-23"
 * 
 * @see HibernateAccessRecyclingTestBase
 * 
 * @author peter.gazdik
 */
@Category(KnownIssue.class)
public class PolymorphPagination_HbmTest extends HibernateBaseModelTestBase {

	/* WHY KnownIssue? */

	/* The pagination works in Eclipse, and probably also in Tomcat, but doesn't work in a unit test.
	 * 
	 * There is a pagination bug in hibernate, and the fix is in HQLQueryPlan in hibernate-access, which is a fixed version of that class from
	 * hibernate-core. The fix works as long as hibernate-access is earlier on classpath than hibernate-core, thus the fixed class is loaded. This is
	 * usually the case - it's alphabetically, but not for unit tests. There the absolute path are sorted, including group-id, and
	 * tribefire/extension/hibernate comes after org/hibernate. This also explains why it worked in com.braintribe.gm.hibernate worked. */

	/* Once the extraction of Hibernate stuff from core is over, we switch to hibernate 6. Then we'll see if the but is still there, if it is, I will
	 * try to address it in hibernate itself and get rid of the patch. */

	@Test
	public void simplest() throws Exception {
		createDataAndRunQueryWithLimitOffset(1, 0);
	}

	@Test
	public void withOffset() throws Exception {
		createDataAndRunQueryWithLimitOffset(1, 1);
	}

	@Test
	public void withOffset_BeyondFirstSubType() throws Exception {
		createDataAndRunQueryWithLimitOffset(3, 4);
	}

	private void createDataAndRunQueryWithLimitOffset(int limit, int offset) {
		createBse("BSE-1");
		createBse("BSE-2");
		createBse("BSE-3");
		createBse("BSE-4");

		createBe("BE-1");
		createBe("BE-2");
		createBe("BE-3");
		createBe("BE-4");

		session.commit();

		resetGmSession();

		List<GenericEntity> entities = session.query().select(entitiesPaginated(limit, offset)).list();

		assertThat(entities).hasSize(limit);
	}

	private SelectQuery entitiesPaginated(int limit, int offset) {
		return new SelectQueryBuilder().select("e").from(GenericEntity.T, "e").paging(limit, offset).done();
	}

}
