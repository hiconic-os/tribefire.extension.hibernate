package tribefire.extension.hibernate.graphfetching.sql;

import java.util.List;
import java.util.Map;

public record HibernateEntityHierarchyOracle(
		String discriminatorColumn, 
		List<HibernatePropertyOracle> scalarProperties, 
		Map<String, HibernatePolymorphicEntityOracle> polymorphicOracles) {
}
