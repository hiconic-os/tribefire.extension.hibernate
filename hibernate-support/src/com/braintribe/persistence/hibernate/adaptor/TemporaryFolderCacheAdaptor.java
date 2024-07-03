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
package com.braintribe.persistence.hibernate.adaptor;

import java.io.File;
import java.util.HashSet;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.DestructionAware;
import com.braintribe.logging.Logger;
import com.braintribe.utils.FileTools;

public class TemporaryFolderCacheAdaptor extends XPathAdaptor implements DestructionAware {

	protected static Logger logger = Logger.getLogger(TemporaryFolderCacheAdaptor.class);

	protected File temporaryCacheFolder = null;

	public TemporaryFolderCacheAdaptor() throws Exception {
		try {
			this.temporaryCacheFolder = File.createTempFile("ehcache", null);
			this.temporaryCacheFolder.delete();
			if (!this.temporaryCacheFolder.mkdirs()) {
				throw new Exception("Could not create directory " + this.temporaryCacheFolder);
			}
			if (logger.isDebugEnabled())
				logger.debug("Created cache directory: " + this.temporaryCacheFolder.getAbsolutePath());
		} catch (Exception e) {
			throw new Exception("Error while creating temporary cache folder.", e);
		}

		super.specifications = new HashSet<>();
		super.specifications.add(new XpathAdapterSpecification("/ehcache/diskStore", "path", this.temporaryCacheFolder.getAbsolutePath()));
	}

	@Override
	public void preDestroy() {
		if (this.temporaryCacheFolder != null) {
			try {
				if (logger.isDebugEnabled())
					logger.debug("Cleaning directory: " + this.temporaryCacheFolder.getAbsolutePath());
				FileTools.deleteDirectoryRecursivelyOnExit(this.temporaryCacheFolder);
			} catch (Exception e) {
				logger.error("Error while cleaning up temporary cache folder: " + this.temporaryCacheFolder, e);
			}
			this.temporaryCacheFolder = null;
		}
	}

	@Override
	public void cleanup() {
		this.preDestroy();
	}

	@Configurable
	public void setTemporaryCacheFolder(File temporaryCacheFolder) {
		this.temporaryCacheFolder = temporaryCacheFolder;
	}

	public static String getBuildVersion() {
		return "$Build_Version$ $Id: TemporaryFolderCacheAdaptor.java 99995 2017-07-24 14:49:31Z andre.goncalves $";
	}

}
