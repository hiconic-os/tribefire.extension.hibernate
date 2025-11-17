package tribefire.extension.hibernate.graphfetching.source;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;

import com.braintribe.gm.graphfetching.api.query.FetchJoin;
import com.braintribe.model.generic.reflection.CollectionType;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.reflection.TypeCode;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQuery;

public class HibernateSessionFetchJoin extends HibernateSessionFetchSource implements FetchJoin {
	public final Property property;
	public final HibernateSessionFetchSource source;
	public final ListJoin<Object, Object> listJoin;
	public boolean left;
	public boolean orderByListIndex;
	public final boolean isCollection;
	public final boolean isList;
	public final boolean isEntity;

	public HibernateSessionFetchJoin(HibernateSessionFetchQuery query, CriteriaQuery<Object[]> criteriaQuery, HibernateSessionFetchSource source, Property property, boolean left) {
		super(query, criteriaQuery);
		query.addJoin(this);
		this.source = source;
		this.property = property;
		this.left = left;
		
		GenericModelType type = property.getType();
		
		isCollection = type.isCollection();
		isList = type.getTypeCode() == TypeCode.listType;
		
		if (isCollection) {
			type = ((CollectionType)type).getCollectionElementType();
		}
		
		isEntity = type.isEntity();
		
		if (isList) {
			listJoin = source.criteriaSource().joinList(property.getName(), left? JoinType.LEFT: JoinType.INNER);
			criteriaSource = listJoin;
		}
		else {
			criteriaSource = source.criteriaSource().join(property.getName(), left? JoinType.LEFT: JoinType.INNER);
			listJoin = null;
		}
	}
	
	@Override
	public GenericModelType type() {
		return property.getType();
	}

	@Override
	public void orderByIfRequired() {
		orderByListIndex = isList;
	}

	@Override
	public String selectExpression() {
		return name;
	}
}

