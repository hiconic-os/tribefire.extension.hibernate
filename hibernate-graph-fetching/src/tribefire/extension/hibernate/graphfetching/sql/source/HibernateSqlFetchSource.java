package tribefire.extension.hibernate.graphfetching.sql.source;

import com.braintribe.gm.graphfetching.api.query.FetchSource;
import com.braintribe.model.generic.reflection.GenericModelType;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQueryFactory;
import tribefire.extension.hibernate.graphfetching.sql.HibernateSqlFetchQuery;

public abstract class HibernateSqlFetchSource extends AbstractHibernateSqlFetchSource implements FetchSource {
	public int pos;
	
	protected HibernateSqlFetchSource(HibernateSessionFetchQueryFactory factory, HibernateSqlFetchQuery query) {
		super(factory, query);
		this.pos = query.nextSegmentPos();
		this.name = query.nextAlias();
	}
	
	public abstract GenericModelType type();
	
	@Override
	public int pos() {
		return pos;
	}
	
	public String tableName() {
		return entityOracle.tableName();
	}

	public String pkColumn() {
		return entityOracle.idProperty().columnName();
	}
	
	@Override
	public int scalarCount() {
		if (entityOracle == null)
			return 1;
		
		return entityOracle.scalarProperties().size();
	}
}

