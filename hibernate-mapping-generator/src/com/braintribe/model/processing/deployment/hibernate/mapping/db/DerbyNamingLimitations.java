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
package com.braintribe.model.processing.deployment.hibernate.mapping.db;

import com.braintribe.model.processing.deployment.hibernate.mapping.HbmXmlGenerationContext;

public class DerbyNamingLimitations extends NamingLimitations {
	
	DerbyNamingLimitations(HbmXmlGenerationContext context) {
		super(context);
		setTableNameMaxLength(128);
		setColumnNameMaxLength(128);
		//setColumnNameIllegalLeadingCharsPattern("^_+");
		setupReservedWords();
	}

	/**
	 * @see <a href="http://db.apache.org/derby/docs/10.1/ref/rrefkeywords29722.html">Apache Derby DB v10 Documentation</a>
	 */
	private void setupReservedWords() {
		registerReserved("add");
		registerReserved("all");
		registerReserved("allocate");
		registerReserved("alter");
		registerReserved("and");
		registerReserved("any");
		registerReserved("are");
		registerReserved("as");
		registerReserved("asc");
		registerReserved("assertion");
		registerReserved("at");
		registerReserved("authorization");
		registerReserved("avg");
		registerReserved("begin");
		registerReserved("between");
		registerReserved("bit");
		registerReserved("boolean");
		registerReserved("both");
		registerReserved("by");
		registerReserved("call");
		registerReserved("cascade");
		registerReserved("cascaded");
		registerReserved("case");
		registerReserved("cast");
		registerReserved("char");
		registerReserved("character");
		registerReserved("check");
		registerReserved("close");
		registerReserved("collate");
		registerReserved("collation");
		registerReserved("column");
		registerReserved("commit");
		registerReserved("connect");
		registerReserved("connection");
		registerReserved("constraint");
		registerReserved("constraints");
		registerReserved("continue");
		registerReserved("convert");
		registerReserved("corresponding");
		registerReserved("count");
		registerReserved("create");
		registerReserved("current");
		registerReserved("current_date");
		registerReserved("current_time");
		registerReserved("current_timestamp");
		registerReserved("current_user");
		registerReserved("cursor");
		registerReserved("deallocate");
		registerReserved("dec");
		registerReserved("decimal");
		registerReserved("declare");
		registerReserved("deferrable");
		registerReserved("deferred");
		registerReserved("delete");
		registerReserved("desc");
		registerReserved("describe");
		registerReserved("diagnostics");
		registerReserved("disconnect");
		registerReserved("distinct");
		registerReserved("double");
		registerReserved("drop");
		registerReserved("else");
		registerReserved("end");
		registerReserved("endexec");
		registerReserved("escape");
		registerReserved("except");
		registerReserved("exception");
		registerReserved("exec");
		registerReserved("execute");
		registerReserved("exists");
		registerReserved("explain");
		registerReserved("external");
		registerReserved("false");
		registerReserved("fetch");
		registerReserved("first");
		registerReserved("float");
		registerReserved("for");
		registerReserved("foreign");
		registerReserved("found");
		registerReserved("from");
		registerReserved("full");
		registerReserved("function");
		registerReserved("get");
		registerReserved("get_current_connection");
		registerReserved("global");
		registerReserved("go");
		registerReserved("goto");
		registerReserved("grant");
		registerReserved("group");
		registerReserved("having");
		registerReserved("hour");
		registerReserved("identity");
		registerReserved("immediate");
		registerReserved("in");
		registerReserved("indicator");
		registerReserved("initially");
		registerReserved("inner");
		registerReserved("inout");
		registerReserved("input");
		registerReserved("insensitive");
		registerReserved("insert");
		registerReserved("int");
		registerReserved("integer");
		registerReserved("intersect");
		registerReserved("into");
		registerReserved("is");
		registerReserved("isolation");
		registerReserved("join");
		registerReserved("key");
		registerReserved("last");
		registerReserved("left");
		registerReserved("like");
		registerReserved("longint");
		registerReserved("lower");
		registerReserved("ltrim");
		registerReserved("match");
		registerReserved("max");
		registerReserved("min");
		registerReserved("minute");
		registerReserved("national");
		registerReserved("natural");
		registerReserved("nchar");
		registerReserved("nvarchar");
		registerReserved("next");
		registerReserved("no");
		registerReserved("not");
		registerReserved("null");
		registerReserved("nullif");
		registerReserved("numeric");
		registerReserved("of");
		registerReserved("on");
		registerReserved("only");
		registerReserved("open");
		registerReserved("option");
		registerReserved("or");
		registerReserved("order");
		registerReserved("out");
		registerReserved("outer");
		registerReserved("output");
		registerReserved("overlaps");
		registerReserved("pad");
		registerReserved("partial");
		registerReserved("prepare");
		registerReserved("preserve");
		registerReserved("primary");
		registerReserved("prior");
		registerReserved("privileges");
		registerReserved("procedure");
		registerReserved("public");
		registerReserved("read");
		registerReserved("real");
		registerReserved("references");
		registerReserved("relative");
		registerReserved("restrict");
		registerReserved("revoke");
		registerReserved("right");
		registerReserved("rollback");
		registerReserved("rows");
		registerReserved("rtrim");
		registerReserved("schema");
		registerReserved("scroll");
		registerReserved("second");
		registerReserved("select");
		registerReserved("session_user");
		registerReserved("set");
		registerReserved("smallint");
		registerReserved("some");
		registerReserved("space");
		registerReserved("sql");
		registerReserved("sqlcode");
		registerReserved("sqlerror");
		registerReserved("sqlstate");
		registerReserved("substr");
		registerReserved("substring");
		registerReserved("sum");
		registerReserved("system_user");
		registerReserved("table");
		registerReserved("temporary");
		registerReserved("timezone_hour");
		registerReserved("timezone_minute");
		registerReserved("to");
		registerReserved("trailing");
		registerReserved("transaction");
		registerReserved("translate");
		registerReserved("translation");
		registerReserved("true");
		registerReserved("union");
		registerReserved("unique");
		registerReserved("unknown");
		registerReserved("update");
		registerReserved("upper");
		registerReserved("user");
		registerReserved("using");
		registerReserved("values");
		registerReserved("varchar");
		registerReserved("varying");
		registerReserved("view");
		registerReserved("whenever");
		registerReserved("where");
		registerReserved("with");
		registerReserved("work");
		registerReserved("write");
		registerReserved("xml");
		registerReserved("xmlexists");
		registerReserved("xmlparse");
		registerReserved("xmlserialize");
		registerReserved("year");
	}
}
