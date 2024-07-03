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
package com.braintribe.model.access.hibernate;

import java.util.List;
import java.util.ListIterator;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import com.braintribe.model.access.hibernate.gm.CompositeIdValues;
import com.braintribe.model.access.hibernate.hql.SelectHqlBuilder;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.value.EntityReference;
import com.braintribe.model.query.PropertyOperand;
import com.braintribe.model.query.SelectQuery;

/* package */ class HibernateAccessTools {

	public static GenericEntity deproxy(GenericEntity maybeProxy) {
		if (maybeProxy instanceof HibernateProxy) {
			HibernateProxy hibernateProxy = (HibernateProxy) maybeProxy;
			if (!Hibernate.isInitialized(maybeProxy))
				Hibernate.initialize(maybeProxy);

			return (GenericEntity) hibernateProxy.getHibernateLazyInitializer().getImplementation();
		} else {
			return maybeProxy;
		}
	}

	public static void ensureIdsAreGmValues(List<?> hqlResults) {
		replaceCompositeIdValusWithStrings(hqlResults);
	}

	public static void ensureIdsAreGmValues(SelectQuery query, List<?> hqlResults) {
		if (isSelectingId(query))
			replaceCompositeIdValusWithStrings(hqlResults);
	}

	private static boolean isSelectingId(SelectQuery query) {
		List<Object> selections = query.getSelections();
		if (selections == null)
			return false;

		for (Object selection : selections)
			if (isIdSelection(selection))
				return true;

		return false;
	}

	private static boolean isIdSelection(Object s) {
		return s instanceof PropertyOperand && GenericEntity.id.equals(((PropertyOperand) s).getPropertyName());
	}

	private static void replaceCompositeIdValusWithStrings(List<?> hqlResults) {
		ListIterator<Object> rows = (ListIterator<Object>) hqlResults.listIterator();
		while (rows.hasNext()) {
			Object row = rows.next();

			if (row == null)
				continue;

			if (row.getClass().isArray()) {
				Object[] vals = (Object[]) row;
				for (int i = 0; i < vals.length; i++)
					if (vals[i] instanceof CompositeIdValues)
						vals[i] = CompositeIdValues.encodeAsString(vals[i]);
				continue;
			}

			if (row instanceof CompositeIdValues)
				rows.set(CompositeIdValues.encodeAsString(row));
		}
	}

	public static void ensureTypeSignatureSelectedProperly(SelectHqlBuilder hqlBuilder, List<?> hqlResults) {
		if (needsTypeSignatureAdjusting(hqlBuilder))
			adjustTypeSignatures(hqlBuilder, hqlResults);
	}

	private static boolean needsTypeSignatureAdjusting(SelectHqlBuilder hqlBuilder) {
		return !hqlBuilder.entitySignaturePositions.isEmpty();
	}

	private static void adjustTypeSignatures(SelectHqlBuilder hqlBuilder, List<?> hqlResults) {
		ListIterator<Object> rows = (ListIterator<Object>) hqlResults.listIterator();
		while (rows.hasNext()) {
			Object row = rows.next();

			if (row == null)
				continue;

			if (row.getClass().isArray()) {
				Object[] vals = (Object[]) row;
				
				for (int i : hqlBuilder.entitySignaturePositions)
					vals[i] = toEntityTypeSignature(vals[i]);
				continue;
			}

			rows.set(toEntityTypeSignature(row));
		}
	}

	private static String toEntityTypeSignature(Object row) {
		return ((GenericEntity)row).entityType().getTypeSignature();
	}

	public static EntityReference createReference(GenericEntity entity) {
		EntityReference ref = entity.reference();
		
		Object id = ref.getRefId();
		if (id instanceof CompositeIdValues)
			ref.setRefId(((CompositeIdValues)id).encodeAsString());
		
		return ref;
	}
}
