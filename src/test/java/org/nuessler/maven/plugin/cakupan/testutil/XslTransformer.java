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
import java.util.logging.Logger;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

public class XslTransformer {
    // TODO use log4j or slf4j etc
    private final Logger log = Logger.getLogger(getClass().getName());
    private final TransformerFactory factory = TransformerFactory.newInstance();
    private final Templates cachedXslt;

    public XslTransformer(File xsltFile) throws TransformerConfigurationException {
        this.cachedXslt = factory.newTemplates(new StreamSource(xsltFile));
    }

    public Document transform(File xmlInputFile) throws TransformerException {
        System.out.println("TransformerFactory: " + factory.getClass());
        StringWriter writer = new StringWriter();

        Transformer transformer = cachedXslt.newTransformer();
        transformer.transform(new StreamSource(xmlInputFile), new StreamResult(writer));

        try {
            log.info(writer.toString());
            return DomUtil.stringToDocument(writer.toString());
        } catch (Exception e) {
            throw new TransformerException(e);
        }
    }

}
