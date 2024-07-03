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
package com.braintribe.model.processing.deployment.hibernate.test.mapping.utils;

import org.junit.Assert;
import org.junit.Test;

import com.braintribe.model.processing.deployment.hibernate.mapping.utils.CamelCaseStringShortener;

public class CamelCaseStringShortenerTest {
	
	private static int [] testTheseLength = {1, 2, 5, 7, 10, 20, 30, 50, 100};
	
	private static String [] testTheseStrings = {
			 "A"
			,"aA"
			,"Aa"
			,"Long"
			,"shortOne"
			,"ThisPropertyShouldProvideASmoothShortening"
			,"ThisPropertyShouldProvideSmoothShortening"
			,"ThisPropertyShouldProvideSmoothShorteningAsLongAsItIsPossible"
			,"CreditCardCompanyEmployeesList"
			,"FinancialInstitutionCompanyIssuedCreditCardList"
			,"myExtremellyHugePropertyNameThatShouldNeverBeUsedInRealLife"
			,"AnUnnecessarilyLargePropertyNameThatCouldExistInTheModel"
			,"UnnecessarilyLargePropertyNameThatMayExist"
			,"bankInternationalIdentificationCodeNumber"
			,"bankInternationalIdentificationCodeNumberSWIFTAddress"
			,"OneFewByTheSeaAreInTheToeQueOnTheFarmWhereTheCowIsFullOfGutMilk"
			,"extremellyLongPropertyNameThatMightClashWithSlightelyDifferentPropertyInSameClassOrHierarchya"
			,"extremellyLongPropertyNameThatMightClashWithSlightelyDifferentPropertyInSameClassOrHierarchyb"
			};
	
	@Test
	public void testShortener() throws Exception {
		
		StringBuffer logLine = null;
		for (int maxLength  : testTheseLength) {
			
			System.out.println("\r\n================== SHORTENING TO MAX "+maxLength+" CHAR(S) ==================");
			
			for (String testMe : testTheseStrings) {
				
				logLine = new StringBuffer(200);
				
				logLine.append("string before: \t\"").append(testMe).append("\" size: ").append(testMe.length());
				testMe = new CamelCaseStringShortener(testMe, maxLength).shorten();
				logLine.append("\r\nstring after:\t\"").append(testMe).append("\" size: ").append(testMe.length());
				
				String log = logLine.toString(); 
				System.out.println(log);
				if (testMe.length() > maxLength) {
					Assert.fail("Shortening Failed: "+log);
				}
			}
			
		}
	}
	

	
}
