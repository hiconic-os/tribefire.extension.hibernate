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

import java.util.function.Supplier;

import org.hibernate.SessionFactory;

import com.braintribe.cfg.Required;
import com.braintribe.util.jdbc.SchemaEnsuringDataSource;

/**
 * A {@link SchemaEnsuringDataSource} which ensures the DB schema using configured Hibernate {@link SessionFactory}
 * 
 * @author peter.gazdik
 */
public class HibernateEnhancedDataSource extends SchemaEnsuringDataSource {

	@SuppressWarnings("unused")
	private String name; // For debugging?
	private Supplier<SessionFactory> sessionFactorySupplier;

	@Required
	public void setName(String name) {
		this.name = name;
	}
	
	@Required
	public void setSessionFactorySupplier(Supplier<SessionFactory> sessionFactorySupplier) {
		this.sessionFactorySupplier = sessionFactorySupplier;
	}

	// This method is guaranteed to only be called once
	@Override
	protected void updateSchema() {
		sessionFactorySupplier.get().openStatelessSession().close();
	}

}
