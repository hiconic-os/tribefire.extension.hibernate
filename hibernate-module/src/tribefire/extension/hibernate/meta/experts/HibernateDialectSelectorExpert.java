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
package tribefire.extension.hibernate.meta.experts;

import java.util.Collection;

import com.braintribe.model.access.hibernate.meta.aspects.HibernateDialectAspect;
import com.braintribe.model.accessdeployment.hibernate.HibernateDialect;
import com.braintribe.model.accessdeployment.hibernate.selector.HibernateDialectSelector;
import com.braintribe.model.meta.selector.UseCaseSelector;
import com.braintribe.model.processing.meta.cmd.context.SelectorContext;
import com.braintribe.model.processing.meta.cmd.context.SelectorContextAspect;
import com.braintribe.model.processing.meta.cmd.context.experts.SelectorExpert;
import com.braintribe.model.processing.meta.cmd.tools.MetaDataTools;

/**
 * Expert for the {@link UseCaseSelector}. The selector is active if it's use-case value is contained within the ones currently specified in the
 * context.
 */
public class HibernateDialectSelectorExpert implements SelectorExpert<HibernateDialectSelector> {

	@Override
	public Collection<Class<? extends SelectorContextAspect<?>>> getRelevantAspects(HibernateDialectSelector selector) throws Exception {
		return MetaDataTools.aspects(HibernateDialectAspect.class);
	}

	@Override
	public boolean matches(HibernateDialectSelector selector, SelectorContext context) throws Exception {
		HibernateDialect desiredDialect = selector.getDialect();

		return desiredDialect == null || desiredDialect == context.get(HibernateDialectAspect.class);
	}

}
