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

import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.xerces.dom.DOMInputImpl;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import com.google.common.collect.Maps;

public class LocalResourceResolver implements LSResourceResolver {
    private static final Logger LOG = Logger.getLogger(LocalResourceResolver.class.getName());
    private final Map<String, String> systemIdToResource = Maps.newLinkedHashMap();

    /**
     * @param systemIdToResource
     *            a map to map system IDs (key) to local resource names (value).
     */
    public LocalResourceResolver(Map<String, String> systemIdToResource) {
        this.systemIdToResource.putAll(systemIdToResource);
    }

    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        LSInput input = new DOMInputImpl(publicId, systemId, null);
        LOG.info(String.format("resolveResource: type=%s, namespaceURI=%s, publicId=%s, systemId=%s, baseURI=%s", type,
                namespaceURI, publicId, systemId, baseURI));

        String resourceName = systemIdToResource.get(systemId);
        if (resourceName == null) {
            resourceName = systemIdToResource.get("http://www.w3.org/MarkUp/SCHEMA/" + systemId);
            if (resourceName == null) {
                String errorMsg = String.format("No resource name found for system ID '%s'.", systemId);
                LOG.warning(errorMsg);
                throw new RuntimeException(errorMsg);
            }
        }

        InputStream in = getClass().getResourceAsStream(resourceName);
        if (in == null) {
            String errorMsg = String.format("Resource '%s' for system ID '%s' not found.", resourceName, systemId);
            LOG.warning(errorMsg);
            throw new RuntimeException(errorMsg);
        }

        input.setByteStream(in);
        return input;
    }

}
