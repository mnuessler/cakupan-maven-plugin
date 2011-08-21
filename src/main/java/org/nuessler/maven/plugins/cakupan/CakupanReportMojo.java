package org.nuessler.maven.plugins.cakupan;

import java.util.Locale;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

/**
 * @author mnuessler
 * @goal myreport
 * @phase site
 */
public class CakupanReportMojo extends AbstractMavenReport {

	public String getOutputName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName(Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDescription(Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Renderer getSiteRenderer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getOutputDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected MavenProject getProject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void executeReport(Locale locale) throws MavenReportException {
		Sink sink = getSink();
		sink.body();
	}

}
