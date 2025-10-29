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
package com.braintribe.model.processing.deployment.hibernate.mapping.db;

import static com.braintribe.utils.lcd.CollectionTools2.acquireSet;
import static com.braintribe.utils.lcd.CollectionTools2.newMap;
import static com.braintribe.utils.lcd.CollectionTools2.newSet;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.braintribe.model.processing.deployment.hibernate.mapping.HbmXmlGenerationContext;
import com.braintribe.model.processing.deployment.hibernate.mapping.render.context.CollectionPropertyDescriptor;
import com.braintribe.model.processing.deployment.hibernate.mapping.render.context.EntityDescriptor;
import com.braintribe.model.processing.deployment.hibernate.mapping.render.context.PropertyDescriptor;
import com.braintribe.model.processing.deployment.hibernate.mapping.utils.CamelCaseStringShortener;
import com.braintribe.model.processing.deployment.hibernate.mapping.utils.Suffixer;
import com.braintribe.utils.lcd.StringTools;

public class NamingStrategyProvider {

	private final HbmXmlGenerationContext context;
	private final Collection<EntityDescriptor> entityDescriptors;
	private final NamingLimitations namingLimitations;

	private final Set<String> reservedTableNames = newSet();
	private final Set<String> reservedIndexNames = newSet();
	private final Map<String, Set<String>> reservedColumnNames = newMap();

	public NamingStrategyProvider(HbmXmlGenerationContext context, Collection<EntityDescriptor> entityDescriptors) {
		this.context = context;
		this.entityDescriptors = entityDescriptors;
		this.namingLimitations = NamingLimitations.create(context);
	}

	public Collection<EntityDescriptor> apply() {
		registerNonOverwritableNames();
		provideDataBaseNames();
		return entityDescriptors;
	}

	/**
	 * Registers previously set database names which shouldn't be overwritten
	 */
	private void registerNonOverwritableNames() {
		// registers tb names initially given
		for (EntityDescriptor entityDescriptor : entityDescriptors) {

			if (entityDescriptor.tableName != null) {
				registerTableName(entityDescriptor.tableName);

				for (PropertyDescriptor propertyDescriptor : entityDescriptor.getProperties())
					registerProvidedNames(propertyDescriptor);
			}
		}
	}

	private void registerProvidedNames(PropertyDescriptor propertyDescriptor) {
		if (propertyDescriptor.columnName != null)
			registerColumnName(propertyDescriptor.entityDescriptor.tableName, propertyDescriptor.columnName);

		if (propertyDescriptor.index != null)
			reservedIndexNames.add(propertyDescriptor.index);

		if (propertyDescriptor instanceof CollectionPropertyDescriptor)
			registerProvidedNames((CollectionPropertyDescriptor) propertyDescriptor);
	}

	private void registerProvidedNames(CollectionPropertyDescriptor propertyDescriptor) {
		String collectionTable = propertyDescriptor.getMany2ManyTable();
		if (collectionTable == null)
			return;

		registerTableName(collectionTable);

		registerColumnNameIfAssigned(collectionTable, propertyDescriptor.keyColumn);

		registerColumnNameIfAssigned(collectionTable, propertyDescriptor.elementColumn);

		if (propertyDescriptor.getIsList())
			registerColumnNameIfAssigned(collectionTable, propertyDescriptor.indexColumn);

		if (propertyDescriptor.getIsMap())
			registerColumnNameIfAssigned(collectionTable, propertyDescriptor.mapKeyColumn);
	}

	/**
	 * Fixes table/column names, based on the given HbmXmlGenerationConfig options
	 */
	private void provideDataBaseNames() {
		for (EntityDescriptor entityDescriptor : entityDescriptors) {
			// if (!entityDescriptor.getDbNamesFromMappingMetadata() && !entityDescriptor.getDbNamesFromGeneratorHistory()) {
			provideDataBaseNames(entityDescriptor);
			// }
			for (PropertyDescriptor propertyDescriptor : entityDescriptor.getProperties()) {
				// if (propertyDescriptor.getDbNamesFromMappingMetadata() || propertyDescriptor.getDbNamesFromGeneratorHistory()) continue;
				provideDataBaseNames(propertyDescriptor);
			}
		}
	}

	private void provideDataBaseNames(EntityDescriptor entityDescriptor) {
		// table names are set only if not previously set
		if (entityDescriptor.getTableName() == null) {
			entityDescriptor.setTableName(generateTableName(entityDescriptor.getTableNameBase()));
			registerTableName(entityDescriptor.getTableName());
		}

		// discriminator column names are set only if not previously set
		if (entityDescriptor.getDiscriminatorColumnName() == null) {
			entityDescriptor.setDiscriminatorColumnName(generateColumnName(entityDescriptor.getTableName(), entityDescriptor.getDiscriminatorName()));
			registerColumnName(entityDescriptor.getTableName(), entityDescriptor.getDiscriminatorColumnName());
		}
	}

	private void provideDataBaseNames(PropertyDescriptor propertyDescriptor) {
		if (propertyDescriptor.columnName == null) {
			String tableName = propertyDescriptor.entityDescriptor.tableName;
			propertyDescriptor.setColumnName(generateColumnName(tableName, propertyDescriptor.name));
			registerColumnName(tableName, propertyDescriptor.columnName);
		}

		if (propertyDescriptor.hasIndexMd && propertyDescriptor.index == null) {
			String tableName = propertyDescriptor.entityDescriptor.tableName;
			String columnName = propertyDescriptor.columnName;
			propertyDescriptor.index = generateIndexName(tableName, columnName);
		}

		if (propertyDescriptor instanceof CollectionPropertyDescriptor) {
			provideDataBaseNames((CollectionPropertyDescriptor) propertyDescriptor);
		}
	}

	private void provideDataBaseNames(CollectionPropertyDescriptor propertyDescriptor) {

		// many2ManyTable is only overwritten if null, as this information could have been provided to the CollectionPropertyDescriptor during its
		// creation with a PropertyHint
		String many2ManyTable = propertyDescriptor.getMany2ManyTable();
		if (many2ManyTable == null) {
			many2ManyTable = generateTableName(propertyDescriptor.getCollectionName());
			propertyDescriptor.setMany2ManyTable(many2ManyTable);
		}

		registerTableName(many2ManyTable);

		if (propertyDescriptor.getKeyColumn() == null) {
			propertyDescriptor.setKeyColumn(generateColumnName(many2ManyTable, propertyDescriptor.getCollectionKeyName()));
			registerColumnName(many2ManyTable, propertyDescriptor.getKeyColumn());
		}

		if (propertyDescriptor.getElementColumn() == null) {
			propertyDescriptor.setElementColumn(generateColumnName(many2ManyTable, propertyDescriptor.getCollectionElementName()));
			registerColumnName(many2ManyTable, propertyDescriptor.getElementColumn());
		}

		if (propertyDescriptor.getIsList() && propertyDescriptor.getIndexColumn() == null) {
			propertyDescriptor.setIndexColumn(generateColumnName(many2ManyTable, propertyDescriptor.getCollectionIndexName()));
			registerColumnName(many2ManyTable, propertyDescriptor.getIndexColumn());
		}

		if (propertyDescriptor.getIsMap() && propertyDescriptor.getMapKeyColumn() == null) {
			propertyDescriptor.setMapKeyColumn(generateColumnName(many2ManyTable, propertyDescriptor.getCollectionMapKeyName()));
			registerColumnName(many2ManyTable, propertyDescriptor.getMapKeyColumn());
		}
	}

	private String generateTableName(String coreTableName) {
		coreTableName = removeIllegalTableNameChar(coreTableName);
		if (nonPrefixedTableNameExceedsMaxLength(coreTableName)) {
			coreTableName = shortenTableName(coreTableName);
		}
		return applyConfiguredCase(uniqueTableName(applyTablePrefix(coreTableName)));
	}

	private String generateColumnName(String tableName, String coreColumnName) {
		String columnName = removeIllegalColumnNameChar(coreColumnName);
		if (columnNameExceedsMaxLength(columnName))
			columnName = shortenColumnName(columnName);

		return applyConfiguredCase(uniqueColumnName(tableName, columnName));
	}

	private String generateIndexName(String tableName, String columnName) {
		String indexName = deriveIndexName(tableName, columnName);
		if (indexNameExceedsMaxLength(indexName))
			indexName = generateShortEnoughIndexName(tableName, columnName);

		return applyConfiguredCase(uniqueIndexName(indexName));
	}

	private String generateShortEnoughIndexName(String tableName, String columnName) {
		String indexName;

		String shorterTabName = shorten(tableName, 10);
		indexName = deriveIndexName(shorterTabName, columnName);
		if (!indexNameExceedsMaxLength(indexName))
			return indexName;

		// 12 chars are already used - 10 for shorterTableName and 2 for "Ix" prefix
		String shorterColumnName = shorten(columnName, namingLimitations.getColumnNameMaxLength() - 12);
		indexName = deriveIndexName(shorterTabName, shorterColumnName);
		if (!indexNameExceedsMaxLength(indexName))
			return indexName;

		return shorten(indexName, namingLimitations.getColumnNameMaxLength());
	}

	private String deriveIndexName(String tableName, String columnName) {
		return "Ix" + StringTools.capitalize(columnName) + StringTools.capitalize(tableName);
	}

	private void registerTableName(String tableName) {
		reservedTableNames.add(tableName.toUpperCase());
	}

	private void registerColumnNameIfAssigned(String tableName, String maybeColumnName) {
		if (maybeColumnName != null)
			registerColumnName(tableName, maybeColumnName);
	}

	private void registerColumnName(String tableName, String columnName) {
		acquireSet(reservedColumnNames, tableName.toUpperCase()).add(columnName.toUpperCase());
	}

	private boolean isValidTableName(String tableName) {
		return isTableNameAvailable(tableName) && !namingLimitations.isReservedWord(tableName);
	}

	private boolean isTableNameAvailable(String tableName) {
		return !reservedTableNames.contains(tableName.toUpperCase());
	}

	private boolean isValidColumnName(String tableName, String columnName) {
		return isColumnNameAvailable(tableName, columnName) && !namingLimitations.isReservedWord(columnName);
	}

	private boolean isColumnNameAvailable(String tableName, String columnName) {
		Set<String> columnNames = reservedColumnNames.get(tableName.toUpperCase());
		return columnNames == null || !columnNames.contains(columnName.toUpperCase());
	}

	private boolean isValidIndexName(String indexName) {
		return isIndexNameAvailable(indexName) && !namingLimitations.isReservedWord(indexName);
	}

	private boolean isIndexNameAvailable(String indexName) {
		return !reservedIndexNames.contains(indexName.toUpperCase());
	}

	private static boolean exceedsMaxLength(String string, Integer maxLength) {
		return (string != null && string.length() > maxLength);
	}

	private static String shorten(String camelCaseString, Integer maxLength) {
		return new CamelCaseStringShortener(camelCaseString, maxLength).shorten();
	}

	private String shortenTableName(String camelCaseString) {
		return shorten(camelCaseString, namingLimitations.getTableNameNonPrefixedMaxLength());
	}

	private String shortenColumnName(String camelCaseString) {
		return shorten(camelCaseString, namingLimitations.getColumnNameMaxLength());
	}

	private boolean tableNameExceedsMaxLength(String tableName) {
		return exceedsMaxLength(tableName, namingLimitations.getTableNameMaxLength());
	}

	private boolean nonPrefixedTableNameExceedsMaxLength(String camelCaseString) {
		return exceedsMaxLength(camelCaseString, namingLimitations.getTableNameNonPrefixedMaxLength());
	}

	private boolean columnNameExceedsMaxLength(String columnName) {
		return exceedsMaxLength(columnName, namingLimitations.getColumnNameMaxLength());
	}

	private boolean indexNameExceedsMaxLength(String indexName) {
		// not sure, but let's use the limit for columns also for indices
		return exceedsMaxLength(indexName, namingLimitations.getColumnNameMaxLength());
	}

	private boolean isTablePrefixConfigured() {
		return !(context.tablePrefix == null || context.tablePrefix.isEmpty());
	}

	private String applyTablePrefix(String coreTableName) {
		if (!isTablePrefixConfigured())
			return coreTableName;
		return context.tablePrefix + coreTableName;
	}

	private String applyConfiguredCase(String name) {
		// TODO: provide support for UPPER, lower, camelCase and PascalCase
		if (context.allUppercase)
			return name.toUpperCase();
		return name;
	}

	private String removeIllegalColumnNameChar(String string) {
		// trailing chars
		if (namingLimitations.getColumnNameIllegalLeadingCharsPattern() != null)
			string = string.replaceAll(namingLimitations.getColumnNameIllegalLeadingCharsPattern(), "");

		return string;
	}

	private String removeIllegalTableNameChar(String string) {
		// trailing chars
		if (namingLimitations.getTableNameIllegalLeadingCharsPattern() != null)
			string = string.replaceAll(namingLimitations.getTableNameIllegalLeadingCharsPattern(), "");

		return string;
	}

	private String uniqueTableName(String tableName) {
		int attempt = 0;
		String originalTableName = tableName;
		while (!isValidTableName(tableName)) {
			if (attempt == 0 && tableNameExceedsMaxLength(tableName)) {
				String tableNameOneCharShorter = tableName.substring(0, tableName.length() - 1);
				if (isValidTableName(tableNameOneCharShorter))
					return tableNameOneCharShorter;
			}
			tableName = Suffixer.suffixIt(originalTableName, attempt++, namingLimitations.getTableNameMaxLength());
		}
		return tableName;
	}

	private String uniqueColumnName(String tableName, String columnName) {
		int attempt = 0;
		String originalColumnName = columnName;
		int maxLength = namingLimitations.getColumnNameMaxLength();
		while (!isValidColumnName(tableName, columnName)) {
			columnName = Suffixer.suffixIt(originalColumnName, attempt++, maxLength);
		}
		return columnName;
	}

	private String uniqueIndexName(String indexName) {
		int attempt = 0;
		String originalIndexName = indexName;
		int maxLength = namingLimitations.getColumnNameMaxLength();
		while (!isValidIndexName(indexName))
			indexName = Suffixer.suffixIt(originalIndexName, attempt++, maxLength);

		return indexName;
	}

}
