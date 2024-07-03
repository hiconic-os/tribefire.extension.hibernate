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
import java.util.List;
import java.util.Set;

import com.braintribe.model.generic.StandardIdentifiable;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;


public interface SimpleCollectionsEntity extends StandardIdentifiable {

	final EntityType<SimpleCollectionsEntity> T = EntityTypes.T(SimpleCollectionsEntity.class);

	String longList = "longList";
	String longSet = "longSet";
	String integerList = "integerList";
	String integerSet = "integerSet";
	String stringList = "stringList";
	String stringSet = "stringSet";
	String dateList = "dateList";
	String dateSet = "dateSet";
	String enumList = "enumList";
	String enumSet = "enumSet";

	List<Long> getLongList();
	void setLongList(List<Long> value);

	Set<Long> getLongSet();
	void setLongSet(Set<Long> value);

	List<Integer> getIntegerList();
	void setIntegerList(List<Integer> value);

	Set<Integer> getIntegerSet();
	void setIntegerSet(Set<Integer> value);

	List<String> getStringList();
	void setStringList(List<String> value);

	Set<String> getStringSet();
	void setStringSet(Set<String> value);

	List<Date> getDateList();
	void setDateList(List<Date> value);

	Set<Date> getDateSet();
	void setDateSet(Set<Date> value);

	List<SimpleEnum> getEnumList();
	void setEnumList(List<SimpleEnum> value);

	Set<SimpleEnum> getEnumSet();
	void setEnumSet(Set<SimpleEnum> value);

}
