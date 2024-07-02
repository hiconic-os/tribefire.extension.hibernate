// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.model.processing.deployment.hibernate.mapping;

import static com.braintribe.utils.lcd.CollectionTools2.isEmpty;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.braintribe.logging.Logger;
import com.braintribe.model.processing.deployment.hibernate.mapping.db.NamingStrategyProvider;
import com.braintribe.model.processing.deployment.hibernate.mapping.hints.EntityHintPersistence;
import com.braintribe.model.processing.deployment.hibernate.mapping.hints.MetaModelEnricher;
import com.braintribe.model.processing.deployment.hibernate.mapping.render.HbmXmlFastRenderer;
import com.braintribe.model.processing.deployment.hibernate.mapping.render.OrmXmlFastRenderer;
import com.braintribe.model.processing.deployment.hibernate.mapping.render.context.EntityDescriptor;
import com.braintribe.model.processing.deployment.hibernate.mapping.render.context.EntityDescriptorFactory;
import com.braintribe.model.processing.deployment.hibernate.mapping.wrapper.HbmEntityType;
import com.braintribe.model.processing.deployment.hibernate.schema.meta.DbUpdateStatementGenerator;

/**
 * @see HbmXmlGeneratingService
 */
class HbmXmlGenerator {

	private final HbmXmlGenerationContext context;

	private Map<String, HbmEntityType> hbmEntityTypes;
	private Collection<EntityDescriptor> entityDescriptors;

	private static final Logger log = Logger.getLogger(HbmXmlGenerator.class);

	public HbmXmlGenerator(HbmXmlGenerationContext context) {
		this.context = context;
		this.entityDescriptors = Collections.<EntityDescriptor> emptyList();
	}

	public void generate() {
		applyHintedMetaData();

		buildHbmEntitiesStructure();

		generateEntityTypeMappings();

		applyDbNamingStrategy();

		generateDbUpdateStatements();

		renderEntityMappings();

		saveDecisions();
	}

	private void applyHintedMetaData() {
		new MetaModelEnricher(context).enrich();
	}

	private void buildHbmEntitiesStructure() {
		hbmEntityTypes = new HbmEntityTypeMapBuilder(context).generateHbmEntityTypeMap();
	}

	/** @see DbUpdateStatementGenerator */
	private void generateDbUpdateStatements() {
		DbUpdateStatementGenerator.run(context, entityDescriptors);
	}
	/**
	 * Creates {@link EntityDescriptor} for each {@link HbmEntityType} collected to {@link #hbmEntityTypes}.
	 * <p>
	 * Sub types are generated before their super types.
	 */
	private void generateEntityTypeMappings() {
		entityDescriptors = new EntityDescriptorFactory(context).createEntityDescriptors(hbmEntityTypes);
	}

	private void saveDecisions() {
		new EntityHintPersistence(context).persist(entityDescriptors);
	}

	private void applyDbNamingStrategy() {
		entityDescriptors = new NamingStrategyProvider(context, entityDescriptors).apply();
	}

	private void renderEntityMappings() {
		if (isEmpty(entityDescriptors)) {
			log.warn("No descriptors to be rendered.");
			return;
		}
		
		var consumer = context.entityMappingConsumer;
		
		if (consumer == null) {
			consumer = new SourceSerializer(context.outputFolder)::writeSourceFile;	
		}
		
		for (EntityDescriptor ed : entityDescriptors) {
			SourceDescriptor sd = context.generateJpaOrm ? //
					OrmXmlFastRenderer.renderEntityType(ed): //
					HbmXmlFastRenderer.renderEntityType(ed);
			
			consumer.accept(sd);
		}
	}

}
