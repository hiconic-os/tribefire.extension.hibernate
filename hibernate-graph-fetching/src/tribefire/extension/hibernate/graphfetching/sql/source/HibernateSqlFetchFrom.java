package tribefire.extension.hibernate.graphfetching.sql.source;

import com.braintribe.model.generic.reflection.EntityType;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQueryFactory;
import tribefire.extension.hibernate.graphfetching.sql.HibernateSqlFetchQuery;

public class HibernateSqlFetchFrom extends HibernateSqlFetchSource {
	public final EntityType<?> entityType;
	
	public HibernateSqlFetchFrom(HibernateSessionFetchQueryFactory factory, HibernateSqlFetchQuery query, EntityType<?> entityType) {
		super(factory, query);
		
		this.entityType = entityType;
		
		entityOracle = factory.getOracle(entityType);

		RsProperty selectProperty = new RsProperty(query.nextPos(), entityOracle.idProperty());
		
		query.addSelectSegment(new ScalarSegment(name, selectProperty));
	}
	
	@Override
	public EntityType<?> type() {
		return entityType;
	}
}

