package tribefire.extension.hibernate.graphfetching;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;

import com.braintribe.gm.graphfetching.api.query.FetchQuery;
import com.braintribe.gm.graphfetching.api.query.FetchResults;
import com.braintribe.gm.graphfetching.api.query.FetchSource;
import com.braintribe.gm.graphfetching.processing.util.FetchingTools;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.utils.lcd.Lazy;

import tribefire.extension.hibernate.graphfetching.source.HibernateSessionFetchFrom;
import tribefire.extension.hibernate.graphfetching.source.HibernateSessionFetchJoin;

public class HibernateSessionFetchQuery implements FetchQuery {
	private static final String PARAMETER_NAME_IDS = "ids";
	private SessionFactory sessionFactory;
	private int posSeq = 0;
	private final HibernateSessionFetchFrom from;
	private Lazy<String> hqlLazy = new Lazy<>(this::buildHql);
	private Lazy<CriteriaQuery<Object[]>> queryLazy = new Lazy<>(this::buildQuery);
	private List<HibernateSessionFetchJoin> joins = new ArrayList<>();
	private CriteriaQuery<Object[]> criteriaQuery;
	private CriteriaBuilder criteriaBuilder;
	private String defaultPartition;

	public HibernateSessionFetchQuery(SessionFactory sessionFactory, EntityType<?> entityType, String defaultPartition) {
		super();
		this.sessionFactory = sessionFactory;
		this.defaultPartition = defaultPartition;
		this.criteriaBuilder = sessionFactory.getCriteriaBuilder();
		this.criteriaQuery  = criteriaBuilder.createQuery(Object[].class);
		this.from = new HibernateSessionFetchFrom(this, entityType);
	}
	
	public CriteriaBuilder criteriaBuilder() {
		return criteriaBuilder;
	}
	
	public CriteriaQuery<Object[]> criteriaQuery() {
		return criteriaQuery;
	}

	@Override
	public FetchSource from() {
		return from;
	}

	@Override
	public FetchResults fetchFor(Set<Object> entityIds) {
		try (StatelessSession session = sessionFactory.openStatelessSession()) {
			List<Object[]> rows = session.createQuery(queryLazy.get()) //
					.setParameter(PARAMETER_NAME_IDS, entityIds)
					.list();
//			List<Object[]> rows = session.createQuery(hqlLazy.get(), Object[].class)
//					.setParameter(PARAMETER_NAME_IDS, entityIds)
//					.list();

			return new HibernateSessionFetchResults(rows, this);
		}
	}

	@Override
	public String stringify() {
		return hqlLazy.get();
	}

	public int nextPos() {
		return posSeq++;
	}
	
	public void addJoin(HibernateSessionFetchJoin join) {
		joins.add(join);
	}
	
	private CriteriaQuery<Object[]> buildQuery() {
		List<Selection<?>> selections = new ArrayList<>();
		
		Path<Object> entityIdPath = from.criteriaSource().get("id");
		selections.add(entityIdPath);

		HibernateSessionFetchJoin orderByListIndex = null;
		
		for (HibernateSessionFetchJoin join: joins) {
			if (join.orderByListIndex)
				orderByListIndex = join;
			
			selections.add(join.criteriaSource());
		}
		
		// define a parameter variable for the collection
		ParameterExpression<Set> idsParam = criteriaBuilder.parameter(Set.class, "ids");
		
		criteriaQuery.where(entityIdPath.in(idsParam));
		
		criteriaQuery.multiselect(selections);
		
		if (orderByListIndex != null) {
			criteriaQuery.orderBy( //
					criteriaBuilder.asc(entityIdPath), //
					criteriaBuilder.asc(orderByListIndex.listJoin.index()) //
			);
		}
		
		return criteriaQuery;
	}
	
	private String buildHql() {
		StringBuilder b = new StringBuilder();
		
		HibernateSessionFetchJoin orderByListIndex = null;
		
		b.append("select ");
		b.append(from.selectExpression());
		for (HibernateSessionFetchJoin join: joins) {
			b.append(", ");
			b.append(join.selectExpression());
			if (join.orderByListIndex)
				orderByListIndex = join;
		}
		b.append(" from ");
		b.append(from.entityType.getTypeSignature());
		b.append(" ");
		b.append(from.name);
		
		for (HibernateSessionFetchJoin join: joins) {
			if (join.left)
				b.append(" left");
			b.append(" join ");
			b.append(join.source.joinAccessExpression());
			b.append('.');
			b.append(join.property.getName());
			b.append(' ');
			b.append(join.name);
		}
		
		b.append(" where ");
		b.append(from.selectExpression());
		b.append(" in :");
		b.append(PARAMETER_NAME_IDS);
		
		if (orderByListIndex != null) {
			b.append(" order by ");
			b.append(from.selectExpression());
			b.append(", index(");
			b.append(orderByListIndex.name);
			b.append(")");
		}
		
		return b.toString();
	}

	public void postProcess(Object[] row) {
		for (HibernateSessionFetchJoin join: joins) {
			if (!join.isEntity)
				continue;
			
			GenericEntity entity = (GenericEntity)row[join.pos];
			
			if (entity != null)
				entity.setPartition(defaultPartition);
		}
	}
}
