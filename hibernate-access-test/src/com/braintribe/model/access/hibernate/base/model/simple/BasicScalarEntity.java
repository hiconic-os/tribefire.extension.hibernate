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
package com.braintribe.model.access.hibernate.base.model.simple;

import java.math.BigDecimal;
import java.util.Date;

import com.braintribe.model.access.hibernate.base.model.HibernateAccessEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface BasicScalarEntity extends HibernateAccessEntity {

	EntityType<BasicScalarEntity> T = EntityTypes.T(BasicScalarEntity.class);

	String stringValue = "stringValue";
	String integerValue = "integerValue";
	String longValue = "longValue";
	String floatValue = "floatValue";
	String doubleValue = "doubleValue";
	String booleanValue = "booleanValue";
	String dateValue = "dateValue";
	String decimalValue = "decimalValue";
	String color = "color";

	String getStringValue();
	void setStringValue(String stringValue);

	Integer getIntegerValue();
	void setIntegerValue(Integer integerValue);

	Long getLongValue();
	void setLongValue(Long longValue);

	Float getFloatValue();
	void setFloatValue(Float floatValue);

	Double getDoubleValue();
	void setDoubleValue(Double doubleValue);

	Boolean getBooleanValue();
	void setBooleanValue(Boolean booleanValue);

	Date getDateValue();
	void setDateValue(Date dateValue);

	BigDecimal getDecimalValue();
	void setDecimalValue(BigDecimal decimalValue);

	BasicColor getColor();
	void setColor(BasicColor color);

}
