package org.nuessler.maven.plugin.cakupan;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @phase generate-sources
 * @goal build-classpath
 * @requiresDependencyResolution test
 * @author mnuessler
 */
public class BuildClasspathMojo extends AbstractMojo {

	/**
	 * @parameter expression="${project}"
	 * @readonly
	 * @required
	 */
	private MavenProject project;

	@SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException, MojoFailureException {
		Collection<Artifact> testScopedArtifacts = Collections2.filter(
				project.getArtifacts(), new Predicate<Artifact>() {
					public boolean apply(Artifact input) {
						// return Artifact.SCOPE_TEST.equals(input.getScope());
						return true;
					}
				});

		Collection<String> classpathElements = Collections2.transform(
				testScopedArtifacts, new Function<Artifact, String>() {
					public String apply(Artifact input) {
						try {
							return input.getFile().getCanonicalPath();
						} catch (IOException e) {
							return "";
						}
					}
				});

		String classpath = StringUtils.join(classpathElements.iterator(),
				File.pathSeparator);
		System.setProperty("cakupan.maven.test.classpath", classpath);
		getLog().info("buildClasspath: " + classpath);
	}
}
