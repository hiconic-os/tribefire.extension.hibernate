package tribefire.extension.hibernate.graphfetching.sql.source;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

public class ScalarSegment implements SelectSegment {
	private final RsProperty selectProperty;
	private final String alias;
	
	public ScalarSegment(String alias, RsProperty selectProperty) {
		super();
		this.selectProperty = selectProperty;
		this.alias = alias;
	}
	
	@Override
	public Object get(ResultSet rs) throws SQLException {
		Object id = selectProperty.getValue(rs);
		return id;
	}
	
	@Override
	public Collection<RsProperty> selectProperties() {
		return Collections.singletonList(selectProperty);
	}

	@Override
	public String alias() {
		return alias;
	}
}