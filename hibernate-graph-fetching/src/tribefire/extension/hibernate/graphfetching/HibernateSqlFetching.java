package tribefire.extension.hibernate.graphfetching;

import java.util.WeakHashMap;

import org.hibernate.SessionFactory;

import com.braintribe.gm.graphfetching.api.query.FetchQueryFactory;

public class HibernateSqlFetching {
	
	private static WeakHashMap<SessionFactory, FetchQueryFactory> factories = new WeakHashMap<SessionFactory, FetchQueryFactory>();
	
	public static FetchQueryFactory queryFactory(SessionFactory sessionFactory) {
		return factories.computeIfAbsent(sessionFactory, HibernateSessionFetchQueryFactory::new);
	}
}
