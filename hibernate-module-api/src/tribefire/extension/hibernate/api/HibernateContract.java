package tribefire.extension.hibernate.api;

import com.braintribe.gm.graphfetching.api.query.FetchQueryFactory;
import com.braintribe.wire.api.space.WireSpace;

public interface HibernateContract extends WireSpace {

	/**
	 * Returns a new instance of the FetchQueryFactory backed by Hibernate's "SessionFactory" for given accessId.
	 * <p>
	 * Note that the accessId given must belong to a HibernateAccess.
	 * 
	 * @throws IllegalArgumentException
	 *             if no Hibernate access exists for given accessId.
	 */
	FetchQueryFactory newFetchQueryFactory(String hibernateAccessId);

}
