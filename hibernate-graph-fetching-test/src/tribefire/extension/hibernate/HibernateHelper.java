package tribefire.extension.hibernate;

import static com.braintribe.utils.lcd.CollectionTools2.newList;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;

import com.braintribe.model.access.hibernate.HibernateAccess;
import com.braintribe.model.generic.processing.IdGenerator;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.core.expert.api.GmExpertRegistry;
import com.braintribe.model.processing.core.expert.impl.ConfigurableGmExpertRegistry;
import com.braintribe.model.processing.deployment.hibernate.HibernateMappingsDirectorySupplier;
import com.braintribe.model.processing.idgenerator.basic.DateIdGenerator;
import com.braintribe.model.processing.idgenerator.basic.UuidGenerator;
import com.braintribe.persistence.hibernate.GmAwareHibernateSessionFactoryBean;
import com.braintribe.persistence.hibernate.HibernateSessionFactoryBean;

public class HibernateHelper {
	/** If true, HQL queries performed by hibernate are printed in the console */
	public static final boolean SHOW_SQL = false;
	public static final boolean FORMAT_SQL = false;
	public static final boolean COMMIT_HIB_SESSION = false;
	
	private static final List<AutoCloseable> closeables = newList();
	
	private static SessionFactory sessionFactory(GmMetaModel model, String dbName) {
		HibernateSessionFactoryBean hsfb = hibernateSessionFactoryBean( //
				() -> model, //
				dataSource_H2(dbName));

		hsfb.setShowSql(SHOW_SQL);
		hsfb.setFormatSql(FORMAT_SQL);

		return hsfb.getObject();
	}


	public static HibernateAccess hibernateAccess(String accessId, Supplier<GmMetaModel> modelSupplier, DataSource dataSource) throws Exception {
		return hibernateAccess(accessId, modelSupplier, hibernateSessionFactory(modelSupplier, dataSource));
	}

	public static HibernateAccess hibernateAccess(String accessId, Supplier<GmMetaModel> modelSupplier, SessionFactory hibernateSessionFactory) {
		HibernateAccess bean = new HibernateAccess();
		bean.setAccessId(accessId);
		bean.setModelSupplier(modelSupplier);
		bean.setHibernateSessionFactory(hibernateSessionFactory);
		bean.setExpertRegistry(expertRegistry());

		return bean;
	}

	private static GmExpertRegistry expertRegistry() {
		ConfigurableGmExpertRegistry bean = new ConfigurableGmExpertRegistry();
		bean.add(IdGenerator.class, String.class, new UuidGenerator());
		bean.add(IdGenerator.class, Date.class, new DateIdGenerator());
		return bean;
	}

	public static SessionFactory hibernateSessionFactory(Supplier<GmMetaModel> modelSupplier, DataSource dataSource) {
		return hibernateSessionFactoryBean(modelSupplier, dataSource).getObject();
	}

	public static GmAwareHibernateSessionFactoryBean hibernateSessionFactoryBean(Supplier<GmMetaModel> modelSupplier, DataSource dataSource) {
		GmAwareHibernateSessionFactoryBean sessionFactory = new GmAwareHibernateSessionFactoryBean();
		sessionFactory.setShowSql(true);
		sessionFactory.setMappingDirectoryLocations(mappingsFolder(modelSupplier));
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setDefaultBatchFetchSize(30);
		sessionFactory.postConstruct();

		closeables.add(sessionFactory::closeFactory);

		return sessionFactory;
	}
	
	private static File mappingsFolder(Supplier<GmMetaModel> modelSupplier) {
		HibernateMappingsDirectorySupplier bean = new HibernateMappingsDirectorySupplier();
		bean.setMetaModel(modelSupplier.get());

		return bean.get();
	}

	public static DataSource dataSource_H2(String dbName) {
		org.h2.jdbcx.JdbcDataSource bean = new org.h2.jdbcx.JdbcDataSource();

		// DB_CLOSE_DELAY -> https://stackoverflow.com/questions/5763747/h2-in-memory-database-table-not-found
		bean.setUrl("jdbc:h2:mem:" + dbName + ";DB_CLOSE_DELAY=-1");

		return bean;
	}


}
