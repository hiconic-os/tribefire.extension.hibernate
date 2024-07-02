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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.model.access.hibernate;

import org.hibernate.EmptyInterceptor;

import com.braintribe.model.generic.GenericEntity;

/**
 * For {@link GenericEntity} instances this interceptor returns the entity type signature (which is equal to the fully
 * qualified name of the class/interfaces that defines the type) as name, hiding the suffix in the fully qualified class
 * name of the actual (possibly enhanced) type which Hibernate would otherwise use.
 * 
 * @author michael.lafite
 */
public class EnhancerHidingInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = -113865171029202291L;

	@Override
	public String getEntityName(final Object object) {
		if (object instanceof GenericEntity) {
			return ((GenericEntity) object).entityType().getTypeSignature();
		} else {
			return super.getEntityName(object);
		}
	}
}
