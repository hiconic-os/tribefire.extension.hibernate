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
package tribefire.extension.hibernate.edr2cc.denotrans;

import static tribefire.module.api.PlatformBindIds.*;
import static tribefire.module.api.PlatformBindIds.TRANSIENT_MESSAGING_DATA_DB_BIND_ID;

import java.util.function.Function;

import com.braintribe.model.accessdeployment.hibernate.HibernateAccess;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.data.MetaData;
import com.braintribe.model.processing.meta.editor.MetaDataEditorBuilder;
import com.braintribe.model.processing.meta.editor.ModelMetaDataEditor;

import tribefire.module.api.DenotationEnrichmentResult;
import tribefire.module.api.DenotationTransformationContext;

/**
 * Configures mappings for known hibernate accesses.
 * <p>
 * This was created as a replacement for hardcoded Hibernate deployables with static hbm files embedded in web-platform.
 * 
 * @author peter.gazdik
 */
public class HibernateAccessEdr2ccEnricher {

	/* package */ final DenotationTransformationContext context;
	private final HibernateAccess access;
	private final GmMetaModel model;
	private final Function<GmMetaModel, MetaDataEditorBuilder> mdEditorFactory;

	/* package */ ModelMetaDataEditor mdEditor;

	public HibernateAccessEdr2ccEnricher(//
			DenotationTransformationContext context, HibernateAccess access, Function<GmMetaModel, MetaDataEditorBuilder> mdEditorFactory) {

		this.context = context;
		this.access = access;
		this.model = access.getMetaModel();
		this.mdEditorFactory = mdEditorFactory;
	}

	public DenotationEnrichmentResult<HibernateAccess> run() {
		switch (context.denotationId()) {
			case AUTH_DB_BIND_ID:
			case USER_SESSIONS_DB_BIND_ID:
			case USER_STATISTICS_DB_BIND_ID:
			case TRANSIENT_MESSAGING_DATA_DB_BIND_ID:
				if (model == null)
					return DenotationEnrichmentResult.nothingYetButCallMeAgain();
				break;

			default:
				return DenotationEnrichmentResult.nothingNowOrEver();
		}

		//unmapGlobalIdAndPartition();
		initMdEditor();

		switch (context.denotationId()) {
			case AUTH_DB_BIND_ID:
				return auth();
			case USER_SESSIONS_DB_BIND_ID:
				return userSessions();
			case USER_STATISTICS_DB_BIND_ID:
				return userStatistics();
			case TRANSIENT_MESSAGING_DATA_DB_BIND_ID:
				return transientMessaging();
			default:
				throw new IllegalStateException("Error in implementation. This code should be unreachable.");
		}
	}

	private void initMdEditor() {
		mdEditor = mdEditorFactory.apply(model).done();
	}

	private DenotationEnrichmentResult<HibernateAccess> auth() {
		HbmConfigurer_Auth.run(this);
		return allDone("Configured hbm mappings.");
	}

	private DenotationEnrichmentResult<HibernateAccess> userSessions() {
		HbmConfigurer_UserSessions.run(this);
		return allDone("Configured hbm mappings.");
	}

	private DenotationEnrichmentResult<HibernateAccess> userStatistics() {
		HbmConfigurer_UserStatistics.run(this);
		return allDone("Configured hbm mappings.");
	}

	private DenotationEnrichmentResult<HibernateAccess> transientMessaging() {
		return allDone("Just globalId and partition.");
	}

	// ################################################
	// ## . . . . . . . . . Shared . . . . . . . . . ##
	// ################################################

	private DenotationEnrichmentResult<HibernateAccess> allDone(String changeDescription) {
		return DenotationEnrichmentResult.allDone(access, changeDescription);
	}

	/* package */ MetaData unmappedEntity() {
		return context.getEntityByGlobalId("hbm:unmapped-entity");
	}

	/* package */ MetaData mappedEntity() {
		return context.getEntityByGlobalId("hbm:mapped-entity");
	}

	/* package */ MetaData unmappedProperty() {
		return context.getEntityByGlobalId("hbm:unmapped-property");
	}

	/* package */ MetaData mappedProperty() {
		return context.getEntityByGlobalId("hbm:mapped-property");
	}
	
}
