package tribefire.extension.hibernate.graphfetching;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
	private List<HibernateSessionFetchJoin> joins = new ArrayList<>();

	public HibernateSessionFetchQuery(SessionFactory sessionFactory, EntityType<?> entityType) {
		super();
		this.sessionFactory = sessionFactory;
		this.from = new HibernateSessionFetchFrom(this, entityType);
	}

	@Override
	public FetchSource from() {
		return from;
	}

	@Override
	public FetchResults fetchFor(Set<Object> entityIds) {
		try (StatelessSession session = sessionFactory.openStatelessSession()) {
			List<Object[]> rows = session.createQuery(hqlLazy.get(), Object[].class)
					.setParameter(PARAMETER_NAME_IDS, entityIds)
					.list();
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
			b.append(join.source.name);
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
				row[join.pos] = FetchingTools.cloneDetachment(entity);
		}
	}
}
