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

import java.io.File;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.bind.ValidationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

public class XsdValidator {
    private final File schemaFile;
    private ErrorHandler errorHandler;
    private final LSResourceResolver resourceResolver;

    public XsdValidator(File schemaFile) {
        this(schemaFile, null);
    }

    public XsdValidator(File schemaFile, LSResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
        this.schemaFile = schemaFile;
    }

    public void validate(String xml) throws ValidationException {
        this.errorHandler = new CollectingErrorHandler();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaFactory.setResourceResolver(resourceResolver);
        Source schemaSource = new StreamSource(schemaFile);
        try {
            Schema schema = schemaFactory.newSchema(schemaSource);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setSchema(schema);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(errorHandler);
            InputSource input = new InputSource(new StringReader(xml));
            builder.parse(input);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("XML parser does not seem to support JAXP 1.2");
        } catch (Exception e) {
            throw new ValidationException(e);
        }
    }

}
