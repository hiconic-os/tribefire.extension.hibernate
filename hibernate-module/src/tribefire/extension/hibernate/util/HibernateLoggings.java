package tribefire.extension.hibernate.util;

import com.braintribe.model.access.hibernate.HibernateLogging;
import com.braintribe.utils.logging.LogLevels;

public interface HibernateLoggings {
	static HibernateLogging convert(com.braintribe.model.accessdeployment.hibernate.HibernateLogging logging) {
		if (logging == null)
			return null;
		
		return new HibernateLogging( //
				LogLevels.convert(logging.getLogLevel()), //
				logging.getLogGMStatements(), //
				logging.getLogHQLStatements(), //
				logging.getLogSQLStatements(), //
				logging.getEnrichSQLParameters(), //
				logging.getLogTimings(), //
				logging.getLogStatistics() //
		);
	}
}
