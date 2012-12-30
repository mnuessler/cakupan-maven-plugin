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
