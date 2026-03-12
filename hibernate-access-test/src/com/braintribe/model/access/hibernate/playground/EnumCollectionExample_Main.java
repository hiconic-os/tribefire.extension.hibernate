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
import static com.braintribe.testing.junit.assertions.assertj.core.api.Assertions.assertThat;
import static com.braintribe.utils.SysPrint.spOut;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.braintribe.common.db.DbVendor;
import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.model.collection.EnumCollectionEntity;
import com.braintribe.model.access.hibernate.base.model.collection.ScalarEnum;
import com.braintribe.model.access.hibernate.base.model.collection.ScalarsEntity;
import com.braintribe.model.access.hibernate.base.tools.HibernateAccessSetupHelper;
import com.braintribe.model.access.hibernate.base.tools.TestHibernateSessionFactoryBean;
import com.braintribe.model.access.hibernate.base.wire.space.HibernateModelsSpace;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.deployment.hibernate.mapping.HbmXmlGeneratingService;

/**
 * @see ScalarsEntity
 * @see HibernateModelsSpace#indexed()
 * 
 * @author peter.gazdik
 */
public class EnumCollectionExample_Main {

	// @formatter:off 
	// docker run --name hbm --rm -d -p 65433:5432 -e POSTGRES_DB=dbtest -e POSTGRES_USER=cortex -e POSTGRES_PASSWORD=cortex postgres:latest
	// @formatter:on

	private static final Supplier<GmMetaModel> modelSupplier = HibernateAccessRecyclingTestBase.hibernateModels::enumCollections;

	private static DbVendor DB_VENDOR = DbVendor.postgres;
	// private static DbVendor DB_VENDOR = DbVendor.h2;

	private static Integer MAPPING_VERSION = HbmXmlGeneratingService.MAPPING_VERSION_1;

	private static final String TEXT = "TEXT";

	public static void main(String[] args) throws Exception {
		try {
			new EnumCollectionExample_Main().run();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			HibernateAccessSetupHelper.close();
		}
	}

	private void run() throws Exception {
		spOut("Running scalar collections test");

		DataSource dataSource = dataSource();

		TestHibernateSessionFactoryBean hsfb = hibernateSessionFactoryBean(modelSupplier, dataSource, MAPPING_VERSION);
		hsfb.setShowSql(false);
		hsfb.postConstruct();

		SessionFactory sessionFactory = hsfb.getObject();

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		EnumCollectionEntity scalars = EnumCollectionEntity.T.create();

		scalars.setName("Entity With Enum Collection");
		scalars.getEnumSet().add(ScalarEnum.earth);
		scalars.getEnumList().add(ScalarEnum.earth);
		scalars.getEnumKeyMap().put(ScalarEnum.earth, TEXT);
		scalars.getEnumValueMap().put(TEXT, ScalarEnum.earth);

		session.persist(scalars);

		t.commit();
		session.close();

		/////////////////////
		// Assertions
		/////////////////////

		session = sessionFactory.openSession();
		t = session.beginTransaction();

		List<EnumCollectionEntity> list = session.createQuery("select e from " + EnumCollectionEntity.class.getName() + " e").list();
		assertThat(list).hasSize(1);

		EnumCollectionEntity e = list.get(0);
		assertThat(e.getName()).isEqualTo("Entity With Enum Collection");
		assertThat(e.getEnumSet()).hasSize(1).contains(ScalarEnum.earth);
		assertThat(e.getEnumList()).hasSize(1).contains(ScalarEnum.earth);
		assertThat(e.getEnumKeyMap()).containsEntry(ScalarEnum.earth, TEXT);
		assertThat(e.getEnumValueMap()).containsEntry(TEXT, ScalarEnum.earth);

		t.commit();
		session.close();
	}

	private DataSource dataSource() {
		return switch (DB_VENDOR) {
			case h2 -> HibernateAccessSetupHelper.dataSource_H2("h2/index.h2");
			case postgres -> HibernateAccessSetupHelper.dataSource_Postgres(65433);
			default -> throw new IllegalArgumentException("Unsupported db vendor: " + DB_VENDOR);
		};
	}

}
