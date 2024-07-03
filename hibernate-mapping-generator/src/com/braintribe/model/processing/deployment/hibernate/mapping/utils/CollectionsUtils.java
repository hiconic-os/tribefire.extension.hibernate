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
package com.braintribe.model.processing.deployment.hibernate.mapping.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Set of collection related helper methods.
 * TODO: Move to proper auxiliary artifact.
 *
 */
public class CollectionsUtils {

	public static <E> Iterable<E> nullSafe(Iterable<E> it) {
		return it != null ? it : Collections.<E> emptySet();
	}
	
	public static <E> boolean isEmpty(Collection<E> col) {
		return (col == null || col.isEmpty());
	}
	
	public static <K, V> boolean isEmpty(Map<K, V> col) {
		return (col == null || col.isEmpty());
	}
	
	public static <E> int safeSize(Collection<E> col) {
		return (col == null || col.isEmpty()) ? 0 : col.size();
	}
	
	public static <K, V> int safeSize(Map<K, V> col) {
		return (col == null || col.isEmpty()) ? 0 : col.size();
	}
	
	public static<K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(Map<K, V> map, final boolean inverse) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
	    Collections.sort(list,
	        new Comparator<Map.Entry<K, V>>() {
	            @Override
				public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
	            	if (inverse) return (o2.getValue().compareTo(o1.getValue()));
	                return (o1.getValue().compareTo(o2.getValue()));
	            }
	        });

	    Map<K, V> result = new LinkedHashMap<K, V>();
	    for (Iterator<Map.Entry<K, V>> it = list.iterator(); it.hasNext();) {
	        Map.Entry<K, V> entry = it.next();
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	}
	
	
	public static<K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(Map<K, V> map) {
		return sortMapByValue(map, false);
	}
	
}
