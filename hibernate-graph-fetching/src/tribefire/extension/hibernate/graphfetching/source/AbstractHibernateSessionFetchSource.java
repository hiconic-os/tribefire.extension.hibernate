package tribefire.extension.hibernate.graphfetching.source;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import com.braintribe.gm.graphfetching.api.query.FetchJoin;
import com.braintribe.gm.graphfetching.api.query.FetchSource;
import com.braintribe.model.generic.reflection.CollectionType;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.utils.lcd.Lazy;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQuery;

public abstract class AbstractHibernateSessionFetchSource implements FetchSource {
	protected final HibernateSessionFetchQuery query;
	private Lazy<Integer> lazyScalarCount = new Lazy<>(this::determineScalarCount);
	
	
	protected AbstractHibernateSessionFetchSource(HibernateSessionFetchQuery query) {
		this.query = query;
	}

	@Override
	public FetchJoin leftJoin(Property property) {
		return new HibernateSessionFetchJoin(query, query.criteriaQuery(), this, property, true);
	}

	@Override
	public FetchJoin join(Property property) {
		return new HibernateSessionFetchJoin(query, query.criteriaQuery(), this, property, false);
	}

	@Override
	public FetchSource as(EntityType<?> entityType) {
		return new HibernateSessionCovariantFetchSource(query, entityType, this);
	}

	public abstract From<Object, Object> criteriaSource();
	public abstract String joinAccessExpression();
	public abstract GenericModelType type();

	public From<Object, Object> criteriaSourceAs(EntityType<?> entityType) {
		From<Object, Object> criteriaSource = criteriaSource();
		if (criteriaSource instanceof Root<?>) {
			Root<Object> root = (Root<Object>) criteriaSource;
			Root<Object> treat = query.criteriaBuilder().treat(root, (Class<Object>) entityType.getJavaType());
			treat.alias(name());
			return treat;
		} else if (criteriaSource instanceof Join<?, ?>) {
			Join<Object, Object> join = (Join<Object, Object>) criteriaSource;
			Join<Object, Object> treat = query.criteriaBuilder().treat(join, (Class<Object>) entityType.getJavaType());
			treat.alias(name());
			return treat;
		}

		throw new IllegalStateException("unexpected From type [" + criteriaSource.getClass().getName() + "]. Must be either Join or Root");
	}
	
	@Override
	public int scalarCount() {
		return lazyScalarCount.get();
	}
	
	private int determineScalarCount() {
		GenericModelType type = type();
		EntityType<?> entityType = null;
		
		if (type.isCollection()) {
			CollectionType collectionType = (CollectionType)type;
			GenericModelType elementType = collectionType.getCollectionElementType();
			if (elementType.isEntity())
				entityType = (EntityType<?>) elementType;
		}
		else {
			if (type.isEntity())
				entityType = (EntityType<?>)type;
		}
		
		if (entityType == null)
			return 1;
		
		int count = 0;
		
		for (Property property: entityType.getProperties()) {
			if (property.isIdentifier() || property.getType().isScalar())
				count++;
		}
		
		return count;
	}



}
