package tribefire.extension.hibernate.graphfetching.source;

import com.braintribe.gm.graphfetching.api.query.FetchSource;
import com.braintribe.model.generic.reflection.GenericModelType;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQuery;

public abstract class HibernateSessionFetchSource extends AbstractHibernateSessionFetchSource implements FetchSource {
	public int pos;
	public String name;
	
	protected HibernateSessionFetchSource(HibernateSessionFetchQuery query) {
		super(query);
		this.pos = query.nextPos();
		this.name = "s" + pos;
	}
	
	public abstract GenericModelType type();
	
	@Override
	public int pos() {
		return pos;
	}
	
	public abstract String selectExpression();
}

