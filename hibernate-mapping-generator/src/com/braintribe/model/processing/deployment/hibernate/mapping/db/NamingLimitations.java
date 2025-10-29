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

import java.util.HashSet;
import java.util.Set;

import com.braintribe.model.processing.deployment.hibernate.mapping.HbmXmlGenerationContext;

public abstract class NamingLimitations {
	
	HbmXmlGenerationContext context;
	private final Set<String> reservedWords = new HashSet<>();
	private int tableNameMaxLength;
	private int tableNameNonPrefixedMaxLength;
	private int columnNameMaxLength;
	private String tableNameIllegalLeadingCharsPattern;
	private String columnNameIllegalLeadingCharsPattern;
	
	NamingLimitations(HbmXmlGenerationContext context) { 
		super();
		this.context = context;
	}
	
	public static NamingLimitations create(HbmXmlGenerationContext context) {

		if (context.targetDb == null || context.targetDb.trim().isEmpty())
			return new VendorNeutralNamingLimitations(context);
		
		String vendorPrefix = context.targetDb.toLowerCase().replaceAll("\\s", "");

		if (vendorPrefix.startsWith("db2"))
			return new Db2NamingLimitations(context);
			
		if (vendorPrefix.startsWith("derby"))
			return new DerbyNamingLimitations(context);
			
		if (vendorPrefix.startsWith("mssql"))
			return new MsSqlNamingLimitations(context);
			
		if (vendorPrefix.startsWith("mysql"))
			return new MySqlNamingLimitations(context);
			
		if (vendorPrefix.startsWith("oracle"))
			return new OracleNamingLimitations(context);
			
		if (vendorPrefix.startsWith("postgre"))
			return new PostgreSqlNamingLimitations(context);
			
		if (vendorPrefix.startsWith("sap"))
			return new SapDbNamingLimitations(context);
			
		if (vendorPrefix.startsWith("sybase"))
			return new SybaseNamingLimitations(context);

		return new VendorNeutralNamingLimitations(context);
	}
	
	public int getTableNameMaxLength() {
		return tableNameMaxLength;
	}
	
	void setTableNameMaxLength(int tableNameMaxLength) {
		this.tableNameMaxLength = tableNameMaxLength;
		this.tableNameNonPrefixedMaxLength = tableNameMaxLength - (context.tablePrefix == null ? 0 : context.tablePrefix.length());
	}

	public int getTableNameNonPrefixedMaxLength() {
		return tableNameNonPrefixedMaxLength;
	}

	public int getColumnNameMaxLength() {
		return columnNameMaxLength;
	}

	void setColumnNameMaxLength(int columnNameMaxLength) {
		this.columnNameMaxLength = columnNameMaxLength;
	}
	 
	public boolean isReservedWord(String word) {
		return (word != null && reservedWords.contains(word.toLowerCase()));
	}
	
	void registerReserved(String reservedWord) { 
		if (reservedWords != null) reservedWords.add(reservedWord.toLowerCase());
	}
	
	public String getTableNameIllegalLeadingCharsPattern() {
		return tableNameIllegalLeadingCharsPattern;
	}

	void setTableNameIllegalLeadingCharsPattern(String tableNameIllegalLeadingCharsPattern) {
		this.tableNameIllegalLeadingCharsPattern = tableNameIllegalLeadingCharsPattern;
	}

	public String getColumnNameIllegalLeadingCharsPattern() {
		return columnNameIllegalLeadingCharsPattern;
	}

	void setColumnNameIllegalLeadingCharsPattern(String columnNameIllegalLeadingCharsPattern) {
		this.columnNameIllegalLeadingCharsPattern = columnNameIllegalLeadingCharsPattern;
	}
	
	
}
