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

import javax.xml.bind.ValidationException;
import javax.xml.transform.TransformerException;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;

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
            String xml = DomUtil.documentToString(transformedXml);
            try {
                new XsdValidator(getEntityResolver(), schema).validate(xml);
            } catch (ValidationException e) {
                errorCollector.addError(e);
            }
        }
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

    /**
     * Return the {@link EntityResolver} to be used by the validator. May be
     * overridden to provide a custom {@link EntityResolver}. May return
     * <code>null</code> if the default implematation should be used.
     *
     * @return the {@link EntityResolver} to be used by the {@link XsdValidator}
     *         or <code>null</code> if the default implementation should be
     *         used.
     */
    protected EntityResolver getEntityResolver() {
        return null;
    }

}
