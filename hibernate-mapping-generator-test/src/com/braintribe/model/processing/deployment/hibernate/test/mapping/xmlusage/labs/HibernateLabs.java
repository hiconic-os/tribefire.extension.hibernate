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
package com.braintribe.model.processing.deployment.hibernate.test.mapping.xmlusage.labs;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import com.braintribe.model.processing.deployment.hibernate.test.mapping.xmlusage.DatabaseTest;
import com.braintribe.model.processing.deployment.hibernate.test.mapping.xmlusage.labs.model.EntityWithLongId;
import com.braintribe.model.processing.deployment.hibernate.test.mapping.xmlusage.labs.model.EntityWithStringId;

public class HibernateLabs extends DatabaseTest {


	private static final String mappingsDir = "src/com/braintribe/model/processing/deployment/hibernate/test/mapping/xmlusage/labs/model/";

	
	public static void main(String[] args) throws Exception {
		
		initializeDatabaseContext();
		
		try {
			
			HibernateLabs lab = new HibernateLabs();
			
			SessionFactory factory = getSessionFactory();
			
			Session session = factory.openSession();

			lab.testEntityWithLongId(session);
			
		} finally {
			destroyDatabaseContext();
		}

	}
	
	protected static SessionFactory getSessionFactory() {

		final Configuration configuration = new Configuration();

		configuration.addDirectory(new File(mappingsDir));

		final Map<String, String> properties = new HashMap<String, String>();

		//properties.put("hibernate.bytecode.use_reflection_optimizer", "false");
		//properties.put("hibernate.bytecode.provider", "cglib");
		properties.put("hibernate.connection.driver_class", driver);
		properties.put("hibernate.connection.url", url);
		properties.put("hibernate.connection.username", user);
		properties.put("hibernate.connection.password", password);

		// required to avoid "No CurrentSessionContext configured!" problem
		properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
		// create/update database tables
		properties.put(Environment.HBM2DDL_AUTO, "update");

		for (final Entry<String, String> propertyEntry : properties.entrySet()) {
			configuration.setProperty(propertyEntry.getKey(), propertyEntry.getValue());
		}

		StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        final SessionFactory hibernateSessionFactory = configuration.buildSessionFactory(ssrb.build());
        
        return hibernateSessionFactory;

        
	}

	protected void testEntityWithStringId(Session session) throws Exception {
		
		EntityWithStringId entity = new EntityWithStringId();
		
		entity.setName("test");
		
		Transaction transaction = session.beginTransaction();
		session.save(entity);
		
		// committing fails due to the lack of id, as Oracle doesn't set string ids for "native" strategy as MSSQL does
		transaction.commit();

	}

	protected void testEntityWithLongId(Session session) throws Exception {
		
		EntityWithLongId entity = new EntityWithLongId();
		
		entity.setId(100L);
		entity.setName("test");
		
		Transaction transaction = session.beginTransaction();
		session.save(entity);
		transaction.commit();

		// As strategy is "native", the actual id persisted in the table differs from the provided in the entity
		System.out.println(entity.getId());

	}
	
	
}
