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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Clean up cakupan-maven-plugin files.
 * 
 * @author Matthias Nuessler
 * @goal clean
 * @phase clean
 */
public class CakupanCleanMojo extends AbstractCakupanMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {
        File instrumentFile = new File(getInstrumentDestDir(),
                COVERAGE_FILE_NAME);
        if (instrumentFile.exists() && instrumentFile.isFile()) {
            boolean success = instrumentFile.delete();
            if (!success) {
                getLog().warn("Failed to delete file " + instrumentFile);
            }
        }
    }
}
