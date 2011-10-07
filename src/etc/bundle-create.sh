#!/bin/bash

#
# Copyright 2011 Matthias Nuessler <m.nuessler@web.de>
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

mvn_cmd="mvn"

if [ "$#" == "0" ]; then
	echo -n "Enter GPG passphrase: "
	read pw
elif [ "$#" == "1" ]; then
	pw="$1"
else
	echo "Usage: $0 [gpg_passphrase]"
	echo "(will prompt for passphrase if not specified)" 
	echo
	exit 1
fi

$mvn_cmd \
	source:jar \
	javadoc:jar \
	package \
	gpg:sign \
	repository:bundle-create \
	-Dgpg.passphrase=$pw
