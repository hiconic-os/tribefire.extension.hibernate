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

import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.braintribe.cfg.Required;
import com.braintribe.logging.Logger;

/**
 * Implementation of the {@link HibernateConfigurationAdaptor} that is able to change attribute values in arbitrary XML files. The attributes are
 * identified by XPath expressions. A regular expression to identify the target files can (and should) also be configured.
 * 
 * Reason for this implementation is the need to change an attribute value in an hibernate mapping file (e.g., change the type of an attribute from
 * clob to text)
 * 
 * @author roman.kurmanowytsch
 */
public class XPathAdaptor implements HibernateConfigurationAdaptor {

	private static Logger logger = Logger.getLogger(XPathAdaptor.class);

	protected Set<XpathAdapterSpecification> specifications = null;

	@Override
	public void cleanup() {
		// do nothing
	}

	@Override
	public void adaptEhCacheConfigurationResource(Document configDocument) throws Exception {

		if (configDocument != null) {
			this.adaptResourceFile(configDocument);
		}

		return;
	}

	/**
	 * Adapts a single file
	 * 
	 * @param configDocument
	 *            The cache document as XML
	 * @throws Exception
	 *             If the file adaptation fails
	 */
	protected void adaptResourceFile(Document configDocument) throws Exception {

		if (specifications == null || specifications.isEmpty()) {
			return;
		}

		try {

			// Apply the XPath expression to get a pointer to the right place

			XPath xpathInstance = XPathFactory.newInstance().newXPath();

			// Note: the current implementation only replaces the value of the first entry identified by the XPath
			for (XpathAdapterSpecification spec : this.specifications) {

				String xpath = spec.nodePath();
				String attrName = spec.attributeName();
				String value = spec.value();

				logger.debug(() -> "Setting value " + value + " at path " + xpath + "/@" + attrName);

				Node node = (Node) xpathInstance.evaluate(xpath, configDocument, XPathConstants.NODE);
				Attr attribute = (Attr) node.getAttributes().getNamedItem(attrName);
				String oldValue = attribute.getValue();

				// String oldValue = (String) context.getValue(xpath);
				if (oldValue != null) {
					if (value.equals(oldValue)) {
						continue;
					}
				}

				attribute.setValue(value);
			}

		} catch (Exception e) {
			throw new Exception("Error while trying to apply xpath.", e);
		}
	}

	@Required
	public void setSpecifications(Set<XpathAdapterSpecification> specifications) {
		this.specifications = specifications;
	}

}
