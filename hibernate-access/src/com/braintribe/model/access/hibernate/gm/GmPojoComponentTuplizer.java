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
package com.braintribe.model.access.hibernate.gm;

import org.hibernate.mapping.Component;
import org.hibernate.tuple.Instantiator;
import org.hibernate.tuple.component.PojoComponentTuplizer;

/**
 * Hibernate does not use interceptors for embedded properties (components), thus we need a different way how to tweak
 * it to be able to instantiate GM entities.
 * 
 * As of the time this was written (21.3.2018), we define this tuplizer for every embedded property, i.e. every
 * "component" tag in the mapping xmls.
 * 
 * @author peter.gazdik
 */
public class GmPojoComponentTuplizer extends PojoComponentTuplizer {

	private static final long serialVersionUID = -6809959369852886449L;

	public GmPojoComponentTuplizer(Component component) {
		super(component);
	}

	@Override
	protected Instantiator buildInstantiator(Component component) {
		return new GmPojoInstantiator(component, null);
	}
}
