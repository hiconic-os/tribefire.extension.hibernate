package com.braintribe.model.access.hibernate.base.model.collection;

import com.braintribe.model.generic.base.EnumBase;
import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.EnumTypes;

/**
 * @author peter.gazdik
 */
public enum ScalarEnum implements EnumBase<ScalarEnum> {

	earth,
	wind,
	fire,
	water;

	public static final EnumType<ScalarEnum> T = EnumTypes.T(ScalarEnum.class);

	@Override
	public EnumType<ScalarEnum> type() {
		return T;
	}

}
