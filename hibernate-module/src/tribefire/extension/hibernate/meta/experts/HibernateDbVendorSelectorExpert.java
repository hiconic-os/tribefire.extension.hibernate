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
package tribefire.extension.hibernate.meta.experts;

import java.util.Collection;

import com.braintribe.model.access.hibernate.meta.aspects.HibernateDialectAspect;
import com.braintribe.model.accessdeployment.hibernate.HibernateDialect;
import com.braintribe.model.accessdeployment.hibernate.selector.HibernateDbVendor;
import com.braintribe.model.accessdeployment.hibernate.selector.HibernateDbVendorSelector;
import com.braintribe.model.meta.selector.UseCaseSelector;
import com.braintribe.model.processing.meta.cmd.context.SelectorContext;
import com.braintribe.model.processing.meta.cmd.context.SelectorContextAspect;
import com.braintribe.model.processing.meta.cmd.context.experts.SelectorExpert;
import com.braintribe.model.processing.meta.cmd.tools.MetaDataTools;

/**
 * Expert for the {@link UseCaseSelector}. The selector is active if it's use-case value is contained within the ones currently specified in the
 * context.
 */
public class HibernateDbVendorSelectorExpert implements SelectorExpert<HibernateDbVendorSelector> {

	@Override
	public Collection<Class<? extends SelectorContextAspect<?>>> getRelevantAspects(HibernateDbVendorSelector selector) throws Exception {
		return MetaDataTools.aspects(HibernateDialectAspect.class);
	}

	@Override
	public boolean matches(HibernateDbVendorSelector selector, SelectorContext context) throws Exception {
		HibernateDbVendor vendor = selector.getVendor();
		if (vendor == null)
			return true;

		HibernateDialect currentDialect = context.get(HibernateDialectAspect.class);
		if (currentDialect == null)
			return false;

		switch (currentDialect) {
			case CUBRIDDialect:
			case Cache71Dialect:
			case DB2390Dialect:
			case DB2400Dialect:
			case DB297Dialect:
			case DB2Dialect:
			case DataDirectOracle9Dialect:
				return false;

			case DerbyDialect:
			case DerbyTenFiveDialect:
			case DerbyTenSevenDialect:
			case DerbyTenSixDialect:
				return vendor == HibernateDbVendor.Derby;

			case FirebirdDialect:
			case FrontBaseDialect:
				return false;

			case H2Dialect:
				return vendor == HibernateDbVendor.H2;

			case HANAColumnStoreDialect:
			case HANARowStoreDialect:
			case HSQLDialect:
				return false;

			case Informix10Dialect:
			case InformixDialect:
				return vendor == HibernateDbVendor.Informix;

			case Ingres10Dialect:
			case Ingres9Dialect:
			case IngresDialect:
			case InterbaseDialect:
				return vendor == HibernateDbVendor.Ingres;

			case JDataStoreDialect:
				return false;

			case MariaDB53Dialect:
			case MariaDBDialect:
				return vendor == HibernateDbVendor.MariaDB;

			case MckoiDialect:
			case MimerSQLDialect:
				return false;

			case MySQL55Dialect:
			case MySQL57Dialect:
			case MySQL57InnoDBDialect:
			case MySQL5Dialect:
			case MySQL5InnoDBDialect:
			case MySQLDialect:
			case MySQLInnoDBDialect:
			case MySQLMyISAMDialect:
				return vendor == HibernateDbVendor.MySQL;

			case Oracle10gDialect:
			case Oracle12cDialect:
			case Oracle8iDialect:
			case Oracle9Dialect:
			case Oracle9iDialect:
			case OracleDialect:
				return vendor == HibernateDbVendor.Oracle;

			case PointbaseDialect:
				return false;

			case PostgreSQL81Dialect:
			case PostgreSQL82Dialect:
			case PostgreSQL91Dialect:
			case PostgreSQL92Dialect:
			case PostgreSQL93Dialect:
			case PostgreSQL94Dialect:
			case PostgreSQL95Dialect:
			case PostgreSQL9Dialect:
			case PostgreSQLDialect:
			case PostgresPlusDialect:
				return vendor == HibernateDbVendor.PostgreSQL;

			case ProgressDialect:
			case RDMSOS2200Dialect:
			case SAPDBDialect:
				return false;

			case SQLServer2005Dialect:
			case SQLServer2008Dialect:
			case SQLServer2012Dialect:
			case SQLServerDialect:
				return vendor == HibernateDbVendor.SQLServer;

			case Sybase11Dialect:
			case SybaseASE157Dialect:
			case SybaseASE15Dialect:
			case SybaseAnywhereDialect:
			case SybaseDialect:
				return vendor == HibernateDbVendor.Sybase;

			case Teradata14Dialect:
			case TeradataDialect:
				return vendor == HibernateDbVendor.Teradata;
			case TimesTenDialect:
				return vendor == HibernateDbVendor.TimesTen;

			default:
				return false;
		}

	}

}
