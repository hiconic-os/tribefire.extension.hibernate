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

import static com.braintribe.model.access.hibernate.base.tools.HibernateAccessSetupHelper.hibernateSessionFactory;
import static com.braintribe.utils.SysPrint.spOut;

import java.util.function.Supplier;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.model.index.IndexedEntity;
import com.braintribe.model.access.hibernate.base.tools.HibernateAccessSetupHelper;
import com.braintribe.model.meta.GmMetaModel;

/**
 * @author peter.gazdik
 */
public class IndexPropsExample_Main {

	// @formatter:off 
	// docker run --name hbm --rm -d -p 65433:5432 -e POSTGRES_DB=dbtest -e POSTGRES_USER=cortex -e POSTGRES_PASSWORD=cortex postgres:latest
	// @formatter:on

	private static final Supplier<GmMetaModel> modelSupplier = HibernateAccessRecyclingTestBase.hibernateModels::indexed;

	public static void main(String[] args) throws Exception {
		try {
			new IndexPropsExample_Main().run();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			HibernateAccessSetupHelper.close();
		}
	}

	private void run() throws Exception {
		spOut("Running index test");

		SessionFactory sessionFactory = hibernateSessionFactory(modelSupplier, HibernateAccessSetupHelper.dataSource_H2_File("h2/index.h2"));
		// SessionFactory sessionFactory = hibernateSessionFactory(modelSupplier, HibernateAccessSetupHelper.dataSource_Postgres(65433));

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		IndexedEntity e = IndexedEntity.T.create();
		e.setName("Indexed 1");

		session.save(e);
		t.commit();
		session.close();
	}

}
