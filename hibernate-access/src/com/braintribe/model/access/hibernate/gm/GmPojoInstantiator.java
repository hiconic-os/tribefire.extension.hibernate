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

import org.hibernate.bytecode.spi.ReflectionOptimizer.InstantiationOptimizer;
import org.hibernate.mapping.Component;
import org.hibernate.tuple.PojoInstantiator;

import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.reflection.EntityType;

/**
 * @see GmPojoComponentTuplizer
 * 
 * @author peter.gazdik
 */
public class GmPojoInstantiator extends PojoInstantiator {

	private static final long serialVersionUID = 3884696492020529323L;

	private final EntityType<?> entityType;

	public GmPojoInstantiator(Component component, InstantiationOptimizer optimizer) {
		super(component, optimizer);

		this.entityType = GMF.getTypeReflection().getEntityType(component.getComponentClassName());
	}

	@Override
	public Object instantiate() {
		return entityType.createPlain();
	}

	@Override
	public boolean isInstance(Object object) {
		return entityType.isInstance(object);
	}

}
