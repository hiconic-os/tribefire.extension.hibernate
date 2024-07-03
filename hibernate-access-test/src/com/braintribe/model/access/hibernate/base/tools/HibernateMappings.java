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
package com.braintribe.model.access.hibernate.base.tools;

import static com.braintribe.utils.lcd.CollectionTools2.asList;

import com.braintribe.model.accessdeployment.hibernate.meta.EntityMapping;
import com.braintribe.model.accessdeployment.hibernate.meta.PropertyMapping;
import com.braintribe.model.accessdeployment.jpa.meta.JpaColumn;
import com.braintribe.model.accessdeployment.jpa.meta.JpaCompositeId;
import com.braintribe.model.accessdeployment.jpa.meta.JpaEmbeddable;
import com.braintribe.model.meta.data.EntityTypeMetaData;
import com.braintribe.model.meta.data.MetaData;

/**
 * @author peter.gazdik
 */
public class HibernateMappings {

	public static final PropertyMapping UNMAPPED_P = unmappedProperty();

	public static EntityTypeMetaData embeddable() {
		return JpaEmbeddable.T.create("hbm:entity:embeddable");
	}

	public static EntityMapping unmappedEntity() {
		EntityMapping result = EntityMapping.T.create("hbm:entity:unmapped");
		result.setMapToDb(false);

		return result;
	}

	public static PropertyMapping unmappedProperty() {
		PropertyMapping result = PropertyMapping.T.create("hbm:property:unmapped");
		result.setMapToDb(false);

		return result;
	}

	public static PropertyMapping mappedProperty() {
		return PropertyMapping.T.create("hbm:property:mapped");
	}
	
	public static MetaData compositeIdMapping() {
		String globalId = "hbm:CompositeId#id";

		JpaCompositeId result = JpaCompositeId.T.create(globalId);
		result.setColumns(asList( //
				hbmColumn(globalId, "id_long", "long"), //
				hbmColumn(globalId, "id_string", "string") //
		));
		return result;
	}

	public static JpaColumn hbmColumn(String globalIdPrefix, String name, String type) {
		JpaColumn result = JpaColumn.T.create(globalIdPrefix + ":" + name);
		result.setName(name);
		result.setType(type);

		return result;
	}

	public static MetaData compositeIdMapping_XmlSnippet() {
		PropertyMapping result = PropertyMapping.T.create("hbm:ComposuteIdEntity#id");
		// @formatter:off
		result.setXml(
			"<composite-id name=\"id\" class=\"com.braintribe.model.access.hibernate.gm.CompositeIdValues\">" +
				"<key-property name=\"value0\" column=\"id_long\" type=\"long\" />" +
				"<key-property name=\"value1\" column=\"id_string\" type=\"string\" />" +
			"</composite-id>");
		// @formatter:on
		return result;
	}

	public static PropertyMapping propertyMapping(String globalIdPrefix, String columnName, String type) {
		PropertyMapping result = PropertyMapping.T.create(globalIdPrefix + ":" + columnName);
		result.setColumnName(columnName);
		result.setType(type);

		return result;
	}


}
