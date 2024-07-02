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
package com.braintribe.model.processing.deployment.hibernate.mapping.utils;

/**
 * 
 */
public class Suffixer {
	
	private static final String SUFFIXES = "_0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
    public static String suffixIt(String str, int attempt, int maxLength) {
    	String s = generateSuffix(attempt);
    	if (str.length() + s.length() > maxLength) {
    		str = str.substring(0, maxLength - s.length());
    	}
    	return (str+s).trim();
    }
    
	private static String generateSuffix(int v) {
		if (v < SUFFIXES.length()) {
			return String.valueOf(SUFFIXES.charAt(v));
		}
		return generateSuffix(v/SUFFIXES.length()-1)+SUFFIXES.charAt(v % SUFFIXES.length());
	}
  
}
