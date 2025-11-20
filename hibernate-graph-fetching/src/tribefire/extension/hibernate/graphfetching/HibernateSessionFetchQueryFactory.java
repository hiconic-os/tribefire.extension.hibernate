package tribefire.extension.hibernate.graphfetching;

import org.hibernate.SessionFactory;

import com.braintribe.gm.graphfetching.api.query.FetchQuery;
import com.braintribe.gm.graphfetching.api.query.FetchQueryFactory;
import com.braintribe.model.generic.reflection.EntityType;

public class HibernateSessionFetchQueryFactory implements FetchQueryFactory {

	private SessionFactory sessionFactory;

	public HibernateSessionFetchQueryFactory(SessionFactory sessionFactory) {
		super();
		this.sessionFactory = sessionFactory;
	}

	@Override
	public FetchQuery createQuery(EntityType<?> entityType, String defaultPartition) {
		return new HibernateSessionFetchQuery(sessionFactory, entityType, defaultPartition);
	}

	@Override
	public boolean supportsSubTypeJoin() {
		return true;
	}
}
