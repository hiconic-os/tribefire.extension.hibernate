package com.braintribe.model.processing.deployment.hibernate.mapping.render.context;

import java.util.Collection;
import java.util.List;

import com.braintribe.model.processing.deployment.hibernate.mapping.index.IndexDescriptor;

/**
 * @author peter.gazdik
 */
public class OutputDescriptors {

	public Collection<EntityDescriptor> entityDescriptors;
	public List<IndexDescriptor> indexDescriptors;

}
