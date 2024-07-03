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

public class SapDbNamingLimitations extends NamingLimitations {

	SapDbNamingLimitations(HbmXmlGenerationContext context) {
		super(context);
		setTableNameMaxLength(32);
		setColumnNameMaxLength(32);
		//setColumnNameIllegalLeadingCharsPattern("^_+");
		setupReservedWords();
	}

	private void setupReservedWords() {
		registerReserved("a");
		registerReserved("abort");
		registerReserved("abs");
		registerReserved("absolute");
		registerReserved("access");
		registerReserved("action");
		registerReserved("ada");
		registerReserved("add");
		registerReserved("admin");
		registerReserved("after");
		registerReserved("aggregate");
		registerReserved("alias");
		registerReserved("all");
		registerReserved("allocate");
		registerReserved("also");
		registerReserved("alter");
		registerReserved("always");
		registerReserved("analyse");
		registerReserved("analyze");
		registerReserved("and");
		registerReserved("any");
		registerReserved("are");
		registerReserved("array");
		registerReserved("as");
		registerReserved("asc");
		registerReserved("asensitive");
		registerReserved("assertion");
		registerReserved("assignment");
		registerReserved("asymmetric");
		registerReserved("at");
		registerReserved("atomic");
		registerReserved("attribute");
		registerReserved("attributes");
		registerReserved("audit");
		registerReserved("authorization");
		registerReserved("auto_increment");
		registerReserved("avg");
		registerReserved("avg_row_length");
		registerReserved("backup");
		registerReserved("backward");
		registerReserved("before");
		registerReserved("begin");
		registerReserved("bernoulli");
		registerReserved("between");
		registerReserved("bigint");
		registerReserved("binary");
		registerReserved("bit");
		registerReserved("bit_length");
		registerReserved("bitvar");
		registerReserved("blob");
		registerReserved("bool");
		registerReserved("boolean");
		registerReserved("both");
		registerReserved("breadth");
		registerReserved("break");
		registerReserved("browse");
		registerReserved("bulk");
		registerReserved("by");
		registerReserved("c");
		registerReserved("cache");
		registerReserved("call");
		registerReserved("called");
		registerReserved("cardinality");
		registerReserved("cascade");
		registerReserved("cascaded");
		registerReserved("case");
		registerReserved("cast");
		registerReserved("catalog");
		registerReserved("catalog_name");
		registerReserved("ceil");
		registerReserved("ceiling");
		registerReserved("chain");
		registerReserved("change");
		registerReserved("char");
		registerReserved("char_length");
		registerReserved("character");
		registerReserved("character_length");
		registerReserved("character_set_catalog");
		registerReserved("character_set_name");
		registerReserved("character_set_schema");
		registerReserved("characteristics");
		registerReserved("characters");
		registerReserved("check");
		registerReserved("checked");
		registerReserved("checkpoint");
		registerReserved("checksum");
		registerReserved("class");
		registerReserved("class_origin");
		registerReserved("clob");
		registerReserved("close");
		registerReserved("cluster");
		registerReserved("clustered");
		registerReserved("coalesce");
		registerReserved("cobol");
		registerReserved("collate");
		registerReserved("collation");
		registerReserved("collation_catalog");
		registerReserved("collation_name");
		registerReserved("collation_schema");
		registerReserved("collect");
		registerReserved("column");
		registerReserved("column_name");
		registerReserved("columns");
		registerReserved("command_function");
		registerReserved("command_function_code");
		registerReserved("comment");
		registerReserved("commit");
		registerReserved("committed");
		registerReserved("completion");
		registerReserved("compress");
		registerReserved("compute");
		registerReserved("condition");
		registerReserved("condition_number");
		registerReserved("connect");
		registerReserved("connection");
		registerReserved("connection_name");
		registerReserved("constraint");
		registerReserved("constraint_catalog");
		registerReserved("constraint_name");
		registerReserved("constraint_schema");
		registerReserved("constraints");
		registerReserved("constructor");
		registerReserved("contains");
		registerReserved("containstable");
		registerReserved("continue");
		registerReserved("conversion");
		registerReserved("convert");
		registerReserved("copy");
		registerReserved("corr");
		registerReserved("corresponding");
		registerReserved("count");
		registerReserved("covar_pop");
		registerReserved("covar_samp");
		registerReserved("create");
		registerReserved("createdb");
		registerReserved("createrole");
		registerReserved("createuser");
		registerReserved("cross");
		registerReserved("csv");
		registerReserved("cube");
		registerReserved("cume_dist");
		registerReserved("current");
		registerReserved("current_date");
		registerReserved("current_default_transform_group");
		registerReserved("current_path");
		registerReserved("current_role");
		registerReserved("current_time");
		registerReserved("current_timestamp");
		registerReserved("current_transform_group_for_type");
		registerReserved("current_user");
		registerReserved("cursor");
		registerReserved("cursor_name");
		registerReserved("cycle");
		registerReserved("data");
		registerReserved("database");
		registerReserved("databases");
		registerReserved("date");
		registerReserved("datetime");
		registerReserved("datetime_interval_code");
		registerReserved("datetime_interval_precision");
		registerReserved("day");
		registerReserved("day_hour");
		registerReserved("day_microsecond");
		registerReserved("day_minute");
		registerReserved("day_second");
		registerReserved("dayofmonth");
		registerReserved("dayofweek");
		registerReserved("dayofyear");
		registerReserved("dbcc");
		registerReserved("deallocate");
		registerReserved("dec");
		registerReserved("decimal");
		registerReserved("declare");
		registerReserved("default");
		registerReserved("defaults");
		registerReserved("deferrable");
		registerReserved("deferred");
		registerReserved("defined");
		registerReserved("definer");
		registerReserved("degree");
		registerReserved("delay_key_write");
		registerReserved("delayed");
		registerReserved("delete");
		registerReserved("delimiter");
		registerReserved("delimiters");
		registerReserved("dense_rank");
		registerReserved("deny");
		registerReserved("depth");
		registerReserved("deref");
		registerReserved("derived");
		registerReserved("desc");
		registerReserved("describe");
		registerReserved("descriptor");
		registerReserved("destroy");
		registerReserved("destructor");
		registerReserved("deterministic");
		registerReserved("diagnostics");
		registerReserved("dictionary");
		registerReserved("disable");
		registerReserved("disconnect");
		registerReserved("disk");
		registerReserved("dispatch");
		registerReserved("distinct");
		registerReserved("distinctrow");
		registerReserved("distributed");
		registerReserved("div");
		registerReserved("do");
		registerReserved("domain");
		registerReserved("double");
		registerReserved("drop");
		registerReserved("dual");
		registerReserved("dummy");
		registerReserved("dump");
		registerReserved("dynamic");
		registerReserved("dynamic_function");
		registerReserved("dynamic_function_code");
		registerReserved("each");
		registerReserved("element");
		registerReserved("else");
		registerReserved("elseif");
		registerReserved("enable");
		registerReserved("enclosed");
		registerReserved("encoding");
		registerReserved("encrypted");
		registerReserved("end");
		registerReserved("end-exec");
		registerReserved("enum");
		registerReserved("equals");
		registerReserved("errlvl");
		registerReserved("escape");
		registerReserved("escaped");
		registerReserved("every");
		registerReserved("except");
		registerReserved("exception");
		registerReserved("exclude");
		registerReserved("excluding");
		registerReserved("exclusive");
		registerReserved("exec");
		registerReserved("execute");
		registerReserved("existing");
		registerReserved("exists");
		registerReserved("exit");
		registerReserved("exp");
		registerReserved("explain");
		registerReserved("external");
		registerReserved("extract");
		registerReserved("false");
		registerReserved("fetch");
		registerReserved("fields");
		registerReserved("file");
		registerReserved("fillfactor");
		registerReserved("filter");
		registerReserved("final");
		registerReserved("first");
		registerReserved("float");
		registerReserved("float4");
		registerReserved("float8");
		registerReserved("floor");
		registerReserved("flush");
		registerReserved("following");
		registerReserved("for");
		registerReserved("force");
		registerReserved("foreign");
		registerReserved("fortran");
		registerReserved("forward");
		registerReserved("found");
		registerReserved("free");
		registerReserved("freetext");
		registerReserved("freetexttable");
		registerReserved("freeze");
		registerReserved("from");
		registerReserved("full");
		registerReserved("fulltext");
		registerReserved("function");
		registerReserved("fusion");
		registerReserved("g");
		registerReserved("general");
		registerReserved("generated");
		registerReserved("get");
		registerReserved("global");
		registerReserved("go");
		registerReserved("goto");
		registerReserved("grant");
		registerReserved("granted");
		registerReserved("grants");
		registerReserved("greatest");
		registerReserved("group");
		registerReserved("grouping");
		registerReserved("handler");
		registerReserved("having");
		registerReserved("header");
		registerReserved("heap");
		registerReserved("hierarchy");
		registerReserved("high_priority");
		registerReserved("hold");
		registerReserved("holdlock");
		registerReserved("host");
		registerReserved("hosts");
		registerReserved("hour");
		registerReserved("hour_microsecond");
		registerReserved("hour_minute");
		registerReserved("hour_second");
		registerReserved("identified");
		registerReserved("identity");
		registerReserved("identity_insert");
		registerReserved("identitycol");
		registerReserved("if");
		registerReserved("ignore");
		registerReserved("ilike");
		registerReserved("immediate");
		registerReserved("immutable");
		registerReserved("implementation");
		registerReserved("implicit");
		registerReserved("in");
		registerReserved("include");
		registerReserved("including");
		registerReserved("increment");
		registerReserved("index");
		registerReserved("indicator");
		registerReserved("infile");
		registerReserved("infix");
		registerReserved("inherit");
		registerReserved("inherits");
		registerReserved("initial");
		registerReserved("initialize");
		registerReserved("initially");
		registerReserved("inner");
		registerReserved("inout");
		registerReserved("input");
		registerReserved("insensitive");
		registerReserved("insert");
		registerReserved("insert_id");
		registerReserved("instance");
		registerReserved("instantiable");
		registerReserved("instead");
		registerReserved("int");
		registerReserved("int1");
		registerReserved("int2");
		registerReserved("int3");
		registerReserved("int4");
		registerReserved("int8");
		registerReserved("integer");
		registerReserved("intersect");
		registerReserved("intersection");
		registerReserved("interval");
		registerReserved("into");
		registerReserved("invoker");
		registerReserved("is");
		registerReserved("isam");
		registerReserved("isnull");
		registerReserved("isolation");
		registerReserved("iterate");
		registerReserved("join");
		registerReserved("k");
		registerReserved("key");
		registerReserved("key_member");
		registerReserved("key_type");
		registerReserved("keys");
		registerReserved("kill");
		registerReserved("lancompiler");
		registerReserved("language");
		registerReserved("large");
		registerReserved("last");
		registerReserved("last_insert_id");
		registerReserved("lateral");
		registerReserved("leading");
		registerReserved("least");
		registerReserved("leave");
		registerReserved("left");
		registerReserved("length");
		registerReserved("less");
		registerReserved("level");
		registerReserved("like");
		registerReserved("limit");
		registerReserved("lineno");
		registerReserved("lines");
		registerReserved("listen");
		registerReserved("ln");
		registerReserved("load");
		registerReserved("local");
		registerReserved("localtime");
		registerReserved("localtimestamp");
		registerReserved("location");
		registerReserved("locator");
		registerReserved("lock");
		registerReserved("login");
		registerReserved("logs");
		registerReserved("long");
		registerReserved("longblob");
		registerReserved("longtext");
		registerReserved("loop");
		registerReserved("low_priority");
		registerReserved("lower");
		registerReserved("m");
		registerReserved("map");
		registerReserved("match");
		registerReserved("matched");
		registerReserved("max");
		registerReserved("max_rows");
		registerReserved("maxextents");
		registerReserved("maxvalue");
		registerReserved("mediumblob");
		registerReserved("mediumint");
		registerReserved("mediumtext");
		registerReserved("member");
		registerReserved("merge");
		registerReserved("message_length");
		registerReserved("message_octet_length");
		registerReserved("message_text");
		registerReserved("method");
		registerReserved("middleint");
		registerReserved("min");
		registerReserved("min_rows");
		registerReserved("minus");
		registerReserved("minute");
		registerReserved("minute_microsecond");
		registerReserved("minute_second");
		registerReserved("minvalue");
		registerReserved("mlslabel");
		registerReserved("mod");
		registerReserved("mode");
		registerReserved("modifies");
		registerReserved("modify");
		registerReserved("module");
		registerReserved("month");
		registerReserved("monthname");
		registerReserved("more");
		registerReserved("move");
		registerReserved("multiset");
		registerReserved("mumps");
		registerReserved("myisam");
		registerReserved("name");
		registerReserved("names");
		registerReserved("national");
		registerReserved("natural");
		registerReserved("nchar");
		registerReserved("nclob");
		registerReserved("nesting");
		registerReserved("new");
		registerReserved("next");
		registerReserved("no");
		registerReserved("no_write_to_binlog");
		registerReserved("noaudit");
		registerReserved("nocheck");
		registerReserved("nocompress");
		registerReserved("nocreatedb");
		registerReserved("nocreaterole");
		registerReserved("nocreateuser");
		registerReserved("noinherit");
		registerReserved("nologin");
		registerReserved("nonclustered");
		registerReserved("none");
		registerReserved("normalize");
		registerReserved("normalized");
		registerReserved("nosuperuser");
		registerReserved("not");
		registerReserved("nothing");
		registerReserved("notify");
		registerReserved("notnull");
		registerReserved("nowait");
		registerReserved("null");
		registerReserved("nullable");
		registerReserved("nullif");
		registerReserved("nulls");
		registerReserved("number");
		registerReserved("numeric");
		registerReserved("object");
		registerReserved("octet_length");
		registerReserved("octets");
		registerReserved("of");
		registerReserved("off");
		registerReserved("offline");
		registerReserved("offset");
		registerReserved("offsets");
		registerReserved("oids");
		registerReserved("old");
		registerReserved("on");
		registerReserved("online");
		registerReserved("only");
		registerReserved("open");
		registerReserved("opendatasource");
		registerReserved("openquery");
		registerReserved("openrowset");
		registerReserved("openxml");
		registerReserved("operation");
		registerReserved("operator");
		registerReserved("optimize");
		registerReserved("option");
		registerReserved("optionally");
		registerReserved("options");
		registerReserved("or");
		registerReserved("order");
		registerReserved("ordering");
		registerReserved("ordinality");
		registerReserved("others");
		registerReserved("out");
		registerReserved("outer");
		registerReserved("outfile");
		registerReserved("output");
		registerReserved("over");
		registerReserved("overlaps");
		registerReserved("overlay");
		registerReserved("overriding");
		registerReserved("owner");
		registerReserved("pack_keys");
		registerReserved("pad");
		registerReserved("parameter");
		registerReserved("parameter_mode");
		registerReserved("parameter_name");
		registerReserved("parameter_ordinal_position");
		registerReserved("parameter_specific_catalog");
		registerReserved("parameter_specific_name");
		registerReserved("parameter_specific_schema");
		registerReserved("parameters");
		registerReserved("partial");
		registerReserved("partition");
		registerReserved("pascal");
		registerReserved("password");
		registerReserved("path");
		registerReserved("pctfree");
		registerReserved("percent");
		registerReserved("percent_rank");
		registerReserved("percentile_cont");
		registerReserved("percentile_disc");
		registerReserved("placing");
		registerReserved("plan");
		registerReserved("pli");
		registerReserved("position");
		registerReserved("postfix");
		registerReserved("power");
		registerReserved("preceding");
		registerReserved("precision");
		registerReserved("prefix");
		registerReserved("preorder");
		registerReserved("prepare");
		registerReserved("prepared");
		registerReserved("preserve");
		registerReserved("primary");
		registerReserved("print");
		registerReserved("prior");
		registerReserved("privileges");
		registerReserved("proc");
		registerReserved("procedural");
		registerReserved("procedure");
		registerReserved("process");
		registerReserved("processlist");
		registerReserved("public");
		registerReserved("purge");
		registerReserved("quote");
		registerReserved("raid0");
		registerReserved("raiserror");
		registerReserved("range");
		registerReserved("rank");
		registerReserved("raw");
		registerReserved("read");
		registerReserved("reads");
		registerReserved("readtext");
		registerReserved("real");
		registerReserved("recheck");
		registerReserved("reconfigure");
		registerReserved("recursive");
		registerReserved("ref");
		registerReserved("references");
		registerReserved("referencing");
		registerReserved("regexp");
		registerReserved("regr_avgx");
		registerReserved("regr_avgy");
		registerReserved("regr_count");
		registerReserved("regr_intercept");
		registerReserved("regr_r2");
		registerReserved("regr_slope");
		registerReserved("regr_sxx");
		registerReserved("regr_sxy");
		registerReserved("regr_syy");
		registerReserved("reindex");
		registerReserved("relative");
		registerReserved("release");
		registerReserved("reload");
		registerReserved("rename");
		registerReserved("repeat");
		registerReserved("repeatable");
		registerReserved("replace");
		registerReserved("replication");
		registerReserved("require");
		registerReserved("reset");
		registerReserved("resignal");
		registerReserved("resource");
		registerReserved("restart");
		registerReserved("restore");
		registerReserved("restrict");
		registerReserved("result");
		registerReserved("return");
		registerReserved("returned_cardinality");
		registerReserved("returned_length");
		registerReserved("returned_octet_length");
		registerReserved("returned_sqlstate");
		registerReserved("returns");
		registerReserved("revoke");
		registerReserved("right");
		registerReserved("rlike");
		registerReserved("role");
		registerReserved("rollback");
		registerReserved("rollup");
		registerReserved("routine");
		registerReserved("routine_catalog");
		registerReserved("routine_name");
		registerReserved("routine_schema");
		registerReserved("row");
		registerReserved("row_count");
		registerReserved("row_number");
		registerReserved("rowcount");
		registerReserved("rowguidcol");
		registerReserved("rowid");
		registerReserved("rownum");
		registerReserved("rows");
		registerReserved("rule");
		registerReserved("save");
		registerReserved("savepoint");
		registerReserved("scale");
		registerReserved("schema");
		registerReserved("schema_name");
		registerReserved("schemas");
		registerReserved("scope");
		registerReserved("scope_catalog");
		registerReserved("scope_name");
		registerReserved("scope_schema");
		registerReserved("scroll");
		registerReserved("search");
		registerReserved("second");
		registerReserved("second_microsecond");
		registerReserved("section");
		registerReserved("security");
		registerReserved("select");
		registerReserved("self");
		registerReserved("sensitive");
		registerReserved("separator");
		registerReserved("sequence");
		registerReserved("serializable");
		registerReserved("server_name");
		registerReserved("session");
		registerReserved("session_user");
		registerReserved("set");
		registerReserved("setof");
		registerReserved("sets");
		registerReserved("setuser");
		registerReserved("share");
		registerReserved("show");
		registerReserved("shutdown");
		registerReserved("signal");
		registerReserved("similar");
		registerReserved("simple");
		registerReserved("size");
		registerReserved("smallint");
		registerReserved("some");
		registerReserved("soname");
		registerReserved("source");
		registerReserved("space");
		registerReserved("spatial");
		registerReserved("specific");
		registerReserved("specific_name");
		registerReserved("specifictype");
		registerReserved("sql");
		registerReserved("sql_big_result");
		registerReserved("sql_big_selects");
		registerReserved("sql_big_tables");
		registerReserved("sql_calc_found_rows");
		registerReserved("sql_log_off");
		registerReserved("sql_log_update");
		registerReserved("sql_low_priority_updates");
		registerReserved("sql_select_limit");
		registerReserved("sql_small_result");
		registerReserved("sql_warnings");
		registerReserved("sqlca");
		registerReserved("sqlcode");
		registerReserved("sqlerror");
		registerReserved("sqlexception");
		registerReserved("sqlstate");
		registerReserved("sqlwarning");
		registerReserved("sqrt");
		registerReserved("ssl");
		registerReserved("stable");
		registerReserved("start");
		registerReserved("starting");
		registerReserved("state");
		registerReserved("statement");
		registerReserved("static");
		registerReserved("statistics");
		registerReserved("status");
		registerReserved("stddev_pop");
		registerReserved("stddev_samp");
		registerReserved("stdin");
		registerReserved("stdout");
		registerReserved("storage");
		registerReserved("straight_join");
		registerReserved("strict");
		registerReserved("string");
		registerReserved("structure");
		registerReserved("style");
		registerReserved("subclass_origin");
		registerReserved("sublist");
		registerReserved("submultiset");
		registerReserved("substring");
		registerReserved("successful");
		registerReserved("sum");
		registerReserved("superuser");
		registerReserved("symmetric");
		registerReserved("synonym");
		registerReserved("sysdate");
		registerReserved("sysid");
		registerReserved("system");
		registerReserved("system_user");
		registerReserved("table");
		registerReserved("table_name");
		registerReserved("tables");
		registerReserved("tablesample");
		registerReserved("tablespace");
		registerReserved("temp");
		registerReserved("template");
		registerReserved("temporary");
		registerReserved("terminate");
		registerReserved("terminated");
		registerReserved("text");
		registerReserved("textsize");
		registerReserved("than");
		registerReserved("then");
		registerReserved("ties");
		registerReserved("time");
		registerReserved("timestamp");
		registerReserved("timezone_hour");
		registerReserved("timezone_minute");
		registerReserved("tinyblob");
		registerReserved("tinyint");
		registerReserved("tinytext");
		registerReserved("to");
		registerReserved("toast");
		registerReserved("top");
		registerReserved("top_level_count");
		registerReserved("trailing");
		registerReserved("tran");
		registerReserved("transaction");
		registerReserved("transaction_active");
		registerReserved("transactions_committed");
		registerReserved("transactions_rolled_back");
		registerReserved("transform");
		registerReserved("transforms");
		registerReserved("translate");
		registerReserved("translation");
		registerReserved("treat");
		registerReserved("trigger");
		registerReserved("trigger_catalog");
		registerReserved("trigger_name");
		registerReserved("trigger_schema");
		registerReserved("trim");
		registerReserved("true");
		registerReserved("truncate");
		registerReserved("trusted");
		registerReserved("tsequal");
		registerReserved("type");
		registerReserved("uescape");
		registerReserved("uid");
		registerReserved("unbounded");
		registerReserved("uncommitted");
		registerReserved("under");
		registerReserved("undo");
		registerReserved("unencrypted");
		registerReserved("union");
		registerReserved("unique");
		registerReserved("unknown");
		registerReserved("unlisten");
		registerReserved("unlock");
		registerReserved("unnamed");
		registerReserved("unnest");
		registerReserved("unsigned");
		registerReserved("until");
		registerReserved("update");
		registerReserved("updatetext");
		registerReserved("upper");
		registerReserved("usage");
		registerReserved("use");
		registerReserved("user");
		registerReserved("user_defined_type_catalog");
		registerReserved("user_defined_type_code");
		registerReserved("user_defined_type_name");
		registerReserved("user_defined_type_schema");
		registerReserved("using");
		registerReserved("utc_date");
		registerReserved("utc_time");
		registerReserved("utc_timestamp");
		registerReserved("vacuum");
		registerReserved("valid");
		registerReserved("validate");
		registerReserved("validator");
		registerReserved("value");
		registerReserved("values");
		registerReserved("var_pop");
		registerReserved("var_samp");
		registerReserved("varbinary");
		registerReserved("varchar");
		registerReserved("varchar2");
		registerReserved("varcharacter");
		registerReserved("variable");
		registerReserved("variables");
		registerReserved("varying");
		registerReserved("verbose");
		registerReserved("view");
		registerReserved("volatile");
		registerReserved("waitfor");
		registerReserved("when");
		registerReserved("whenever");
		registerReserved("where");
		registerReserved("while");
		registerReserved("width_bucket");
		registerReserved("window");
		registerReserved("with");
		registerReserved("within");
		registerReserved("without");
		registerReserved("work");
		registerReserved("write");
		registerReserved("writetext");
		registerReserved("x509");
		registerReserved("xor");
		registerReserved("year");
		registerReserved("year_month");
		registerReserved("zerofill");
		registerReserved("zone");
	}

}
