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
