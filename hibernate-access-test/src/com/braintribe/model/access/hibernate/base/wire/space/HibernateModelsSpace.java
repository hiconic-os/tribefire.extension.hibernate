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
package com.braintribe.model.access.hibernate.base.wire.space;

import static com.braintribe.model.access.hibernate.base.tools.HibernateMappings.UNMAPPED_P;
import static com.braintribe.utils.lcd.CollectionTools2.asList;
import static com.braintribe.utils.lcd.CollectionTools2.concat;
import static com.braintribe.utils.lcd.CollectionTools2.first;

import java.util.List;
import java.util.function.Consumer;

import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.model.acl.AclHaTestEntity;
import com.braintribe.model.access.hibernate.base.model.collection.ScalarsEntity;
import com.braintribe.model.access.hibernate.base.model.index.IndexedEntity;
import com.braintribe.model.access.hibernate.base.model.index.ReferencedEntity;
import com.braintribe.model.access.hibernate.base.model.n8ive.AmbiguousEntity;
import com.braintribe.model.access.hibernate.base.model.n8ive.Player;
import com.braintribe.model.access.hibernate.base.model.optimistic.VersionedEntity;
import com.braintribe.model.access.hibernate.base.model.simple.BasicCollectionEntity;
import com.braintribe.model.access.hibernate.base.model.simple.BasicEntity;
import com.braintribe.model.access.hibernate.base.model.simple.BasicScalarEntity;
import com.braintribe.model.access.hibernate.base.model.simple.GraphNodeEntity;
import com.braintribe.model.access.hibernate.base.model.simple.HierarchyBase;
import com.braintribe.model.access.hibernate.base.model.simple.HierarchySubA;
import com.braintribe.model.access.hibernate.base.model.simple.HierarchySubB;
import com.braintribe.model.access.hibernate.base.model.simple.StringIdEntity;
import com.braintribe.model.access.hibernate.base.model.simple.StringIdWithCascadingEntity;
import com.braintribe.model.access.hibernate.base.wire.contract.HibernateModelsContract;
import com.braintribe.model.accessdeployment.hibernate.meta.PropertyMapping;
import com.braintribe.model.accessdeployment.jpa.meta.JpaColumn;
import com.braintribe.model.accessdeployment.jpa.meta.JpaCompositeId;
import com.braintribe.model.acl.Acl;
import com.braintribe.model.descriptive.HasName;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.StandardIdentifiable;
import com.braintribe.model.generic.StandardIntegerIdentifiable;
import com.braintribe.model.generic.StandardStringIdentifiable;
import com.braintribe.model.generic.i18n.LocalizedString;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.GmReflectionTools;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.GmType;
import com.braintribe.model.meta.data.MetaData;
import com.braintribe.model.meta.data.constraint.TypeSpecification;
import com.braintribe.model.meta.data.query.Index;
import com.braintribe.model.meta.data.query.Version;
import com.braintribe.model.processing.meta.editor.BasicModelMetaDataEditor;
import com.braintribe.model.processing.meta.editor.ModelMetaDataEditor;
import com.braintribe.model.processing.meta.oracle.BasicModelOracle;
import com.braintribe.model.processing.meta.oracle.ModelOracle;
import com.braintribe.model.util.meta.NewMetaModelGeneration;
import com.braintribe.utils.lcd.ReflectionTools;
import com.braintribe.wire.api.annotation.Managed;

/**
 * @author peter.gazdik
 */
@Managed
public class HibernateModelsSpace implements HibernateModelsContract {

	public static final String NON_CP_ENTITY_SIG = "synthetic.NonCpEntity";

	// @formatter:off
	private static final List<EntityType<?>> simpleTypes = asList( //
			BasicEntity.T, //
			BasicScalarEntity.T, //
			BasicCollectionEntity.T, //
			StringIdEntity.T, //
			StringIdWithCascadingEntity.T, //

			HierarchySubA.T, //
			HierarchySubB.T, //
			HierarchyBase.T //
	);

	private static final List<EntityType<?>> ALL_TYPES = concat( //
			simpleTypes //
	);

	private static final List<EntityType<?>> nativeTypes = asList( //
			Player.T, //
			
			AmbiguousEntity.T, //
			com.braintribe.model.access.hibernate.base.model.n8ive.sub.AmbiguousEntity.T //
	);

	private static final List<EntityType<?>> graphTypes = asList( //
			GraphNodeEntity.T //
	);

	private static final List<EntityType<?>> aclTypes = asList( //
			Acl.T,
			AclHaTestEntity.T //
			
	);

	private static final List<EntityType<?>> indexedTypes = asList( //
			IndexedEntity.T, //
			ReferencedEntity.T //
	);

	private static final List<EntityType<?>> scalarCollectionTypes = asList( //
			ScalarsEntity.T //
	);

	private static final List<EntityType<?>> versionedTypes = asList( //
			VersionedEntity.T //
	);
	// @formatter:on

	// ###################################################
	// ## . . . . . . . . Mapped models . . . . . . . . ##
	// ###################################################

	@Override
	@Managed
	public GmMetaModel basic_NoPartition() {
		GmMetaModel result = allRaw("basic");

		BasicModelMetaDataEditor md = new BasicModelMetaDataEditor(result);
		unmapGlobalIdAndPartition(md);

		md.onEntityType(StringIdWithCascadingEntity.T) //
				.addPropertyMetaData(StringIdWithCascadingEntity.others, cascadeAll());

		return result;
	}

	private PropertyMapping cascadeAll() {
		PropertyMapping result = PropertyMapping.T.create();
		result.setCascade("all");

		return result;
	}

	@Override
	@Managed
	public GmMetaModel compositeId() {
		GmMetaModel result = allRaw("compositeId");

		BasicModelMetaDataEditor md = new BasicModelMetaDataEditor(result);
		unmapGlobalIdAndPartition(md);

		md.onEntityType(BasicEntity.T) //
				.addPropertyMetaData(GenericEntity.id, compositeIdMapping());

		return result;
	}

	private static MetaData compositeIdMapping() {
		String globalId = "hbm:CompositeId#id";

		JpaCompositeId result = JpaCompositeId.T.create(globalId);
		result.setColumns(asList( //
				hbmColumn(globalId, "integerValue", "integer"), //
				hbmColumn(globalId, "stringValue", "string") //
		));

		return result;
	}

	private static JpaColumn hbmColumn(String globalIdPrefix, String name, String type) {
		JpaColumn result = JpaColumn.T.create(globalIdPrefix + ":" + name);
		result.setName(name);
		result.setType(type);

		return result;
	}

	@Override
	@Managed
	public GmMetaModel n8ive() {
		return unmapGlobalIdAndPartition(nativeRaw());
	}

	@Override
	@Managed
	public GmMetaModel graph() {
		return unmapGlobalIdAndPartition(graphRaw());
	}

	@Override
	@Managed
	public GmMetaModel acl() {
		return unmapGlobalIdAndPartition(aclRaw());
	}

	@Override
	@Managed
	public GmMetaModel nonClasspath() {
		GmMetaModel result = new NewMetaModelGeneration().buildMetaModel("hibernate-test:all-nonCp-model", asList(HasName.T));

		GmEntityType hasName = first(result.getTypes());
		hasName.setTypeSignature(NON_CP_ENTITY_SIG);
		hasName.setIsAbstract(false);

		result.deploy();

		return result;
	}

	@Override
	public GmMetaModel indexed() {
		GmMetaModel result = indexedRaw();

		BasicModelMetaDataEditor md = new BasicModelMetaDataEditor(result);
		unmapGlobalIdAndPartition(md);

		Index index = Index.T.create();

		md.onEntityType(IndexedEntity.T) //
				.addPropertyMetaData("str", index) //
				.addPropertyMetaData("entity", index) //
				.addPropertyMetaData("strSet", index) //
				.addPropertyMetaData("strList", index) //
				.addPropertyMetaData("strStrMap", index) //
		;

		return result;
	}

	@Override
	@Managed
	public GmMetaModel scalarCollections() {
		return unmapGlobalIdAndPartition(scalarCollectionRaw());
	}

	@Override
	@Managed
	public GmMetaModel scalarCollections_v1() {
		return versionCopy(scalarCollections(), 1);
	}

	@Override
	public GmMetaModel versioned() {
		GmMetaModel result = verisionedRaw();

		BasicModelMetaDataEditor md = new BasicModelMetaDataEditor(result);
		unmapGlobalIdAndPartition(md);

		md.onEntityType(VersionedEntity.T) //
				.addPropertyMetaData(VersionedEntity.version, Version.T.create()) //
		// .addPropertyMetaData(VersionedEntity.version, UNMAPPED_P) //
		;
		return result;
	}

	// ###################################################
	// ## . . . . . . . Mapping helpers . . . . . . . . ##
	// ###################################################

	private GmMetaModel unmapGlobalIdAndPartition(GmMetaModel model) {
		BasicModelMetaDataEditor md = new BasicModelMetaDataEditor(model);
		unmapGlobalIdAndPartition(md);

		return model;
	}

	private void unmapGlobalIdAndPartition(BasicModelMetaDataEditor md) {
		md.onEntityType(GenericEntity.T) //
				.addPropertyMetaData(GenericEntity.globalId, UNMAPPED_P) //
				.addPropertyMetaData(GenericEntity.partition, UNMAPPED_P) //
		;
	}

	// ###################################################
	// ## . . . . . . . . . Raw models . . . . . . . . .##
	// ###################################################

	/** Pretty much raw, but LocalizedString values are configured with length 1000 */
	private GmMetaModel allRaw(String name) {
		GmMetaModel i18nModel = LocalizedString.T.getModel().getMetaModel();

		GmMetaModel result = new NewMetaModelGeneration().buildMetaModel("hibernate-test:all-" + name + "-model", ALL_TYPES, asList(i18nModel));
		addMd(result, this::addLocalizedStringMd);

		return result;
	}

	// @formatter:off
	private GmMetaModel nativeRaw() { return rawModel("native", nativeTypes); }
	private GmMetaModel graphRaw() { return rawModel("graph", graphTypes); }
	private GmMetaModel indexedRaw() { return rawModel("indexed", indexedTypes); }
	private GmMetaModel scalarCollectionRaw() { return rawModel("scalar-collection", scalarCollectionTypes); }
	private GmMetaModel verisionedRaw() { return rawModel("versioned", versionedTypes); }
	private GmMetaModel aclRaw() { return rawModel("acl", aclTypes); }
	// @formatter:on

	private GmMetaModel rawModel(String what, List<EntityType<?>> types) {
		GmMetaModel result = new NewMetaModelGeneration().buildMetaModel("hibernate-test:" + what + "-model", types);
		addMd(result, null);

		return result;
	}

	private void addMd(GmMetaModel model, Consumer<ModelMetaDataEditor> customMdConfigurer) {
		ModelOracle modelOracle = new BasicModelOracle(model);

		ModelMetaDataEditor md = new BasicModelMetaDataEditor(model);
		md.onEntityType(StandardIdentifiable.T) //
				.addPropertyMetaData(GenericEntity.id, typeSpecification(modelOracle.getGmLongType()));
		md.onEntityType(StandardStringIdentifiable.T) //
				.addPropertyMetaData(GenericEntity.id, typeSpecification(modelOracle.getGmStringType()));
		md.onEntityType(StandardIntegerIdentifiable.T) //
				.addPropertyMetaData(GenericEntity.id, typeSpecification(modelOracle.getGmIntegerType()));

		if (customMdConfigurer != null)
			customMdConfigurer.accept(md);
	}

	private void addLocalizedStringMd(ModelMetaDataEditor md) {
		md.onEntityType(LocalizedString.T) //
				.addPropertyMetaData(LocalizedString.localizedValues, length(1000L));
	}

	private PropertyMapping length(Long length) {
		PropertyMapping result = PropertyMapping.T.create();
		result.setLength(length);
		return result;
	}

	@Managed
	private TypeSpecification typeSpecification(GmType gmType) {
		TypeSpecification result = TypeSpecification.T.create();
		result.setType(gmType);

		return result;
	}

	// ###################################################
	// ## . . . . . . . . Other Helpers . . . . . . . . ##
	// ###################################################

	/**
	 * Creates a wrapper for given model with a given name, because DB name is derived from a model name, and we need a new DB with new mapping if we
	 * use a different mapping version.
	 * 
	 * @see HibernateAccessRecyclingTestBase#dataSource
	 */
	private GmMetaModel versionCopy(GmMetaModel baseModel, int version) {
		GmMetaModel result = GmReflectionTools.makeShallowCopy(baseModel);
		result.setName(result.getName().replace("-model", "-v" + version + "-model"));

		return result;
	}

}
