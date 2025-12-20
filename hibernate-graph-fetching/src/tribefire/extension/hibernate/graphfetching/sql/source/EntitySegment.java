package tribefire.extension.hibernate.graphfetching.sql.source;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.braintribe.gm.graphfetching.processing.util.FetchingTools;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;

import tribefire.extension.hibernate.graphfetching.sql.HibernateEntityOracle;
import tribefire.extension.hibernate.graphfetching.sql.HibernatePolymorphicEntityOracle;
import tribefire.extension.hibernate.graphfetching.sql.HibernatePropertyOracle;
import tribefire.extension.hibernate.graphfetching.sql.HibernateSqlFetchQuery;

public class EntitySegment implements SelectSegment {
	private final EntityType<?> entityType;
	private final List<RsProperty> selectProperties;
	private final String defaultPartition;
	private final String alias;
	private final RsProperty idProperty;
	private final RsProperty typeProperty;
	private int scalarPropertyOffset;
	private int entityPropertyOffset;
	private final Map<String, HibernatePolymorphicEntityOracle> polymorphicOracles;
	
	public static EntitySegment build(String name, HibernateEntityOracle entityOracle, HibernateSqlFetchQuery query) {
		Collection<HibernatePropertyOracle> scalarProperties = entityOracle.scalarProperties();
		
		int rsPropertiesSize = scalarProperties.size() + 1;
		
		boolean polymorphic = entityOracle.isPolymorphic();
		boolean hydrateAbsentEntities = query.getOptions().getHydrateAbsentEntitiesIfPossible();
		if (polymorphic) {
			rsPropertiesSize++;
			if (hydrateAbsentEntities)
				rsPropertiesSize += entityOracle.entityProperties().size();
		}
		
		List<RsProperty> rsProperties = new ArrayList<>(rsPropertiesSize);
		
		HibernatePropertyOracle idProperty = entityOracle.idProperty();
		int idPos = query.nextPos();
		RsProperty idRsProperty = new RsProperty(idPos, idProperty);
		rsProperties.add(idRsProperty);
		
		RsProperty typeRsProperty = null; 
		
		
		Map<String, HibernatePolymorphicEntityOracle> polymorphicOracles = null;
		
		if (polymorphic) {
			int typePos = query.nextPos();
			typeRsProperty = new RsProperty(typePos, entityOracle.getDiscriminatorColumn(), null, ResultValueExtractor.STRING);
			rsProperties.add(typeRsProperty);
			
			polymorphicOracles = entityOracle.hasHierarchyOracle().polymorphicOracles();
		}
		
		int standardPropertyOffset = rsProperties.size();
		
		for (HibernatePropertyOracle scalarProperty: scalarProperties) {
			int scalarPos = query.nextPos();
			rsProperties.add(new RsProperty(scalarPos, scalarProperty));
		}
		
		final int entityPropertyOffset; 
		
		if (hydrateAbsentEntities) {
			entityPropertyOffset = rsProperties.size();
			for (HibernatePropertyOracle entityProperty: entityOracle.entityProperties()) {
				int scalarPos = query.nextPos();
				rsProperties.add(new RsProperty(scalarPos, entityProperty));
			}
		}
		else {
			entityPropertyOffset = -1;
		}
		
		return new EntitySegment(name, query.defaultPartition(), entityOracle.entityType(), rsProperties, standardPropertyOffset, entityPropertyOffset, idRsProperty, typeRsProperty, polymorphicOracles);
	}
	
	public EntitySegment(String alias, String defaultPartition, EntityType<?> entityType, List<RsProperty> selectProperties, int scalarPropertyOffset, int entityPropertyOffset, RsProperty idProperty, RsProperty typeProperty, Map<String, HibernatePolymorphicEntityOracle> polymorphicOracles) {
		this.alias = alias;
		this.defaultPartition = defaultPartition;
		this.entityType = entityType;
		this.selectProperties = selectProperties;
		this.scalarPropertyOffset = scalarPropertyOffset;
		this.idProperty = idProperty;
		this.typeProperty = typeProperty;
		this.polymorphicOracles = polymorphicOracles;
		this.entityPropertyOffset = entityPropertyOffset;
	}

	@Override
	public GenericEntity get(ResultSet rs) throws SQLException {
		Object id = idProperty.getValue(rs);
		
		if (id == null)
			return null;

		final GenericEntity entity;
		
		if (typeProperty == null) {
			entity = create(entityType);

			int propertyCount = selectProperties.size();
			
			for (int i = scalarPropertyOffset; i < propertyCount; i++) {
				selectProperties.get(i).transfer(rs, entity);
			}
		}
		else {
			String discriminator = (String) typeProperty.getValue(rs);
			
			HibernatePolymorphicEntityOracle oracle = polymorphicOracles.get(discriminator);

			entity = create(oracle.entityType);
			
			for (int i: oracle.scalarPositions) {
				selectProperties.get(i + scalarPropertyOffset).transfer(rs, entity);
			}
			
			if (entityPropertyOffset != -1) {
				for (int i: oracle.entityPositions) {
					selectProperties.get(i + entityPropertyOffset).transfer(rs, entity);
				}
			}
		}
		
		entity.setId(id);
		if (entity.getPartition() == null)
			entity.setPartition(defaultPartition);
		
		return entity;
	}
	
	private GenericEntity create(EntityType<?> entityType) {
		GenericEntity entity = entityType.createRaw();
		FetchingTools.absentifyNonScalarProperties(entity);
		return entity;
	}

	@Override
	public String alias() {
		return alias;
	}

	@Override
	public Collection<RsProperty> selectProperties() {
		return selectProperties;
	}
}
