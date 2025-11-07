package com.braintribe.model.processing.deployment.hibernate.mapping.index;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface IndexDescriptor extends GenericEntity {

	EntityType<IndexDescriptor> T = EntityTypes.T(IndexDescriptor.class);

	void setIndexName(String indexName);
	String getIndexName();

	String getTableName();
	void setTableName(String tableName);

	String getColumnName();
	void setColumnName(String columnName);

	String getEntityTypeSignature();
	void setEntityTypeSignature(String entityTypeSignature);

	String getPropertyName();
	void setPropertyName(String propertyName);

	// Calling it purpose because IndexType is already used elsewhere
	IndexPurpose getPurpose();
	void setPurpose(IndexPurpose purpose);

}
