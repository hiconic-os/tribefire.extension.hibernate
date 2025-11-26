package tribefire.extension.hibernate.graphfetching;

import java.util.List;

import com.braintribe.gm.graphfetching.api.query.FetchResults;

public class HibernateSessionFetchResults implements FetchResults {
	private final List<Object[]> rows;
	private final int size;
	private int i = 0;
	private Object[] row;
	private HibernateSessionFetchQuery query;
	
	public HibernateSessionFetchResults(List<Object[]> rows, HibernateSessionFetchQuery query) {
		this.rows = rows;
		this.query = query;
		this.size = rows.size();
	}

	@Override
	public boolean next() {
		if (i < size) {
			row = rows.get(i);
			query.postProcess(row);
			i++;
			return true;
		}
		else {
			row = null;
			return false;
		}
	}

	@Override
	public <V> V get(int col) {
		if (row == null)
			throw new IllegalStateException("Either you miss a next() call or you at the end of the results");
		
		return (V) row[col];
	}

	@Override
	public void close() {
		// nothing to do here
	}
}
