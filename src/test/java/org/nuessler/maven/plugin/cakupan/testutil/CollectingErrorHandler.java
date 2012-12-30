package org.nuessler.maven.plugin.cakupan.testutil;

import java.util.Collections;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.google.common.collect.Lists;

/**
 * {@link ErrorHandler} implementation that collects all parsing errors and
 * warning in lists for later checking.
 *
 * @author Matthias Nuessler
 */
public class CollectingErrorHandler implements ErrorHandler {
    private final List<SAXParseException> errors = Lists.newArrayList();
    private final List<SAXParseException> fatalErrors = Lists.newArrayList();
    private final List<SAXParseException> warnings = Lists.newArrayList();

    @Override
    public void error(SAXParseException e) throws SAXException {
        errors.add(e);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        fatalErrors.add(e);
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        warnings.add(e);
    }

    /**
     * @return <code>true</code> if the error handler collected any errors
     *         (fatal or normal).
     */
    public boolean hasErrors() {
        return !(errors.isEmpty() && fatalErrors.isEmpty());
    }

    /**
     * @return an unmodifiable list of collected errors
     */
    public List<SAXParseException> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    /**
     * @return an unmodifiable list of collected fatal errors
     */
    public List<SAXParseException> getFatalErrors() {
        return Collections.unmodifiableList(fatalErrors);
    }

    /**
     * @return an unmodifiable list of collected warnings
     */
    public List<SAXParseException> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }

}
