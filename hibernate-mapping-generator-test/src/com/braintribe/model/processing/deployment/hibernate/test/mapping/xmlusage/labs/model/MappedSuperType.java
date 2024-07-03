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
package com.braintribe.model.processing.deployment.hibernate.test.mapping.xmlusage.labs.model;

public class MappedSuperType {

	private Long id;
	private String name;
	private UnmappedSuperType unmappedSuperType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UnmappedSuperType getUnmappedSuperType() {
		return unmappedSuperType;
	}

	public void setUnmappedSuperType(UnmappedSuperType unmappedSuperType) {
		this.unmappedSuperType = unmappedSuperType;
	}

}
