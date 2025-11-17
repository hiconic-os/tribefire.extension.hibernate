package tribefire.extension.hibernate.graphfetching.test;

import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.junit.BeforeClass;
import org.junit.Test;

import com.braintribe.gm._GraphFetchingTestModel_;
import com.braintribe.gm.graphfetching.test.model.Document;
import com.braintribe.gm.graphfetching.test.model.Person;
import com.braintribe.gm.graphfetching.test.model.SignableDocument;
import com.braintribe.gm.graphfetching.test.model.Signature;
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
	
	@Test
	public void testTreating() {
		System.out.println(org.hibernate.Version.getVersionString());
		PersistenceGmSession session = newSession();
		
		Document d1 = session.create(Document.T);
		SignableDocument d2 = session.create(SignableDocument.T);
		
		d1.setName("D1");
		d2.setName("D2");
		
		Signature s1 = session.create(Signature.T);
		Signature s2 = session.create(Signature.T);
		
		s1.setSignature("foo");
		s1.setHash("foo");
		s2.setSignature("bar");
		s2.setHash("bar");
		
		d2.getSignatures().add(s1);
		d2.getSignatures().add(s2);
		
		session.commit();
		
		StatelessSession statelessSession = sessionFactory.openStatelessSession();
		
		CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

		Root<Document> d = cq.from(Document.class);
		Join<SignableDocument, Signature> s =
		    cb.treat(d, SignableDocument.class).join("signatures", JoinType.LEFT);

		cq.multiselect(d, s);
		List<Object[]> rows = statelessSession.createQuery(cq).getResultList();
		
		for (Object[] row: rows) {
			System.out.println(row[0]);
			System.out.println(row[1]);
			System.out.println("---");
		}
		
		List<Object[]> results = statelessSession.createQuery("select d, s from Document d left join treat(d as SignableDocument).signatures s").list();
		
		for (Object[] row: results) {
			System.out.println(row[0]);
			System.out.println(row[1]);
			System.out.println("---");
		}
	}

	
	private PersistenceGmSession newSession() {
		return GmTestTools.newSession(access);
	}

}
