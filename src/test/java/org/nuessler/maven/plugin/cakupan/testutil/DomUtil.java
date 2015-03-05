/**
 * Copyright 2011-2015 Matthias Nuessler <m.nuessler@web.de>
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

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Provides utility methods to convert a DOM document into a string and vice
 * versa.
 *
 * @author Matthias Nuessler
 */
public final class DomUtil {

    private DomUtil() {
        // prevent instantiation
    }

    /**
     * Converts an XML string into a DOM {@link Document}.
     *
     * @param xml
     *            the XML string to convert
     * @return the equivalent XML DOM document
     * @throws Exception
     *             if the given string does not contain valid XML or conversion
     *             fails for other reasons
     */
    public static Document stringToDocument(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        InputSource input = new InputSource(new StringReader(xml));
        return factory.newDocumentBuilder().parse(input);
    }

    /**
     * Converts a DOM {@link Document} into a string.
     *
     * @param doc
     *            the DOM document to convert
     * @return the XML document as a string
     * @throws TransformerException
     *             if conversion fails
     */
    public static String documentToString(Document doc) throws TransformerException {
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.newTransformer().transform(source, result);
        return writer.toString();
    }

}
