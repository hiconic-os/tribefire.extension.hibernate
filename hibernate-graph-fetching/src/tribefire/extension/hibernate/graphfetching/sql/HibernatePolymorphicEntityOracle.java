package tribefire.extension.hibernate.graphfetching.sql;

import com.braintribe.model.generic.reflection.EntityType;

public class HibernatePolymorphicEntityOracle {
	public final EntityType<?> entityType;
	public int[] positions;
	
	public HibernatePolymorphicEntityOracle(EntityType<?> entityType) {
		this.entityType = entityType;
	}
	
	public EntityType<?> entityType() {
		return entityType;
	}

	public void setPositions(int[] positions) {
		this.positions = positions;
	}
}
