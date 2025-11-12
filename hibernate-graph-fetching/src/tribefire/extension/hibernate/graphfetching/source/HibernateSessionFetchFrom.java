package tribefire.extension.hibernate.graphfetching.source;

import com.braintribe.model.generic.reflection.EntityType;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQuery;

public class HibernateSessionFetchFrom extends HibernateSessionFetchSource {
	public final EntityType<?> entityType;
	private final String selectExpression;
	
	public HibernateSessionFetchFrom(HibernateSessionFetchQuery query, EntityType<?> entityType) {
		super(query);
		this.entityType = entityType;
		selectExpression = name + "." + entityType.getIdProperty().getName();
	}

	@Override
	public String selectExpression() {
		return selectExpression;
	}
}

