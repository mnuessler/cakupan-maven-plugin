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

import java.io.File;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.ValidationException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.w3c.dom.Document;

import com.google.common.collect.HashBiMap;

public abstract class XslTransformationTestCase {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Test
    public void testTransformation() throws TransformerException {
        XslTransformer transformer = new XslTransformer(getTransformationFile());
        Document transformedXml = transformer.transform(getXmlInputFile());
        checkResult(transformedXml);

        File schema = getTargetSchemaFile();
        if (schema != null) {
            String xml = documentToString(transformedXml);
            try {
                new XsdValidator(schema).validate(xml);
            } catch (ValidationException e) {
                errorCollector.addError(e);
            }
        }
    }

    private String documentToString(Document doc) throws TransformerException {
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        return writer.toString();
    }

    protected <T> void assertThat(T value, Matcher<T> matcher) {
        assertThat("", value, matcher);
    }

    protected <T> void assertThat(String reason, T value, Matcher<T> matcher) {
        errorCollector.checkThat(reason, value, matcher);
    }

    protected abstract File getTransformationFile();

    protected abstract File getXmlInputFile();

    protected abstract File getTargetSchemaFile();

    protected abstract void checkResult(Document doc);

    public static class SimpleNamespaceContext implements NamespaceContext {
        private HashBiMap<String, String> namespaces = HashBiMap.create();

        public SimpleNamespaceContext() {
        }

        public SimpleNamespaceContext(String prefix, String namespaceURI) {
            namespaces.put(prefix, namespaceURI);
        }

        public SimpleNamespaceContext(Map<String, String> prefixNamespaceMap) {
            namespaces.putAll(prefixNamespaceMap);
        }

        public SimpleNamespaceContext put(String prefix, String namespaceURI) {
            namespaces.put(prefix, namespaceURI);
            return this;
        }

        @Override
        public String getNamespaceURI(String prefix) {
            return namespaces.get(prefix);
        }

        @Override
        public String getPrefix(String namespaceURI) {
            return namespaces.inverse().get(namespaceURI);
        }

        @Override
        public Iterator<String> getPrefixes(String namespaceURI) {
            return Collections.singletonList(getPrefix(namespaceURI)).iterator();
        }
    }

}
