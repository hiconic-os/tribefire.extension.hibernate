package tribefire.extension.hibernate.graphfetching.source;

import com.braintribe.gm.graphfetching.api.query.FetchJoin;
import com.braintribe.gm.graphfetching.api.query.FetchSource;
import com.braintribe.model.generic.reflection.Property;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQuery;

public abstract class HibernateSessionFetchSource implements FetchSource {
	public int pos;
	public String name;
	protected final HibernateSessionFetchQuery query;
	
	public HibernateSessionFetchSource(HibernateSessionFetchQuery query) {
		super();
		this.query = query;
		this.pos = query.nextPos();
		this.name = "s" + pos;
	}
	
	@Override
	public int pos() {
		return pos;
	}

	@Override
	public FetchJoin leftJoin(Property property) {
		return new HibernateSessionFetchJoin(query, this, property, true);
	}

	@Override
	public FetchJoin join(Property property) {
		return new HibernateSessionFetchJoin(query, this, property, false);
	}
	
	public abstract String selectExpression();
}

