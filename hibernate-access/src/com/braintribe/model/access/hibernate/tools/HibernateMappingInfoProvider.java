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
package com.braintribe.model.access.hibernate.tools;

import static com.braintribe.utils.lcd.CollectionTools2.newMap;
import static com.braintribe.utils.lcd.CollectionTools2.newSet;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.Metamodel;

import com.braintribe.model.access.hibernate.gm.CompositeIdValues;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmEntityType;

/**
 * @author peter.gazdik
 */
public class HibernateMappingInfoProvider {

	/** Mapped entity type signature -> GM names of its mapped properties (including inherited ones). */
	private final Map<String, Set<String>> mappedPropertiesByEntity = newMap();
	/** Unmapped entity type signature -> GM names of properties mapped in all its mapped sub-types. Computed lazily. */
	private final Map<String, Set<String>> mappedPropertiesByUnmappedEntity = new ConcurrentHashMap<>();
	private final Set<String> compositeIdEntityTypes = newSet();

	public HibernateMappingInfoProvider(EntityManagerFactory emFactory) {
		Metamodel metamodel = emFactory.getMetamodel();

		for (jakarta.persistence.metamodel.EntityType<?> javaxEntityType : metamodel.getEntities())
			index(javaxEntityType);
	}

	private void index(jakarta.persistence.metamodel.EntityType<?> javaxEntityType) {
		String entityName = javaxEntityType.getJavaType().getName();

		if (hasCompositeId(javaxEntityType))
			compositeIdEntityTypes.add(entityName);

		Set<? extends Attribute<?, ?>> attributes = javaxEntityType.getAttributes();
		Set<String> propertyNamesSet = attributes.stream() //
				.map(Attribute::getName) //
				.map(this::ensureGmPropertyName) //
				.collect(Collectors.toSet());

		mappedPropertiesByEntity.put(entityName, propertyNamesSet);
	}

	private boolean hasCompositeId(jakarta.persistence.metamodel.EntityType<?> javaxEntityType) {
		return javaxEntityType.getIdType().getJavaType() == CompositeIdValues.class;
	}

	public boolean isEntityMapped(GmEntityType gmEntityType) {
		return mappedPropertiesByEntity.containsKey(gmEntityType.getTypeSignature());
	}

	public boolean isEntityMapped(EntityType<?> entityType) {
		return mappedPropertiesByEntity.containsKey(entityType.getTypeSignature());
	}

	public boolean isPropertyMapped(String ownerTypeSignature, String propertyName) {
		Set<String> props = mappedPropertiesByEntity.get(ownerTypeSignature);
		return props != null && props.contains(propertyName);
	}

	/**
	 * Checks if given property is mapped for given owner type. The owner is the type through which the property is accessed (e.g. the type of a
	 * query source), not the property's declaring type, because an inherited property might be mapped for one sub-type but not for another.
	 * <p>
	 * If the owner is not mapped itself (e.g. an abstract super-type), the property is considered mapped iff it is mapped in all its mapped sub-types.
	 */
	public boolean isPropertyMapped(EntityType<?> owner, String propertyName) {
		Set<String> props = mappedPropertiesByEntity.get(owner.getTypeSignature());
		if (props == null)
			props = mappedPropertiesByUnmappedEntity.computeIfAbsent(owner.getTypeSignature(), sig -> mappedInAllSubTypes(owner));

		return props.contains(propertyName);
	}

	private Set<String> mappedInAllSubTypes(EntityType<?> owner) {
		Set<String> result = null;

		for (Map.Entry<String, Set<String>> e : mappedPropertiesByEntity.entrySet()) {
			EntityType<?> mappedType = GMF.getTypeReflection().getEntityType(e.getKey());
			if (!owner.isAssignableFrom(mappedType))
				continue;

			if (result == null)
				result = newSet(e.getValue());
			else
				result.retainAll(e.getValue());
		}

		return result == null ? Set.of() : result;
	}

	public boolean hasCompositeId(String typeSignature) {
		return compositeIdEntityTypes.contains(typeSignature);
	}

	/* Reverts ReflectionTools.ensureValidJavaBeansName */
	private String ensureGmPropertyName(String propertyName) {
		if (propertyName.length() > 0 && Character.isUpperCase(propertyName.charAt(0))) {
			return propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
		}
		return propertyName;
	}

}
