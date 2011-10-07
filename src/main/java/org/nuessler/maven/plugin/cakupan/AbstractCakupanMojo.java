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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;

/**
 * Base class for Cakupan mojos.
 * 
 * @author mnuessler
 */
public abstract class AbstractCakupanMojo extends AbstractMojo {

    protected static final String COVERAGE_FILE_NAME = "coverage.xml";

    /**
     * The Maven Project Object
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter expression="${xslt.instrument.destdir}"
     *            default-value="${project.build.directory}/cakupan-instrument"
     */
    private File instrumentDestDir;

    protected File getInstrumentDestDir() {
        return instrumentDestDir;
    }

    protected MavenProject getProject() {
        return project;
    }

}
