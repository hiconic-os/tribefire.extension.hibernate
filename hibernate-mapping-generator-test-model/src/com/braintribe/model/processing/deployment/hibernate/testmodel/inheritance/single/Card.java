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
package com.braintribe.model.processing.deployment.hibernate.testmodel.inheritance.single;

import java.util.Date;

import com.braintribe.model.generic.StandardIdentifiable;
import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.annotation.ToStringInformation;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@ToStringInformation("Card branded by ${brand} + issued by ${issuer} international coverage is ${internationalCoverage}")
@Abstract
public interface Card extends StandardIdentifiable {

	EntityType<Card> T = EntityTypes.T(Card.class);

	// private static Long numberSequence = 10000L;

	// @formatter:off
	String getNumber();
	void setNumber(String number);

	CardCompany getBrand();
	void setBrand(CardCompany brand);

	Bank getIssuer();
	void setIssuer(Bank issuer);

	CardInternationalCoverage getInternationalCoverage();
	void setInternationalCoverage(CardInternationalCoverage internationalCoverage);

	Date getDate();
	void setDate(Date date);
	// @formatter:on

}
