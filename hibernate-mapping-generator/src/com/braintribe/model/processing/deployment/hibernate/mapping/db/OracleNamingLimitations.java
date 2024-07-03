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

public class OracleNamingLimitations extends NamingLimitations {

	OracleNamingLimitations(HbmXmlGenerationContext context) { 
		super(context);
		setTableNameMaxLength(30);
		setColumnNameMaxLength(30);
		//setColumnNameIllegalLeadingCharsPattern("^_+");
		setupReservedWords();
	}

	/**
	 * @see <a href="http://docs.oracle.com/cd/B19306_01/server.102/b14200/ap_keywd.htm">Oracle 10g Documentation</a>
	 */
	private void setupReservedWords() {
		registerReserved("access");
		registerReserved("add");
		registerReserved("all");
		registerReserved("alter");
		registerReserved("and");
		registerReserved("any");
		registerReserved("as");
		registerReserved("asc");
		registerReserved("audit");
		registerReserved("between");
		registerReserved("by");
		registerReserved("char");
		registerReserved("check");
		registerReserved("cluster");
		registerReserved("column");
		registerReserved("comment");
		registerReserved("compress");
		registerReserved("connect");
		registerReserved("create");
		registerReserved("current");
		registerReserved("date");
		registerReserved("decimal");
		registerReserved("default");
		registerReserved("delete");
		registerReserved("desc");
		registerReserved("distinct");
		registerReserved("drop");
		registerReserved("else");
		registerReserved("exclusive");
		registerReserved("exists");
		registerReserved("file");
		registerReserved("float");
		registerReserved("for");
		registerReserved("from");
		registerReserved("grant");
		registerReserved("group");
		registerReserved("having");
		registerReserved("identified");
		registerReserved("immediate");
		registerReserved("in");
		registerReserved("increment");
		registerReserved("index");
		registerReserved("initial");
		registerReserved("insert");
		registerReserved("integer");
		registerReserved("intersect");
		registerReserved("into");
		registerReserved("is");
		registerReserved("level");
		registerReserved("like");
		registerReserved("lock");
		registerReserved("long");
		registerReserved("maxextents");
		registerReserved("minus");
		registerReserved("mlslabel");
		registerReserved("mode");
		registerReserved("modify");
		registerReserved("noaudit");
		registerReserved("nocompress");
		registerReserved("not");
		registerReserved("nowait");
		registerReserved("null");
		registerReserved("number");
		registerReserved("of");
		registerReserved("offline");
		registerReserved("on");
		registerReserved("online");
		registerReserved("option");
		registerReserved("or");
		registerReserved("order");
		registerReserved("pctfree");
		registerReserved("prior");
		registerReserved("privileges");
		registerReserved("public");
		registerReserved("raw");
		registerReserved("rename");
		registerReserved("resource");
		registerReserved("revoke");
		registerReserved("row");
		registerReserved("rowid");
		registerReserved("rownum");
		registerReserved("rows");
		registerReserved("select");
		registerReserved("session");
		registerReserved("set");
		registerReserved("share");
		registerReserved("size");
		registerReserved("smallint");
		registerReserved("start");
		registerReserved("successful");
		registerReserved("synonym");
		registerReserved("sysdate");
		registerReserved("table");
		registerReserved("then");
		registerReserved("to");
		registerReserved("trigger");
		registerReserved("uid");
		registerReserved("union");
		registerReserved("unique");
		registerReserved("update");
		registerReserved("user");
		registerReserved("validate");
		registerReserved("values");
		registerReserved("varchar");
		registerReserved("varchar2");
		registerReserved("view");
		registerReserved("whenever");
		registerReserved("where");
		registerReserved("with");
	}
}
