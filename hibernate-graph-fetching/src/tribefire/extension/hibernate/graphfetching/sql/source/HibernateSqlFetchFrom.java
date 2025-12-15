package tribefire.extension.hibernate.graphfetching.sql.source;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.braintribe.model.generic.reflection.EntityType;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQueryFactory;
import tribefire.extension.hibernate.graphfetching.sql.HibernatePolymorphicEntityOracle;
import tribefire.extension.hibernate.graphfetching.sql.HibernatePropertyOracle;
import tribefire.extension.hibernate.graphfetching.sql.HibernateSqlFetchQuery;

public class HibernateSqlFetchFrom extends HibernateSqlFetchSource {
	public final EntityType<?> entityType;
	
	public HibernateSqlFetchFrom(HibernateSessionFetchQueryFactory factory, HibernateSqlFetchQuery query, EntityType<?> entityType, boolean asEntity) {
		super(factory, query);
		
		this.entityType = entityType;
		
		entityOracle = factory.getOracle(entityType);

		if (asEntity) {
			query.addSelectSegment(EntitySegment.build(name, entityOracle, query));
		}
		else {
			RsProperty selectProperty = new RsProperty(query.nextPos(), entityOracle.idProperty());
			query.addSelectSegment(new ScalarSegment(name, selectProperty));
		}
	}
	
	@Override
	public EntityType<?> type() {
		return entityType;
	}
}

