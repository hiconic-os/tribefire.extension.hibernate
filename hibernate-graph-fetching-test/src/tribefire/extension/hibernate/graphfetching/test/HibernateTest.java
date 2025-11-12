package tribefire.extension.hibernate.graphfetching.test;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.junit.BeforeClass;
import org.junit.Test;

import com.braintribe.gm._GraphFetchingTestModel_;
import com.braintribe.gm.graphfetching.test.model.Address;
import com.braintribe.gm.graphfetching.test.model.City;
import com.braintribe.gm.graphfetching.test.model.Person;
import com.braintribe.model.access.hibernate.HibernateAccess;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.testing.tools.gm.GmTestTools;

import tribefire.extension.hibernate.HibernateHelper;

public class HibernateTest {
	private static SessionFactory sessionFactory;
	private static HibernateAccess access;
	
	@BeforeClass
	public static void initPersistence() {
		GmMetaModel model = GMF.getTypeReflection().getModel(_GraphFetchingTestModel_.name).getMetaModel();
		sessionFactory = HibernateHelper.hibernateSessionFactory(() -> model, HibernateHelper.dataSource_H2("graph-fetching-test"));
		access = HibernateHelper.hibernateAccess("test", () -> model, sessionFactory);
	}
	
	@Test
	public void testPersistence() {
		PersistenceGmSession session = newSession();

		Person p1 = session.create(Person.T);
		p1.setFirstName("Hans");
		
		session.commit();
		
		StatelessSession statelessSession = sessionFactory.openStatelessSession();
		
		Person p2 = (Person)statelessSession.get(Person.class, p1.getId());
		
		System.out.println(p2);
		
		
	}

	
	private PersistenceGmSession newSession() {
		return GmTestTools.newSession(access);
	}

}
