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
	private int standardPropertyOffset;
	private final Map<String, HibernatePolymorphicEntityOracle> polymorphicOracles;
	
	public static EntitySegment build(String name, HibernateEntityOracle entityOracle, HibernateSqlFetchQuery query) {
		Collection<HibernatePropertyOracle> scalarProperties = entityOracle.scalarProperties();
		
		List<RsProperty> rsProperties = new ArrayList<>(scalarProperties.size() + 1);
		
		HibernatePropertyOracle idProperty = entityOracle.idProperty();
		int idPos = query.nextPos();
		RsProperty idRsProperty = new RsProperty(idPos, idProperty);
		rsProperties.add(idRsProperty);
		
		RsProperty typeRsProperty = null; 
		
		int standardPropertyOffset = 1;
		
		Map<String, HibernatePolymorphicEntityOracle> polymorphicOracles = null;
		
		if (entityOracle.isPolymorphic()) {
			int typePos = query.nextPos();
			typeRsProperty = new RsProperty(typePos, entityOracle.getDiscriminatorColumn(), null, ResultValueExtractor.STRING);
			rsProperties.add(typeRsProperty);
			standardPropertyOffset++;
			
			polymorphicOracles = entityOracle.hasHierarchyOracle().polymorphicOracles();
		}
		
		for (HibernatePropertyOracle scalarProperty: scalarProperties) {
			int scalarPos = query.nextPos();
			rsProperties.add(new RsProperty(scalarPos, scalarProperty));
		}
		
		return new EntitySegment(name, query.defaultPartition(), entityOracle.entityType(), rsProperties, standardPropertyOffset, idRsProperty, typeRsProperty, polymorphicOracles);
	}
	
	public EntitySegment(String alias, String defaultPartition, EntityType<?> entityType, List<RsProperty> selectProperties, int standardPropertyOffset, RsProperty idProperty, RsProperty typeProperty, Map<String, HibernatePolymorphicEntityOracle> polymorphicOracles) {
		this.alias = alias;
		this.defaultPartition = defaultPartition;
		this.entityType = entityType;
		this.selectProperties = selectProperties;
		this.standardPropertyOffset = standardPropertyOffset;
		this.idProperty = idProperty;
		this.typeProperty = typeProperty;
		this.polymorphicOracles = polymorphicOracles;
	}

	@Override
	public GenericEntity get(ResultSet rs) throws SQLException {
		Object id = idProperty.getValue(rs);
		
		if (id == null)
			return null;

		final GenericEntity entity;
		
		if (typeProperty == null) {
			entity = entityType.createRaw();

			int propertyCount = selectProperties.size();
			
			for (int i = standardPropertyOffset; i < propertyCount; i++) {
				selectProperties.get(i).transfer(rs, entity);
			}
		}
		else {
			String discriminator = (String) typeProperty.getValue(rs);
			
			HibernatePolymorphicEntityOracle oracle = polymorphicOracles.get(discriminator);

			entity = oracle.entityType.createRaw();
			for (int i: oracle.positions) {
				selectProperties.get(i + standardPropertyOffset).transfer(rs, entity);
			}
		}
		
		FetchingTools.absentifyNonScalarProperties(entity);
		entity.setId(id);
		if (entity.getPartition() == null)
			entity.setPartition(defaultPartition);
		
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
