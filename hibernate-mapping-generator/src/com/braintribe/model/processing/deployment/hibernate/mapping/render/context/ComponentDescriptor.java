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
package com.braintribe.model.processing.deployment.hibernate.mapping.render.context;

import static com.braintribe.utils.lcd.CollectionTools2.newList;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.braintribe.model.accessdeployment.jpa.meta.JpaEmbedded;
import com.braintribe.model.accessdeployment.jpa.meta.JpaPropertyMapping;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.meta.GmProperty;
import com.braintribe.model.processing.deployment.hibernate.mapping.HbmXmlGenerationContext;
import com.braintribe.model.processing.deployment.hibernate.mapping.hints.PropertyHint;
import com.braintribe.model.processing.meta.oracle.EntityTypeOracle;

/**
 * Descriptor for an embedded property.
 * 
 * @author peter.gazdik
 */
public class ComponentDescriptor extends PropertyDescriptor {

	private final JpaEmbedded jpaEmbedded;
	private final List<PropertyDescriptor> embeddedProperties;

	public ComponentDescriptor(HbmXmlGenerationContext context, EntityDescriptor descriptor, GmProperty gmProperty,
			PropertyDescriptorMetaData metaData) {
		super(context, descriptor, gmProperty, metaData);

		this.jpaEmbedded = (JpaEmbedded) metaData.jpaPropertyMapping;
		this.embeddedProperties = createEmbeddedProperties();
	}

	private List<PropertyDescriptor> createEmbeddedProperties() {
		List<PropertyDescriptor> result = newList();
		GmEntityType embeddableType = (GmEntityType) gmProperty.getType();
		EntityTypeOracle embeddableTypeOracle = context.getModelOracle().findEntityTypeOracle(embeddableType);

		Map<String, JpaPropertyMapping> embeddedPropertyMappings = jpaEmbedded.getEmbeddedPropertyMappings();

		for (Entry<String, JpaPropertyMapping> entry : embeddedPropertyMappings.entrySet()) {
			String propertyName = entry.getKey();
			GmProperty gmProperty = embeddableTypeOracle.getProperty(propertyName).asGmProperty();

			PropertyDescriptorMetaData embeddedMetaData = new PropertyDescriptorMetaData();
			embeddedMetaData.jpaPropertyMapping = entry.getValue();

			PropertyDescriptor embeddedPropertyDescriptor = PropertyDescriptor.create(context, gmProperty, entityDescriptor, embeddedMetaData);
			result.add(embeddedPropertyDescriptor);
		}

		return result;
	}

	/** See the remark on the super-implementation. */
	@Override
	protected PropertyHint resolvePropertyHint(GmProperty gmProperty, EntityDescriptor descriptor) {
		return null;
	}

	@Override
	public boolean getIsEmbedded() {
		return true;
	}

	public String getIdPrefix() {
		return gmProperty.getType().getTypeSignature() + "#" + gmProperty.getName() + "#";
	}

	public String getOwnerIdQuotedColumnName() {
		PropertyDescriptor idProperty = entityDescriptor.getIdProperty();
		return Optional.ofNullable(idProperty.getQuotedColumnName()).orElse("id");
	}

	public List<PropertyDescriptor> getEmbeddedProperties() {
		return embeddedProperties;
	}

}
