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
package com.braintribe.model.processing.deployment.hibernate.testmodel.inheritance.overlaidproperty;

import com.braintribe.model.generic.StandardIdentifiable;

public interface Product extends StandardIdentifiable {
	
	@Override
	<T> T getId();
	@Override
	void setId(Object id);
	
	String getModel();
	void setModel(String model);
	
	//results in ERROR:
	//Caused by: javassist.bytecode.DuplicateMemberException: duplicate method: getManufacturer in org.hibernate.proxy.HibernateProxy_$$_javassist_42
	//Manufacturer getManufacturer();
	//void setManufacturer(Manufacturer manufacturer);
	
}
