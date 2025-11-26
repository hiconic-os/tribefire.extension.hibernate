package tribefire.extension.hibernate.graphfetching.sql;

import org.hibernate.type.Type;

public record PropertyIdentTuple(
		String propertyName,
		String tableName,
		String columnName,
		Type type
) {}
