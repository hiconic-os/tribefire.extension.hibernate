package com.braintribe.model.processing.deployment.hibernate.mapping.index;

import com.braintribe.model.generic.base.EnumBase;
import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.EnumTypes;
import com.braintribe.model.meta.data.query.Index;

/**
 * @author peter.gazdik
 */
public enum IndexPurpose implements EnumBase<IndexPurpose> {

	/** This type of index is automatically created for every collection property, it indices the foreign key in the collection table */
	COLLECTION_FOREIGN_KEY,
	/** Index on the value of a collection, assuming the collection property has the {@link Index} meta-data  */
	COLLECTION_ELEMENT, //
	;

	public static final EnumType<IndexPurpose> T = EnumTypes.T(IndexPurpose.class);

	@Override
	public EnumType<IndexPurpose> type() {
		return T;
	}

}
