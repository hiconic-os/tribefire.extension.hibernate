package tribefire.extension.hibernate.graphfetching.sql.source;

import com.braintribe.gm.graphfetching.api.query.FetchSource;
import com.braintribe.model.generic.reflection.EntityType;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQueryFactory;
import tribefire.extension.hibernate.graphfetching.sql.HibernateSqlFetchQuery;

public class HibernateSqlCovariantFetchSource extends AbstractHibernateSqlFetchSource implements FetchSource {

	private final AbstractHibernateSqlFetchSource baseSource;

	public HibernateSqlCovariantFetchSource(HibernateSessionFetchQueryFactory factory, HibernateSqlFetchQuery query, EntityType<?> entityType, AbstractHibernateSqlFetchSource baseSource) {
		super(factory, query);
		this.baseSource = baseSource;
		this.entityOracle = factory.getOracle(entityType);
		this.name = baseSource.name;
	}

	@Override
	public int pos() {
		return baseSource.pos();
	}
	
	@Override
	public String pkColumn() {
		return baseSource.pkColumn();
	}
	
	@Override
	public int scalarCount() {
		// no additional count as the baseSource is already fetching everything
		return 0;
	}
}

