package tribefire.extension.hibernate.graphfetching.sql;

import java.util.List;

public record HibernateEntityHierarchyOracle(String discriminatorColumn, List<HibernatePropertyOracle> scalarProperties) {

}
