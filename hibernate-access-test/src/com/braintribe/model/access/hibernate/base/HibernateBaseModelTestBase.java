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

import com.braintribe.model.access.hibernate.base.model.simple.BasicEntity;
import com.braintribe.model.access.hibernate.base.model.simple.BasicScalarEntity;
import com.braintribe.model.access.hibernate.base.model.simple.HierarchySubA;
import com.braintribe.model.access.hibernate.base.model.simple.HierarchySubB;
import com.braintribe.model.meta.GmMetaModel;

/**
 * @author peter.gazdik
 */
public abstract class HibernateBaseModelTestBase extends HibernateAccessRecyclingTestBase {

	@Override
	protected GmMetaModel model() {
		return HibernateAccessRecyclingTestBase.hibernateModels.basic_NoPartition();
	}

	// #################################################
	// ## . . . . . . . . Data creation . . . . . . . ##
	// #################################################

	protected BasicScalarEntity createBse(String name) {
		return create(BasicScalarEntity.T, name);
	}

	protected BasicEntity createBe(String name) {
		return create(BasicEntity.T, name);
	}

	protected HierarchySubA createSubA(String name) {
		return create(HierarchySubA.T, name);
	}

	protected HierarchySubB createSubB(String name) {
		return create(HierarchySubB.T, name);
	}

}
