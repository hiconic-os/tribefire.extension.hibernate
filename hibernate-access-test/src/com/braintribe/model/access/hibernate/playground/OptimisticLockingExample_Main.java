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

import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.braintribe.common.db.DbVendor;
import com.braintribe.common.lcd.function.XConsumer;
import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.model.optimistic.VersionedEntity;
import com.braintribe.model.access.hibernate.base.tools.HibernateAccessSetupHelper;
import com.braintribe.model.access.hibernate.base.tools.TestHibernateSessionFactoryBean;
import com.braintribe.model.access.hibernate.base.wire.space.HibernateModelsSpace;
import com.braintribe.model.meta.GmMetaModel;

/**
 * @see VersionedEntity
 * @see HibernateModelsSpace#versioned()
 * 
 * @author peter.gazdik
 */
public class OptimisticLockingExample_Main {

	// @formatter:off 
	// docker run --name hbm --rm -d -p 65433:5432 -e POSTGRES_DB=dbtest -e POSTGRES_USER=cortex -e POSTGRES_PASSWORD=cortex postgres:latest
	// @formatter:on

	private static final int POSTGRES_PORT = 65432;

	private static final Supplier<GmMetaModel> modelSupplier = HibernateAccessRecyclingTestBase.hibernateModels::versioned;

	private static DbVendor DB_VENDOR = DbVendor.postgres;
	// private static DbVendor DB_VENDOR = DbVendor.h2;

	public static void main(String[] args) throws Exception {
		try {
			new OptimisticLockingExample_Main().run();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			HibernateAccessSetupHelper.close();
		}
	}

	private TestHibernateSessionFactoryBean hsfb;
	private Long id;

	private static int CONCURRENCY = 2;
	CountDownLatch transStartCdl = new CountDownLatch(CONCURRENCY);
	CountDownLatch transEndCdl = new CountDownLatch(CONCURRENCY);

	// This fails when CONCURRENCY is at least 2
	// -> in such case 2 threads are updating the same entity twice and they wait for each other via transStartCdl.
	private void run() throws Exception {
		spOut("Running Optimistic Locking Test");

		DataSource dataSource = dataSource();

		hsfb = hibernateSessionFactoryBean(modelSupplier, dataSource);
		hsfb.setShowSql(true);
		hsfb.postConstruct();

		// Create the single entity
		doHibernateTransaction(session -> {
			VersionedEntity ve = VersionedEntity.T.create();
			ve.setName("The Versioned");
			ve.setCount(1);
			session.save(ve);

			id = ve.getId();
			spOut("Saved with ID: " + ve.getId());
		});

		for (int i = 0; i < CONCURRENCY; i++)
			Thread.ofVirtual().start(this::queryUpdateCommit);

		transEndCdl.await();
	}

	private void queryUpdateCommit() {
		try {
			doHibernateTransaction(session -> {
				Query<VersionedEntity> query = session.createQuery("select e from VersionedEntity e where id = ?1");
				query.setParameter(1, id);
				// query.setParameter(1, 1L);
				VersionedEntity ve = query.getSingleResult();

				transStartCdl.countDown();
				// here all virtual threads wait until all reach this point.
				// this means they all read the entity in the initial state with version = 0, count - 1
				transStartCdl.await();

				spOut("VE: " + ve);

				ve.setCount(ve.getCount() + 1);

				spOut("Saving with count: " + ve.getCount());
			});

			spOut("Saved successfully");

		} finally {
			transEndCdl.countDown();
		}
	}

	private void doHibernateTransaction(XConsumer<Session> task) {
		SessionFactory sessionFactory = hsfb.getObject();

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		try {
			task.accept(session);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		t.commit();
		session.close();
	}

	private DataSource dataSource() {
		return switch (DB_VENDOR) {
			case h2 -> HibernateAccessSetupHelper.dataSource_H2_File("h2/versioned.h2");
			case postgres -> HibernateAccessSetupHelper.dataSource_Postgres(POSTGRES_PORT);
			default -> throw new IllegalArgumentException("Unsupported db vendor: " + DB_VENDOR);
		};
	}

}
