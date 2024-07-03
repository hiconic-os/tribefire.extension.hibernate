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

public class PostgreSqlNamingLimitations extends NamingLimitations {

	PostgreSqlNamingLimitations(HbmXmlGenerationContext context) {
		super(context);
		setTableNameMaxLength(31);
		setColumnNameMaxLength(31);
		//setColumnNameIllegalLeadingCharsPattern("^_+");
		setupReservedWords();
	}

	private void setupReservedWords() {
		registerReserved("a");
		registerReserved("abort");
		registerReserved("abs");
		registerReserved("absent");
		registerReserved("absolute");
		registerReserved("access");
		registerReserved("according");
		registerReserved("action");
		registerReserved("ada");
		registerReserved("add");
		registerReserved("admin");
		registerReserved("after");
		registerReserved("aggregate");
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
		registerReserved("array_agg");
		registerReserved("array_max_cardinality");
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
		registerReserved("authorization");
		registerReserved("avg");
		registerReserved("backward");
		registerReserved("base64");
		registerReserved("before");
		registerReserved("begin");
		registerReserved("begin_frame");
		registerReserved("begin_partition");
		registerReserved("bernoulli");
		registerReserved("between");
		registerReserved("bigint");
		registerReserved("binary");
		registerReserved("bit");
		registerReserved("bit_length");
		registerReserved("blob");
		registerReserved("blocked");
		registerReserved("bom");
		registerReserved("boolean");
		registerReserved("both");
		registerReserved("breadth");
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
		registerReserved("char");
		registerReserved("character");
		registerReserved("characteristics");
		registerReserved("characters");
		registerReserved("character_length");
		registerReserved("character_set_catalog");
		registerReserved("character_set_name");
		registerReserved("character_set_schema");
		registerReserved("char_length");
		registerReserved("check");
		registerReserved("checkpoint");
		registerReserved("class");
		registerReserved("class_origin");
		registerReserved("clob");
		registerReserved("close");
		registerReserved("cluster");
		registerReserved("coalesce");
		registerReserved("cobol");
		registerReserved("collate");
		registerReserved("collation");
		registerReserved("collation_catalog");
		registerReserved("collation_name");
		registerReserved("collation_schema");
		registerReserved("collect");
		registerReserved("column");
		registerReserved("columns");
		registerReserved("column_name");
		registerReserved("command_function");
		registerReserved("command_function_code");
		registerReserved("comment");
		registerReserved("comments");
		registerReserved("commit");
		registerReserved("committed");
		registerReserved("concurrently");
		registerReserved("condition");
		registerReserved("condition_number");
		registerReserved("configuration");
		registerReserved("conflict");
		registerReserved("connect");
		registerReserved("connection");
		registerReserved("connection_name");
		registerReserved("constraint");
		registerReserved("constraints");
		registerReserved("constraint_catalog");
		registerReserved("constraint_name");
		registerReserved("constraint_schema");
		registerReserved("constructor");
		registerReserved("contains");
		registerReserved("content");
		registerReserved("continue");
		registerReserved("control");
		registerReserved("conversion");
		registerReserved("convert");
		registerReserved("copy");
		registerReserved("corr");
		registerReserved("corresponding");
		registerReserved("cost");
		registerReserved("count");
		registerReserved("covar_pop");
		registerReserved("covar_samp");
		registerReserved("create");
		registerReserved("cross");
		registerReserved("csv");
		registerReserved("cube");
		registerReserved("cume_dist");
		registerReserved("current");
		registerReserved("current_catalog");
		registerReserved("current_date");
		registerReserved("current_default_transform_group");
		registerReserved("current_path");
		registerReserved("current_role");
		registerReserved("current_row");
		registerReserved("current_schema");
		registerReserved("current_time");
		registerReserved("current_timestamp");
		registerReserved("current_transform_group_for_type");
		registerReserved("current_user");
		registerReserved("cursor");
		registerReserved("cursor_name");
		registerReserved("cycle");
		registerReserved("data");
		registerReserved("database");
		registerReserved("datalink");
		registerReserved("date");
		registerReserved("datetime_interval_code");
		registerReserved("datetime_interval_precision");
		registerReserved("day");
		registerReserved("db");
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
		registerReserved("delete");
		registerReserved("delimiter");
		registerReserved("delimiters");
		registerReserved("dense_rank");
		registerReserved("depth");
		registerReserved("deref");
		registerReserved("derived");
		registerReserved("desc");
		registerReserved("describe");
		registerReserved("descriptor");
		registerReserved("deterministic");
		registerReserved("diagnostics");
		registerReserved("dictionary");
		registerReserved("disable");
		registerReserved("discard");
		registerReserved("disconnect");
		registerReserved("dispatch");
		registerReserved("distinct");
		registerReserved("dlnewcopy");
		registerReserved("dlpreviouscopy");
		registerReserved("dlurlcomplete");
		registerReserved("dlurlcompleteonly");
		registerReserved("dlurlcompletewrite");
		registerReserved("dlurlpath");
		registerReserved("dlurlpathonly");
		registerReserved("dlurlpathwrite");
		registerReserved("dlurlscheme");
		registerReserved("dlurlserver");
		registerReserved("dlvalue");
		registerReserved("do");
		registerReserved("document");
		registerReserved("domain");
		registerReserved("double");
		registerReserved("drop");
		registerReserved("dynamic");
		registerReserved("dynamic_function");
		registerReserved("dynamic_function_code");
		registerReserved("each");
		registerReserved("element");
		registerReserved("else");
		registerReserved("empty");
		registerReserved("enable");
		registerReserved("encoding");
		registerReserved("encrypted");
		registerReserved("end");
		registerReserved("end-exec");
		registerReserved("end_frame");
		registerReserved("end_partition");
		registerReserved("enforced");
		registerReserved("enum");
		registerReserved("equals");
		registerReserved("escape");
		registerReserved("event");
		registerReserved("every");
		registerReserved("except");
		registerReserved("exception");
		registerReserved("exclude");
		registerReserved("excluding");
		registerReserved("exclusive");
		registerReserved("exec");
		registerReserved("execute");
		registerReserved("exists");
		registerReserved("exp");
		registerReserved("explain");
		registerReserved("expression");
		registerReserved("extension");
		registerReserved("external");
		registerReserved("extract");
		registerReserved("false");
		registerReserved("family");
		registerReserved("fetch");
		registerReserved("file");
		registerReserved("filter");
		registerReserved("final");
		registerReserved("first");
		registerReserved("first_value");
		registerReserved("flag");
		registerReserved("float");
		registerReserved("floor");
		registerReserved("following");
		registerReserved("for");
		registerReserved("force");
		registerReserved("foreign");
		registerReserved("fortran");
		registerReserved("forward");
		registerReserved("found");
		registerReserved("frame_row");
		registerReserved("free");
		registerReserved("freeze");
		registerReserved("from");
		registerReserved("fs");
		registerReserved("full");
		registerReserved("function");
		registerReserved("functions");
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
		registerReserved("greatest");
		registerReserved("group");
		registerReserved("grouping");
		registerReserved("groups");
		registerReserved("handler");
		registerReserved("having");
		registerReserved("header");
		registerReserved("hex");
		registerReserved("hierarchy");
		registerReserved("hold");
		registerReserved("hour");
		registerReserved("id");
		registerReserved("identity");
		registerReserved("if");
		registerReserved("ignore");
		registerReserved("ilike");
		registerReserved("immediate");
		registerReserved("immediately");
		registerReserved("immutable");
		registerReserved("implementation");
		registerReserved("implicit");
		registerReserved("import");
		registerReserved("in");
		registerReserved("including");
		registerReserved("increment");
		registerReserved("indent");
		registerReserved("index");
		registerReserved("indexes");
		registerReserved("indicator");
		registerReserved("inherit");
		registerReserved("inherits");
		registerReserved("initially");
		registerReserved("inline");
		registerReserved("inner");
		registerReserved("inout");
		registerReserved("input");
		registerReserved("insensitive");
		registerReserved("insert");
		registerReserved("instance");
		registerReserved("instantiable");
		registerReserved("instead");
		registerReserved("int");
		registerReserved("integer");
		registerReserved("integrity");
		registerReserved("intersect");
		registerReserved("intersection");
		registerReserved("interval");
		registerReserved("into");
		registerReserved("invoker");
		registerReserved("is");
		registerReserved("isnull");
		registerReserved("isolation");
		registerReserved("join");
		registerReserved("k");
		registerReserved("key");
		registerReserved("key_member");
		registerReserved("key_type");
		registerReserved("label");
		registerReserved("lag");
		registerReserved("language");
		registerReserved("large");
		registerReserved("last");
		registerReserved("last_value");
		registerReserved("lateral");
		registerReserved("lead");
		registerReserved("leading");
		registerReserved("leakproof");
		registerReserved("least");
		registerReserved("left");
		registerReserved("length");
		registerReserved("level");
		registerReserved("library");
		registerReserved("like");
		registerReserved("like_regex");
		registerReserved("limit");
		registerReserved("link");
		registerReserved("listen");
		registerReserved("ln");
		registerReserved("load");
		registerReserved("local");
		registerReserved("localtime");
		registerReserved("localtimestamp");
		registerReserved("location");
		registerReserved("locator");
		registerReserved("lock");
		registerReserved("locked");
		registerReserved("logged");
		registerReserved("lower");
		registerReserved("m");
		registerReserved("map");
		registerReserved("mapping");
		registerReserved("match");
		registerReserved("matched");
		registerReserved("materialized");
		registerReserved("max");
		registerReserved("maxvalue");
		registerReserved("max_cardinality");
		registerReserved("member");
		registerReserved("merge");
		registerReserved("message_length");
		registerReserved("message_octet_length");
		registerReserved("message_text");
		registerReserved("method");
		registerReserved("min");
		registerReserved("minute");
		registerReserved("minvalue");
		registerReserved("mod");
		registerReserved("mode");
		registerReserved("modifies");
		registerReserved("module");
		registerReserved("month");
		registerReserved("more");
		registerReserved("move");
		registerReserved("multiset");
		registerReserved("mumps");
		registerReserved("name");
		registerReserved("names");
		registerReserved("namespace");
		registerReserved("national");
		registerReserved("natural");
		registerReserved("nchar");
		registerReserved("nclob");
		registerReserved("nesting");
		registerReserved("new");
		registerReserved("next");
		registerReserved("nfc");
		registerReserved("nfd");
		registerReserved("nfkc");
		registerReserved("nfkd");
		registerReserved("nil");
		registerReserved("no");
		registerReserved("none");
		registerReserved("normalize");
		registerReserved("normalized");
		registerReserved("not");
		registerReserved("nothing");
		registerReserved("notify");
		registerReserved("notnull");
		registerReserved("nowait");
		registerReserved("nth_value");
		registerReserved("ntile");
		registerReserved("null");
		registerReserved("nullable");
		registerReserved("nullif");
		registerReserved("nulls");
		registerReserved("number");
		registerReserved("numeric");
		registerReserved("object");
		registerReserved("occurrences_regex");
		registerReserved("octets");
		registerReserved("octet_length");
		registerReserved("of");
		registerReserved("off");
		registerReserved("offset");
		registerReserved("oids");
		registerReserved("old");
		registerReserved("on");
		registerReserved("only");
		registerReserved("open");
		registerReserved("operator");
		registerReserved("option");
		registerReserved("options");
		registerReserved("or");
		registerReserved("order");
		registerReserved("ordering");
		registerReserved("ordinality");
		registerReserved("others");
		registerReserved("out");
		registerReserved("outer");
		registerReserved("output");
		registerReserved("over");
		registerReserved("overlaps");
		registerReserved("overlay");
		registerReserved("overriding");
		registerReserved("owned");
		registerReserved("owner");
		registerReserved("p");
		registerReserved("pad");
		registerReserved("parameter");
		registerReserved("parameter_mode");
		registerReserved("parameter_name");
		registerReserved("parameter_ordinal_position");
		registerReserved("parameter_specific_catalog");
		registerReserved("parameter_specific_name");
		registerReserved("parameter_specific_schema");
		registerReserved("parser");
		registerReserved("partial");
		registerReserved("partition");
		registerReserved("pascal");
		registerReserved("passing");
		registerReserved("passthrough");
		registerReserved("password");
		registerReserved("path");
		registerReserved("percent");
		registerReserved("percentile_cont");
		registerReserved("percentile_disc");
		registerReserved("percent_rank");
		registerReserved("period");
		registerReserved("permission");
		registerReserved("placing");
		registerReserved("plans");
		registerReserved("pli");
		registerReserved("policy");
		registerReserved("portion");
		registerReserved("position");
		registerReserved("position_regex");
		registerReserved("power");
		registerReserved("precedes");
		registerReserved("preceding");
		registerReserved("precision");
		registerReserved("prepare");
		registerReserved("prepared");
		registerReserved("preserve");
		registerReserved("primary");
		registerReserved("prior");
		registerReserved("privileges");
		registerReserved("procedural");
		registerReserved("procedure");
		registerReserved("program");
		registerReserved("public");
		registerReserved("quote");
		registerReserved("range");
		registerReserved("rank");
		registerReserved("read");
		registerReserved("reads");
		registerReserved("real");
		registerReserved("reassign");
		registerReserved("recheck");
		registerReserved("recovery");
		registerReserved("recursive");
		registerReserved("ref");
		registerReserved("references");
		registerReserved("referencing");
		registerReserved("refresh");
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
		registerReserved("rename");
		registerReserved("repeatable");
		registerReserved("replace");
		registerReserved("replica");
		registerReserved("requiring");
		registerReserved("reset");
		registerReserved("respect");
		registerReserved("restart");
		registerReserved("restore");
		registerReserved("restrict");
		registerReserved("result");
		registerReserved("return");
		registerReserved("returned_cardinality");
		registerReserved("returned_length");
		registerReserved("returned_octet_length");
		registerReserved("returned_sqlstate");
		registerReserved("returning");
		registerReserved("returns");
		registerReserved("revoke");
		registerReserved("right");
		registerReserved("role");
		registerReserved("rollback");
		registerReserved("rollup");
		registerReserved("routine");
		registerReserved("routine_catalog");
		registerReserved("routine_name");
		registerReserved("routine_schema");
		registerReserved("row");
		registerReserved("rows");
		registerReserved("row_count");
		registerReserved("row_number");
		registerReserved("rule");
		registerReserved("savepoint");
		registerReserved("scale");
		registerReserved("schema");
		registerReserved("schema_name");
		registerReserved("scope");
		registerReserved("scope_catalog");
		registerReserved("scope_name");
		registerReserved("scope_schema");
		registerReserved("scroll");
		registerReserved("search");
		registerReserved("second");
		registerReserved("section");
		registerReserved("security");
		registerReserved("select");
		registerReserved("selective");
		registerReserved("self");
		registerReserved("sensitive");
		registerReserved("sequence");
		registerReserved("sequences");
		registerReserved("serializable");
		registerReserved("server");
		registerReserved("server_name");
		registerReserved("session");
		registerReserved("session_user");
		registerReserved("set");
		registerReserved("setof");
		registerReserved("sets");
		registerReserved("share");
		registerReserved("show");
		registerReserved("similar");
		registerReserved("simple");
		registerReserved("size");
		registerReserved("skip");
		registerReserved("smallint");
		registerReserved("snapshot");
		registerReserved("some");
		registerReserved("source");
		registerReserved("space");
		registerReserved("specific");
		registerReserved("specifictype");
		registerReserved("specific_name");
		registerReserved("sql");
		registerReserved("sqlcode");
		registerReserved("sqlerror");
		registerReserved("sqlexception");
		registerReserved("sqlstate");
		registerReserved("sqlwarning");
		registerReserved("sqrt");
		registerReserved("stable");
		registerReserved("standalone");
		registerReserved("start");
		registerReserved("state");
		registerReserved("statement");
		registerReserved("static");
		registerReserved("statistics");
		registerReserved("stddev_pop");
		registerReserved("stddev_samp");
		registerReserved("stdin");
		registerReserved("stdout");
		registerReserved("storage");
		registerReserved("strict");
		registerReserved("strip");
		registerReserved("structure");
		registerReserved("style");
		registerReserved("subclass_origin");
		registerReserved("submultiset");
		registerReserved("substring");
		registerReserved("substring_regex");
		registerReserved("succeeds");
		registerReserved("sum");
		registerReserved("symmetric");
		registerReserved("sysid");
		registerReserved("system");
		registerReserved("system_time");
		registerReserved("system_user");
		registerReserved("t");
		registerReserved("table");
		registerReserved("tables");
		registerReserved("tablesample");
		registerReserved("tablespace");
		registerReserved("table_name");
		registerReserved("temp");
		registerReserved("template");
		registerReserved("temporary");
		registerReserved("text");
		registerReserved("then");
		registerReserved("ties");
		registerReserved("time");
		registerReserved("timestamp");
		registerReserved("timezone_hour");
		registerReserved("timezone_minute");
		registerReserved("to");
		registerReserved("token");
		registerReserved("top_level_count");
		registerReserved("trailing");
		registerReserved("transaction");
		registerReserved("transactions_committed");
		registerReserved("transactions_rolled_back");
		registerReserved("transaction_active");
		registerReserved("transform");
		registerReserved("transforms");
		registerReserved("translate");
		registerReserved("translate_regex");
		registerReserved("translation");
		registerReserved("treat");
		registerReserved("trigger");
		registerReserved("trigger_catalog");
		registerReserved("trigger_name");
		registerReserved("trigger_schema");
		registerReserved("trim");
		registerReserved("trim_array");
		registerReserved("true");
		registerReserved("truncate");
		registerReserved("trusted");
		registerReserved("type");
		registerReserved("types");
		registerReserved("uescape");
		registerReserved("unbounded");
		registerReserved("uncommitted");
		registerReserved("under");
		registerReserved("unencrypted");
		registerReserved("union");
		registerReserved("unique");
		registerReserved("unknown");
		registerReserved("unlink");
		registerReserved("unlisten");
		registerReserved("unlogged");
		registerReserved("unnamed");
		registerReserved("unnest");
		registerReserved("until");
		registerReserved("untyped");
		registerReserved("update");
		registerReserved("upper");
		registerReserved("uri");
		registerReserved("usage");
		registerReserved("user");
		registerReserved("user_defined_type_catalog");
		registerReserved("user_defined_type_code");
		registerReserved("user_defined_type_name");
		registerReserved("user_defined_type_schema");
		registerReserved("using");
		registerReserved("vacuum");
		registerReserved("valid");
		registerReserved("validate");
		registerReserved("validator");
		registerReserved("value");
		registerReserved("values");
		registerReserved("value_of");
		registerReserved("varbinary");
		registerReserved("varchar");
		registerReserved("variadic");
		registerReserved("varying");
		registerReserved("var_pop");
		registerReserved("var_samp");
		registerReserved("verbose");
		registerReserved("version");
		registerReserved("versioning");
		registerReserved("view");
		registerReserved("views");
		registerReserved("volatile");
		registerReserved("when");
		registerReserved("whenever");
		registerReserved("where");
		registerReserved("whitespace");
		registerReserved("width_bucket");
		registerReserved("window");
		registerReserved("with");
		registerReserved("within");
		registerReserved("without");
		registerReserved("work");
		registerReserved("wrapper");
		registerReserved("write");
		registerReserved("xml");
		registerReserved("xmlagg");
		registerReserved("xmlattributes");
		registerReserved("xmlbinary");
		registerReserved("xmlcast");
		registerReserved("xmlcomment");
		registerReserved("xmlconcat");
		registerReserved("xmldeclaration");
		registerReserved("xmldocument");
		registerReserved("xmlelement");
		registerReserved("xmlexists");
		registerReserved("xmlforest");
		registerReserved("xmliterate");
		registerReserved("xmlnamespaces");
		registerReserved("xmlparse");
		registerReserved("xmlpi");
		registerReserved("xmlquery");
		registerReserved("xmlroot");
		registerReserved("xmlschema");
		registerReserved("xmlserialize");
		registerReserved("xmltable");
		registerReserved("xmltext");
		registerReserved("xmlvalidate");
		registerReserved("year");
		registerReserved("yes");
		registerReserved("zone");
	}

}
