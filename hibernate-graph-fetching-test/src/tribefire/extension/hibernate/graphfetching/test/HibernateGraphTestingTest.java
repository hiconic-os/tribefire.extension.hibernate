package tribefire.extension.hibernate.graphfetching.test;

import org.hibernate.SessionFactory;
import org.junit.BeforeClass;

import com.braintribe.gm._GraphFetchingTestModel_;
import com.braintribe.gm.graphfetching.AbstractGraphFetchingTest;
import com.braintribe.gm.graphfetching.api.FetchBuilder;
import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.meta.GmMetaModel;

import tribefire.extension.hibernate.HibernateHelper;
import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQueryFactory;

public class HibernateGraphTestingTest extends AbstractGraphFetchingTest {
	private static SessionFactory sessionFactory;
	
	@BeforeClass
	public static void initPersistence() {
		GmMetaModel model = GMF.getTypeReflection().getModel(_GraphFetchingTestModel_.name).getMetaModel();
		sessionFactory = HibernateHelper.hibernateSessionFactory(() -> model, HibernateHelper.dataSource_H2("graph-fetching-test"));
	}
	
	@Override
	protected IncrementalAccess buildAccess() {
		return HibernateHelper.hibernateAccess("test", () -> model, sessionFactory);
	}
	
	@Override
	protected FetchBuilder configure(FetchBuilder fetchBuilder) {
		fetchBuilder.queryFactory(new HibernateSessionFetchQueryFactory(sessionFactory));
		return fetchBuilder;
	}
}
