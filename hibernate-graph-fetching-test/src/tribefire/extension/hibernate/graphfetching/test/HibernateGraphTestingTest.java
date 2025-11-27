package tribefire.extension.hibernate.graphfetching.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.type.Type;
import org.junit.BeforeClass;
import org.junit.Test;

import com.braintribe.gm._GraphFetchingTestModel_;
import com.braintribe.gm.graphfetching.AbstractGraphFetchingTest;
import com.braintribe.gm.graphfetching.api.FetchBuilder;
import com.braintribe.gm.graphfetching.api.Fetching;
import com.braintribe.gm.graphfetching.api.node.EntityGraphNode;
import com.braintribe.gm.graphfetching.test.model.data.ChunkedSource;
import com.braintribe.gm.graphfetching.test.model.data.DataManagement;
import com.braintribe.gm.graphfetching.test.model.data.DataResource;
import com.braintribe.gm.graphfetching.test.model.data.DataSource;
import com.braintribe.gm.graphfetching.test.model.data.FileSource;
import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.meta.oracle.BasicModelOracle;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.testing.junit.assertions.assertj.core.api.Assertions;

import tribefire.extension.hibernate.HibernateHelper;
import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQueryFactory;
import tribefire.extension.hibernate.graphfetching.sql.HibernateEntityOracle;
import tribefire.extension.hibernate.graphfetching.sql.HibernatePropertyOracle;

public class HibernateGraphTestingTest extends AbstractGraphFetchingTest {
	private static SessionFactory sessionFactory;
	
	@BeforeClass
	public static void initPersistence() {
		GmMetaModel model = GMF.getTypeReflection().getModel(_GraphFetchingTestModel_.name).getMetaModel();
		sessionFactory = HibernateHelper.hibernateSessionFactory(() -> model, HibernateHelper.dataSource_H2("graph-fetching-test"));
	}
	
	@Override
	protected IncrementalAccess buildAccess() {
		return HibernateHelper.hibernateAccess(ACCESS_ID_TEST, () -> model, sessionFactory);
	}
	
	@Override
	protected FetchBuilder configure(FetchBuilder fetchBuilder) {
		fetchBuilder.queryFactory(new HibernateSessionFetchQueryFactory(sessionFactory));
		return fetchBuilder;
	}
	
	@Test
	public void sqlTest() throws Exception {
		BasicModelOracle oracle = new BasicModelOracle(model);
		EntityGraphNode graphNode = Fetching.reachable(DataManagement.T).polymorphy(oracle).build();
		
		System.out.println(graphNode.stringify());
		PersistenceGmSession session = newSession();
		HibernateSessionFetchQueryFactory factory = new HibernateSessionFetchQueryFactory(sessionFactory);
		
		List<DataManagement> dataManagements = session.queryDetached().entities(EntityQuery.create(DataManagement.T)).list();
		
		Fetching.build(session, graphNode).queryFactory(factory).fetch(dataManagements);
		
		System.out.println(dataManagements);
	}
	
	@Test
	public void oracleTest() throws Exception {
		HibernateSessionFetchQueryFactory factory = new HibernateSessionFetchQueryFactory(sessionFactory);
		
		HibernateEntityOracle dataSourceOracle = factory.getOracle(DataSource.T);
		HibernateEntityOracle fileSourceOracle = factory.getOracle(FileSource.T);
		
		Assertions.assertThat(dataSourceOracle.hasHierarchyOracle()).isSameAs(fileSourceOracle.hasHierarchyOracle());
		
		Collection<HibernatePropertyOracle> scalarProperties = dataSourceOracle.scalarProperties();
		
		for (HibernatePropertyOracle propertyOracle: scalarProperties) {
			System.out.println(propertyOracle.property() + " -> " + propertyOracle.columnName());
		}
	}
	
	@Test
	public void sqlDirectPolyLab() throws Exception {
		SessionFactoryImplementor sfi = (SessionFactoryImplementor) sessionFactory;
		AbstractEntityPersister pDataResource = (AbstractEntityPersister)sfi.getMetamodel().entityPersister(DataResource.class);
		AbstractEntityPersister pDataSource = (AbstractEntityPersister)sfi.getMetamodel().entityPersister(DataSource.class);
		AbstractEntityPersister pFileSource = (AbstractEntityPersister)sfi.getMetamodel().entityPersister(FileSource.class);
		AbstractEntityPersister pChunkedSource = (AbstractEntityPersister)sfi.getMetamodel().entityPersister(ChunkedSource.class);

		
		for (AbstractEntityPersister persister: Arrays.asList(pDataResource, pDataSource, pFileSource, pChunkedSource)) {
			String entityName = persister.getEntityName();
			String rootEntityName = persister.getRootEntityName();
			boolean rootEntity = entityName.equals(rootEntityName);
			System.out.println("=====================================");
			System.out.println("EntityName:          " + entityName);
			System.out.println("isPolymorphic:       " + persister.isPolymorphic());
			System.out.println("hasSubClasses:       " + persister.hasSubclasses());
			System.out.println("rootEntityName:      " + rootEntityName);
			System.out.println("discriminatorColumn: " + persister.getDiscriminatorColumnName());
			System.out.println("discriminator:       " + persister.getDiscriminatorValue());
			System.out.println("properties:");
			for (int i = 0; i < persister.getPropertyNames().length; i++) {
				if (persister.isDefinedOnSubclass(i))
					continue;
				
				Type type = persister.getPropertyTypes()[i];
				
				System.out.print("- ");
				System.out.print(persister.getPropertyNames()[i]);
				if (!type.isCollectionType()) {
					System.out.print(" column=");
					System.out.print(persister.getPropertyColumnNames(i)[0]);
				}
				System.out.print(" type=");
				System.out.print(type.getName());
				System.out.println();
			}
			int subPropCount = persister.countSubclassProperties();
			for (int i = 0; i < subPropCount; i++) {
				
				Type type = persister.getSubclassPropertyType(i);
				
				System.out.print("^ ");
				System.out.print(persister.getSubclassPropertyName(i));
				if (!type.isCollectionType()) {
					System.out.print(" column=");
					System.out.print(persister.getSubclassPropertyColumnNames(i)[0]);
				}
				System.out.print(" type=");
				System.out.print(type.getName());
				System.out.print(" type=");
				System.out.print(type.getName());
				System.out.println();
			}
		}

		
		
	}
	@Test
	public void sqlDirectTest() throws Exception {
		SessionFactoryImplementor sfi = (SessionFactoryImplementor) sessionFactory;
		AbstractEntityPersister persister = (AbstractEntityPersister)sfi.getMetamodel().entityPersister(DataResource.class);
		
		ConnectionProvider cp =
			    sfi.getServiceRegistry().getService(ConnectionProvider.class);

		record ScalarInfo(int pos, String alias, String columnName, Property property, ResultValueExtractor extractor) {}
		
		EntityType<?> entityType = DataResource.T;

		
		List<ScalarInfo> infos = new ArrayList<>();
		int seq = 0;
		
		for (Property property: entityType.getProperties()) {
			GenericModelType type = property.getType();
			boolean identifier = property.isIdentifier();
			if (!identifier && !type.isScalar())
				continue;
			
			final String columnName;
			
			if (identifier) {
				columnName = persister.getIdentifierColumnNames()[0];
			}
			else {
				int propertyIndex = persister.getPropertyIndex(property.getName());
				columnName = persister.getPropertyColumnNames(propertyIndex)[0];
			}
			
			ResultValueExtractor extractor = ResultValueExtractor.get(type);
			
			int pos = seq++;
			
			String sa = "s" + pos;

			infos.add(new ScalarInfo(pos, sa, columnName, property, extractor));
		}
		
		String alias = "a";
		StringBuilder sqlB = new StringBuilder();
		sqlB.append("select ");
		boolean first = true;
		for (ScalarInfo info: infos) {
			if (first)
				first = false;
			else
				sqlB.append(",");
			
			sqlB.append(alias);
			sqlB.append('.');
			sqlB.append(info.columnName());
		}
		
		sqlB.append(" from ");
		sqlB.append(persister.getTableName());
		sqlB.append(" as ");
		sqlB.append(alias);
		
		String sql = sqlB.toString();
		
		List<GenericEntity> entities = new ArrayList<>();
		
		try (Connection connection = cp.getConnection()) {
			PreparedStatement ps = connection.prepareStatement(sql);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					GenericEntity entity = entityType.createRaw();

					for (ScalarInfo info: infos) {
						
						Object value = info.extractor().getValue(rs, info.pos() + 1);
	
						info.property().set(entity, value);
					}
					entities.add(entity);
				}
				
			}
		}
		
		for (GenericEntity entity: entities) {
			System.out.println(entity);
		}
	}
}
