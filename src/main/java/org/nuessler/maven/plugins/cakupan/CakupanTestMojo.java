package org.nuessler.maven.plugins.cakupan;

////execute lifecycle="cakupan" phase="test"
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

/**
 * @author mnuessler
 * @goal xslt-coverage
 * @phase test
 * @requiresDependencyResolution test
 */
public class CakupanTestMojo extends AbstractMojo {

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
	 * @parameter default-value="${project.build.testOutputDirectory}"
	 */
	private File testClassesDirectory;

	/**
	 * The directory containing generated classes of the project being tested.
	 * This will be included after the test classes in the test classpath.
	 * 
	 * @parameter default-value="${project.build.outputDirectory}"
	 */
	private File classesDirectory;

	/**
	 * The Maven Project Object.
	 * 
	 * @parameter default-value="${project}"
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * The test source directory containing test class sources.
	 * 
	 * @parameter default-value="${project.build.testSourceDirectory}"
	 * @required
	 */
	private File testSourceDirectory;

	/**
	 * Specify this parameter to run individual tests by file name, overriding
	 * the <code>includes/excludes</code> parameters. Each pattern you specify
	 * here will be used to create an include pattern formatted like
	 * <code>**&#47;${test}.java</code>, so you can just type "-Dtest=MyTest" to
	 * run a single test called "foo/MyTest.java".<br/>
	 * This parameter overrides the <code>includes/excludes</code> parameters,
	 * and the TestNG <code>suiteXmlFiles</code> parameter.
	 * <p/>
	 * since 2.7.3 You can execute a limited number of method in the test with
	 * adding #myMethod or #my*ethod. Si type "-Dtest=MyTest#myMethod"
	 * <b>supported for junit 4.x and testNg</b>
	 * 
	 * @parameter expression="${test}"
	 */
	private String test;

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
	 * ArtifactRepository of the localRepository. To obtain the directory of
	 * localRepository in unit tests use System.getProperty("localRepository").
	 * 
	 * @parameter expression="${localRepository}"
	 * @required
	 * @readonly
	 */
	private ArtifactRepository localRepository;

	/**
	 * Map of plugin artifacts.
	 * 
	 * @parameter expression="${plugin.artifactMap}"
	 * @required
	 * @readonly
	 */
	private Map pluginArtifactMap;

	/**
	 * <i>Maven Internal</i>: List of artifacts for the plugin.
	 * 
	 * ([mnuessler] from AbstractCoberturaMojo)
	 * 
	 * @parameter default-value="${plugin.artifacts}"
	 * @required
	 * @readonly
	 */
	private List<Artifact> pluginClasspathList;

	/**
	 * Map of project artifacts.
	 * 
	 * @parameter expression="${project.artifactMap}"
	 * @required
	 * @readonly
	 */
	private Map projectArtifactMap;

	/**
	 * Resolves the artifacts needed.
	 * 
	 * @component
	 */
	private ArtifactResolver artifactResolver;

	/**
	 * Creates the artifact.
	 * 
	 * @component
	 */
	private ArtifactFactory artifactFactory;

	/**
	 * The remote plugin repositories declared in the POM.
	 * 
	 * @parameter expression="${project.pluginArtifactRepositories}"
	 * @since 2.2
	 */
	private List remoteRepositories;

	/**
	 * For retrieval of artifact's metadata.
	 * 
	 * @component
	 */
	private ArtifactMetadataSource metadataSource;

	/**
	 * The current build session instance.
	 * 
	 * @parameter expression="${session}"
	 * @required
	 * @readonly
	 */
	private MavenSession session;

	/**
	 * Used for attaching the source jar to the project.
	 * 
	 * @component
	 */
	private MavenProjectHelper projectHelper;

	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("CakupanTestMojo.execute");
		try {
			for (Object artifact : pluginClasspathList) {
				getLog().info(String.valueOf(artifact));
			}
			addCakupanDependcies();
			for (Object object : project.getArtifacts()) {
				getLog().info(object + " : " + object.getClass());
			}
			getLog().info(
					"testClasspathElements: "
							+ project.getTestClasspathElements());
		} catch (DependencyResolutionRequiredException e) {
			throw new MojoExecutionException(e.getMessage());
		} catch (InvalidVersionSpecificationException e) {
			throw new MojoExecutionException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private void addCakupanDependcies()
			throws InvalidVersionSpecificationException {
		Set<Artifact> artifacts = project.getArtifacts();
		Artifact cakupanArtifact = artifactFactory.createDependencyArtifact(
				"com.cakupan", "cakupan-xslt",
				VersionRange.createFromVersionSpec("1.0"), "jar", null,
				Artifact.SCOPE_TEST);
		artifacts.add(cakupanArtifact);
		// TODO add Cakupan dependencies
		// TODO do not add when artifacts already exist
	}
}
