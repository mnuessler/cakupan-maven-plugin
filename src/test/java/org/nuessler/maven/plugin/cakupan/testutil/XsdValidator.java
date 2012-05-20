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
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XsdValidator {
    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    private File[] schemas;
    private ErrorHandler errorHandler;

    public XsdValidator(File... schema) {
        this.schemas = schema;
    }

    public void validate(String xml) throws ValidationException {
        this.errorHandler = new CollectingErrorHandler();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        try {
            factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            factory.setAttribute(JAXP_SCHEMA_SOURCE, schemas);
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            documentBuilder.setErrorHandler(errorHandler);
            documentBuilder.parse(new InputSource(new StringReader(xml)));
        } catch (IllegalArgumentException e) {
            throw new ValidationException("XML parser does not seem to support JAXP 1.2");
        } catch (Exception e) {
            throw new ValidationException(e);
        }
    }

    private static class CollectingErrorHandler implements ErrorHandler {
        private List<SAXParseException> errors = new ArrayList<SAXParseException>();
        private List<SAXParseException> fatalErrors = new ArrayList<SAXParseException>();
        private List<SAXParseException> warnings = new ArrayList<SAXParseException>();

        @Override
        public void error(SAXParseException e) throws SAXException {
            errors.add(e);
        }

        @Override
        public void fatalError(SAXParseException e) throws SAXException {
            fatalErrors.add(e);
        }

        @Override
        public void warning(SAXParseException e) throws SAXException {
            warnings.add(e);
        }

        public boolean hasErrors() {
            return !(errors.isEmpty() && fatalErrors.isEmpty());
        }
    }
}
