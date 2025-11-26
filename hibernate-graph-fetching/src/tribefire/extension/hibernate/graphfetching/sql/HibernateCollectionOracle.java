package tribefire.extension.hibernate.graphfetching.sql;

import org.hibernate.persister.collection.QueryableCollection;

import com.braintribe.model.generic.reflection.CollectionType;
import com.braintribe.model.generic.reflection.Property;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQueryFactory;

public class HibernateCollectionOracle {
	private CollectionType type;
	private org.hibernate.type.CollectionType hibernateType;
	private Property property;
	private QueryableCollection persister;
	private String joinTableName;
	private String ownerColumn;
	private String indexColumn;
	private String keyColumn;
	private String elementColumn;
	private boolean hasJoinTable;
	
	public HibernateCollectionOracle(HibernateSessionFetchQueryFactory factory, org.hibernate.type.CollectionType hibernateType,
			Property property) {
		super();
		this.hibernateType = hibernateType;
		this.property = property;
		this.type = (CollectionType) property.getType();
		this.persister = (QueryableCollection)factory.getMetamodel().collectionPersister(hibernateType.getRole());
		
		hasJoinTable = persister.isManyToMany();
		
		joinTableName = persister.getTableName();
		
		ownerColumn = findFirst(persister.getKeyColumnNames());
		elementColumn = findFirst(persister.getElementColumnNames());
		
		switch (type.getCollectionKind()) {
		case list:
			indexColumn = findFirst(persister.getIndexColumnNames());
			break;
		case map:
			keyColumn = findFirst(persister.getIndexColumnNames());
			break;
		default:
			break;
		}
	}
	
	String findFirst(String[] columns) {
		if (columns.length == 0)
			return null;
		
		return columns[0];
	}
	
	public String joinTableName() {
		return joinTableName;
	}
	
	public String indexColumn() {
		return indexColumn;
	}
	
	public String keyColumn() {
		return keyColumn;
	}
	
	public String ownerColumn() {
		return ownerColumn;
	}
	
	public String elementColumn() {
		return elementColumn;
	}
	
	public Property property() {
		return property;
	}
	
	public org.hibernate.type.CollectionType hibernateType() {
		return hibernateType;
	}
	
	public boolean hasJoinTable() {
		return hasJoinTable;
	}
}
