package tribefire.extension.hibernate.graphfetching;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.spi.MetamodelImplementor;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;

import com.braintribe.gm.graphfetching.api.query.FetchQuery;
import com.braintribe.gm.graphfetching.api.query.FetchQueryFactory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.generic.reflection.Property;

import tribefire.extension.hibernate.graphfetching.sql.HibernateEntityHierarchyOracle;
import tribefire.extension.hibernate.graphfetching.sql.HibernateEntityOracle;
import tribefire.extension.hibernate.graphfetching.sql.HibernatePropertyOracle;
import tribefire.extension.hibernate.graphfetching.sql.HibernateSqlFetchQuery;
import tribefire.extension.hibernate.graphfetching.sql.PropertyIdentTuple;

public class HibernateSessionFetchQueryFactory implements FetchQueryFactory {

	private SessionFactory sessionFactory;
	private SessionFactoryImplementor sfi;
	private ConnectionProvider cp;
	private MetamodelImplementor metamodel;
	private Map<EntityType<?>, HibernateEntityOracle> entityOracles = new ConcurrentHashMap<>();
	private Map<String, HibernateEntityHierarchyOracle> entityHierarchyOracles = new ConcurrentHashMap<>();
	private final Map<String, List<AbstractEntityPersister>> entityHierarchyPersisters = new ConcurrentHashMap<>();
	private boolean sqlDirect = false;
	
	public HibernateSessionFetchQueryFactory(SessionFactory sessionFactory) {
		this(sessionFactory, false);
	}
	
	public HibernateSessionFetchQueryFactory(SessionFactory sessionFactory, boolean sqlDirect) {
		super();
		this.sqlDirect = sqlDirect;
		this.sessionFactory = sessionFactory;
		this.sfi = (SessionFactoryImplementor) sessionFactory;
		this.cp = sfi.getServiceRegistry().getService(ConnectionProvider.class);
		this.metamodel = sfi.getMetamodel();
		
		for (EntityPersister p: metamodel.entityPersisters().values()) {
			if (!(p instanceof AbstractEntityPersister)) 
				continue;
			
			AbstractEntityPersister persister = (AbstractEntityPersister)p;
			
			if (!persister.isPolymorphic())
				continue;
				
			List<AbstractEntityPersister> persisters = entityHierarchyPersisters.computeIfAbsent(persister.getRootEntityName(), n -> new ArrayList<>());
			persisters.add(persister);
		}
	}
	
	public Connection getConnection() throws SQLException {
		return cp.getConnection();
	}
	
	public MetamodelImplementor getMetamodel() {
		return metamodel;
	}
	
	public SessionFactoryImplementor getSessionFactoryImplementor() {
		return sfi;
	}
	
	public HibernateEntityHierarchyOracle getHierarchyOracle(String rootName) {
		return entityHierarchyOracles.computeIfAbsent(rootName, this::buildHierarchyOracle);
	}
	
	private HibernateEntityHierarchyOracle buildHierarchyOracle(String rootName) {
		Map<PropertyIdentTuple, Property> propertyMap = new HashMap<>(); 
		
		String discriminatorColumn = null;
		String tableName;
		
		for (AbstractEntityPersister persister: entityHierarchyPersisters.get(rootName)) {
			tableName = persister.getTableName();
			discriminatorColumn = persister.getDiscriminatorColumnName();
			EntityType<?> entityType = EntityTypes.get(persister.getEntityName());

			String[] propertyNames = persister.getPropertyNames();
			for (int i = 0; i < propertyNames.length; i++) {
				if (persister.isDefinedOnSubclass(i))
					continue;
				
				Type type = persister.getPropertyTypes()[i];
				if (type.isCollectionType() || type.isEntityType())
					continue;
				
				String columnName = persister.getPropertyColumnNames(i)[0];
				String propertyName = propertyNames[i];
				PropertyIdentTuple key = new PropertyIdentTuple(propertyName, tableName, columnName, type);
				Property property = entityType.getProperty(propertyName);
				
				propertyMap.compute(key, (k,v) -> {
					if (v == null || v == property)
						return property;
					
					property.getDeclaringType().isAssignableFrom(v.getDeclaringType());
					return property;
				});
			}
		}
		
		List<HibernatePropertyOracle> propertyOracles = new ArrayList<>(propertyMap.size());
		
		for (Map.Entry<PropertyIdentTuple, Property> entry: propertyMap.entrySet()) {
			PropertyIdentTuple identTuple = entry.getKey();
			Property property = entry.getValue();
			HibernatePropertyOracle oracle = new HibernatePropertyOracle(identTuple.type(), property, identTuple.columnName(), this);
			propertyOracles.add(oracle);
		}
		
		propertyOracles.sort((o1, o2) -> o1.property().getName().compareTo(o2.property().getName()));
		
		return new HibernateEntityHierarchyOracle(discriminatorColumn, propertyOracles);
	}
	
	public HibernateEntityOracle getOracle(EntityType<?> entityType) {
		return entityOracles.computeIfAbsent(entityType, this::buildOrache);
	}
	
	private HibernateEntityOracle buildOrache(EntityType<?> entityType) {
		AbstractEntityPersister entityPersister = (AbstractEntityPersister)metamodel.entityPersisters().get(entityType.getTypeSignature());
		return new HibernateEntityOracle(this, entityPersister, entityType);
	}

	@Override
	public FetchQuery createQuery(EntityType<?> entityType, String defaultPartition) {
		if (sqlDirect)
			return new HibernateSqlFetchQuery(this, entityType, defaultPartition);
		else 
			return new HibernateSessionFetchQuery(sessionFactory, entityType, defaultPartition);
	}
	
	@Override
	public boolean supportsSubTypeJoin() {
		return true;
	}
	

}
