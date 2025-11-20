package tribefire.extension.hibernate.graphfetching.source;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import com.braintribe.gm.graphfetching.api.query.FetchJoin;
import com.braintribe.gm.graphfetching.api.query.FetchSource;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Property;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQuery;

public abstract class AbstractHibernateSessionFetchSource implements FetchSource {
	protected final HibernateSessionFetchQuery query;
	
	protected AbstractHibernateSessionFetchSource(HibernateSessionFetchQuery query) {
		this.query = query;
	}
	
	@Override
	public FetchJoin leftJoin(Property property) {
		return new HibernateSessionFetchJoin(query, query.criteriaQuery(), this, property, true);
	}

	@Override
	public FetchJoin join(Property property) {
		return new HibernateSessionFetchJoin(query, query.criteriaQuery(), this, property, false);
	}
	
	@Override
	public FetchSource as(EntityType<?> entityType) {
		return new HibernateSessionCovariantFetchSource(query, entityType, this);
	}
	
	public abstract From<Object, Object> criteriaSource();
	public abstract String joinAccessExpression();

	public From<Object, Object> criteriaSourceAs(EntityType<?> entityType) {
		From<Object,Object> criteriaSource = criteriaSource();
		if (criteriaSource instanceof Root<?>) {
			Root<Object> root = (Root<Object>)criteriaSource;
			Root<Object> treat = query.criteriaBuilder().treat(root, (Class<Object>)entityType.getJavaType());
			return treat;
		}
		else if (criteriaSource instanceof Join<?, ?>) {
			Join<Object, Object> join = (Join<Object, Object>)criteriaSource;
			Join<Object, Object> treat = query.criteriaBuilder().treat(join, (Class<Object>)entityType.getJavaType());
			return treat;
		}
		
		throw new IllegalStateException("unexpected From type [" + criteriaSource.getClass().getName() + "]. Must be either Join or Root");
	}


}
