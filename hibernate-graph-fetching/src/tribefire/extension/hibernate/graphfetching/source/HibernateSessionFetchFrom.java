package tribefire.extension.hibernate.graphfetching.source;

import javax.persistence.criteria.CriteriaQuery;

import com.braintribe.model.generic.reflection.EntityType;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQuery;

public class HibernateSessionFetchFrom extends HibernateSessionFetchSource {
	public final EntityType<?> entityType;
	private final String selectExpression;
	
	public HibernateSessionFetchFrom(HibernateSessionFetchQuery query, CriteriaQuery<Object[]> criteriaQuery, EntityType<?> entityType) {
		super(query, criteriaQuery);
		this.entityType = entityType;
		selectExpression = name + "." + entityType.getIdProperty().getName();
		this.criteriaSource = criteriaQuery.from(entityType.getJavaType());
	}
	
	@Override
	public EntityType<?> type() {
		return entityType;
	}

	@Override
	public String selectExpression() {
		return selectExpression;
	}
}

