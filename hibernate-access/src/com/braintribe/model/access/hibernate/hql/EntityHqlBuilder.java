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
package com.braintribe.model.access.hibernate.hql;

import org.hibernate.query.Query;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.model.query.From;
import com.braintribe.model.query.functions.Localize;

/**
 * This is only used when looking for referenced entities on delete.
 * 
 * @author peter.gazdik
 */
public class EntityHqlBuilder extends HqlBuilder<EntityQuery> {

	public EntityHqlBuilder(EntityQuery query) {
		super(query);
	}

	@Override
	public Query<GenericEntity> encode() {
		context.setReturnedType(typeReflection.<EntityType<?>> getType(query.getEntityTypeSignature()));

		// build select clause
		builder.append("select ");
		builder.append(context.aquireAlias(null));
		builder.append(' ');

		// build from and join clause
		encodeFroms();

		encodeCondition();

		encodeOrdering();

		return finishQuery();
	}

	private void encodeFroms() {
		From mainFrom = From.T.create();
		mainFrom.setEntityTypeSignature(query.getEntityTypeSignature());

		builder.append("from ");

		String alias = context.aquireAlias(null);
		encodeFrom(mainFrom, alias);
	}

	@Override
	protected void encodeLocalize(Localize localize) {
		String locale = localize.getLocale();
		if (locale == null)
			locale = context.getLocale();

		String escapedLocale = escapeStringLiteralBody(locale);

		// Useless. If localiedValues[locale] doesn't exist then the default won't work either, as we have already done a cross-join due to locale
		// We don't care though, this method should not be reached as EntityQuery is only used when deleting, so no localized strings there
		builder.append("case when '");
		builder.append(escapedLocale);
		builder.append("' in indices(");
		encodeOperand(localize.getLocalizedStringOperand(), false, false);
		builder.append(".localizedValues) then ");
		encodeOperand(localize.getLocalizedStringOperand(), false, false);
		builder.append(".localizedValues['");
		builder.append(escapedLocale);
		builder.append("'] else ");
		encodeOperand(localize.getLocalizedStringOperand(), false, false);
		builder.append(".localizedValues['default'] end");
	}

}