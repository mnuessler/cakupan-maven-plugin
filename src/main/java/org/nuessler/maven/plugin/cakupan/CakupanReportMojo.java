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
package org.nuessler.maven.plugin.cakupan;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.apache.tools.ant.BuildException;

import com.cakupan.xslt.exception.XSLTCoverageException;
import com.cakupan.xslt.util.CoverageIOUtil;
import com.cakupan.xslt.util.XSLTCakupanUtil;

/**
 * @author mnuessler
 * @goal report
 * @execute phase="site"
 */
public class CakupanReportMojo extends AbstractMavenReport {

	/**
	 * <i>Maven Internal</i>: The Doxia Site Renderer.
	 * 
	 * @component
	 */
	private Renderer siteRenderer;

	/**
	 * <i>Maven Internal</i>: Project to interact with.
	 * 
	 * @parameter default-value="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * The output directory for the report.
	 * 
	 * @parameter default-value="${project.reporting.outputDirectory}/cakupan"
	 * @required
	 */
	private File outputDirectory;

	public String getOutputName() {
		return "cakupan-index";
	}

	public String getName(Locale locale) {
		return getBundle(locale).getString("report.cakupan.name");
	}

	public String getDescription(Locale locale) {
		return getBundle(locale).getString("report.cakupan.description");
	}

	private ResourceBundle getBundle(Locale locale) {
		return ResourceBundle.getBundle("cakupan-report", locale);
	}

	@Override
	protected Renderer getSiteRenderer() {
		return siteRenderer;
	}

	@Override
	protected String getOutputDirectory() {
		return outputDirectory.getAbsolutePath();
	}

	@Override
	protected MavenProject getProject() {
		return project;
	}

	@Override
	public boolean isExternalReport() {
		return true;
	}

	@Override
	public boolean canGenerateReport() {
		return super.canGenerateReport();
	}

	@Override
	protected void executeReport(Locale locale) throws MavenReportException {
		if (!outputDirectory.exists()) {
			outputDirectory.mkdirs();
		}
		getLog().info("Start reporttask mojo");
		getLog().info("output dir: " + getOutputDirectory());
		CoverageIOUtil.setDestDir(outputDirectory);
		try {
			XSLTCakupanUtil.generateCoverageReport();
		} catch (XSLTCoverageException e) {
			if (e.getRefId() == XSLTCoverageException.NO_COVERAGE_FILE) {
				getLog().error(
						"No coverage files found in ["
								+ outputDirectory.getPath() + "]!");
			} else {
				throw new BuildException("Failed to make a coverage report!", e);
			}
		}
		getLog().info("End reporttask mojo");
	}

}
