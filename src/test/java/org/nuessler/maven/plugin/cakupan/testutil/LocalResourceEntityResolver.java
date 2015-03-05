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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

/**
 * XML {@link EntityResolver} which resolves system IDs (i.e. schema URLs) from
 * local resources. Can be used to avoid (repeated) downloads of schema files
 * when validating against a schema. Requires a mapping from the system ID to a
 * local resource name which can then be used to read the resource using
 * {@link Class#getResourceAsStream(String)}.
 *
 * @author Matthias Nuessler
 */
public class LocalResourceEntityResolver implements EntityResolver {
    private static final Logger LOG = Logger.getLogger(LocalResourceEntityResolver.class.getName());
    private final Map<String, String> systemIdToResource = Maps.newLinkedHashMap();

    /**
     * @param systemIdToResource
     *            a map to map system IDs (key) to local resource names (value).
     */
    public LocalResourceEntityResolver(Map<String, String> systemIdToResource) {
        this.systemIdToResource.putAll(systemIdToResource);
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        String resourceName = systemIdToResource.get(systemId);
        if (resourceName == null) {
            String errorMsg = String.format("No resource name found for system ID '%s'.", systemId);
            LOG.warning(errorMsg);
            throw new IOException(errorMsg);
        }

        InputStream in = getClass().getResourceAsStream(resourceName);
        if (in == null) {
            String errorMsg = String.format("Resource '%s' for system ID '%s' not found.", resourceName, systemId);
            LOG.warning(errorMsg);
            throw new IOException(errorMsg);
        }

        InputSource source = new InputSource(in);
        source.setSystemId(systemId);
        source.setPublicId(publicId);
        return source;
    }

}
