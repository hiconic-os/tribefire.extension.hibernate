package tribefire.extension.hibernate.graphfetching.source;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;

import com.braintribe.gm.graphfetching.api.query.FetchJoin;
import com.braintribe.gm.graphfetching.api.query.FetchSource;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.Property;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQuery;

public abstract class HibernateSessionFetchSource implements FetchSource {
	public int pos;
	public String name;
	protected final HibernateSessionFetchQuery query;
	protected final CriteriaQuery<Object[]> criteriaQuery;
	protected From<?, ?> criteriaSource;
	
	protected HibernateSessionFetchSource(HibernateSessionFetchQuery query, CriteriaQuery<Object[]> criteriaQuery) {
		super();
		this.query = query;
		this.criteriaQuery = criteriaQuery;
		this.pos = query.nextPos();
		this.name = "s" + pos;
	}
	
	public abstract GenericModelType type();
	
	@Override
	public int pos() {
		return pos;
	}

	@Override
	public FetchJoin leftJoin(Property property) {
		return new HibernateSessionFetchJoin(query, criteriaQuery, this, property, true);
	}

	@Override
	public FetchJoin join(Property property) {
		return new HibernateSessionFetchJoin(query, criteriaQuery, this, property, false);
	}
	
	public abstract String selectExpression();
	
	public From<?, ?> criteriaSource() {
		return criteriaSource;
	}
}

