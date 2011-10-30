<!--

    Copyright 2011 Matthias Nuessler <m.nuessler@web.de>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->

### Inclusion of tests

By default, the Cakupan Plugin will automatically include all test classes with the following wildcard patterns:

* "**/*TransformationTest.java" - includes all of its subdirectories and all java filenames that start with "TransformationTest".
* "**/*XsltTest.java" - includes all of its subdirectories and all java filenames that end with "XsltTest".

If your test classes do not follow this naming convention, then configure the plugin and specify the tests
(or wildcard pattern) you want to include:

	<project>
	  [...]
	  <build>
	    <plugins>
	      <plugin>
	        <groupId>org.nuessler.maven.plugins</groupId>
	        <artifactId>cakupan-maven-plugin</artifactId>
	        <version>0.2</version>
	        <configuration>
	          <testIncludes>
	            <include>Sample.java</include>
	          </testIncludes>
	        </configuration>
	      </plugin>
	    </plugins>
	  </build>
	  [...]
	</project>


### Exclusion of tests

In certain cases it may be necessary to exclude tests. To achieve this, the plugin can be configured as follows:

	<project>
	  [...]
	  <build>
	    <plugins>
	      <plugin>
	        <groupId>org.nuessler.maven.plugins</groupId>
	        <artifactId>cakupan-maven-plugin</artifactId>
	        <version>0.2</version>
	        <configuration>
	          <testExcludes>
	            <exclude>Sample.java</exclude>
	          </testExcludes>
	        </configuration>
	      </plugin>
	    </plugins>
	  </build>
	  [...]
	</project>
