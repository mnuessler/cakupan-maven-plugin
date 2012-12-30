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

public class SimpleNamespaceContext implements NamespaceContext {
    private final HashBiMap<String, String> namespaces = HashBiMap.create();

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