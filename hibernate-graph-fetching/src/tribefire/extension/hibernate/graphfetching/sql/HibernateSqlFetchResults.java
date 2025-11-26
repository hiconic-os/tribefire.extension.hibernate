package tribefire.extension.hibernate.graphfetching.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.braintribe.gm.graphfetching.api.query.FetchResults;

import tribefire.extension.hibernate.graphfetching.sql.source.SelectSegment;

public class HibernateSqlFetchResults implements FetchResults {
	private List<SelectSegment> segments;
	private ResultSet rs;
	private Object row[];
	private PreparedStatement ps;
	private Connection connection;
	
	public HibernateSqlFetchResults(Connection connection, PreparedStatement ps, ResultSet rs, List<SelectSegment> segments) {
		this.connection = connection;
		this.ps = ps;
		this.rs = rs;
		this.segments = segments;
		this.row = new Object[segments.size()];
	}

	@Override
	public boolean next() {
		try {
			if (rs.next()) {
				hydrate();
				return true;
			}
			else {
				row = null;
				return false;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <V> V get(int col) {
		if (row == null)
			throw new IllegalStateException("Either you miss a next() call or you at the end of the results");
		
		return (V) row[col];
	}
	
	public void hydrate() throws SQLException {
		int pos = 0;
		for (SelectSegment segment: segments) {
			final Object value = segment.get(rs);
			row[pos++] = value;
		}
	}
	
	@Override
	public void close() {
		Closeables.close(rs, ps, connection);
	}
}
