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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * @phase generate-test-sources
 * @goal build-classpath
 * @requiresDependencyResolution test
 * @author mnuessler
 */
public class CakupanClasspathMojo extends AbstractMojo {

	/**
	 * @parameter expression="${project}"
	 * @readonly
	 * @required
	 */
	private MavenProject project;

	/**
	 * @component
	 */
	private ArtifactFactory artifactFactory;

	/**
	 * @component
	 */
	private ArtifactResolver artifactResolver;

	/**
	 * <i>Maven Internal</i>: List of artifacts for the plugin.
	 * 
	 * @parameter default-value="${plugin.artifacts}"
	 * @required
	 * @readonly
	 */
	protected List<Artifact> pluginClasspathList;

	/**
	 * ArtifactRepository of the localRepository. To obtain the directory of
	 * localRepository in unit tests use System.getProperty("localRepository").
	 * 
	 * @parameter expression="${localRepository}"
	 * @required
	 * @readonly
	 */
	private ArtifactRepository localRepository;

	private String cakupunGroupId = "com.cakupan";

	private String cakupanArtifactId = "cakupan-xslt";

	private String cakupanVersion = "1.0";

	private Artifact cakupanArtifact;

	@SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException, MojoFailureException {
		final List<String> cakupanDepArtifactIds = Arrays.asList(
				"cakupan-xslt", "xstream", "saxon", "xalan", "xpp3");
		Collection<Artifact> cakupanArtifacts = Collections2.filter(
				pluginClasspathList, new Predicate<Artifact>() {
					public boolean apply(Artifact input) {
						return cakupanDepArtifactIds.contains(input
								.getArtifactId());
					}
				});

		for (Artifact artifact : cakupanArtifacts) {
			if (!projectContainsArtifact(artifact)) {
				addArtifact(artifact);
			} else {
				getLog().info("Cakupan artifact exists in project");
			}
		}

		Collection<String> classpathElements = Collections2.transform(
				project.getArtifacts(), new Function<Artifact, String>() {
					public String apply(Artifact input) {
						try {
							return input.getFile().getCanonicalPath();
						} catch (IOException e) {
							getLog().error(e);
							return "";
						}
					}
				});

		List<String> reverse = Lists.reverse(new ArrayList<String>(
				classpathElements));
		String classpath = StringUtils.join(reverse.iterator(),
				File.pathSeparator);
		if (!classpath.contains("cakupan")) {
			getLog().warn("Cakupan not on classpath!");
		}
		// TODO use: project.getProperties().put(key, value)
		System.setProperty("cakupan.maven.test.classpath", classpath);
		getLog().info("buildClasspath: " + classpath);
	}

	private void addArtifact(Artifact artifact) {
		getLog().info("Adding cakupan artifact to project");
		// Artifact cakupanArtifact = artifactFactory.createArtifact(
		// cakupunGroupId, cakupanArtifactId, cakupanVersion,
		// Artifact.SCOPE_TEST, "jar");
		// TODO resolve artifacts
		project.getArtifacts().add(artifact);
		// project.getArtifacts().add(
		// artifactFactory.createArtifact("com.thoughtworks.xstream",
		// "xsteam", "1.2.2", Artifact.SCOPE_TEST, "jar"));
	}

	private void resolveArtifact(Artifact artifact)
			throws MojoExecutionException {
		try {
			artifactResolver.resolve(artifact,
					project.getRemoteArtifactRepositories(), localRepository);
		} catch (ArtifactResolutionException e) {
			throw new MojoExecutionException(e.getMessage());
		} catch (ArtifactNotFoundException e) {
			throw new MojoExecutionException(e.getMessage());
		}
	}

	private boolean projectContainsArtifact(Artifact artifact) {
		return project.getArtifactMap().get(
				ArtifactUtils.versionlessKey(artifact.getGroupId(),
						artifact.getArtifactId())) != null;
	}
}
