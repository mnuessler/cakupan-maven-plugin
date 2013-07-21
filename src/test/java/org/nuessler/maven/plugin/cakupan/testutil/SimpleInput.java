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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.ls.LSInput;

public class SimpleInput implements LSInput {
    private static Logger LOG = Logger.getLogger(SimpleInput.class.getName());
    private InputStream byteStream;
    private String systemId;
    private String publicId;
    private String baseURI;
    private String encoding = "UTF-8";
    private boolean certifiedText;

    public SimpleInput(String publicId, String systemId, String baseURI) {
        this.publicId = publicId;
        this.systemId = systemId;
        this.baseURI = baseURI;
    }

    @Override
    public Reader getCharacterStream() {
        LOG.info("getCharacterStream");
        return new InputStreamReader(byteStream);
    }

    @Override
    public void setCharacterStream(Reader characterStream) {
        LOG.info("setCharacterStream");
        try {
            this.byteStream = new ByteArrayInputStream(IOUtils.toByteArray(characterStream));
        } catch (IOException e) {
        }
    }

    @Override
    public InputStream getByteStream() {
        LOG.info("getByteStream");
        return byteStream;
    }

    @Override
    public void setByteStream(InputStream byteStream) {
        LOG.info("setByteStream");
        this.byteStream = byteStream;
    }

    @Override
    public String getStringData() {
        LOG.info("getStringData");
        try {
            String data = IOUtils.toString(byteStream, encoding);
            LOG.info("data:" + data);
            return data;
        } catch (IOException e) {
            LOG.info(e.getMessage());
            return null;
        }
    }

    @Override
    public void setStringData(String stringData) {
        LOG.info("setStringData");
        this.byteStream = IOUtils.toInputStream(stringData);
    }

    @Override
    public String getSystemId() {
        LOG.info("getSystemId");
        return systemId;
    }

    @Override
    public void setSystemId(String systemId) {
        LOG.info("setSystemId");
        this.systemId = systemId;
    }

    @Override
    public String getPublicId() {
        LOG.info("getPublicId");
        return publicId;
    }

    @Override
    public void setPublicId(String publicId) {
        LOG.info("setPublicId");
        this.publicId = publicId;
    }

    @Override
    public String getBaseURI() {
        LOG.info("getBaseURI");
        return baseURI;
    }

    @Override
    public void setBaseURI(String baseURI) {
        this.baseURI = baseURI;
    }

    @Override
    public String getEncoding() {
        return encoding;
    }

    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public boolean getCertifiedText() {
        return certifiedText;
    }

    @Override
    public void setCertifiedText(boolean certifiedText) {
        this.certifiedText = certifiedText;
    }

}
