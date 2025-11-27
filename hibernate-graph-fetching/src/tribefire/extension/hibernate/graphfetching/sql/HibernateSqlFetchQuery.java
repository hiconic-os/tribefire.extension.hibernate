package tribefire.extension.hibernate.graphfetching.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.braintribe.common.lcd.Pair;
import com.braintribe.exception.Exceptions;
import com.braintribe.gm.graphfetching.api.query.FetchQuery;
import com.braintribe.gm.graphfetching.api.query.FetchResults;
import com.braintribe.gm.graphfetching.api.query.FetchSource;
import com.braintribe.logging.Logger;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.TypeCode;
import com.braintribe.utils.lcd.Lazy;

import tribefire.extension.hibernate.graphfetching.HibernateSessionFetchQueryFactory;
import tribefire.extension.hibernate.graphfetching.sql.source.HibernateSqlFetchFrom;
import tribefire.extension.hibernate.graphfetching.sql.source.HibernateSqlFetchJoin;
import tribefire.extension.hibernate.graphfetching.sql.source.RsProperty;
import tribefire.extension.hibernate.graphfetching.sql.source.SelectSegment;

public class HibernateSqlFetchQuery implements FetchQuery {
	private static final Logger logger = Logger.getLogger(HibernateSqlFetchQuery.class);
	private int posSeq = 0;
	private int segmentPosSeq = 0;
	private int aliasSeq = 0;
	private final HibernateSqlFetchFrom from;
	private Lazy<Pair<CharSequence, CharSequence>> sqlLazy = new Lazy<>(this::buildSql);
	private List<HibernateSqlFetchJoin> joins = new ArrayList<>();
	private final String defaultPartition;
	
	private List<SelectSegment> selectSegments = new ArrayList<SelectSegment>();
	private HibernateSessionFetchQueryFactory factory;

	public HibernateSqlFetchQuery(HibernateSessionFetchQueryFactory factory, EntityType<?> entityType, String defaultPartition) {
		super();
		this.factory = factory;
		this.defaultPartition = defaultPartition;
		this.from = new HibernateSqlFetchFrom(factory, this, entityType);
	}
	
	public String defaultPartition() {
		return defaultPartition;
	}
	
	public void addSelectSegment(SelectSegment segment) {
		selectSegments.add(segment);
	}
	
	@Override
	public FetchSource from() {
		return from;
	}

	@Override
	public FetchResults fetchFor(Set<Object> entityIds) {
		Pair<CharSequence, CharSequence> sqlFragments = sqlLazy.get();
		Connection connection = null; 
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		StringBuilder cb = new StringBuilder();
		
		cb.append(sqlFragments.first());
		
		cb.append(" where ");
		cb.append(from.name);
		cb.append('.');
		cb.append(from.pkColumn());
		cb.append(" in (");
			int idCount = entityIds.size();
			for (int i = 0; i < idCount; i++) {
				if (i == 0)
					cb.append('?');
				else
					cb.append(",?");
			}
		cb.append(")");
		
		cb.append(sqlFragments.second());
		
		String sql = cb.toString();
		
		try 
		{
			connection = factory.getConnection();
			ps = connection.prepareStatement(sql);
			
			int i = 1;
			for (Object id: entityIds)
				ps.setObject(i++, id);
			
			rs = ps.executeQuery();
			
			return new HibernateSqlFetchResults(connection, ps, rs, selectSegments);
		}
		catch (Throwable e) {
			Closeables.close(rs, ps, connection);
			throw Exceptions.unchecked(e);
		}
	}
	
	@Override
	public String stringify() {
		StringBuilder b = new StringBuilder();
		Pair<CharSequence, CharSequence> pair = sqlLazy.get();
		b.append(pair.first());
		b.append(" where ");
		b.append(from.name);
		b.append('.');
		b.append(from.pkColumn());
		b.append(" in (?)");
		b.append(pair.second());
		return b.toString();
	}

	public int nextPos() {
		return posSeq++;
	}
	
	public int nextSegmentPos() {
		return segmentPosSeq++;
	}
	
	public String nextAlias() {
		return "a" + aliasSeq++;
	}
	
	public void addJoin(HibernateSqlFetchJoin join) {
		joins.add(join);
	}
	
	private Pair<CharSequence, CharSequence> buildSql() {
		StringBuilder b = new StringBuilder();
		
		b.append("select ");
		
		boolean firstSelect = true;

		for (SelectSegment segment: selectSegments) {
			String alias = segment.alias();
			for (RsProperty property: segment.selectProperties()) {
				if (firstSelect)
					firstSelect = false;
				else
					b.append(", ");
					
				b.append(alias);
				b.append('.');
				b.append(property.columnName());
			}
		}
		
		b.append(" from ");
		b.append(from.tableName());
		b.append(" ");
		b.append(from.name);
		
		String orderAlias = null;
		String orderColumn = null;
		
		for (HibernateSqlFetchJoin join: joins) {
			HibernateCollectionOracle collectionOracle = join.isCollectionJoin();

			final String elementsAlias;
			
			if (collectionOracle != null) {
				if (collectionOracle.hasIntermediateJoinTable()) {
					String joinAlias = join.linkAlias();
					elementsAlias = joinAlias;
					
					HibernateSqlFetchJoin aJoin = join.hasAssociatorJoin();
					
					join(b, join.left, join.source.name, join.source.pkColumn(), collectionOracle.joinTableName(), joinAlias, collectionOracle.ownerColumn());
					
					if (aJoin != null)
						join(b, true, joinAlias, collectionOracle.keyColumn(), aJoin.tableName(), aJoin.name, aJoin.pkColumn());

					join(b, true, joinAlias, collectionOracle.elementColumn(), join.tableName(), join.name, join.pkColumn());
				}
				else {
					HibernateSqlFetchJoin aJoin = join.hasAssociatorJoin();
					
					elementsAlias = join.name;
					join(b, join.left, join.source.name, join.source.pkColumn(), collectionOracle.joinTableName(), join.name, collectionOracle.ownerColumn());

					if (aJoin != null)
						join(b, true, join.name, collectionOracle.keyColumn(), aJoin.tableName(), aJoin.name, aJoin.pkColumn());
				}
				
				if (join.orderByListIndex) {
					orderAlias = elementsAlias;
					orderColumn = collectionOracle.indexColumn();
				}
			}
			else {
				join(b, join.left, join.source.name, join.column(), join.tableName(), join.name, join.pkColumn());
			}
		}
		
		StringBuilder ob = new StringBuilder();
		
		if (orderAlias != null) {
			ob.append(" order by ");
			ob.append(from.name);
			ob.append('.');
			ob.append(from.pkColumn());
			ob.append(" asc, ");
			ob.append(orderAlias);
			ob.append('.');
			ob.append(orderColumn);
			ob.append(" asc");
		}
		
		return Pair.of(b, ob);
	}
	
	private void join(StringBuilder b, boolean left, String onAlias, String onColumn, String tableName, String toAlias, String toColumn) {
		b.append(left? " left join ": " join ");
		b.append(tableName);
		b.append(' ');
		b.append(toAlias);
		b.append(" on ");
		b.append(onAlias);
		b.append('.');
		b.append(onColumn);
		b.append('=');
		b.append(toAlias);
		b.append('.');
		b.append(toColumn);
	}
}
