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
package com.braintribe.model.processing.deployment.hibernate.mapping;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.braintribe.logging.Logger;

/**
 * Stores given source-files represented by {@link SourceDescriptor} inside the {@link #outputDirectory} passed through
 * constructor.
 */
class SourceSerializer {

	private File outputDirectory;
	private static final Logger log = Logger.getLogger(SourceSerializer.class);

	public SourceSerializer(File outputDirectory) {
		this.outputDirectory = outputDirectory;

		if (outputDirectory.exists()) {
			if (!outputDirectory.isDirectory()) {
				throw new RuntimeException("Source writing initialization failed as '"
						+ outputDirectory.getAbsolutePath() + "' is not a valid directory.");
			}
		}
	}

	public void writeSourceFile(SourceDescriptor sourceDescriptor) {
		try {
			writeSourceHelper(sourceDescriptor);

		} catch (Exception e) {
			log.warn("Failed to save source file '" + sourceDescriptor.sourceRelativePath + "' as " + e);
		}
	}

	private void writeSourceHelper(SourceDescriptor sourceDescriptor) {
		File outpuFile = getFileInExistingFolder(sourceDescriptor.sourceRelativePath);

		try {
			FileWriter fw = new FileWriter(outpuFile);

			try {
				fw.write(sourceDescriptor.sourceCode);

			} finally {
				fw.close();
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private File getFileInExistingFolder(String relativeSourcePath) {
		File result = new File(outputDirectory, relativeSourcePath);

		File parentFile = result.getParentFile();
		if (parentFile.isDirectory() || parentFile.mkdirs()) {
			return result;
		}

		throw new RuntimeException("Failed to create folders to store file: " + result.getAbsolutePath());
	}

}
