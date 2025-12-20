package tribefire.extension.hibernate.graphfetching.sql.source;

import com.braintribe.gm.graphfetching.api.query.FetchJoin;
import com.braintribe.gm.graphfetching.api.query.FetchSource;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Property;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQueryFactory;
import tribefire.extension.hibernate.graphfetching.sql.HibernateEntityOracle;
import tribefire.extension.hibernate.graphfetching.sql.HibernateSqlFetchQuery;

public abstract class AbstractHibernateSqlFetchSource implements FetchSource {
	protected final HibernateSqlFetchQuery query;
	protected final HibernateSessionFetchQueryFactory factory;

	protected HibernateEntityOracle entityOracle;
	public String name;
	
	protected AbstractHibernateSqlFetchSource(HibernateSessionFetchQueryFactory factory, HibernateSqlFetchQuery query) {
		this.factory = factory;
		this.query = query;
	}
	
	public HibernateEntityOracle isEntity() {
		return entityOracle;
	}
	
	@Override
	public FetchJoin leftJoin(Property property) {
		if (entityOracle == null)
			throw new IllegalStateException("Illegal property join on non-entity source");
		
		return new HibernateSqlFetchJoin(factory, query, this, property, true);
	}

	@Override
	public FetchJoin join(Property property) {
		if (entityOracle == null)
			throw new IllegalStateException("Illegal property join on non-entity source");
		
		return new HibernateSqlFetchJoin(factory, query, this, property, false);
	}
	
	@Override
	public FetchSource as(EntityType<?> entityType) {
		return new HibernateSqlCovariantFetchSource(factory, query, entityType, this);
	}
	
	public abstract String pkColumn();
}
