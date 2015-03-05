<!--

    Copyright 2011-2015 Matthias Nuessler <m.nuessler@web.de>

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
Cakupan Maven Plugin
--------------------

This plugin integrates the [Cakupan XSLT coverage tool][ckpn] into the Maven
environment. More information about can be found on the [Cakupan project website][ckpn].

### Goals Overview

* [cakupan:instrument](instrument-mojo.html) instruments XSLT files for coverage.
* [cakupan:test](test-mojo.html) runs the transformation tests using Surefire/Junit.
* [cakupan:cakupan](cakupan-mojo.html) generates a test coverage report for the XSLT files.

### Usage

General instructions on how to use the Cakupan Plugin can be found on the [usage page](usage.html).
Some more specific use cases are described in the examples given below.
In case you still have questions regarding the plugin's usage, please have a look at the [FAQ](faq.html)
and feel free to contact the [user mailing list](mail-lists.html).

If you feel like the plugin is missing a feature or has a defect, you can fill a feature request or bug report
in the [issue tracker](issue-tracking.html).

### Examples

The following examples show how to use the Cakupan Maven plugin in several use cases:

* [Inclusion and Exclusion of Tests](inclusion-exclusion-tests.html)
* [Inclusion and Exclusion of XSTL files](inclusion-exclusion-xslt.html)


[ckpn]: http://code.google.com/p/cakupan/ "Cakupan project page"
