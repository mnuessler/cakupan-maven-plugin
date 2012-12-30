package org.nuessler.maven.plugin.cakupan.testutil;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Simple {@link EntityResolver} which retrieves the entity via HTTP if the
 * given system ID is a valid {@link URL}. Returns <code>null</code> otherwise.
 *
 * @author Matthias Nuessler
 */
public class SimpleHttpEntityResolver implements EntityResolver {

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        URLConnection con = new URL(systemId).openConnection();
        InputSource source = new InputSource(con.getInputStream());
        source.setSystemId(systemId);
        source.setPublicId(publicId);
        return source;
    }

}
