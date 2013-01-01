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
import static org.junit.Assert.assertTrue

def outDir = new File(basedir, 'target/cakupan-instrument')

def instrumentFile = new File(outDir, 'coverage.xml')
def traceFile = new File(outDir, 'sample.xsl.xml')
def datFile = new File(outDir, 'sample.xsl.dat')

[instrumentFile, traceFile, datFile].each { file ->
    println "Checking that file exists: $file"
    assertTrue("File does not exist: '$file'", file.exists())
}

return true
