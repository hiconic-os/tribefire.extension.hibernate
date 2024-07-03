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
package com.braintribe.model.access.hibernate.time;

import java.time.Duration;
import java.time.Instant;

import com.braintribe.utils.DateTools;
import com.braintribe.utils.StringTools;
import com.braintribe.utils.date.NanoClock;

public class HibernateTimingEvent {

	private Duration duration;
	private String context;
	private Instant recordingTime;
	
	public HibernateTimingEvent(Duration duration, String context) {
		super();
		this.duration = duration;
		this.context = context;
		this.recordingTime = NanoClock.INSTANCE.instant();
	}
	
	@Override
	public String toString() {
		String recTime = DateTools.encode(recordingTime, DateTools.ISO8601_DATE_WITH_MS_FORMAT);
		String durString = StringTools.prettyPrintDuration(duration, true, null);
		StringBuilder sb = new StringBuilder(recTime);
		sb.append(": ");
		sb.append(context);
		sb.append(" (duration: ");
		sb.append(durString);
		sb.append(")");
		return sb.toString();
	}
	
}
