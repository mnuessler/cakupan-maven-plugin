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

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

import com.google.common.collect.HashBiMap;

/**
 * Simple Map-based {@link NamespaceContext} implementation.
 *
 * @author Matthias Nuessler
 * @see org.hamcrest.xml.HasXPath#hasXPath(String, NamespaceContext)
 * @see org.hamcrest.xml.HasXPath#hasXPath(String, NamespaceContext,
 *      org.hamcrest.Matcher)
 */
public class SimpleNamespaceContext implements NamespaceContext {
    private final HashBiMap<String, String> namespaces = HashBiMap.create();

    /**
     * Creates an empty context.
     */
    public SimpleNamespaceContext() {
    }

    /**
     * Creates a context instance containing a single prefix and namespaxce URI.
     *
     * @param prefix
     *            the namespace prefix
     * @param namespaceURI
     *            the URI mapped to the given prefix.
     */
    public SimpleNamespaceContext(String prefix, String namespaceURI) {
        namespaces.put(prefix, namespaceURI);
    }

    /**
     * Creates a context instance using the provided map. The map must contain
     * prefixes as keys and URIs as values.
     *
     * @param prefixNamespaceMap
     *            the map with prefix / namespace URI pairs the context should
     *            contain initially.
     */
    public SimpleNamespaceContext(Map<String, String> prefixNamespaceMap) {
        namespaces.putAll(prefixNamespaceMap);
    }

    /**
     * Adds the given prefix and namespace mapping to the context.
     *
     * @param prefix
     *            the prefix
     * @param namespaceURI
     *            the namespace URI for the given prefix.
     * @return the current instance to allow method chaining.
     */
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