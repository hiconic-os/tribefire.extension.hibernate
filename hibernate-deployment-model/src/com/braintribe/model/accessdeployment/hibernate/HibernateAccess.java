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
package com.braintribe.model.accessdeployment.hibernate;

import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Name;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author gunther.schenk
 */
/* TODO: It would be great to extract this to hibernate-deployment-model. That's currently not possible as it would lead to a cyclic group dependency,
 * because tf.cortex references hibernate, and hibernate would have to reference tf.cortex, as HibernateComponent references DatabaseConnectionPool
 * from tf.cortex:database-deployment-model.
 * 
 * When extracting hibernate stuff, tf.cortex should not know anything about it and hibernate should be tribefire.extension.hibernate. */
public interface HibernateAccess extends IncrementalAccess, HibernateComponent {

	EntityType<HibernateAccess> T = EntityTypes.T(HibernateAccess.class);

	String durationWarningThreshold = "durationWarningThreshold";
	String objectNamePrefix = "objectNamePrefix";
	String tableNamePrefix = "tableNamePrefix";
	String foreignKeyNamePrefix = "foreignKeyNamePrefix";
	String uniqueKeyNamePrefix = "uniqueKeyNamePrefix";
	String indexNamePrefix = "indexNamePrefix";
	String schemaUpdate = "schemaUpdate";
	String deadlockRetryLimit = "deadlockRetryLimit";
	String logging = "logging";
	String schemaUpdateOnlyOnModelChange = "schemaUpdateOnlyOnModelChange";

	/** If a query exceeds this number of milliseconds, a warning entry is logged. */
	Long getDurationWarningThreshold();
	void setDurationWarningThreshold(Long durationWarningThreshold);

	String getObjectNamePrefix();
	void setObjectNamePrefix(String objectNamePrefix);

	String getTableNamePrefix();
	void setTableNamePrefix(String tableNamePrefix);

	String getForeignKeyNamePrefix();
	void setForeignKeyNamePrefix(String foreignKeyNamePrefix);

	String getUniqueKeyNamePrefix();
	void setUniqueKeyNamePrefix(String uniqueKeyNamePrefix);

	@Description("Defines the prefix for each index name")
	String getIndexNamePrefix();
	void setIndexNamePrefix(String indexNamePrefix);

	@Description("Maximum number of times 'applyManipulation' is attempted in case it fails due to a deadlock.")
	Integer getDeadlockRetryLimit();
	void setDeadlockRetryLimit(Integer deadlockRetryLimit);

	@Name("Logging")
	@Description("Manual Logging configuration - if set Hibernate related information will be logged")
	HibernateLogging getLogging();
	void setLogging(HibernateLogging logging);

	@Name("Schema Update on Model Change")
	@Description("When true, the DB schema will only be checked if a model was changed."
			+ " Model checksums is stored in the TF_SCHEMA_UPDATE_TMP table."
			+ " Set this to false if, e.g. the Access must not create this extra table.")
	@Initializer("true")
	Boolean getSchemaUpdateOnlyOnModelChange();
	void setSchemaUpdateOnlyOnModelChange(Boolean schemaUpdateOnlyOnModelChange);
}
