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
package com.braintribe.model.access.hibernate.playground;

import static com.braintribe.model.access.hibernate.base.tools.HibernateAccessSetupHelper.hibernateSessionFactoryBean;
import static com.braintribe.model.processing.deployment.hibernate.mapping.HbmXmlGeneratingService.MAPPING_VERSION_2;
import static com.braintribe.testing.junit.assertions.assertj.core.api.Assertions.assertThat;
import static com.braintribe.utils.SysPrint.spOut;
import static com.braintribe.utils.lcd.CollectionTools2.first;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.braintribe.common.db.DbVendor;
import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.model.collection.ScalarEnum;
import com.braintribe.model.access.hibernate.base.model.collection.ScalarsEntity;
import com.braintribe.model.access.hibernate.base.tools.HibernateAccessSetupHelper;
import com.braintribe.model.access.hibernate.base.tools.TestHibernateSessionFactoryBean;
import com.braintribe.model.access.hibernate.base.wire.space.HibernateModelsSpace;
import com.braintribe.model.meta.GmMetaModel;

/**
 * @see ScalarsEntity
 * @see HibernateModelsSpace#indexed()
 * 
 * @author peter.gazdik
 */
public class CollectionPropsExample_Main {

	// @formatter:off 
	// docker run --name hbm --rm -d -p 65433:5432 -e POSTGRES_DB=dbtest -e POSTGRES_USER=cortex -e POSTGRES_PASSWORD=cortex postgres:latest
	// @formatter:on

	private static final Supplier<GmMetaModel> modelSupplier = HibernateAccessRecyclingTestBase.hibernateModels::scalarCollections;

	private static DbVendor DB_VENDOR = DbVendor.postgres;
	// private static DbVendor DB_VENDOR = DbVendor.h2;

	public static void main(String[] args) throws Exception {
		try {
			new CollectionPropsExample_Main().run();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			HibernateAccessSetupHelper.close();
		}
	}

	private static final Date DATE_VALUE = new Date(81498843465L);
	private static final String TEXT = "TEXT";

	private void run() throws Exception {
		spOut("Running scalar collections test");

		DataSource dataSource = dataSource();

		TestHibernateSessionFactoryBean hsfb = hibernateSessionFactoryBean(modelSupplier, dataSource, MAPPING_VERSION_2);
		hsfb.setShowSql(false);
		hsfb.postConstruct();

		SessionFactory sessionFactory = hsfb.getObject();

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();


		ScalarsEntity scalars = ScalarsEntity.T.create();

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

		session.save(scalars);

		t.commit();
		session.close();

		// Date doesn't have time, - it is a date, not a timestamp for some reason
		// BigDecimal doesn't work at all.
		// Enum is an array

		/////////////////////
		// Assertions
		/////////////////////

		session = sessionFactory.openSession();
		t = session.beginTransaction();

		List<ScalarsEntity> list = session.createQuery("select e from " + ScalarsEntity.class.getName() + " e").list();
		assertThat(list).hasSize(1);

		Timestamp TIMESTAMP_VALUE = new Timestamp(DATE_VALUE.getTime());
		
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
		assertEquals(first(e.getDecimalValueMap().keySet()), TEXT);
		assertEqualsDecimal(first(e.getDecimalValueMap().values()), BigDecimal.ONE);

		t.commit();
		session.close();
	}

	private void assertEqualsDecimal(BigDecimal v1, BigDecimal v2) {
		assertThat(v1.longValueExact()).isEqualTo(v2.longValueExact());
	}

	private DataSource dataSource() {
		return switch (DB_VENDOR) {
			case h2 -> HibernateAccessSetupHelper.dataSource_H2("h2/index.h2");
			case postgres -> HibernateAccessSetupHelper.dataSource_Postgres(65433);
			default -> throw new IllegalArgumentException("Unsupported db vendor: " + DB_VENDOR);
		};
	}

}
