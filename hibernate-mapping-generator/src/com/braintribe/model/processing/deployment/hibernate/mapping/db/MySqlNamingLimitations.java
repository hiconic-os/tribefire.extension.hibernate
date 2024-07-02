// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.model.processing.deployment.hibernate.mapping.db;

import com.braintribe.model.processing.deployment.hibernate.mapping.HbmXmlGenerationContext;

public class MySqlNamingLimitations extends NamingLimitations {

	MySqlNamingLimitations(HbmXmlGenerationContext context) {
		super(context);
		setTableNameMaxLength(64);
		setColumnNameMaxLength(64);
		//setColumnNameIllegalLeadingCharsPattern("^_+");
		setupReservedWords();
	}

	/**
	 * @see <a href="http://dev.mysql.com/doc/mysqld-version-reference/en/mysqld-version-reference-reservedwords-5-7.html">MySQL 5.7 documentation</a>
	 */
	private void setupReservedWords() {
		registerReserved("accessible");
		registerReserved("add");
		registerReserved("all");
		registerReserved("alter");
		registerReserved("analyze");
		registerReserved("and");
		registerReserved("as");
		registerReserved("asc");
		registerReserved("asensitive");
		registerReserved("before");
		registerReserved("between");
		registerReserved("bigint");
		registerReserved("binary");
		registerReserved("blob");
		registerReserved("both");
		registerReserved("by");
		registerReserved("call");
		registerReserved("cascade");
		registerReserved("case");
		registerReserved("change");
		registerReserved("char");
		registerReserved("character");
		registerReserved("check");
		registerReserved("collate");
		registerReserved("column");
		registerReserved("condition");
		registerReserved("constraint");
		registerReserved("continue");
		registerReserved("convert");
		registerReserved("create");
		registerReserved("cross");
		registerReserved("current_date");
		registerReserved("current_time");
		registerReserved("current_timestamp");
		registerReserved("current_user");
		registerReserved("cursor");
		registerReserved("database");
		registerReserved("databases");
		registerReserved("day_hour");
		registerReserved("day_microsecond");
		registerReserved("day_minute");
		registerReserved("day_second");
		registerReserved("dec");
		registerReserved("decimal");
		registerReserved("declare");
		registerReserved("default");
		registerReserved("delayed");
		registerReserved("delete");
		registerReserved("desc");
		registerReserved("describe");
		registerReserved("deterministic");
		registerReserved("distinct");
		registerReserved("distinctrow");
		registerReserved("div");
		registerReserved("double");
		registerReserved("drop");
		registerReserved("dual");
		registerReserved("each");
		registerReserved("else");
		registerReserved("elseif");
		registerReserved("enclosed");
		registerReserved("escaped");
		registerReserved("exists");
		registerReserved("exit");
		registerReserved("explain");
		registerReserved("false");
		registerReserved("fetch");
		registerReserved("float");
		registerReserved("float4");
		registerReserved("float8");
		registerReserved("for");
		registerReserved("force");
		registerReserved("foreign");
		registerReserved("from");
		registerReserved("fulltext");
		registerReserved("get");
		registerReserved("grant");
		registerReserved("group");
		registerReserved("having");
		registerReserved("high_priority");
		registerReserved("hour_microsecond");
		registerReserved("hour_minute");
		registerReserved("hour_second");
		registerReserved("if");
		registerReserved("ignore");
		registerReserved("in");
		registerReserved("index");
		registerReserved("infile");
		registerReserved("inner");
		registerReserved("inout");
		registerReserved("insensitive");
		registerReserved("insert");
		registerReserved("int");
		registerReserved("int1");
		registerReserved("int2");
		registerReserved("int3");
		registerReserved("int4");
		registerReserved("int8");
		registerReserved("integer");
		registerReserved("interval");
		registerReserved("into");
		registerReserved("io_after_gtids");
		registerReserved("io_before_gtids");
		registerReserved("is");
		registerReserved("iterate");
		registerReserved("join");
		registerReserved("key");
		registerReserved("keys");
		registerReserved("kill");
		registerReserved("leading");
		registerReserved("leave");
		registerReserved("left");
		registerReserved("like");
		registerReserved("limit");
		registerReserved("linear");
		registerReserved("lines");
		registerReserved("load");
		registerReserved("localtime");
		registerReserved("localtimestamp");
		registerReserved("lock");
		registerReserved("long");
		registerReserved("longblob");
		registerReserved("longtext");
		registerReserved("loop");
		registerReserved("low_priority");
		registerReserved("master_bind");
		registerReserved("master_ssl_verify_server_cert");
		registerReserved("match");
		registerReserved("maxvalue");
		registerReserved("mediumblob");
		registerReserved("mediumint");
		registerReserved("mediumtext");
		registerReserved("middleint");
		registerReserved("minute_microsecond");
		registerReserved("minute_second");
		registerReserved("mod");
		registerReserved("modifies");
		registerReserved("natural");
		registerReserved("nonblocking");
		registerReserved("not");
		registerReserved("no_write_to_binlog");
		registerReserved("null");
		registerReserved("numeric");
		registerReserved("on");
		registerReserved("optimize");
		registerReserved("option");
		registerReserved("optionally");
		registerReserved("or");
		registerReserved("order");
		registerReserved("out");
		registerReserved("outer");
		registerReserved("outfile");
		registerReserved("partition");
		registerReserved("precision");
		registerReserved("primary");
		registerReserved("procedure");
		registerReserved("purge");
		registerReserved("range");
		registerReserved("read");
		registerReserved("reads");
		registerReserved("read_write");
		registerReserved("real");
		registerReserved("references");
		registerReserved("regexp");
		registerReserved("release");
		registerReserved("rename");
		registerReserved("repeat");
		registerReserved("replace");
		registerReserved("require");
		registerReserved("resignal");
		registerReserved("restrict");
		registerReserved("return");
		registerReserved("revoke");
		registerReserved("right");
		registerReserved("rlike");
		registerReserved("schema");
		registerReserved("schemas");
		registerReserved("second_microsecond");
		registerReserved("select");
		registerReserved("sensitive");
		registerReserved("separator");
		registerReserved("set");
		registerReserved("show");
		registerReserved("signal");
		registerReserved("smallint");
		registerReserved("spatial");
		registerReserved("specific");
		registerReserved("sql");
		registerReserved("sqlexception");
		registerReserved("sqlstate");
		registerReserved("sqlwarning");
		registerReserved("sql_big_result");
		registerReserved("sql_calc_found_rows");
		registerReserved("sql_small_result");
		registerReserved("ssl");
		registerReserved("starting");
		registerReserved("straight_join");
		registerReserved("table");
		registerReserved("terminated");
		registerReserved("then");
		registerReserved("tinyblob");
		registerReserved("tinyint");
		registerReserved("tinytext");
		registerReserved("to");
		registerReserved("trailing");
		registerReserved("trigger");
		registerReserved("true");
		registerReserved("undo");
		registerReserved("union");
		registerReserved("unique");
		registerReserved("unlock");
		registerReserved("unsigned");
		registerReserved("update");
		registerReserved("usage");
		registerReserved("use");
		registerReserved("using");
		registerReserved("utc_date");
		registerReserved("utc_time");
		registerReserved("utc_timestamp");
		registerReserved("values");
		registerReserved("varbinary");
		registerReserved("varchar");
		registerReserved("varcharacter");
		registerReserved("varying");
		registerReserved("when");
		registerReserved("where");
		registerReserved("while");
		registerReserved("with");
		registerReserved("write");
		registerReserved("xor");
		registerReserved("year_month");
		registerReserved("zerofill");
	}

}
