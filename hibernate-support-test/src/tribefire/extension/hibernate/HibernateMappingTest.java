package tribefire.extension.hibernate;

import java.io.File;

import javax.sql.DataSource;

import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;

import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.meta.cmd.CmdResolver;
import com.braintribe.model.processing.meta.cmd.CmdResolverBuilder;
import com.braintribe.model.processing.meta.cmd.CmdResolverImpl;
import com.braintribe.model.processing.meta.configuration.ConfigurationModels;
import com.braintribe.model.processing.meta.configured.ConfigurationModelBuilder;
import com.braintribe.model.processing.meta.editor.BasicModelMetaDataEditor;
import com.braintribe.model.processing.meta.oracle.BasicModelOracle;
import com.braintribe.persistence.hibernate.HibernateModelSessionFactoryBuilder;
import com.braintribe.utils.FileTools;

import tribefire.extension.hibernate.mapping_test.model.Person;

public class HibernateMappingTest {
	
	private CmdResolver buildModel() {
		ConfigurationModelBuilder builder = ConfigurationModels.create(_HibernateMappingTestModel_.groupId + ":configured-" + _HibernateMappingTestModel_.artifactId);
		
		builder.addDependency(_HibernateMappingTestModel_.reflection);
		
		GmMetaModel model = builder.get();
		
		BasicModelMetaDataEditor editor = BasicModelMetaDataEditor.create(model).done();
		
		BasicModelOracle oracle = new BasicModelOracle(model);
		
		CmdResolverBuilder resolverBuilder = CmdResolverImpl.create(oracle);
		
		CmdResolver cmdResolver = resolverBuilder.done();
		
		return cmdResolver;
	}
	
	@Test
	public void testSimpleMapping() {
		DataSource dataSource = DataSources.newDataSource();
		
		HibernateModelSessionFactoryBuilder factory = new HibernateModelSessionFactoryBuilder(buildModel(), dataSource);
		
		File ormDebugOutputFolder = new File("res/out");
		FileTools.deleteDirectoryRecursivelyUnchecked(ormDebugOutputFolder);
		
		factory.setOrmDebugOutputFolder(ormDebugOutputFolder);
		
		SessionFactory sessionFactory = factory.build();
		
		Object id;
		String name = "Hans";
		String lastName = "Wurst";
		
		try (Session session = sessionFactory.openSession()) {
			Transaction transaction = session.beginTransaction();
			Person p = Person.T.create();
			p.setName(name);
			p.setLastName(lastName);
			session.persist(p);
			transaction.commit();
			id = p.getId();
		}
		
		try (Session session = sessionFactory.openSession()) {
			Person p = session.createQuery("from Person where id = :id", Person.class).setParameter("id", id).uniqueResult();
			Assertions.assertThat(p).isNotNull();
			Assertions.assertThat(p.getName()).isEqualTo(name);
			Assertions.assertThat(p.getLastName()).isEqualTo(lastName);
		}
		
	}
}
