package tribefire.extension.hibernate.graphfetching.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.type.Type;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Property;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQueryFactory;

public class HibernateEntityOracle {
	private AbstractEntityPersister persister;
	private EntityType<?> entityType;

	private HibernatePropertyOracle idProperty;
	private Map<Property, HibernatePropertyOracle> properties = new LinkedHashMap<>();
	private List<HibernatePropertyOracle> scalarProperties = new ArrayList<>();
	private List<HibernatePropertyOracle> entityProperties = new ArrayList<>();
	private List<HibernatePropertyOracle> collectionProperties = new ArrayList<>();
	private HibernateSessionFetchQueryFactory factory;
	private String tableName;
	private HibernateEntityHierarchyOracle hierarchyOracle;
	private boolean polymorphic;
	private String discriminatorColumn;
	
	public HibernateEntityOracle(HibernateSessionFetchQueryFactory factory, AbstractEntityPersister persister, EntityType<?> entityType) {
		super();
		this.factory = factory;
		this.persister = persister;
		this.entityType = entityType;
		
		init();
	}
	
	private void init() {
		if (!isMapped())
			return;
		
		this.tableName = persister.getTableName();
		
		String idColumn = persister.getIdentifierColumnNames()[0];
		idProperty = new HibernatePropertyOracle(persister.getIdentifierType(), entityType.getIdProperty(), idColumn, factory);
		
		polymorphic = persister.isPolymorphic();
		
		if (polymorphic) {
			hierarchyOracle = factory.getHierarchyOracle(persister.getRootEntityName());
			
			for (HibernatePropertyOracle propertyOracle: hierarchyOracle.scalarProperties()) {
				scalarProperties.add(propertyOracle);
				properties.put(propertyOracle.property(), propertyOracle);
			}
			discriminatorColumn = persister.getDiscriminatorColumnName();
		}

		int propertyCount = persister.getPropertyNames().length;
		
		for (int i = 0; i < propertyCount; i++) {
			Type type = persister.getPropertyTypes()[i];
			
			String columnName = findFirst(persister.getPropertyColumnNames(i));
			String propertyName = persister.getPropertyNames()[i];
			Property property = entityType.getProperty(propertyName);
			HibernatePropertyOracle propertyOracle = new HibernatePropertyOracle(type, property, columnName, factory);

			if (propertyOracle.isScalar()) {
				if (!polymorphic) {
					scalarProperties.add(propertyOracle);
					properties.put(property, propertyOracle);
				}
			}
			else if (propertyOracle.isEntity()) {
				entityProperties.add(propertyOracle);
				properties.put(property, propertyOracle);
			}
			else if (propertyOracle.isCollection()) {
				collectionProperties.add(propertyOracle);
				properties.put(property, propertyOracle);
			}
		}
		
	}
	
	String findFirst(String[] columns) {
		if (columns.length == 0)
			return null;
		
		return columns[0];
	}
	
	public HibernateEntityHierarchyOracle hasHierarchyOracle() {
		return hierarchyOracle;
	}
	
	public String tableName() {
		return tableName;
	}
	
	public HibernatePropertyOracle idProperty() {
		return idProperty;
	}
	
	public Collection<HibernatePropertyOracle> scalarProperties() {
		return scalarProperties;
	}
	
	public Collection<HibernatePropertyOracle> entityProperties() {
		return entityProperties;
	}
	
	public Collection<HibernatePropertyOracle> collectionProperties() {
		return entityProperties;
	}
	
	public HibernatePropertyOracle findProperty(Property property) {
		return properties.get(property);
	}

	public boolean isMapped() {
		return this.persister != null;
	}
	
	public boolean isPolymorphic() {
		return polymorphic;
	}
	
	public String getDiscriminatorColumn() {
		return discriminatorColumn;
	}
}
