package tribefire.extension.hibernate.graphfetching.source;

import javax.persistence.criteria.From;

import com.braintribe.gm.graphfetching.api.query.FetchSource;
import com.braintribe.model.generic.reflection.EntityType;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQuery;

public class HibernateSessionCovariantFetchSource extends AbstractHibernateSessionFetchSource implements FetchSource {

	private final AbstractHibernateSessionFetchSource baseSource;
	private final From<Object, Object> criteriaSource;
	private final String joinAccessExpression;

	public HibernateSessionCovariantFetchSource(HibernateSessionFetchQuery query, EntityType<?> entityType, AbstractHibernateSessionFetchSource baseSource) {
		super(query);
		this.baseSource = baseSource;
		this.criteriaSource = baseSource.criteriaSourceAs(entityType);
		joinAccessExpression = "treat(" + baseSource.joinAccessExpression() + " as " + entityType.getTypeSignature() + ")";
	}

	@Override
	public int pos() {
		return baseSource.pos();
	}

	@Override
	public From<Object, Object> criteriaSource() {
		return criteriaSource;
	}


	@Override
	public String joinAccessExpression() {
		return joinAccessExpression;
	}
}

