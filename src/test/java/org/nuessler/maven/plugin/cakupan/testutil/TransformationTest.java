/**
 * Copyright 2011 Matthias Nuessler <m.nuessler@web.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nuessler.maven.plugin.cakupan.testutil;

import static java.util.Arrays.asList;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.hamcrest.xml.HasXPath.hasXPath;

import java.io.File;
import java.net.URL;
import java.util.Map;

import javax.annotation.Nullable;
import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class TransformationTest extends XslTransformationTestCase {
    private static final String TRUE = Boolean.TRUE.toString();
    private static final String SCHEMA_PATH = "/schemas/xhtml/";
    private static Map<String, String> systemIdToResource = createSystemIdToResourceMap();

    @Override
    protected File getTransformationFile() {
        return resourceNameToFile("/books.xsl");
    }

    @Override
    protected File getXmlInputFile() {
        return resourceNameToFile("/books.xml");
    }

    @Override
    protected File getTargetSchemaFile() {
        return resourceNameToFile(SCHEMA_PATH + "xhtml-basic.xsd");
    }

    @Override
    protected EntityResolver getEntityResolver() {
        return new LocalResourceEntityResolver(systemIdToResource);
    }

    private File resourceNameToFile(String resource) {
        URL resourceUrl = getClass().getResource(resource);
        String reason = String.format("resource with name '%s' not found", resource);
        assertThat(reason, resourceUrl, notNullValue());
        return new File(resourceUrl.getFile());
    }

    @Override
    protected void checkResult(Document doc) {
        NamespaceContext ctx = new SimpleNamespaceContext("x", "http://www.w3.org/1999/xhtml");

        assertThat(doc, hasXPath("//x:title", ctx, equalTo("Reading List")));

        assertThat(doc, hasXPath("//x:table/x:th/text()='Title'", ctx, equalTo(TRUE)));
        assertThat(doc, hasXPath("//x:table/x:th", ctx, equalTo("Title")));
        assertThat(doc, hasXPath("//x:table/x:th/text()='Author'", ctx, equalTo(TRUE)));
        assertThat(doc, hasXPath("//x:table/x:th/text()='Pages'", ctx, equalTo(TRUE)));
        assertThat(doc, hasXPath("//x:table/x:th/text()='Publisher'", ctx, equalTo(TRUE)));
        assertThat(doc, hasXPath("//x:table/x:th/text()='Edition'", ctx, equalTo(TRUE)));

        checkBookCleanCode(doc, ctx);
        checkBookContinousDelivery(doc, ctx);
        checkBookProgrammingRuby(doc, ctx);
        checkBookWebOperations(doc, ctx);
        checkBookRubyOnRailsTutorial(doc, ctx);
    }

    private void checkBookCleanCode(Document doc, NamespaceContext ctx) {
        assertThat(doc, hasXPath("//x:tr[@id='1']/x:td[1]", ctx, startsWith("Clean Code")));
        assertThat(doc, hasXPath("//x:tr[@id='1']/x:td[2]", ctx, containsString("Robert C. Martin")));
        assertThat(doc, hasXPath("//x:tr[@id='1']/x:td[3]", ctx, equalTo("464")));
        assertThat(doc, hasXPath("//x:tr[@id='1']/x:td[4]", ctx, equalTo("Prentice Hall")));
        assertThat(
                doc,
                hasXPath("//x:tr[@id='1']/x:td[5]", ctx,
                        allOf(containsString("1st edition"), containsString("August 11, 2008"))));
        assertThat(doc, hasXPath("//x:tr[@id='1']/x:td[6]", ctx, equalTo("0132350882")));
    }

    private void checkBookContinousDelivery(Document doc, NamespaceContext ctx) {
        assertThat(doc, hasXPath("//x:tr[@id='2']/x:td[1]", ctx, startsWith("Continuous Delivery")));
        assertThat(doc,
                hasXPath("//x:tr[@id='2']/x:td[2]", ctx, stringContainsInOrder(asList("Jez Humble", "David Farley"))));
        assertThat(doc, hasXPath("//x:tr[@id='2']/x:td[3]", ctx, equalTo("512")));
        assertThat(doc, hasXPath("//x:tr[@id='2']/x:td[4]", ctx, startsWith("Addison-Wesley")));
        assertThat(
                doc,
                hasXPath("//x:tr[@id='2']/x:td[5]", ctx,
                        allOf(containsString("1st edition"), containsString("August 6, 2010"))));
        assertThat(doc, hasXPath("//x:tr[@id='2']/x:td[6]", ctx, equalTo("0321601912")));
    }

    private void checkBookProgrammingRuby(Document doc, NamespaceContext ctx) {
        assertThat(doc, hasXPath("//x:tr[@id='3']/x:td[1]", ctx, startsWith("Programming Ruby")));
        assertThat(
                doc,
                hasXPath("//x:tr[@id='3']/x:td[2]", ctx,
                        stringContainsInOrder(asList("Dave Thomas", "Chad Fowler", "Andy Hunt"))));
        assertThat(doc, hasXPath("//x:tr[@id='3']/x:td[3]", ctx, equalTo("1000")));
        assertThat(doc, hasXPath("//x:tr[@id='3']/x:td[4]", ctx, equalTo("Pragmatic Bookshelf")));
        assertThat(
                doc,
                hasXPath("//x:tr[@id='3']/x:td[5]", ctx,
                        allOf(containsString("3rd edition"), containsString("April 28, 2009"))));
        assertThat(doc, hasXPath("//x:tr[@id='3']/x:td[6]", ctx, equalTo("1934356085")));
    }

    private void checkBookWebOperations(Document doc, NamespaceContext ctx) {
        assertThat(doc, hasXPath("//x:tr[@id='4']/x:td[1]", ctx, startsWith("Web Operations")));
        assertThat(
                doc,
                hasXPath("//x:tr[@id='4']/x:td[2]", ctx, stringContainsInOrder(asList("John Allspaw", "Jesse Robbins"))));
        assertThat(doc, hasXPath("//x:tr[@id='4']/x:td[3]", ctx, equalTo("336")));
        assertThat(doc, hasXPath("//x:tr[@id='4']/x:td[4]", ctx, startsWith("O'Reilly")));
        assertThat(
                doc,
                hasXPath("//x:tr[@id='4']/x:td[5]", ctx,
                        allOf(containsString("1st edition"), containsString("June 28, 2010"))));
        assertThat(doc, hasXPath("//x:tr[@id='4']/x:td[6]", ctx, equalTo("1449377440")));
    }

    private void checkBookRubyOnRailsTutorial(Document doc, NamespaceContext ctx) {
        assertThat(doc, hasXPath("//x:tr[@id='5']/x:td[1]", ctx, startsWith("Ruby on Rails")));
        assertThat(doc, hasXPath("//x:tr[@id='5']/x:td[2]", ctx, stringContainsInOrder(asList("Michael Hartl"))));
        assertThat(doc, hasXPath("//x:tr[@id='5']/x:td[3]", ctx, equalTo("576")));
        assertThat(doc, hasXPath("//x:tr[@id='5']/x:td[4]", ctx, startsWith("Addison-Wesley")));
        assertThat(
                doc,
                hasXPath("//x:tr[@id='5']/x:td[5]", ctx,
                        allOf(containsString("1st edition"), containsString("December 26, 2010"))));
        assertThat(doc, hasXPath("//x:tr[@id='5']/x:td[6]", ctx, equalTo("0321743121")));
    }

    /*
     * Creates a map which maps all system IDs required to validate an XHTML
     * document to local resources. So it can be avoided to download the files
     * for every validation using a custom EntityResolver.
     */
    private static Map<String, String> createSystemIdToResourceMap() {
        Map<String, String> m = Maps.newLinkedHashMap();

        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-datatypes-1.xsd", "xhtml-datatypes-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-framework-1.xsd", "xhtml-framework-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-attribs-1.xsd", "xhtml-attribs-1.xsd");
        m.put("http://www.w3.org/2001/xml.xsd", "xml.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-text-1.xsd", "xhtml-text-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-blkphras-1.xsd", "xhtml-blkphras-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-blkstruct-1.xsd", "xhtml-blkstruct-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-inlphras-1.xsd", "xhtml-inlphras-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-inlstruct-1.xsd", "xhtml-inlstruct-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-hypertext-1.xsd", "xhtml-hypertext-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-list-1.xsd", "xhtml-list-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-struct-1.xsd", "xhtml-struct-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-pres-1.xsd", "xhtml-pres-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-blkpres-1.xsd", "xhtml-blkpres-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-inlpres-1.xsd", "xhtml-inlpres-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-link-1.xsd", "xhtml-link-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-meta-1.xsd", "xhtml-meta-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-base-1.xsd", "xhtml-base-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-script-1.xsd", "xhtml-script-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-style-1.xsd", "xhtml-style-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-inlstyle-1.xsd", "xhtml-inlstyle-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-image-1.xsd", "xhtml-image-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-object-1.xsd", "xhtml-object-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-param-1.xsd", "xhtml-param-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-basic-table-1.xsd", "xhtml-basic-table-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-form-1.xsd", "xhtml-form-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-events-1.xsd", "xhtml-events-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-target-1.xsd", "xhtml-target-1.xsd");
        m.put("http://www.w3.org/MarkUp/SCHEMA/xhtml-inputmode-1.xsd", "xhtml-inputmode-1.xsd");

        return Maps.transformValues(m, new Function<String, String>() {
            @Override
            @Nullable
            public String apply(@Nullable String input) {
                return (input == null) ? null : (SCHEMA_PATH + input);
            }
        });
    }

}
