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
package com.braintribe.model.access.hibernate.interceptor;

import org.hibernate.CallbackException;
import org.hibernate.Interceptor;
import org.hibernate.metamodel.RepresentationMode;

import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.GenericModelTypeReflection;

/**
 * Interceptor which provides correct instances for {@link GenericEntity}s and resolves their names.
 */
public class GmAdaptionInterceptor implements Interceptor {

	private static final long serialVersionUID = -5221026406788869293L;
	private static final GenericModelTypeReflection typeReflection = GMF.getTypeReflection();

	@Override
	public String getEntityName(Object object) {
		if (object instanceof GenericEntity) {
			return ((GenericEntity) object).entityType().getTypeSignature();

		} else {
			return Interceptor.super.getEntityName(object);
		}
	}

	@Override
	public Object instantiate(String entityName, RepresentationMode representationMode, Object id)
			throws CallbackException {
		if (representationMode != RepresentationMode.POJO) 
			return Interceptor.super.instantiate(entityName, representationMode, id);
		
		EntityType<?> entityType = typeReflection.getType(entityName);

		GenericEntity ge = entityType.createPlain();
		ge.setId(id);

		return ge;
	}
}
