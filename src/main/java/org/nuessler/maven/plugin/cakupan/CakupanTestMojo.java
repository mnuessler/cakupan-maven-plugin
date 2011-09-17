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

import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executeMojo;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.groupId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;
import static org.twdata.maven.mojoexecutor.MojoExecutor.version;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @phase test
 * @goal test
 * @execute phase="test" lifecycle="cakupan"
 * @requiresDependencyResolution test
 * @author mnuessler
 */
public class CakupanTestMojo extends AbstractMojo {
	/**
	 * The Maven Project Object
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * The Maven Session Object
	 * 
	 * @parameter expression="${session}"
	 * @required
	 * @readonly
	 */
	private MavenSession session;

	/**
	 * The Maven PluginManager Object
	 * 
	 * @component
	 * @required
	 */
	private PluginManager pluginManager;

	/**
	 * <i>Maven Internal</i>: List of artifacts for the plugin.
	 * 
	 * @parameter default-value="${plugin.artifacts}"
	 * @required
	 * @readonly
	 */
	protected List<Artifact> pluginArtifacs;

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
	private List includes;

	/**
	 * A list of &lt;exclude> elements specifying the tests (by pattern) that
	 * should be excluded in testing. When not specified and when the
	 * <code>test</code> parameter is not specified, the default excludes will
	 * be <code><br/>
	 * &lt;excludes><br/>
	 * &nbsp;&lt;exclude>**&#47;*$*&lt;/exclude><br/>
	 * &lt;/excludes><br/>
	 * </code> (which excludes all inner classes).<br>
	 * This parameter is ignored if the TestNG <code>suiteXmlFiles</code>
	 * parameter is specified.
	 * 
	 * @parameter
	 */
	private List excludes;

	/**
	 * @parameter expression="${xslt.instrument.destdir}"
	 *            default-value="${project.build.directory}/cakupan-report"
	 */
	private File instrumentDestDir;

	public void execute() throws MojoExecutionException, MojoFailureException {
		String destDir = null;
		try {
			destDir = instrumentDestDir.getCanonicalPath();
		} catch (IOException e) {
			throw new MojoFailureException("dest dir not found");
		}

		String[] classpathElements = getAdditionalClasspathElements();
		getLog().info("add to classpath: " + Arrays.toString(classpathElements));
		executeMojo(
				plugin(groupId("org.apache.maven.plugins"),
						artifactId("maven-surefire-plugin"), version("2.9")),
				goal("test"),
				configuration(
						// element(name("reportsDirectory"), destDir),
						element(name("includes"),
								element(name("include"),
										"**/*TransformationTest.java"),
								element(name("include"), "**/*XsltTest.java")),
						element(name("additionalClasspathElements"),
								element(name("additionalClasspathElement"),
										classpathElements[0]),
								element(name("additionalClasspathElement"),
										classpathElements[1]),
								element(name("additionalClasspathElement"),
										classpathElements[2]),
								element(name("additionalClasspathElement"),
										classpathElements[3]),
								element(name("additionalClasspathElement"),
										classpathElements[4]),
								element(name("additionalClasspathElement"),
										classpathElements[5])),
						element(name("systemPropertyVariables"),
								element(name("javax.xml.transform.TransformerFactory"),
										"com.cakupan.xslt.transform.SaxonCakupanTransformerInstrumentFactoryImpl"),
								element(name("cakupan.dir"), destDir))),
				executionEnvironment(project, session, pluginManager));
	}

	private String[] getAdditionalClasspathElements() {
		final List<String> cakupanDepArtifactIds = Arrays.asList(
				"cakupan-xslt", "xstream", "saxon", "xalan", "xpp3");
		Collection<Artifact> cakupanArtifacts = Collections2.filter(
				pluginArtifacs, new Predicate<Artifact>() {
					public boolean apply(Artifact input) {
						return cakupanDepArtifactIds.contains(input
								.getArtifactId());
					}
				});

		Collection<String> classpathElements = Collections2.transform(
				cakupanArtifacts, new Function<Artifact, String>() {
					public String apply(final Artifact input) {
						try {
							return input.getFile().getCanonicalPath();
						} catch (final IOException e) {
							getLog().error(e);
							return "";
						}
					}
				});
		return classpathElements.toArray(new String[classpathElements.size()]);
	}
}
