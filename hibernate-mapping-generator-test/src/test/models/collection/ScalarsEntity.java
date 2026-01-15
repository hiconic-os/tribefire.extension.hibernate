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
package test.models.collection;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface ScalarsEntity extends GenericEntity {

	EntityType<ScalarsEntity> T = EntityTypes.T(ScalarsEntity.class);

	ScalarEnum getEnumV();
	void setEnumV(ScalarEnum enumValue);

	BigDecimal getDecimalV();
	void setDecimalV(BigDecimal decimal);

	Date getDateV();
	void setDateV(Date date);

	Long getLongV();
	void setLongV(Long longValue);

	Integer getIntV();
	void setIntV(Integer intValue);

	Double getDoubleV();
	void setDoubleV(Double doubleValue);

	Float getFloatV();
	void setFloatV(Float floatValue);

	Boolean getBooleanV();
	void setBooleanV(Boolean booleanValue);

	String getStringV();
	void setStringV(String stringValue);

	//////////////////////////////////////////////////////////////

	Set<ScalarEnum> getEnumSet();
	void setEnumSet(Set<ScalarEnum> enumSet);

	Set<BigDecimal> getDecimalSet();
	void setDecimalSet(Set<BigDecimal> decimalSet);

	Set<Date> getDateSet();
	void setDateSet(Set<Date> dateSet);

	Set<Long> getLongSet();
	void setLongSet(Set<Long> longSet);

	Set<Integer> getIntSet();
	void setIntSet(Set<Integer> intSet);

	Set<Double> getDoubleSet();
	void setDoubleSet(Set<Double> doubleSet);

	Set<Float> getFloatSet();
	void setFloatSet(Set<Float> floatSet);

	Set<Boolean> getBooleanSet();
	void setBooleanSet(Set<Boolean> booleanSet);

	Set<String> getStringSet();
	void setStringSet(Set<String> stringSet);

	//////////////////////////////////////////////////////////////

	Map<Date, String> getDateKeyMap();
	void setDateKeyMap(Map<Date, String> dateKeyMap);

	Map<BigDecimal, String> getDecimalKeyMap();
	void setDecimalKeyMap(Map<BigDecimal, String> decimalKeyMap);

	Map<ScalarEnum, String> getEnumKeyMap();
	void setEnumKeyMap(Map<ScalarEnum, String> enumKeyMap);

	//////////////////////////////////////////////////////////////

	Map<String, Date> getDateValueMap();
	void setDateValueMap(Map<String, Date> dateValueMap);

	Map<String, BigDecimal> getDecimalValueMap();
	void setDecimalValueMap(Map<String, BigDecimal> decimalValueMap);

	Map<String, ScalarEnum> getEnumValueMap();
	void setEnumValueMap(Map<String, ScalarEnum> enumValueMap);

}
