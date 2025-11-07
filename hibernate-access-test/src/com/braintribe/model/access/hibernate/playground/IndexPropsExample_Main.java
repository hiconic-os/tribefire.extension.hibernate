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

import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.braintribe.common.db.DbVendor;
import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.model.index.IndexedEntity;
import com.braintribe.model.access.hibernate.base.tools.HibernateAccessSetupHelper;
import com.braintribe.model.access.hibernate.base.tools.TestHibernateSessionFactoryBean;
import com.braintribe.model.access.hibernate.base.wire.space.HibernateModelsSpace;
import com.braintribe.model.access.hibernate.schema.meta.DbIndexCreator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.deployment.hibernate.mapping.HbmXmlGeneratingService;
import com.braintribe.model.processing.deployment.hibernate.mapping.index.IndexDescriptor;
import com.braintribe.util.jdbc.JdbcTools;
import com.braintribe.utils.lcd.StringTools;
import com.braintribe.utils.TimeTracker;

/**
 * @see IndexedEntity
 * @see HibernateModelsSpace#indexed()
 * 
 * @author peter.gazdik
 */
public class IndexPropsExample_Main {

	// @formatter:off 
	// docker run --name hbm --rm -d -p 65433:5432 -e POSTGRES_DB=dbtest -e POSTGRES_USER=cortex -e POSTGRES_PASSWORD=cortex postgres:latest
	// @formatter:on

	private static final Supplier<GmMetaModel> modelSupplier = HibernateAccessRecyclingTestBase.hibernateModels::indexed;

	private static DbVendor DB_VENDOR = DbVendor.postgres;
	// private static DbVendor DB_VENDOR = DbVendor.h2;

	public static void main(String[] args) throws Exception {
		try {
			new IndexPropsExample_Main().run();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			HibernateAccessSetupHelper.close();
		}
	}

	private String tableName;

	private void run() throws Exception {
		spOut("Running index test");

		DataSource dataSource = dataSource();

		TestHibernateSessionFactoryBean hsfb = hibernateSessionFactoryBean(modelSupplier, dataSource);
		hsfb.setShowSql(false);
		hsfb.addAfterSchemaCreationTask(() -> dbIndexCreator(dataSource, hsfb).createIndices());
		hsfb.postConstruct();

		SessionFactory sessionFactory = hsfb.getObject();

		TimeTracker.start("all");
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		// for (int i = 1; i<=1000; i++) {
		// IndexedEntity e = IndexedEntity.T.create();
		// e.setName("Indexed Loop 7 " + i);
		// Set<String> strSet = e.getStrSet();
		// strSet.add("a");
		// strSet.add("b");
		// strSet.add("c");
		// strSet.add("" + i);
		// strSet.add("X" + i);
		// strSet.add("Y" + i);
		//
		// session.save(e);
		// }

		t.commit();
		session.close();

		TimeTracker.stopAndPrint("all");

		JdbcTools.withConnection(dataSource, false, () -> "Verifying created indices.", connection -> {
			//
			tableName = toTableName(IndexedEntity.T); // ixindexedentityidindentstst
			indicesExist(connection, propIx("entity"), propIx("str"));
		});
	}

	private DbIndexCreator dbIndexCreator(DataSource dataSource, TestHibernateSessionFactoryBean hsfb) {
		File mappingsDirectory = hsfb.mappingDirectoryLocations()[0];
		File indicesJson = new File(mappingsDirectory, HbmXmlGeneratingService.INDICES_JSON_FILE_NAME);

		List<IndexDescriptor> indexDescriptors = HbmXmlGeneratingService.readIndexDescriptors(indicesJson);

		DbIndexCreator bean = new DbIndexCreator();
		bean.setContextDescription(getClass().getSimpleName());
		bean.setDataSource(dataSource);
		bean.setIndexDescriptors(indexDescriptors);

		return bean;
	}

	private DataSource dataSource() {
		return switch (DB_VENDOR) {
			case h2 -> HibernateAccessSetupHelper.dataSource_H2("h2/index.h2");
			case postgres -> HibernateAccessSetupHelper.dataSource_Postgres(65433);
			default -> throw new IllegalArgumentException("Unsupported db vendor: " + DB_VENDOR);
		};
	}

	private void indicesExist(Connection connection, String... indexNames) {
		Set<String> indices = JdbcTools.indicesExist(connection, tableName, indexNames);

		spOut("Existing indices for table: \t" + tableName);
		spOut(indices);
		spOut("");
	}

	private String toTableName(EntityType<?> et) {
		String shortName = et.getShortName();
		return switch (DB_VENDOR) {
			case h2 -> shortName.toUpperCase();
			case postgres -> shortName.toLowerCase();
			default -> throw new IllegalArgumentException("Unsupported db vendor: " + DB_VENDOR);
		};
	}

	private String propIx(String propName) {
		// return "IX" + propName.toUpperCase() + tableName;
		return "Ix" + StringTools.capitalize(propName) + tableName;
	}

}
