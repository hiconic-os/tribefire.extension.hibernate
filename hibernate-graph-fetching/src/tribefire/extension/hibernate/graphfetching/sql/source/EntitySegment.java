package tribefire.extension.hibernate.graphfetching.sql.source;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.braintribe.gm.graphfetching.processing.util.FetchingTools;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public class EntitySegment implements SelectSegment {
	private final EntityType<?> entityType;
	private final List<RsProperty> selectProperties;
	private final String defaultPartition;
	private final String alias;
	private final RsProperty idProperty;
	private final RsProperty typeProperty;
	private int standardPropertyOffset;
	
	public EntitySegment(String alias, String defaultPartition, EntityType<?> entityType, List<RsProperty> selectProperties, int standardPropertyOffset, RsProperty idProperty, RsProperty typeProperty) {
		this.alias = alias;
		this.defaultPartition = defaultPartition;
		this.entityType = entityType;
		this.selectProperties = selectProperties;
		this.standardPropertyOffset = standardPropertyOffset;
		this.idProperty = idProperty;
		this.typeProperty = typeProperty;
	}

	public GenericEntity get(ResultSet rs) throws SQLException {
		GenericEntity entity = hydrateEntity(rs);
		return entity;
	}
	
	private GenericEntity hydrateEntity(ResultSet rs) throws SQLException {
		GenericEntity entity = createEntityInitialized(rs);

		if (entity == null)
			return null;
		
		int propertyCount = selectProperties.size();
		
		for (int i = standardPropertyOffset; i < propertyCount; i++) {
			RsProperty rsProperty = selectProperties.get(i);
			Object value = rsProperty.getValue(rs);
			
			if (value != null)
				rsProperty.property().set(entity, value);
		}

		if (entity.getPartition() == null)
			entity.setPartition(defaultPartition);
		
		return entity;
	}

	private GenericEntity createEntityInitialized(ResultSet rs) throws SQLException {
		Object id = idProperty.getValue(rs);
		
		if (id == null)
			return null;

		GenericEntity entity = createEntity(rs);
		FetchingTools.absentifyNonScalarProperties(entity);
		entity.setId(id);

		
		return entity;
	}
	
	private GenericEntity createEntity(ResultSet rs) throws SQLException {
		if (typeProperty == null)
			return entityType.createRaw();
		
		String typeSignature = (String) typeProperty.getValue(rs);
		EntityType<?> effectiveType = EntityTypes.get(typeSignature);
		
		return effectiveType.createRaw();
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
