package com.braintribe.model.access.hibernate.base.model.inverse1toN;

import java.util.Set;

import com.braintribe.model.access.hibernate.base.model.HibernateAccessEntity;
import com.braintribe.model.generic.StandardStringIdentifiable;
import com.braintribe.model.generic.annotation.ToStringInformation;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
@ToStringInformation("KnowsParent[${id}]")
public interface KnowsParent extends HibernateAccessEntity, StandardStringIdentifiable {

	EntityType<KnowsParent> T = EntityTypes.T(KnowsParent.class);

	String parentId = "parentId";
	String children = "children";

	String getParentId();
	void setParentId(String parentId);

	Set<KnowsParent> getChildren();
	void setChildren(Set<KnowsParent> children);

}
