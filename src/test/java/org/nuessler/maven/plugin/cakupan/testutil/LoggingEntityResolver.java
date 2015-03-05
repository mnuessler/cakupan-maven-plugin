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
import java.util.logging.Logger;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * {@link EntityResolver} which logs <code>publicId</code> and
 * <code>systemId</code> to resolve and then delegates to the given
 * {@link EntityResolver} delegate or to the {@link DefaultHandler} if none
 * given. May be useful for debugging.
 *
 * @author Matthias Nuessler
 */
public class LoggingEntityResolver implements EntityResolver {
    private static final Logger LOG = Logger.getLogger(LoggingEntityResolver.class.getName());
    private final EntityResolver delegate;

    public LoggingEntityResolver() {
        this(new DefaultHandler());
    }

    public LoggingEntityResolver(EntityResolver delegate) {
        this.delegate = delegate;
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        LOG.info(String.format("Resolving publicId='%s', systemId='%s'", publicId, systemId));
        return delegate.resolveEntity(publicId, systemId);
    }
}
