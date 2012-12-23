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
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

def instrumentFile = new File(basedir, 'target/cakupan-instrument/coverage.xml');
def reportDir = new File(basedir, 'target/site/cakupan/');

assert !instrumentFile.exists();

assert reportDir.exists();
assert reportDir.isDirectory();

def File[] reportFiles = reportDir.listFiles();
assert reportFiles.length > 0
reportFiles.each {
    assert it.length() > 0;
}

return true;
