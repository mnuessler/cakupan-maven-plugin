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
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

def instrumentFile = new File(basedir, 'target/cakupan-instrument/coverage.xml');
def reportDir = new File(basedir, 'target/site/cakupan/');
def indexFile = new File(reportDir, 'index.html')
def sampleXslReport = new File(reportDir, 'sample.xsl.html')

assert reportDir.exists();
assert reportDir.isDirectory();

assert indexFile.exists()
assert indexFile.length() > 0

assert sampleXslReport.exists()
assert sampleXslReport.length() > 0

resourceFiles = [
    'blank.png',
    'customsorttypes.js',
    'downsimple.png',
    'main.css',
    'sortabletable.css',
    'sortabletable.js',
    'source-viewer.css',
    'upsimple.png'
    ].collect { new File(reportDir, it) }

resourceFiles.each { file ->
    assertTrue("File '$file' does not exist", file.exists())
    assertThat("File '$file' is empty", file.length(), greaterThan(0L))
}

return true;
