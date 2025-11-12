package tribefire.extension.hibernate.graphfetching.source;

import com.braintribe.gm.graphfetching.api.query.FetchJoin;
import com.braintribe.model.generic.reflection.CollectionType;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.reflection.TypeCode;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQuery;

public class HibernateSessionFetchJoin extends HibernateSessionFetchSource implements FetchJoin {
	public final Property property;
	public final HibernateSessionFetchSource source;
	public boolean left;
	public boolean orderByListIndex;
	public final boolean isCollection;
	public final boolean isList;
	public final boolean isEntity;

	public HibernateSessionFetchJoin(HibernateSessionFetchQuery query, HibernateSessionFetchSource source, Property property, boolean left) {
		super(query);
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

