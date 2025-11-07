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
package com.braintribe.model.processing.deployment.hibernate.mapping;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.braintribe.model.accessdeployment.hibernate.HibernateDialect;
import com.braintribe.model.accessdeployment.hibernate.meta.ModelMapping;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.data.MetaData;
import com.braintribe.model.processing.deployment.hibernate.mapping.exception.HbmXmlGeneratingServiceException;
import com.braintribe.model.processing.deployment.hibernate.mapping.index.IndexDescriptor;
import com.braintribe.model.processing.meta.cmd.CmdResolver;
import com.braintribe.model.processing.meta.cmd.CmdResolverBuilder;

/**
 * Not really a service, but this is the public API for Hibernate mapping generation.
 * <p>
 * Random notes:
 * <ul>
 * <li>All {@link MetaData} are resolved with "jpa" use-case.
 * </ul>
 */
public class HbmXmlGeneratingService {

	public static final String INDICES_JSON_FILE_NAME = "indices.json";

	private final HbmXmlGenerationContext context = new HbmXmlGenerationContext();

	public HbmXmlGeneratingService() {
	}

	public HbmXmlGeneratingService(GmMetaModel gmMetaModel, File outputFolder) throws HbmXmlGeneratingServiceException {
		this(gmMetaModel, outputFolder, "");
	}

	public HbmXmlGeneratingService(GmMetaModel gmMetaModel, File outputFolder, String tablePrefix) throws HbmXmlGeneratingServiceException {
		this(gmMetaModel, outputFolder, tablePrefix, false);
	}

	public HbmXmlGeneratingService(GmMetaModel gmMetaModel, File outputFolder, String tablePrefix, boolean allUppercase)
			throws HbmXmlGeneratingServiceException {
		this(gmMetaModel, outputFolder, tablePrefix, allUppercase, "");
	}

	/**
	 * <p>
	 * Creates a new {@link HbmXmlGeneratingService} instance
	 * 
	 * @throws HbmXmlGeneratingServiceException
	 *             If the service fails to be constructed
	 */
	public HbmXmlGeneratingService(GmMetaModel gmMetaModel, File outputFolder, String tablePrefix, boolean allUppercase, String targetDb)
			throws HbmXmlGeneratingServiceException {
		setGmMetaModel(gmMetaModel);
		setOutputFolder(outputFolder);
		setTablePrefix(tablePrefix);
		setAllUppercase(allUppercase);
		setTargetDb(targetDb);
	}

	public void setGmMetaModel(GmMetaModel model) {
		context.setGmMetaModel(model);
	}

	/** @deprecated use {@link #setGmMetaModel(GmMetaModel)} No reason for this class to support xml file parsing */
	@SuppressWarnings("unused")
	@Deprecated
	public void setGmMetaModel(File gmMetaModelFile) {
		throw new UnsupportedOperationException("");
	}

	public void setGmMetaModel(String gmMetaModelFilePath) {
		setGmMetaModel(createFile(gmMetaModelFilePath));
	}

	public void setOutputFolder(File outputFolder) {
		context.outputFolder = outputFolder;
	}

	public void setOutputFolder(String outputFolderPath) throws HbmXmlGeneratingServiceException {
		setOutputFolder(createFile(outputFolderPath));
	}

	public void setTablePrefix(String prefix) {
		context.tablePrefix = prefix;
	}

	public HbmXmlGeneratingService tablePrefix(String prefix) {
		context.tablePrefix = prefix;
		return this;
	}

	public void setForeignKeyNamePrefix(String prefix) {
		context.foreignKeyNamePrefix = prefix;
	}

	public HbmXmlGeneratingService foreignKeyNamePrefix(String prefix) {
		context.foreignKeyNamePrefix = prefix;
		return this;
	}

	public void setUniqueKeyNamePrefix(String prefix) {
		context.uniqueKeyNamePrefix = prefix;
	}

	public HbmXmlGeneratingService uniqueKeyNamePrefix(String prefix) {
		context.uniqueKeyNamePrefix = prefix;
		return this;
	}

	public void setIndexNamePrefix(String prefix) {
		context.indexNamePrefix = prefix;
	}

	public HbmXmlGeneratingService indexNamePrefix(String prefix) {
		context.indexNamePrefix = prefix;
		return this;
	}

	public void setAllUppercase(boolean allUppercase) {
		context.allUppercase = allUppercase;
	}

	public void setTargetDb(String targetDb) {
		context.targetDb = targetDb;
	}

	//
	// PGA: Seems these type hints are only used in this artifact and tests, not in the hibernate-access-module
	//

	public void setTypeHints(String typeHints) {
		context.typeHints = typeHints;
	}

	public void setTypeHintsFile(String typeHintsFile) {
		if (typeHintsFile != null && !typeHintsFile.trim().isEmpty())
			setTypeHintsFile(createFile(typeHintsFile));
	}

	public void setTypeHintsFile(File typeHintsFile) {
		context.typeHintsFile = typeHintsFile;
	}

	public void setTypeHintsOutputFile(String typeHintsOutputFile) {
		if (typeHintsOutputFile != null && !typeHintsOutputFile.trim().isEmpty())
			setTypeHintsOutputFile(createFile(typeHintsOutputFile));
	}

	public void setTypeHintsOutputFile(File typeHintsOutputFile) {
		context.typeHintsOutputFile = typeHintsOutputFile;
	}

	public HbmXmlGeneratingService defaultSchema(String defaultSchema) {
		context.defaultSchema = defaultSchema;
		return this;
	}

	public HbmXmlGeneratingService defaultCatalog(String defaultCatalog) {
		context.defaultCatalog = defaultCatalog;
		return this;
	}

	public HbmXmlGeneratingService cmdResolver(CmdResolver cmdResolver) {
		context.setCmdResolver(cmdResolver);
		return this;
	}

	public HbmXmlGeneratingService generateJpaOrm() {
		context.generateJpaOrm = true;
		return this;
	}

	/**
	 * Configures the {@link CmdResolver} and also its the GmMetaModel via {@link #setGmMetaModel(GmMetaModel)}
	 */
	public HbmXmlGeneratingService cmdResolverAndModel(CmdResolver cmdResolver) {
		context.setCmdResolver(cmdResolver);
		context.setGmMetaModel(cmdResolver.getModelOracle().getGmMetaModel());
		return this;
	}

	public HbmXmlGeneratingService cmdResolverFactory(Function<GmMetaModel, CmdResolverBuilder> cmdResolverFactory) {
		context.setCmdResolverFactory(cmdResolverFactory);
		return this;
	}

	public HbmXmlGeneratingService dialect(HibernateDialect dialect) {
		context.setDialect(dialect);
		return this;
	}

	public HbmXmlGeneratingService entityMappingConsumer(Consumer<SourceDescriptor> consumer) {
		context.entityMappingConsumer = consumer;
		return this;
	}

	/**
	 * <p>
	 * Generates hibernate xml mappings for the provided GmMetaModel.
	 * <p>
	 * XML files are generated in the provided output folder.
	 * 
	 * @throws HbmXmlGeneratingServiceException
	 *             If the mappings generation fails
	 */
	public void renderMappings() {
		try {
			new HbmXmlGenerator(context).generate();
		} catch (Exception e) {
			throw new HbmXmlGeneratingServiceException("Failed to generate hibernate mappings", e);
		}
	}

	/* This method is here since the generator builds a CmdResolver internally and I wanted to use that. This whole thing could be cleaned up a bit
	 * though. */
	public boolean keepHbmXmlDir() {
		ModelMapping mm = context.getMetaData().meta(ModelMapping.T).exclusive();
		return mm != null && mm.getForbidDeletingOfMappingsDir();
	}

	private static File createFile(String sourceFilePath) throws HbmXmlGeneratingServiceException {
		return new File(sourceFilePath);
	}

	public static List<IndexDescriptor> readIndexDescriptors(File indicesJson) {
		return HbmXmlGenerator.readIndexDescriptors(indicesJson);
	}

}
