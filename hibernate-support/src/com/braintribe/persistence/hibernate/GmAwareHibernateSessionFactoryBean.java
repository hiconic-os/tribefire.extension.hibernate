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
package com.braintribe.persistence.hibernate;

import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;

import com.braintribe.model.access.hibernate.interceptor.GmAdaptionInterceptor;
import com.braintribe.model.access.hibernate.interceptor.GmTuplizerIntegrator;
import com.braintribe.model.generic.GMF;

/**
 * @author peter.gazdik
 */
public class GmAwareHibernateSessionFactoryBean extends HibernateSessionFactoryBean {

	public GmAwareHibernateSessionFactoryBean() {
		this.setClassLoader(itwOrModuleClassLoader());
		this.setEntityInterceptor(new GmAdaptionInterceptor());
	}
	
	
	@Override
	protected void enrich(BootstrapServiceRegistryBuilder builder) {
		builder.applyIntegrator(new GmTuplizerIntegrator());
	}

	private static ClassLoader itwOrModuleClassLoader() {
		if (isLoadedByModule())
			// Module CL has ITW CL as parent
			return GmAwareHibernateSessionFactoryBean.class.getClassLoader();
		else
			return (ClassLoader) GMF.getTypeReflection().getItwClassLoader();
	}

	private static boolean isLoadedByModule() {
		return GmAwareHibernateSessionFactoryBean.class.getClassLoader().getClass().getSimpleName().startsWith("ModuleClassLoader");
	}

}
