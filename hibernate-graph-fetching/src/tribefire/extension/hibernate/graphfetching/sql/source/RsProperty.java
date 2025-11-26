package tribefire.extension.hibernate.graphfetching.sql.source;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.braintribe.model.generic.reflection.Property;

import tribefire.extension.hibernate.graphfetching.sql.HibernatePropertyOracle;

public record RsProperty(int pos, String columnName, Property property, ResultValueExtractor extractor) {
	public RsProperty(int pos, HibernatePropertyOracle oracle) {
		this(pos, oracle.columnName(), oracle.property(), oracle.extractor());
	}
	
	public Object getValue(ResultSet rs) throws SQLException {
		return extractor.getValue(rs, pos + 1);
	}
}