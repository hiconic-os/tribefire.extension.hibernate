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
package com.braintribe.model.access.hibernate;

import static com.braintribe.utils.lcd.CollectionTools2.newList;
import static com.braintribe.utils.lcd.CollectionTools2.newMap;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hibernate.Interceptor;
import org.hibernate.dialect.Dialect;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.processing.IdGenerator;
import com.braintribe.utils.MapTools;

public class HibernateAccessInitializationContext {

	private Map<String, String> hibernateConfigurationProperties = newMap();
	private List<File> hibernateMappingsFolders;
	private List<URL> hibernateMappingsResources;
	private String modelName;
	private Map<Class<? extends GenericEntity>, IdGenerator> idGenerators;
	private Interceptor interceptor;

	public void addHibernateConfigurationProperties(final String... keysAndValues) {
		getHibernateConfigurationProperties().putAll(MapTools.decodeEntries(Arrays.asList(keysAndValues), "="));
	}

	public void addHibernateConfigurationProperty(final String key, final String value) {
		getHibernateConfigurationProperties().put(key, value);
	}

	public void setConnectionDriver(final Class<?> connectionDriver) {
		addHibernateConfigurationProperty("hibernate.connection.driver_class", connectionDriver.getName());
	}

	public void setConnectionUrl(final String connectionUrl) {
		addHibernateConfigurationProperty("hibernate.connection.url", connectionUrl);
	}

	public void setConnectionUsername(final String connectionUsername) {
		addHibernateConfigurationProperty("hibernate.connection.username", connectionUsername);
	}

	public void setConnectionPassword(final String connectionPassword) {
		addHibernateConfigurationProperty("hibernate.connection.password", connectionPassword);
	}

	public void setDialect(final Class<? extends Dialect> dialect) {
		addHibernateConfigurationProperty("hibernate.dialect", dialect.getName());
	}

	public Map<String, String> getHibernateConfigurationProperties() {
		return this.hibernateConfigurationProperties;
	}

	public void setHibernateConfigurationProperties(final Map<String, String> hibernateConfigurationProperties) {
		this.hibernateConfigurationProperties = hibernateConfigurationProperties;
	}

	public List<File> getHibernateMappingsFolders() {
		return this.hibernateMappingsFolders;
	}

	public void setHibernateMappingsFolders(final List<File> hibernateMappingsFolders) {
		this.hibernateMappingsFolders = hibernateMappingsFolders;
	}

	public void addHibernateMappingsFolder(final File hibernateMappingsFolder) {
		if (getHibernateMappingsFolders() == null)
			setHibernateMappingsFolders(newList());
		
		getHibernateMappingsFolders().add(hibernateMappingsFolder);
	}

	public List<URL> getHibernateMappingsResources() {
		return this.hibernateMappingsResources;
	}

	public void setHibernateMappingsResources(final List<URL> hibernateMappingsResources) {
		this.hibernateMappingsResources = hibernateMappingsResources;
	}

	public void addHibernateMappingsResource(final URL hibernateMappingsResource) {
		if (getHibernateMappingsResources() == null)
			setHibernateMappingsResources(newList());

		getHibernateMappingsResources().add(hibernateMappingsResource);
	}

	public String getModelName() {
		return this.modelName;
	}

	public void setModelName(final String modelName) {
		this.modelName = modelName;
	}

	public Map<Class<? extends GenericEntity>, IdGenerator> getIdGenerators() {
		return this.idGenerators;
	}

	public void setIdGenerators(final Map<Class<? extends GenericEntity>, IdGenerator> idGenerators) {
		this.idGenerators = idGenerators;
	}

	public Interceptor getInterceptor() {
		return this.interceptor;
	}

	public void setInterceptor(final Interceptor interceptor) {
		this.interceptor = interceptor;
	}

	@Override
	public String toString() {
		return "HibernateAccessInitializationContext [hibernateConfigurationProperties="
				+ this.hibernateConfigurationProperties + ", hibernateMappingsFolders=" + this.hibernateMappingsFolders
				+ ", hibernateMappingsResources=" + this.hibernateMappingsResources + ", modelName=" + this.modelName
				+ ", idGenerators=" + this.idGenerators + ", interceptor=" + this.interceptor + "]";
	}

}
