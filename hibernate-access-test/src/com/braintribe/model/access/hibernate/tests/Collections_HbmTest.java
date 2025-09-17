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
import static com.braintribe.utils.lcd.CollectionTools2.first;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.HibernateBaseModelTestBase;
import com.braintribe.model.access.hibernate.base.model.simple.BasicCollectionEntity;
import com.braintribe.model.access.hibernate.base.model.simple.BasicColor;
import com.braintribe.model.access.hibernate.hql.Intersects;
import com.braintribe.model.processing.query.fluent.SelectQueryBuilder;
import com.braintribe.model.query.SelectQuery;

/**
 * Testing querying related to collectionsBasic tests for entity with scalar properties only.
 * 
 * @see HibernateAccessRecyclingTestBase
 * 
 * @author peter.gazdik
 */
public class Collections_HbmTest extends HibernateBaseModelTestBase {

	@Test
	public void queryOwnerByMapKey() throws Exception {
		String MAP_OWNER_NAME = "mapOwner";

		BasicCollectionEntity keyEntity = newCe("entityToDelete");

		BasicCollectionEntity mapOwner = newCe(MAP_OWNER_NAME);
		mapOwner.getEntityToString().put(keyEntity, "value");

		commitAndReset();

		SelectQuery query = new SelectQueryBuilder() //
				.select("e") //
				.from(BasicCollectionEntity.T, "e") //
				/**/.join("e", "entityToString", "map") //
				.where() //
				/**/.mapKey("map").eq().entity(keyEntity) //
				.done();

		List<BasicCollectionEntity> entities = session.query().select(query).list();

		assertThat(entities).hasSize(1);

		BasicCollectionEntity e = first(entities);
		assertThat(e.getName()).isEqualTo(MAP_OWNER_NAME);
	}

	/**
	 * Tests a query with a special disjunction eligible for the {@link Intersects} condition, used by
	 * {@link com.braintribe.model.access.hibernate.hql.DisjunctedInOptimizer}
	 */
	@Test
	public void queryEntityViaIntersectsClaus() throws Exception {
		BasicCollectionEntity entityWithEnums = newCe("e1");

		entityWithEnums.getColorsSet().addAll(List.of(BasicColor.red));

		commitAndReset();

		// @formatter:off
		SelectQuery query = new SelectQueryBuilder()
				.select("e")
				.from(BasicCollectionEntity.T, "e")
				.where()
					.disjunction()
						.value(BasicColor.red).in().property("e","colorsSet")
						.value(BasicColor.green).in().property("e","colorsSet")
					.close()
				.done();
		// @formatter:on

		List<BasicCollectionEntity> entities = session.query().select(query).list();

		assertThat(entities).hasSize(1);

		BasicCollectionEntity e = first(entities);
		assertThat(e.getName()).isEqualTo("e1");
	}

	private void commitAndReset() {
		session.commit();
		resetGmSession();
	}

	private BasicCollectionEntity newCe(String name) {
		BasicCollectionEntity bse = session.create(BasicCollectionEntity.T);
		bse.setName(name);
		return bse;
	}

}
