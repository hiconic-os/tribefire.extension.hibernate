package com.braintribe.model.access.hibernate.base.tools;

import java.io.File;

import com.braintribe.persistence.hibernate.GmAwareHibernateSessionFactoryBean;

/**
 * @author peter.gazdik
 */
public class TestHibernateSessionFactoryBean extends GmAwareHibernateSessionFactoryBean {

	public File[] mappingDirectoryLocations() {
		return mappingDirectoryLocations;
	}

}
