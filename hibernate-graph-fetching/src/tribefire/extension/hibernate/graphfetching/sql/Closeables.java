package tribefire.extension.hibernate.graphfetching.sql;

import com.braintribe.logging.Logger;

public class Closeables {
	private static final Logger logger = Logger.getLogger(HibernateSqlFetchQuery.class);
	
	public static void close(AutoCloseable... closables) {
		for (AutoCloseable closable: closables) {
			try {
				if (closable != null)
					closable.close();
			} catch (Exception e) {
				logger.error("Error while closing: " + closable);
			}
		}
	}

}
