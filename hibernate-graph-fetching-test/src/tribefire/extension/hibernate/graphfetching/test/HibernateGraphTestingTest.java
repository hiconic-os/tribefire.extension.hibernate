package tribefire.extension.hibernate.graphfetching.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.type.Type;
import org.junit.BeforeClass;
import org.junit.Test;

import com.braintribe.codec.marshaller.json.JsonStreamMarshaller;
import com.braintribe.gm._GraphFetchingTestModel_;
import com.braintribe.gm.graphfetching.AbstractGraphFetchingTest;
import com.braintribe.gm.graphfetching.api.FetchBuilder;
import com.braintribe.gm.graphfetching.api.Fetching;
import com.braintribe.gm.graphfetching.api.node.EntityGraphNode;
import com.braintribe.gm.graphfetching.api.query.FetchQuery;
import com.braintribe.gm.graphfetching.api.query.FetchQueryOptions;
import com.braintribe.gm.graphfetching.api.query.FetchResults;
import com.braintribe.gm.graphfetching.test.model.data.ChunkedSource;
import com.braintribe.gm.graphfetching.test.model.data.DataManagement;
import com.braintribe.gm.graphfetching.test.model.data.DataResource;
import com.braintribe.gm.graphfetching.test.model.data.DataSource;
import com.braintribe.gm.graphfetching.test.model.data.FileSource;
import com.braintribe.gm.graphfetching.test.model.data.InmemorySource;
import com.braintribe.gm.graphfetching.test.model.tech.Entitya;
import com.braintribe.gm.graphfetching.test.model.tech.Entityb;
import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.accessdeployment.hibernate.meta.PropertyMapping;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.pr.AbsentEntity;
import com.braintribe.model.generic.pr.criteria.TraversingCriterion;
import com.braintribe.model.generic.processing.pr.fluent.TC;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.value.ValueDescriptor;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.core.commons.comparison.AssemblyComparison;
import com.braintribe.model.processing.core.commons.comparison.AssemblyComparisonResult;
import com.braintribe.model.processing.meta.configuration.ConfigurationModels;
import com.braintribe.model.processing.meta.editor.BasicModelMetaDataEditor;
import com.braintribe.model.processing.meta.oracle.BasicModelOracle;
import com.braintribe.model.processing.query.building.SelectQueries;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.model.query.From;
import com.braintribe.model.query.SelectQuery;
import com.braintribe.testing.junit.assertions.assertj.core.api.Assertions;

import tribefire.extension.hibernate.HibernateHelper;
import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQueryFactory;
import tribefire.extension.hibernate.graphfetching.sql.HibernateEntityOracle;

public class HibernateGraphTestingTest extends AbstractGraphFetchingTest {
	private static SessionFactory sessionFactory;
	
	@BeforeClass
	public static void initPersistence() {
		GmMetaModel model = GMF.getTypeReflection().getModel(_GraphFetchingTestModel_.name).getMetaModel();
		
		GmMetaModel configuredModel = ConfigurationModels.extend(model).get();
		
		BasicModelMetaDataEditor editor = new BasicModelMetaDataEditor(configuredModel);
		
		PropertyMapping idMapping = PropertyMapping.T.create();
		idMapping.setIdGeneration("assigned");
		
		editor.onEntityType(GenericEntity.T).addPropertyMetaData(GenericEntity.id, idMapping);
		
		javax.sql.DataSource dataSource = HibernateHelper.dataSource_H2("graph-fetching-test");
		//javax.sql.DataSource dataSource = HibernateHelper.dataSource_PG("graph-fetching-test");
		
		sessionFactory = HibernateHelper.hibernateSessionFactory(() -> configuredModel, dataSource);
	}
	
	@Override
	protected IncrementalAccess buildAccess() {
		return HibernateHelper.hibernateAccess(ACCESS_ID_TEST, () -> model, sessionFactory);
	}
	
	@Override
	protected FetchBuilder configure(FetchBuilder fetchBuilder) {
		fetchBuilder.queryFactory(new HibernateSessionFetchQueryFactory(sessionFactory));
		fetchBuilder.polymorphicJoin(true);
		fetchBuilder.bulkSize(200);
		return fetchBuilder;
	}
	
	@Test
	public void entityWithToOnes() throws Exception {
		HibernateSessionFetchQueryFactory factory = new HibernateSessionFetchQueryFactory(sessionFactory);
		FetchQueryOptions options = new FetchQueryOptions();
		options.setHydrateFrom(true);
		options.setHydrateAbsentEntitiesIfPossible(true);
		FetchQuery query = factory.createQuery(DataSource.T, ACCESS_ID_TEST, options);
		PersistenceGmSession session = newSession();
		
		From from = SelectQueries.source(DataSource.T);
		SelectQuery selectQuery = SelectQueries.from(from).select(SelectQueries.property(from, GenericEntity.id));
		Set<Object> ids = new HashSet<>(session.queryDetached().select(selectQuery).list());
		
		FetchResults fr = query.fetchFor(ids);
		
		Property referenceProperty = FileSource.T.getProperty(FileSource.reference);
		Property binaryDataProperty = InmemorySource.T.getProperty(InmemorySource.binaryData);
		
		while (fr.next()) {
			GenericEntity entity = fr.get(0);

			System.out.println("====");
			System.out.println(entity.entityType().getShortName());

			if (entity instanceof FileSource) {
				ValueDescriptor vd = referenceProperty.getVd(entity);
				if (vd instanceof AbsentEntity) {
					AbsentEntity absentEntity = (AbsentEntity)vd;
					Object refId = absentEntity.getRefId();
					System.out.println("FileSource.reference: " + refId);
				}
				else {
					Assertions.fail("missing expected AbsentEntity on FileSource.reference property");
				}
			}
			else if (entity instanceof InmemorySource) {
				ValueDescriptor vd = binaryDataProperty.getVd(entity);

				if (vd instanceof AbsentEntity) {
					AbsentEntity absentEntity = (AbsentEntity)vd;
					Object refId = absentEntity.getRefId();
					System.out.println("InmemorySource.binaryData: " + refId);
				}
				else {
					Assertions.fail("missing expected AbsentEntity on InmemorySource.binaryData property");
				}
			}
		}
	}
	
	@Test
	public void pgTest() throws Exception {
		BasicModelOracle oracle = new BasicModelOracle(model);
		EntityGraphNode graphNode = Fetching.reachable(Entitya.T).polymorphy(oracle).build();
		
		PersistenceGmSession session = newSession();
		
		GenericEntity expectedEntity = lazyTechDataGenerator.get().getPool().get(Entitya.T).get(0);
		
		boolean detached = true;
		
		fetchTestBuilder(session, graphNode, detached) //
			.addAllTc(session, detached) //
			.test(expectedEntity, () -> session.queryDetached().entity(Entitya.T, expectedEntity.getId()).find());
	}
	
	@Test
	public void sqlTest() throws Exception {
		BasicModelOracle oracle = new BasicModelOracle(model);
		EntityGraphNode graphNode = Fetching.reachable(DataManagement.T).polymorphy(oracle).build();
		
		PersistenceGmSession session = newSession();
	
		DataManagement expected = lazyDataGenerator.get().getDataManagement();
		
		
		fetchTestBuilder(session, graphNode, false) //
		.addAllTc(session, false) //
		.test(expected, () -> session.queryDetached().entity(DataManagement.T, expected.getId()).find());
	}
	
	@Test
	public void oracleTest() throws Exception {
		HibernateSessionFetchQueryFactory factory = new HibernateSessionFetchQueryFactory(sessionFactory);
		
		HibernateEntityOracle dataSourceOracle = factory.getOracle(DataSource.T);
		HibernateEntityOracle fileSourceOracle = factory.getOracle(FileSource.T);
		
		Assertions.assertThat(dataSourceOracle.hasHierarchyOracle()).isSameAs(fileSourceOracle.hasHierarchyOracle());
	}
	
	// @Test
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
	//@Test
	public void sqlDirectLab() throws Exception {
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
	
	@Override
	public void testPolymorphism() {
		super.testPolymorphism();
	}
	
	
}
