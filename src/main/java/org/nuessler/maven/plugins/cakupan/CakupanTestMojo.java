package org.nuessler.maven.plugins.cakupan;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @author mnuessler
 * @goal test
 * @execute lifecycle="cakupan" phase="test"
 * @requiresDependencyResolution runtime
 */
public class CakupanTestMojo extends AbstractMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("CakupanTestMojo.execute");
	}

}
