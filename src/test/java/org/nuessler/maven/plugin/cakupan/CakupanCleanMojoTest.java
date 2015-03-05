/**
 * Copyright 2011-2015 Matthias Nuessler <m.nuessler@web.de>
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

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.util.ReflectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link CakupanCleanMojo}.
 *
 * @author Matthias Nuessler
 */
public class CakupanCleanMojoTest {
    private AbstractCakupanMojo mojo;
    private File destDir;
    private File cvrgFile;

    @Before
    public void setUp() {
        mojo = new CakupanCleanMojo();
        destDir = new File("target/tmp");
        destDir.mkdirs();
        cvrgFile = new File(destDir, AbstractCakupanMojo.COVERAGE_FILE_NAME);
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(destDir);
    }

    @Test
    public void shouldDeleteCoverageFileIfExists() throws Exception {
        // Given
        cvrgFile.createNewFile();
        ReflectionUtils.setVariableValueInObject(mojo, "instrumentDestDir", destDir);

        // When
        mojo.execute();

        // Then
        assertThat("File not cleaned", cvrgFile.exists(), equalTo(false));
    }

    @Test
    public void shouldDoNothingIfFileDoesNotExist() throws Exception {
        // Given
        ReflectionUtils.setVariableValueInObject(mojo, "instrumentDestDir", destDir);

        // When
        mojo.execute();

        // Then
        assertThat("File not cleaned", cvrgFile.exists(), equalTo(false));
    }

}
