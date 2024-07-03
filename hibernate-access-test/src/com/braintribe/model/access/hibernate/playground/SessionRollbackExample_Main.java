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
package com.braintribe.model.access.hibernate.playground;

import static com.braintribe.utils.SysPrint.spOut;

import java.util.List;
import java.util.function.Supplier;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.model.simple.BasicScalarEntity;
import com.braintribe.model.access.hibernate.base.tools.HibernateAccessSetupHelper;
import com.braintribe.model.meta.GmMetaModel;

/**
 * @author peter.gazdik
 */
public class SessionRollbackExample_Main {

	private static final String dbName = "hibernate-test-all";
	private static final Supplier<GmMetaModel> modelSupplier = HibernateAccessRecyclingTestBase.hibernateModels::basic_NoPartition;

	public static void main(String[] args) throws Exception {
		try {
			new SessionRollbackExample_Main().run();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			HibernateAccessSetupHelper.close();
		}
	}

	private void run() throws Exception {
		SessionFactory sessionFactory = HibernateAccessSetupHelper.hibernateSessionFactory(modelSupplier,
				HibernateAccessSetupHelper.dataSource_H2(dbName));

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		BasicScalarEntity bse = BasicScalarEntity.T.create();
		bse.setName("BSE 1");

		session.save(bse);
		spOut("ENTITY:" + bse);

		String QUERY = "select e from com.braintribe.model.access.hibernate.base.model.simple.BasicScalarEntity e";

		List<?> results = session.createQuery(QUERY).getResultList();
		spOut("BEFORE ROLLBACK:" + results);

		t.rollback();
		session.beginTransaction();

		results = session.createQuery(QUERY).getResultList();
		spOut("AFTER ROLLBACK:" + results);

		session.close();
	}

}
