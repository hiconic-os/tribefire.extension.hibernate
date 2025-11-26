package tribefire.extension.hibernate.graphfetching.sql.source;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public interface SelectSegment {
	String alias();
	Collection<RsProperty> selectProperties();
	Object get(ResultSet rs) throws SQLException;
}
