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

import java.util.function.Consumer;

import org.hibernate.Session;

import com.braintribe.model.access.ModelAccessException;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.meta.data.QualifiedProperty;
import com.braintribe.model.processing.detachrefs.AbstractAccessBasedReferenceDetacher;
import com.braintribe.model.processing.manipulator.api.ReferenceDetacherException;
import com.braintribe.model.processing.meta.cmd.CmdResolver;
import com.braintribe.model.query.SelectQuery;
import com.braintribe.model.query.SelectQueryResult;

/**
 * 
 */
public class HibernateReferenceDetacher extends AbstractAccessBasedReferenceDetacher<HibernateAccess> {

	private final Session applyManipulationSession;

	public HibernateReferenceDetacher(HibernateAccess access, CmdResolver cmdResolver, Session applyManipulationSession) {
		super(access, cmdResolver);
		this.applyManipulationSession = applyManipulationSession;
	}

	@Override
	protected void executeDetach(SelectQuery query, QualifiedProperty qualifiedProperty, Property property, GenericEntity entityToDetach,
			String detachProblem) throws ReferenceDetacherException {

		class Detacher implements Consumer<SelectQueryResult> {
			boolean hasMore;

			@Override
			public void accept(SelectQueryResult queryResult) throws RuntimeException {
				try {
					checkDetachAllowed(detachProblem, queryResult, qualifiedProperty, entityToDetach);
				} catch (ReferenceDetacherException e) {
					throw new RuntimeException(e);
				}

				removeReferences(queryResult, qualifiedProperty, property, entityToDetach);
				hasMore = queryResult.getHasMore();
			}
		}

		try {
			Detacher detacher = new Detacher();

			do {
				// TODO improve, no need to build the whole query over and over again, when it can be just re-used.
				// Also, we should investigate the flushing here
				access.doFor(query, detacher, applyManipulationSession);
			} while (detacher.hasMore);

		} catch (ModelAccessException e) {
			throw new ReferenceDetacherException("Error while detaching: " + entityToDetach, e);
		}
	}

	@Override
	protected SelectQueryResult executeQuery(SelectQuery query) throws ReferenceDetacherException {
		try {
			return access.query(query);

		} catch (ModelAccessException e) {
			throw new ReferenceDetacherException("Error while querying references.", e);
		}
	}

}
