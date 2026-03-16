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
package com.braintribe.model.processing.deployment.hibernate.mapping.render;

import static com.braintribe.utils.lcd.CollectionTools2.first;

import java.util.List;
import java.util.stream.Collectors;

import com.braintribe.model.accessdeployment.jpa.meta.JpaColumn;
import com.braintribe.model.generic.tools.AbstractStringifier;
import com.braintribe.model.meta.GmType;
import com.braintribe.model.processing.deployment.hibernate.mapping.HbmXmlGenerationContext;
import com.braintribe.model.processing.deployment.hibernate.mapping.SourceDescriptor;
import com.braintribe.model.processing.deployment.hibernate.mapping.render.context.CollectionPropertyDescriptor;
import com.braintribe.model.processing.deployment.hibernate.mapping.render.context.EntityDescriptor;
import com.braintribe.model.processing.deployment.hibernate.mapping.render.context.EntityMappingContextTools;
import com.braintribe.model.processing.deployment.hibernate.mapping.render.context.EnumDescriptor;
import com.braintribe.model.processing.deployment.hibernate.mapping.render.context.PropertyDescriptor;

/**
 * @author peter.gazdik
 */
public class HbmXmlFastRenderer extends AbstractStringifier {

	public static SourceDescriptor renderEntityType(EntityDescriptor entityDescriptor, HbmXmlGenerationContext context) {
		SourceDescriptor result = new SourceDescriptor();
		result.sourceRelativePath = entityDescriptor.getFullName() + ".hbm.xml";
		result.sourceCode = renderXml(entityDescriptor, context);

		return result;
	}

	private static String renderXml(EntityDescriptor ed, HbmXmlGenerationContext context) {
		if (ed.xml != null)
			return ed.xml;
		else
			return new HbmXmlFastRenderer(ed, context).renderHbmXml();
	}

	private static final String Q = "\"";

	private final EntityDescriptor ed;
	private final HbmXmlGenerationContext context;

	private HbmXmlFastRenderer(EntityDescriptor ed, HbmXmlGenerationContext context) {
		this.ed = ed;
		this.context = context;
	}

	private String renderHbmXml() {
		println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		println();
		print("<hibernate-mapping auto-import=\"true\"");
		optAttr("schema", ed.schema);
		optAttr("catalog", ed.catalog);
		endOpenTag();

		levelUp();
		{
			openTag(ed.tag);
			attr("name", ed.fullName);
			attr("entity-name", ed.fullName);
			if (ed.isTopLevel)
				attr("table", ed.getQuotedTableName());
			attr("abstract", ed.abstractFlag());
			if (!ed.isTopLevel)
				attr("extends", ed.hbmSuperType);
			if (!ed.isTopLevel && !empty(ed.discriminatorValue))
				attr("discriminator-value", ed.discriminatorValue);
			endOpenTag();

			levelUp();
			{
				renderProperties();
			}
			levelDown();

			closeTag(ed.tag);
		}
		levelDown();

		println("</hibernate-mapping>");

		return builder.toString();
	}

	private void renderProperties() {
		for (PropertyDescriptor pd : ed.properties)
			render(pd);
	}

	private void render(PropertyDescriptor pd) {
		if (pd.xml != null)
			println(pd.xml.trim());

		else if (pd.hasVersionMd)
			return;

		else if (pd.isCollection())
			renderCollectionProperty((CollectionPropertyDescriptor) pd);

		else if (pd.isEnum())
			renderEnumProperty((EnumDescriptor) pd);

		else if (pd.getIsCompositeIdProperty())
			renderCompositeId(pd);

		// REMOVED EMBEDDABLE
		// else if (pd.isEmbedded())
		// renderEmbedded((ComponentDescriptor) pd);

		else if (pd.toOneEntitySignature != null)
			renderManyToOne(pd);

		else if (!pd.isIdProperty && !pd.getHasColumnAttributes())
			renderNonIdNoColumnAttrs(pd);

		else {
			if (pd.isIdProperty && !pd.getHasColumnAttributes())
				renderIdNoColumnAttrs(pd);
			else
				renderWithColumnAttrs(pd);

			if (pd.isIdProperty) {
				levelUp();
				{
					openTag("generator");
					attr("class", pd.idGenerator);
					closeTag();
				}
				levelDown();
			}

			closeTag(pd.tag); // can be 'id' or 'property'

			renderDiscriminatorMaybe(pd);
		}

		if (pd.isIdProperty)
			renderVersionOpt();
	}

	private void renderVersionOpt() {
		List<PropertyDescriptor> versions = ed.properties.stream().filter(pd -> pd.hasVersionMd).toList();
		if (versions.isEmpty())
			return;

		if (versions.size() > 1)
			throw new IllegalArgumentException("Entity " + ed.fullName + " has multiple version properties: "
					+ versions.stream().map(pd -> pd.name).collect(Collectors.joining(", ")));

		PropertyDescriptor pd = first(versions);
		// E.g.: <version name="propName" type="long">
		openTag("version");
		attr("name", pd.name);
		optAttr("type", pd.explicitType);

		levelUp();
		{
			endOpenTag();
			// <column name="VERSION" default="0" not-null="true" />
			openTag("column");
			attr("name", pd.getQuotedColumnName());
			attr("default", defaultValueForType(pd));
			attr("not-null", "true");
			closeTag();
		}
		levelDown();

		closeTag("version");
	}

	private String defaultValueForType(PropertyDescriptor pd) {
		GmType type = pd.getGmType();
		switch (type.typeKind()) {
			case LONG:
				return "0";
			default:
				throw new UnsupportedOperationException(
						"Property " + pd.fullName() + " is marked as version, but only Long type is supported, not" + type.getTypeSignature());
		}
	}

	private void renderCollectionProperty(CollectionPropertyDescriptor pd) {
		boolean enumsAreStrings = context.versionImpliesStringsForCollectionEnums();
		
		openTag(pd.tag);
		attr("name", pd.name);
		if (!pd.isOneToMany)
			attr("table", pd.getQuotedMany2ManyTable());

		optAttr("lazy", pd.lazy);
		optAttr("cascade", pd.cascade);
		optAttr("fetch", pd.fetch);
		endOpenTag();

		levelUp();
		{
			openTag("key");
			attr("column", pd.getQuotedKeyColumn());
			optAttr("not-null", pd.keyColumnNotNull);
			optAttr("property-ref", pd.keyPropertyRef);
			optAttr("foreign-key", pd.foreignKey);
			closeTag();

			if (pd.getIsList()) {
				openTag("list-index");
				attr("column", pd.getQuotedIndexColumn());
				closeTag();

			} else if (pd.getIsMap()) {
				if (pd.hasScalarMapKey) {
					openTag("map-key");
					if (!pd.hasEnumMapKey)
						attr("type", pd.mapKeySimpleType);
					else if (!enumsAreStrings)
						attr("type", "serializable");
					endOpenTag();

					levelUp();
					{
						openTag("column");
						attr("name", pd.getQuotedMapKeyColumn());
						optAttr("index", pd.mapKeyIndexName);
						optAttr("length", pd.mapKeyLength);
						closeTag();

						if (pd.hasEnumMapKey && enumsAreStrings)
							renderTypeForEnumAsString(pd.mapKeySignature);
					}
					levelDown();

					closeTag("map-key");

				} else {
					openTag("map-key-many-to-many");
					optAttr("foreign-key", pd.mapKeyForeignKey);
					attr("class", pd.mapKeySignature);
					endOpenTag();

					levelUp();
					{
						openTag("column");
						attr("name", pd.getQuotedMapKeyColumn());
						optAttr("index", pd.mapKeyIndexName);
						closeTag();
					}
					levelDown();

					closeTag("map-key-many-to-many");
				}
			}

			if (pd.hasScalarElement) {
				openTag("element");
				attr("column", pd.getQuotedElementColumn());
				if (!pd.hasEnumElement)
					attr("type", pd.elementSimpleType);
				else if (!enumsAreStrings)
					attr("type", "serializable");
				
				optAttr("length", pd.length);
				optAttr("precision", pd.precision);
				optAttr("scale", pd.scale);

				optAttr("unique", pd.isUnique);
				optAttr("not-null", pd.isNotNull);

				if (!pd.hasEnumElement || !enumsAreStrings) {
					closeTag();
				} else {
					endOpenTag();
					renderTypeForEnumAsString(pd.elementSignature);
					closeTag("element");
				}

			} else if (pd.isOneToMany) {
				openTag("many-to-many");
				attr("class", pd.elementSignature);
				closeTag();

			} else {
				openTag("many-to-many");
				attr("class", pd.elementSignature);
				attr("column", pd.getQuotedElementColumn());

				optAttr("unique", pd.isUnique);
				optAttr("fetch", pd.manyToManyFetch);
				optAttr("foreign-key", pd.elementForeignKey);

				closeTag();
			}
		}

		levelDown();
		closeTag(pd.tag);
	}

	private void renderEnumProperty(EnumDescriptor pd) {
		openTag("property");
		attr("name", pd.name);
		attr("column", pd.getQuotedColumnName());
		optAttr("unique", pd.isUnique);
		optAttr("not-null", pd.isNotNull);
		optAttr("index", pd.index);
		endOpenTag();

		levelUp();
		{
			renderTypeForEnumAsString(pd.enumClass);
		}
		levelDown();

		closeTag("property");
	}

	private void renderTypeForEnumAsString(String enumTypeSignature) {
		openTag("type");
		attr("name", "org.hibernate.type.EnumType");
		endOpenTag();

		levelUp();
		{
			println("<param name=\"enumClass\">" + enumTypeSignature + "</param>");
			println("<param name=\"type\">12</param>");
		}
		levelDown();

		closeTag("type");
	}

	private void renderCompositeId(PropertyDescriptor pd) {
		println("<composite-id name=\"id\" class=\"com.braintribe.model.access.hibernate.gm.CompositeIdValues\">");
		levelUp();

		int i = 0;
		for (JpaColumn c : pd.getCompositeId().getColumns()) {
			openTag("key-property");
			attr("name", "value" + i++);
			attr("column", EntityMappingContextTools.quoteIdentifier(c.getName()));
			attr("type", c.getType());
			closeTag();
		}

		levelDown();
		closeTag("composite-id");
	}

	// REMOVED EMBEDDABLE
	// private void renderEmbedded(ComponentDescriptor pd) {
	// 	openTag("component");
	// 	attr("name", pd.name);
	// 	attr("class", pd.toOneEntitySignature);
	// 	endOpenTag();
	//
	// 	levelUp();
	// 	{
	// 		println("<tuplizer entity-mode=\"pojo\" class=\"com.braintribe.model.access.hibernate.gm.GmPojoComponentTuplizer\"/>");
	// 		println("<property name=\"id\" formula=\"(select concat('" + pd.getIdPrefix() + "', " + pd.getOwnerIdQuotedColumnName()
	// 				+ "))\" type=\"string\" />");
	//
	// 		for (PropertyDescriptor embeddedPd : pd.getEmbeddedProperties())
	// 			render(embeddedPd);
	//
	// 	}
	// 	levelDown();
	//
	// 	closeTag("component");
	// }

	private void renderManyToOne(PropertyDescriptor pd) {
		openTag("many-to-one");
		attr("name", pd.name);
		attr("class", pd.toOneEntitySignature);
		attr("column", pd.getQuotedColumnName());

		optAttr("unique", pd.isUnique);
		optAttr("not-null", pd.isNotNull);
		optAttr("index", pd.index);

		optAttr("lazy", pd.lazy);
		optAttr("cascade", pd.cascade);
		optAttr("fetch", pd.fetch);

		if (Boolean.TRUE.equals(pd.isInvalidReferencesIgnored))
			attr("not-found", "ignore");

		optAttr("property-ref", pd.referencedProperty);
		optAttr("foreign-key", pd.foreignKey);

		if (Boolean.TRUE.equals(pd.isReadOnly)) {
			attr("insert", "false");
			attr("update", "false");
		}

		closeTag();
	}

	private void renderNonIdNoColumnAttrs(PropertyDescriptor pd) {
		openTag(pd.tag);
		attr("name", pd.name);
		attr("column", pd.getQuotedColumnName());

		optAttr("type", pd.explicitType);
		optAttr("lazy", pd.lazy);
		if (Boolean.TRUE.equals(pd.isReadOnly)) {
			attr("insert", "false");
			attr("update", "false");
		}

		optAttr("length", pd.length);
		optAttr("precision", pd.precision);
		optAttr("scale", pd.scale);

		optAttr("unique", pd.isUnique);
		optAttr("not-null", pd.isNotNull);

		optAttr("index", pd.index);
		optAttr("unique-key", pd.uniqueKey);
		optAttr("access", pd.access);
		optAttr("node", pd.node);
		optAttr("formula", pd.formula);
		optAttr("optimistic-lock", pd.isOptimisticLock);

		closeTag();
	}

	private void renderIdNoColumnAttrs(PropertyDescriptor pd) {
		openTag(pd.tag); // id
		attr("name", pd.name);
		attr("column", pd.getQuotedColumnName());

		optAttr("type", pd.explicitType);

		optAttr("length", pd.length);
		optAttr("precision", pd.precision);
		optAttr("scale", pd.scale);

		optAttr("index", pd.index);
		optAttr("unique-key", pd.uniqueKey);
		optAttr("access", pd.access);
		optAttr("node", pd.node);
		optAttr("formula", pd.formula);
		optAttr("optimistic-lock", pd.isOptimisticLock);

		endOpenTag();
	}

	private void renderWithColumnAttrs(PropertyDescriptor pd) {
		openTag(pd.tag);
		attr("name", pd.name);
		attr("column", pd.getQuotedColumnName());
		optAttr("type", pd.explicitType);

		optAttr("lazy", pd.lazy);
		if (Boolean.TRUE.equals(pd.isReadOnly)) {
			attr("insert", "false");
			attr("update", "false");
		}

		optAttr("length", pd.length);
		optAttr("precision", pd.precision);
		optAttr("scale", pd.scale);

		optAttr("unique", pd.isUnique);
		optAttr("not-null", pd.isNotNull);

		optAttr("index", pd.index);
		optAttr("unique-key", pd.uniqueKey);
		optAttr("access", pd.access);
		optAttr("node", pd.node);
		optAttr("formula", pd.formula);
		optAttr("optimistic-lock", pd.isOptimisticLock);

		endOpenTag();

		levelUp();
		{
			openTag("column");
			optAttr("sql-type", pd.sqlType);
			optAttr("check", pd.check);
			optAttr("default", pd.defaultValue);
			optAttr("write", pd.write);
			closeTag();

		}
		levelDown();
		// @formatter:on
	}

	private void renderDiscriminatorMaybe(PropertyDescriptor pd) {
		EntityDescriptor ped = pd.entityDescriptor;
		if (pd.isIdProperty && ped.isTopLevel && ped.hasSubTypes) {
			openTag("discriminator");
			if (empty(ped.discriminatorFormula)) {
				attr("column", ped.getQuotedDiscriminatorColumnName());
				attr("type", valueOrDefault(ped.discriminatorType, "string"));
			} else {
				attr("formula", ped.discriminatorFormula);
			}
			closeTag();
		}
	}

	// #################################################
	// ## . . . . . . . . . Helpers . . . . . . . . . ##
	// #################################################

	private void optAttr(String name, Object value) {
		if (value != null)
			attr(name, value.toString());
	}

	private void optAttr(String name, String value) {
		if (!empty(value))
			attr(name, value);
	}

	private void attr(String name, String value) {
		print(" ");
		print(name);
		print("=");
		print(Q);
		print(value);
		print(Q);
	}

	private void openTag(String tag) {
		print("<");
		print(tag);
	}

	private void endOpenTag() {
		println(">");
	}

	private void closeTag(String tag) {
		println("</" + tag + ">");
	}

	private void closeTag() {
		println(" />");
	}

	private String valueOrDefault(String s, String _default) {
		return empty(s) ? _default : s;
	}

	private boolean empty(String s) {
		return s == null || s.isEmpty();
	}

}
