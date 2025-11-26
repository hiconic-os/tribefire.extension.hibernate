package tribefire.extension.hibernate.graphfetching.sql.source;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.braintribe.gm.graphfetching.api.query.FetchJoin;
import com.braintribe.model.generic.reflection.CollectionType;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.MapType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.reflection.TypeCode;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQueryFactory;
import tribefire.extension.hibernate.graphfetching.sql.HibernateCollectionOracle;
import tribefire.extension.hibernate.graphfetching.sql.HibernateEntityOracle;
import tribefire.extension.hibernate.graphfetching.sql.HibernatePropertyOracle;
import tribefire.extension.hibernate.graphfetching.sql.HibernateSqlFetchQuery;

public class HibernateSqlFetchJoin extends HibernateSqlFetchSource implements FetchJoin {
	public final Property property;
	public final AbstractHibernateSqlFetchSource source;
	public boolean left;
	public boolean orderByListIndex;
	private String column;
	private HibernatePropertyOracle refererProperty;

	public HibernateSqlFetchJoin(HibernateSessionFetchQueryFactory factory, HibernateSqlFetchQuery query, AbstractHibernateSqlFetchSource source, Property property, boolean left) {
		super(factory, query);
		this.source = source;
		this.property = property;
		this.left = left;
		
		query.addJoin(this);
		
		HibernateEntityOracle sourceEntityOracle = source.isEntity();
		
		if (sourceEntityOracle != null) {
			refererProperty = sourceEntityOracle.findProperty(property);
			if (refererProperty != null)
				column = refererProperty.columnName();
		}

		final GenericModelType propertyType = property.getType();
		
		final GenericModelType joinedType;
		GenericModelType joinedKeyType = null;
		CollectionType propertyCollectionType = null;
				
		if (propertyType.isCollection()) {
			propertyCollectionType = (CollectionType)propertyType;
			joinedType = propertyCollectionType.getCollectionElementType();
			
			if (propertyType.getTypeCode() == TypeCode.mapType) {
				MapType mapType = (MapType)propertyType;
				joinedKeyType = mapType.getKeyType();
			}
		}
		else {
			joinedType = propertyType;
		}
		
		if (joinedKeyType != null) {
			if (joinedKeyType.isScalar()) {
				int scalarPos = query.nextPos();
				HibernateCollectionOracle collects = refererProperty.collects();
				RsProperty scalarRsProperty = new RsProperty(scalarPos, collects.keyColumn(), null, ResultValueExtractor.get(joinedKeyType));
				boolean hasJoinTable = collects.hasJoinTable();
				if (hasJoinTable)
					query.addSelectSegment(new ScalarSegment(linkAlias(), scalarRsProperty));
				else
					query.addSelectSegment(new ScalarSegment(name, scalarRsProperty));
			}
			else if (joinedKeyType.isEntity()) {
				
			}
		}
		
		if (joinedType.isScalar()) {
			int scalarPos = query.nextPos();
			
			if (propertyCollectionType != null) {
				HibernateCollectionOracle collects = refererProperty.collects();
				RsProperty scalarRsProperty = new RsProperty(scalarPos, collects.elementColumn(), null, ResultValueExtractor.get(joinedType));
				query.addSelectSegment(new ScalarSegment(name, scalarRsProperty));
			}
			else {
				final HibernatePropertyOracle scalarOracle = refererProperty;
				RsProperty scalarRsProperty = new RsProperty(scalarPos, scalarOracle);
				query.addSelectSegment(new ScalarSegment(name, scalarRsProperty));
			}
			
		}
		else if (joinedType.isEntity()) {
			EntityType<?> joinedEntityType = (EntityType<?>)joinedType;
			// entity join
			entityOracle = factory.getOracle(joinedEntityType);
			Collection<HibernatePropertyOracle> scalarProperties = entityOracle.scalarProperties();
			
			List<RsProperty> rsProperties = new ArrayList<>(scalarProperties.size() + 1);
			
			HibernatePropertyOracle idProperty = entityOracle.idProperty();
			int idPos = query.nextPos();
			RsProperty idRsProperty = new RsProperty(idPos, idProperty);
			rsProperties.add(idRsProperty);
			
			RsProperty typeRsProperty = null; 
			
			int standardPropertyOffset = 1;
			
			if (entityOracle.isPolymorphic()) {
				int typePos = query.nextPos();
				typeRsProperty = new RsProperty(typePos, entityOracle.getDiscriminatorColumn(), null, ResultValueExtractor.STRING);
				rsProperties.add(typeRsProperty);
				standardPropertyOffset++;
			}
			
			for (HibernatePropertyOracle scalarProperty: scalarProperties) {
				int scalarPos = query.nextPos();
				rsProperties.add(new RsProperty(scalarPos, scalarProperty));
			}
			
			query.addSelectSegment(new EntitySegment(name, query.defaultPartition(), joinedEntityType, rsProperties, standardPropertyOffset, idRsProperty, typeRsProperty));
		}
	}
	
	@Override
	public GenericModelType type() {
		return property.getType();
	}

	@Override
	public void orderByIfRequired() {
		orderByListIndex = property.getType().getTypeCode() == TypeCode.listType;
	}
	
	/**
	 * The column of the property in case of scalar or To-One relationship. I case of collection this is null
	 */
	public String column() {
		return column;
	}
	
	public HibernateCollectionOracle isCollectionJoin() {
		return refererProperty.collects();
	}
}

