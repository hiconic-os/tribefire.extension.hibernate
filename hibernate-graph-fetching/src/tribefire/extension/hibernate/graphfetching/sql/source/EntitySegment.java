package tribefire.extension.hibernate.graphfetching.sql.source;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.braintribe.gm.graphfetching.processing.util.FetchingTools;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;

import tribefire.extension.hibernate.graphfetching.sql.HibernatePolymorphicEntityOracle;

public class EntitySegment implements SelectSegment {
	private final EntityType<?> entityType;
	private final List<RsProperty> selectProperties;
	private final String defaultPartition;
	private final String alias;
	private final RsProperty idProperty;
	private final RsProperty typeProperty;
	private int standardPropertyOffset;
	private final Map<String, HibernatePolymorphicEntityOracle> polymorphicOracles;
	
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
