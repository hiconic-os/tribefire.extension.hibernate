package com.braintribe.model.access.hibernate;

import com.braintribe.logging.Logger.LogLevel;

public record HibernateLogging(
		LogLevel logLevel, 
		boolean logGMStatements, 
		boolean logHQLStatements, 
		boolean logSQLStatements, 
		boolean enrichSQLParameters,
		boolean logTimings,
		boolean logStatistics
) {
}
