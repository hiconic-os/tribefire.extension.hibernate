package tribefire.extension.hibernate.graphfetching.sql;

import org.hibernate.type.Type;

import com.braintribe.model.generic.reflection.Property;

public record PropertyIdentTuple(
		Property property,
		String tableName,
		String columnName,
		Type type
) {}
