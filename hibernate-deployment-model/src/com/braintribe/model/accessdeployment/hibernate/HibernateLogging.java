// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.model.accessdeployment.hibernate;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.annotation.meta.Name;
import com.braintribe.model.generic.annotation.meta.Priority;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.logging.LogLevel;

public interface HibernateLogging extends GenericEntity {

	EntityType<HibernateLogging> T = EntityTypes.T(HibernateLogging.class);

	String logLevel = "logLevel";
	String logGMStatements = "logGMStatements";
	String logHQLStatements = "logHQLStatements";
	String logSQLStatements = "logSQLStatements";
	String enrichSQLParameters = "enrichSQLParameters";
	String logTimings = "logTimings";
	String logStatistics = "logStatistics";

	@Mandatory
	@Initializer("enum(com.braintribe.model.logging.LogLevel,DEBUG)")
	@Name("Log Level")
	@Description("Sets the logLevel to be used for logging Hibernate related information")
	@Priority(1.0)
	LogLevel getLogLevel();
	void setLogLevel(LogLevel logLevel);

	@Mandatory
	@Initializer("true")
	@Name("Log GM Statements")
	@Description("Log Generic Model (GM) statements")
	boolean getLogGMStatements();
	void setLogGMStatements(boolean logGMStatements);

	@Mandatory
	@Initializer("false")
	@Name("Log HQL Statements")
	@Description("Log Hibernate Query Language (HQL) statements")
	boolean getLogHQLStatements();
	void setLogHQLStatements(boolean logHQLStatements);

	@Mandatory
	@Initializer("true")
	@Name("Log SQL Statements")
	@Description("Log Structured Query Language (SQL) statements")
	boolean getLogSQLStatements();
	void setLogSQLStatements(boolean logSQLStatements);

	@Mandatory
	@Initializer("true")
	@Name("Enrich SQL Parameters")
	@Description("Enrich Structured Query Language (SQL) parameters")
	boolean getEnrichSQLParameters();
	void setEnrichSQLParameters(boolean enrichSQLParameters);

	@Mandatory
	@Initializer("false")
	@Name("Log Timings")
	@Description("Log Timings of Hibernate Execution")
	boolean getLogTimings();
	void setLogTimings(boolean logTimings);

	@Mandatory
	@Initializer("false")
	@Name("Log Statistics")
	@Description("Log Hibernate Statistics")
	boolean getLogStatistics();
	void setLogStatistics(boolean logStatistics);

}
