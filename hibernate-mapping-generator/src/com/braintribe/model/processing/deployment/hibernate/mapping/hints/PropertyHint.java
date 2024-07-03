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
package com.braintribe.model.processing.deployment.hibernate.mapping.hints;

public class PropertyHint {
	
	public Boolean mapToDb = true; 
	
	public String type;
	public String column;
	
	public Boolean unique;
	public Boolean notNull;
	
	public String uniqueKey;
	public String index;
	public String foreignKey;
	
	public String idGeneration;
	
	public Long length;
	public Long precision;
	public Long scale;
	
	public String lazy;
	public String fetch;
	
	// for collection properties:
	public Boolean oneToMany;
	public String table;
	public String keyType;
	public Long keyLength;
	public String keyColumn;
	public String keyPropertyRef;
	public String indexColumn;
	public String mapKeyColumn;
	public String mapKeyForeignKey;
	public String elementColumn;
	public String elementForeignKey;
	public String manyToManyFetch; //special case for <many-to-many> 'fetch' attribute
	
}
