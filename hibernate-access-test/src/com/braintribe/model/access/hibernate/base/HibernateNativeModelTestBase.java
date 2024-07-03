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
package com.braintribe.model.access.hibernate.base;

import com.braintribe.model.access.hibernate.base.model.n8ive.AmbiguousEntity;
import com.braintribe.model.access.hibernate.base.model.n8ive.Player;
import com.braintribe.model.meta.GmMetaModel;

/**
 * @author peter.gazdik
 */
public abstract class HibernateNativeModelTestBase extends HibernateAccessRecyclingTestBase {

	@Override
	protected GmMetaModel model() {
		return HibernateAccessRecyclingTestBase.hibernateModels.n8ive();
	}

	// #################################################
	// ## . . . . . . . . Data creation . . . . . . . ##
	// #################################################

	protected Player player(String name) {
		return create(Player.T, name);
	}

	protected Player player(String name, String lastName) {
		Player result = create(Player.T, name);
		result.setLastName(lastName);
		return result;
	}

	protected Player player(String name, Player teammate) {
		Player result = create(Player.T, name);
		result.setTeammate(teammate);
		return result;
	}

	protected AmbiguousEntity ambigTop(String name) {
		return create(AmbiguousEntity.T, name);
	}

	protected com.braintribe.model.access.hibernate.base.model.n8ive.sub.AmbiguousEntity ambigSub(String name) {
		return create(com.braintribe.model.access.hibernate.base.model.n8ive.sub.AmbiguousEntity.T, name);
	}

}
