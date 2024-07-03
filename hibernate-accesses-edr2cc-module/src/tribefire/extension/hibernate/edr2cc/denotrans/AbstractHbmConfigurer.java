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

import com.braintribe.model.accessdeployment.hibernate.meta.EntityMapping;
import com.braintribe.model.accessdeployment.hibernate.meta.PropertyMapping;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.data.constraint.TypeSpecification;
import com.braintribe.model.meta.data.display.NameConversion;
import com.braintribe.model.meta.data.display.NameConversionStyle;
import com.braintribe.model.processing.meta.editor.ModelMetaDataEditor;

import tribefire.module.api.DenotationTransformationContext;

/**
 * @author peter.gazdik
 */
/* package */ class AbstractHbmConfigurer {

	protected final HibernateAccessEdr2ccEnricher enricher;
	protected final DenotationTransformationContext context;
	protected final ModelMetaDataEditor mdEditor;
	protected final String gidPrefix;

	public AbstractHbmConfigurer(HibernateAccessEdr2ccEnricher enricher, String gidPrefix) {
		this.enricher = enricher;
		this.context = enricher.context;
		this.mdEditor = enricher.mdEditor;
		this.gidPrefix = gidPrefix;
	}

	protected NameConversion screamingCamelCaseConversion() {
		NameConversion result = context.findEntityByGlobalId(NameConversion.NAME_CONVERSION_SCREAMING_SNAKE_CASE_GLOBAL_ID);
		if (result != null)
			return result;

		result = context.create(NameConversion.T);
		result.setGlobalId(NameConversion.NAME_CONVERSION_SCREAMING_SNAKE_CASE_GLOBAL_ID);
		result.setStyle(NameConversionStyle.screamingSnakeCase);
		return result;
	}

	protected TypeSpecification stringTypeSpecification() {
		TypeSpecification result = context.findEntityByGlobalId(TypeSpecification.STRING_TYPE_SPECIFICATION_GLOBAL_ID);
		if (result != null)
			return result;

		result = TypeSpecification.T.create();
		result.setGlobalId(TypeSpecification.STRING_TYPE_SPECIFICATION_GLOBAL_ID);
		result.setType(context.getEntityByGlobalId("type:string"));

		return result;
	}

	protected EntityMapping entityMapping(String tableName, EntityType<?> owner) {
		return entityMapping(tableName, null, owner);
	}

	protected EntityMapping entityMapping(String tableName, String discriminator, EntityType<?> owner) {
		EntityMapping result = context.create(EntityMapping.T);
		result.setGlobalId(entityMappingGlobalId(owner));
		result.setTableName(tableName);
		result.setDiscriminatorColumnName(discriminator);
		return result;
	}

	private String entityMappingGlobalId(EntityType<?> owner) {
		return gidPrefix + owner.getShortName();
	}

	protected PropertyMapping propMapping(String columnName, String gidSuffix) {
		PropertyMapping result = propMapping(gidSuffix);
		result.setColumnName(columnName);

		return result;
	}

	protected PropertyMapping propMapping(String gidSuffix) {
		PropertyMapping result = context.create(PropertyMapping.T);
		result.setGlobalId(gidPrefix + gidSuffix);

		return result;
	}

	protected PropertyMapping index(String indexName, PropertyMapping propMapping) {
		propMapping.setIndex(indexName);
		return propMapping;
	}

}
