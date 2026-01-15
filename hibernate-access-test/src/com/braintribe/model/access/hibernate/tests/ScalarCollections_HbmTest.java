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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.model.collection.ScalarEnum;
import com.braintribe.model.access.hibernate.base.model.collection.ScalarsEntity;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;

/**
 * @see ScalarsEntity
 * @see HibernateAccessRecyclingTestBase
 * 
 * @author peter.gazdik
 */
public class ScalarCollections_HbmTest extends HibernateAccessRecyclingTestBase {

	private static final Date DATE_VALUE = new Date(81498843465L);
	private static final Timestamp TIMESTAMP_VALUE = new Timestamp(DATE_VALUE.getTime());
	private static final String TEXT = "TEXT";

	@Override
	protected GmMetaModel model() {
		return hibernateModels.scalarCollections();
	}

	@Test
	public void storesAndLoadsScalarCollections() throws Exception {
		run_storesAndLoadsScalarCollections();
	}

	protected void run_storesAndLoadsScalarCollections() throws Exception {
		ScalarsEntity scalars = session.create(ScalarsEntity.T);

		scalars.setEnumV(ScalarEnum.earth);
		scalars.setDateV(DATE_VALUE);
		scalars.setDecimalV(BigDecimal.ONE);
		scalars.setLongV(1L);
		scalars.setIntV(1);
		scalars.setDoubleV(1.0d);
		scalars.setFloatV(1.0f);
		scalars.setBooleanV(Boolean.TRUE);
		scalars.setStringV(TEXT);

		scalars.getEnumSet().add(ScalarEnum.earth);
		scalars.getDateSet().add(DATE_VALUE);
		scalars.getDecimalSet().add(BigDecimal.ONE);
		scalars.getLongSet().add(1L);
		scalars.getIntSet().add(1);
		scalars.getDoubleSet().add(1.0d);
		scalars.getFloatSet().add(1.0f);
		scalars.getBooleanSet().add(Boolean.TRUE);
		scalars.getStringSet().add(TEXT);

		scalars.getDateKeyMap().put(DATE_VALUE, TEXT);
		scalars.getEnumKeyMap().put(ScalarEnum.earth, TEXT);
		scalars.getDecimalKeyMap().put(BigDecimal.ONE, TEXT);

		scalars.getDateValueMap().put(TEXT, DATE_VALUE);
		scalars.getEnumValueMap().put(TEXT, ScalarEnum.earth);
		scalars.getDecimalValueMap().put(TEXT, BigDecimal.ONE);

		session.commit();

		resetGmSession();

		List<ScalarsEntity> list = session.query().entities(EntityQueryBuilder.from(ScalarsEntity.T).done()).list();

		assertThat(list).hasSize(1);

		ScalarsEntity e = list.get(0);
		assertThat(e.getEnumV()).isEqualTo(ScalarEnum.earth);
		assertThat(e.getDateV()).isEqualTo(TIMESTAMP_VALUE);
		assertEqualsDecimal(e.getDecimalV(), BigDecimal.ONE);
		assertThat(e.getLongV()).isEqualTo(1L);
		assertThat(e.getIntV()).isEqualTo(1);
		assertThat(e.getDoubleV()).isEqualTo(1.0d);
		assertThat(e.getFloatV()).isEqualTo(1.0f);
		assertThat(e.getBooleanV()).isEqualTo(Boolean.TRUE);
		assertThat(e.getStringV()).isEqualTo(TEXT);

		assertThat(e.getEnumSet()).contains(ScalarEnum.earth);
		assertThat(e.getDateSet()).contains(TIMESTAMP_VALUE);
		assertEqualsDecimal(first(e.getDecimalSet()), BigDecimal.ONE);
		assertThat(e.getLongSet()).contains(1L);
		assertThat(e.getIntSet()).contains(1);
		assertThat(e.getDoubleSet()).contains(1.0d);
		assertThat(e.getFloatSet()).contains(1.0f);
		assertThat(e.getBooleanSet()).contains(Boolean.TRUE);
		assertThat(e.getStringSet()).contains(TEXT);

		assertThat(e.getDateValueMap()).containsEntry(TEXT, TIMESTAMP_VALUE);
		assertThat(e.getEnumValueMap()).containsEntry(TEXT, ScalarEnum.earth);
		assertThat(e.getDecimalValueMap()).hasSize(1);
		assertThat((String) first(e.getDecimalValueMap().keySet())).isEqualTo(TEXT);
		assertEqualsDecimal(first(e.getDecimalValueMap().values()), BigDecimal.ONE);
	}

	private void assertEqualsDecimal(BigDecimal v1, BigDecimal v2) {
		assertThat(v1.longValueExact()).isEqualTo(v2.longValueExact());
	}
}
