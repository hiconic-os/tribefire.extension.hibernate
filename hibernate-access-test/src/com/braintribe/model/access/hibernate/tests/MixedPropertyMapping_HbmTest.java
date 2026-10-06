package com.braintribe.model.access.hibernate.tests;

import org.junit.Test;

import com.braintribe.model.access.hibernate.base.HibernateAccessRecyclingTestBase;
import com.braintribe.model.access.hibernate.base.model.simple.BasicEntity;
import com.braintribe.model.access.hibernate.base.model.simple.BasicScalarEntity;
import com.braintribe.model.access.hibernate.base.wire.contract.HibernateModelsContract;
import com.braintribe.model.meta.GmMetaModel;

/**
 * Tests that an inherited property (globalId) mapped for one entity ({@link BasicEntity}) is still treated as unmapped for another entity
 * ({@link BasicScalarEntity}).
 * <p>
 * Grouping by an entity expands to all its mapped properties. If the "is mapped" check ignored the owner type, the GROUP BY would contain
 * {@code globalId} of {@link BasicScalarEntity}, which Hibernate cannot resolve.
 * 
 * @see HibernateModelsContract#basic_GlobalIdMappedForBasicEntityOnly()
 */
public class MixedPropertyMapping_HbmTest extends HibernateAccessRecyclingTestBase {

	@Override
	protected GmMetaModel model() {
		return hibernateModels.basic_GlobalIdMappedForBasicEntityOnly();
	}

	@Test
	public void groupByFromSourceSkipsPropertyUnmappedForItsType() throws Exception {
		BasicScalarEntity a = bse("A", 1);
		BasicScalarEntity b = bse("B", 2);
		session.commit();

		runSelectQuery(from(BasicScalarEntity.T, "bse") //
				.select("bse") //
				.select().sum("bse", BasicScalarEntity.integerValue) //
				.done() //
		);

		qra.assertContains(a, 1L);
		qra.assertContains(b, 2L);
		qra.assertNoMoreResults();
	}

	@Test
	public void groupByJoinSourceSkipsPropertyUnmappedForItsType() throws Exception {
		BasicScalarEntity a = bse("A", 1);
		BasicScalarEntity c = bse("C", 3);

		be("BE-1", a, 1);
		be("BE-2", a, 2);
		be("BE-3", c, 3);
		session.commit();

		runSelectQuery(from(BasicEntity.T, "be") //
				.join("be", BasicEntity.scalarEntity, "bse") //
				.select("bse") //
				.select().sum("be", BasicEntity.integerValue) //
				.done() //
		);

		qra.assertContains(a, 3L);
		qra.assertContains(c, 3L);
		qra.assertNoMoreResults();
	}

	@Test
	public void groupByFromSourceKeepsPropertyMappedForItsType() throws Exception {
		BasicEntity be = createBe("BE-1");
		be.setGlobalId("be-1");
		be.setIntegerValue(1);
		session.commit();

		runSelectQuery(from(BasicEntity.T, "be") //
				.select("be", BasicEntity.globalId) //
				.select().sum("be", BasicEntity.integerValue) //
				.done() //
		);

		qra.assertContains("be-1", 1L);
		qra.assertNoMoreResults();
	}

	private BasicScalarEntity bse(String name, int integerValue) {
		BasicScalarEntity result = create(BasicScalarEntity.T, name);
		result.setIntegerValue(integerValue);

		return result;
	}

	private BasicEntity be(String name, BasicScalarEntity bse, int integerValue) {
		BasicEntity result = createBe(name);
		result.setScalarEntity(bse);
		result.setIntegerValue(integerValue);

		return result;
	}

	private BasicEntity createBe(String name) {
		return create(BasicEntity.T, name);
	}

}
