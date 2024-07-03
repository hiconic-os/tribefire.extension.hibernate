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
package com.braintribe.persistence.hibernate.adaptor;

import java.util.List;

import org.w3c.dom.Document;

import com.braintribe.cfg.Required;
import com.braintribe.logging.Logger;

public class CompoundHibernateConfigurationAdaptor implements HibernateConfigurationAdaptor {

	private List<HibernateConfigurationAdaptor> adaptors;

	protected static Logger log = Logger.getLogger(CompoundHibernateConfigurationAdaptor.class);

	@Required
	public void setAdaptors(List<HibernateConfigurationAdaptor> adaptors) {
		this.adaptors = adaptors;
	}

	@Override
	public void adaptEhCacheConfigurationResource(Document configurationDocument) throws Exception {
		for (HibernateConfigurationAdaptor adaptor : adaptors) {
			adaptor.adaptEhCacheConfigurationResource(configurationDocument);
		}
	}

	@Override
	public void cleanup() {
		for (HibernateConfigurationAdaptor adaptor : adaptors) {
			try {
				adaptor.cleanup();
			} catch (Throwable t) {
				log.error("Failed to cleanup adaptor " + adaptor, t);
			}
		}
	}

}
