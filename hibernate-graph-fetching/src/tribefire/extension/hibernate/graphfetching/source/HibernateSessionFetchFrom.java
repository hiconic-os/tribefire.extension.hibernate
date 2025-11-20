package tribefire.extension.hibernate.graphfetching.source;

import javax.persistence.criteria.Root;

import com.braintribe.model.generic.reflection.EntityType;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQuery;

public class HibernateSessionFetchFrom extends HibernateSessionFetchSource {
	public final EntityType<?> entityType;
	private final String selectExpression;
	private Root<Object> criteriaSource;

	public HibernateSessionFetchFrom(HibernateSessionFetchQuery query, EntityType<?> entityType) {
		super(query);
		this.entityType = entityType;
		selectExpression = name + "." + entityType.getIdProperty().getName();
		this.criteriaSource = query.criteriaQuery().from((Class<Object>) entityType.getJavaType());
		criteriaSource.alias(name);
	}

	@Override
	public String joinAccessExpression() {
		return name;
	}

	@Override
	public EntityType<?> type() {
		return entityType;
	}

	@Override
	public String selectExpression() {
		return selectExpression;
	}

	@Override
	public Root<Object> criteriaSource() {
		return criteriaSource;
	}

	@Override
	public Root<Object> criteriaSourceAs(EntityType<?> entityType) {
		Root<Object> treat = query.criteriaBuilder().treat(criteriaSource, (Class<Object>) entityType.getJavaType());
		treat.alias(name);
		return treat;
	}
}
