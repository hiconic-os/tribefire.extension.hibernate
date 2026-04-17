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
import com.braintribe.model.access.hibernate.base.model.inverse1toN.KnowsParent;
import com.braintribe.model.access.hibernate.base.tools.HibernateAccessSetupHelper;
import com.braintribe.model.access.hibernate.base.tools.TestHibernateSessionFactoryBean;
import com.braintribe.model.access.hibernate.base.wire.space.HibernateModelsSpace;
import com.braintribe.model.accessdeployment.hibernate.meta.MappingVersion;
import com.braintribe.model.meta.GmMetaModel;

/**
 * @see KnowsParent
 * @see HibernateModelsSpace#inverseOneToMany()
 * 
 * @author peter.gazdik
 */
public class InverseOneToManyExample_Main {

	// @formatter:off 
	// docker run --name hbm --rm -d -p 65433:5432 -e POSTGRES_DB=dbtest -e POSTGRES_USER=cortex -e POSTGRES_PASSWORD=cortex postgres:latest
	// @formatter:on

	private static final Supplier<GmMetaModel> modelSupplier = HibernateAccessRecyclingTestBase.hibernateModels::inverseOneToMany;

//	private static DbVendor DB_VENDOR = DbVendor.postgres;
	 private static DbVendor DB_VENDOR = DbVendor.h2;

	private static Integer MAPPING_VERSION = MappingVersion.MAPPING_VERSION_1;

	public static void main(String[] args) throws Exception {
		try {
			new InverseOneToManyExample_Main().run();

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

		KnowsParent parent = KnowsParent.T.create();
		parent.setId("parent");
		session.persist(parent);

		KnowsParent child1 = KnowsParent.T.create();
		child1.setId("child1");
		child1.setParentId("parent");
		session.persist(child1);
		
		KnowsParent child2 = KnowsParent.T.create();
		child2.setId("child2");
		child2.setParentId("parent");
		session.persist(child2);
		
		KnowsParent grandChild11 = KnowsParent.T.create();
		grandChild11.setId("grandChild11");
		child1.setParentId("child1");
		session.persist(grandChild11);

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

		@SuppressWarnings("deprecation")
		List<KnowsParent> list = session.createQuery("select kp from " + KnowsParent.class.getName() + " kp left join kp.children c where c is null").list();
		assertThat(list).hasSize(2);

		spOut(list);

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
