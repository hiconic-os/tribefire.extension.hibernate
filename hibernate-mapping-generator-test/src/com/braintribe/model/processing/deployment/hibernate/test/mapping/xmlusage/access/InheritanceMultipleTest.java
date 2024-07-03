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
package com.braintribe.model.processing.deployment.hibernate.test.mapping.xmlusage.access;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class InheritanceMultipleTest extends HibernateAccessBasedTest {

	@Override
	public Class<?>[] getTestEntityClasses() {
		return new Class<?>[] { com.braintribe.model.processing.deployment.hibernate.testmodel.inheritance.multiple.G.class, com.braintribe.model.processing.deployment.hibernate.testmodel.inheritance.multiple.H.class };
	}

	public static void main(String[] args) throws Exception {
		InheritanceMultipleTest test = new InheritanceMultipleTest();
		test.testPersistAndQuery();
	}

	@BeforeClass
	public static void start() throws Exception {
		initialize();
	}

	@AfterClass
	public static void stop() throws Exception {
		destroy();
	}

	@Override
	@Test
	public void testPersistAndQuery() throws Exception {
		super.testPersistAndQuery();
	}

}
