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
import java.util.Map;

import com.braintribe.model.generic.StandardIdentifiable;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;


public interface SimpleMapsEntity extends StandardIdentifiable {

	final EntityType<SimpleMapsEntity> T = EntityTypes.T(SimpleMapsEntity.class);

	Map<Long, Long> getLongToLongMap();
	void setLongToLongMap(Map<Long, Long> value);

	Map<Long, String> getLongToStringMap();
	void setLongToStringMap(Map<Long, String> value);

	Map<Long, Date> getLongToDateMap();
	void setLongToDateMap(Map<Long, Date> value);

	Map<Long, SimpleEnum> getLongToEnumMap();
	void setLongToEnumMap(Map<Long, SimpleEnum> value);

	Map<String, String> getStringToStringMap();
	void setStringToStringMap(Map<String, String> value);

	Map<String, Long> getStringToLongMap();
	void setStringToLongMap(Map<String, Long> value);

	Map<String, Date> getStringToDateMap();
	void setStringToDateMap(Map<String, Date> value);

	Map<String, SimpleEnum> getStringToEnumMap();
	void setStringToEnumMap(Map<String, SimpleEnum> value);

	Map<SimpleEnum, String> getEnumToStringMap();
	void setEnumToStringMap(Map<SimpleEnum, String> value);

	Map<SimpleEnum, Long> getEnumToLongMap();
	void setEnumToLongMap(Map<SimpleEnum, Long> value);

	Map<SimpleEnum, Date> getEnumToDateMap();
	void setEnumToDateMap(Map<SimpleEnum, Date> value);

	Map<SimpleEnum, SimpleEnum> getEnumToEnumMap();
	void setEnumToEnumMap(Map<SimpleEnum, SimpleEnum> value);

	Map<Date, Long> getDateToLongMap();
	void setDateToLongMap(Map<Date, Long> value);

	Map<Date, String> getDateToStringMap();
	void setDateToStringMap(Map<Date, String> value);

	Map<Date, Date> getDateToDateMap();
	void setDateToDateMap(Map<Date, Date> value);

	Map<Date, SimpleEnum> getDateToEnumMap();
	void setDateToEnumMap(Map<Date, SimpleEnum> value);

}
