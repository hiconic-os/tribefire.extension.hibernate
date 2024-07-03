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
package com.braintribe.model.access.hibernate;

/**
 * A simple helper class that keeps tracks on manipulations statistics.
 * @author gunther.schenk
 *
 */
public class HibernateApplyStatistics {

	private int creations = 0;
	private int deletions = 0;
	private int valueChanges = 0;
	
	public void increaseCreations() {
		creations++;
	}
	
	public void increaseDeletions() {
		deletions++;
	}

	public void increaseValueChanges() {
		valueChanges++;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("applyStatistics: %d creations done, %d deletions done, %d updates done", creations, deletions, valueChanges);
	}
	public static String getBuildVersion() {

		return "$Build_Version$ $Id: HibernateApplyStatistics.java 86391 2015-05-28 14:25:17Z roman.kurmanowytsch $";
	}
}
