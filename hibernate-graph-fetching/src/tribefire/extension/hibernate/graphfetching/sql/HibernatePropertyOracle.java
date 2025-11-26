package tribefire.extension.hibernate.graphfetching.sql;

import org.hibernate.type.CollectionType;
import org.hibernate.type.Type;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EssentialTypes;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.Property;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQueryFactory;
import tribefire.extension.hibernate.graphfetching.sql.source.ResultValueExtractor;

public class HibernatePropertyOracle {
	private Property property;
	private String columnName;
	private HibernateSessionFetchQueryFactory factory;
	private HibernateEntityOracle references;
	private HibernateCollectionOracle collects;
	private boolean isScalar;
	private boolean isEntity;
	private boolean isCollection;
	private final ResultValueExtractor extractor;
	private Type hibernateType;
	
	public HibernatePropertyOracle(Type type, Property property, String columnName, HibernateSessionFetchQueryFactory factory) {
		super();
		this.hibernateType = type;
		this.property = property;
		this.columnName = columnName;
		this.factory = factory;
		
		GenericModelType propertyType = property.getType();
		this.isScalar = property.isIdentifier() || propertyType.isScalar();;
		this.isEntity = propertyType.isEntity();
		this.isCollection = propertyType.isCollection();
		
		if (isScalar)
			extractor = ResultValueExtractor.get(propertyType);
		else if (isEntity)
			extractor = ResultValueExtractor.get(EssentialTypes.TYPE_OBJECT);
		else 
			extractor = null;
	}
	
	public ResultValueExtractor extractor() {
		return extractor;
	}

	public Property property() {
		return property;
	}
	
	public String columnName() {
		return columnName;
	}
	
	public boolean isScalar() {
		return isScalar;
	}
	
	public boolean isEntity() {
		return isEntity;
	}

	public boolean isCollection() {
		return isCollection;
	}
	
	public HibernateEntityOracle references() {
		if (!isEntity)
			return null;
		
		if (references == null) {
			references = factory.getOracle((EntityType<?>)property.getType());
		}
		
		return references;
	}
	
	public HibernateCollectionOracle collects() {
		if (!isCollection)
			return null;
		
		if (collects == null) {
			collects = new HibernateCollectionOracle(factory, (CollectionType) hibernateType, property);
		}
		
		return collects;
	}
}
