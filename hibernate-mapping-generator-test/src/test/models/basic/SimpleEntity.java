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
package test.models.basic;

import java.util.Date;

import com.braintribe.model.generic.StandardIdentifiable;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;


public interface SimpleEntity extends StandardIdentifiable {

	final EntityType<SimpleEntity> T = EntityTypes.T(SimpleEntity.class);

	String longProperty = "longProperty";
	String integerProperty = "integerProperty";
	String booleanProperty = "booleanProperty";
	String stringProperty = "stringProperty";
	String dateProperty = "dateProperty";
	String enumProperty = "enumProperty";

	Long getLongProperty();
	void setLongProperty(Long value);

	Integer getIntegerProperty();
	void setIntegerProperty(Integer value);

	Boolean getBooleanProperty();
	void setBooleanProperty(Boolean value);

	String getStringProperty();
	void setStringProperty(String value);

	Date getDateProperty();
	void setDateProperty(Date value);

	SimpleEnum getEnumProperty();
	void setEnumProperty(SimpleEnum value);

}
