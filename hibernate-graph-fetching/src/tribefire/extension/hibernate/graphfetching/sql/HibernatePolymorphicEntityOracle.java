package tribefire.extension.hibernate.graphfetching.sql;

import com.braintribe.model.generic.reflection.EntityType;

public class HibernatePolymorphicEntityOracle {
	public final EntityType<?> entityType;
	public int[] scalarPositions;
	public int[] entityPositions;
	
	public HibernatePolymorphicEntityOracle(EntityType<?> entityType) {
		this.entityType = entityType;
	}
	
	public EntityType<?> entityType() {
		return entityType;
	}

	public void setScalarPositions(int[] positions) {
		this.scalarPositions = positions;
	}
	
	public void setEntityPositions(int[] entityPositions) {
		this.entityPositions = entityPositions;
	}
}
