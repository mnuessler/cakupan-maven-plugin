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
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;

import com.cakupan.xslt.exception.XSLTCoverageException;
import com.cakupan.xslt.instrument.InstrumentXSLT;
import com.cakupan.xslt.util.CoverageIOUtil;
import com.cakupan.xslt.util.XSLTCakupanUtil;

/**
 * @author mnuessler
 * @goal instrument
 * @phase process-test-classes
 * @requiresDependencyResolution test
 */
public class CakupanInstrumentMojo extends AbstractMojo {

	/**
	 * The base directory of the project being tested. This can be obtained in
	 * your unit test via System.getProperty("basedir").
	 * 
	 * @parameter default-value="${basedir}"
	 */
	private File basedir;

	/**
	 * The directory containing generated test classes of the project being
	 * tested. This will be included at the beginning of the test classpath. *
	 * 
	 * @parameter default-value="${project.build.directory}"
	 */
	private File buildDirectory;

	/**
	 * @parameter default-value="${project.build.outputDirectory}"
	 */
	private File buildOutputDirectory;

	/**
	 * @parameter expression="${xslt.instrument.destdir}"
	 *            default-value="${project.build.directory}/cakupan-instrument"
	 */
	private File instrumentDestDir;

	/**
	 * A list of &lt;include> elements specifying the tests (by pattern) that
	 * should be included in testing. When not specified and when the
	 * <code>test</code> parameter is not specified, the default includes will
	 * be <code><br/>
	 * &lt;includes><br/>
	 * &nbsp;&lt;include>**&#47;Test*.java&lt;/include><br/>
	 * &nbsp;&lt;include>**&#47;*Test.java&lt;/include><br/>
	 * &nbsp;&lt;include>**&#47;*TestCase.java&lt;/include><br/>
	 * &lt;/includes><br/>
	 * </code> This parameter is ignored if the TestNG
	 * <code>suiteXmlFiles</code> parameter is specified.
	 * 
	 * @parameter
	 */
	private List<String> includes;

	/**
	 * @parameter
	 */
	private List<String> excludes;

	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("instrumentDestDir: " + instrumentDestDir);
		getLog().info("buildOutputDirectory: " + buildOutputDirectory);
		if (includes == null) {
			getLog().info("includes is null");
		} else {
			for (String s : includes) {
				getLog().info(s);
			}
		}

		if (!instrumentDestDir.exists()) {
			instrumentDestDir.mkdirs();
		}

		if (CollectionUtils.isEmpty(includes)) {
			includes = Collections.singletonList("**/*.xslt");
		}

		InstrumentXSLT instrumentXslt = new InstrumentXSLT();
		getLog().info("Start instrumenting XSLTs.");
		CoverageIOUtil.setDestDir(instrumentDestDir);

		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setBasedir(buildOutputDirectory);
		scanner.setCaseSensitive(true);
		scanner.addDefaultExcludes();
		if (CollectionUtils.isNotEmpty(includes)) {
			scanner.setIncludes(includes.toArray(new String[includes.size()]));
		}
		if (CollectionUtils.isNotEmpty(excludes)) {
			scanner.setExcludes(excludes.toArray(new String[excludes.size()]));
		}
		scanner.scan();
		String[] includedFiles = scanner.getIncludedFiles();

		if (ArrayUtils.isEmpty(includedFiles)) {
			getLog().info("No XSLT files found");
			return;
		}

		for (String includedFile : includedFiles) {
			try {
				getLog().info("Instrumenting XSLT file " + includedFile);
				instrumentXslt.initCoverageMap(new File(buildOutputDirectory,
						includedFile).getCanonicalPath());
			} catch (Exception e) {
				getLog().error(e);
				throw new MojoExecutionException("Instrumenting file ["
						+ includedFile + "] failed!");
			}
		}

		try {
			XSLTCakupanUtil.dumpCoverageStats();
		} catch (XSLTCoverageException e) {
			throw new MojoExecutionException("Dump coverage file failed!");
		} finally {
			XSLTCakupanUtil.cleanCoverageStats();
		}
	}

}
