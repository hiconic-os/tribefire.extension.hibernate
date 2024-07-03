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
package com.braintribe.persistence.hibernate.sql;

import org.hibernate.SessionFactory;

import com.braintribe.cfg.Required;
import com.braintribe.util.jdbc.SchemaEnsuringDataSource;

/**
 * A {@link SchemaEnsuringDataSource} which ensures the DB schema using configured Hibernate {@link SessionFactory}
 * 
 * @author peter.gazdik
 */
public class HibernateEnhancedDataSource extends SchemaEnsuringDataSource {

	private SessionFactory sessionFactory;

	// @formatter:off
	@Required public void setSessionFactory(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }
	// @formatter:on

	@Override
	protected void updateSchema() {
		sessionFactory.openStatelessSession().close();
	}

}
