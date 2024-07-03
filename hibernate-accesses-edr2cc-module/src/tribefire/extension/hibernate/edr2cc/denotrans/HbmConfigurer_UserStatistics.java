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
package tribefire.extension.hibernate.edr2cc.denotrans;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.user.statistics.UserStatistics;

/**
 * @author peter.gazdik
 */
class HbmConfigurer_UserStatistics extends AbstractHbmConfigurer {

	public static void run(HibernateAccessEdr2ccEnricher enricher) {
		new HbmConfigurer_UserStatistics(enricher).run();
	}

	private HbmConfigurer_UserStatistics(HibernateAccessEdr2ccEnricher enricher) {
		super(enricher, "hbm:edr2cc/user-sessions/");
	}

	private void run() {
		mdEditor.onEntityType(UserStatistics.T) //
				.addPropertyMetaData(screamingCamelCaseConversion()) //
				.addMetaData(entityMapping("TF_SS_USER_STATISTICS", UserStatistics.T)) //
				.addPropertyMetaData(GenericEntity.id, stringTypeSpecification());
	}

}
