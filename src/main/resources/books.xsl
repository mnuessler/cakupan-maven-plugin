<?xml version="1.0"?>
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
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:b="http://www.example.org/books"
	xmlns="http://www.w3.org/1999/xhtml">

	<xsl:template match="/">
		<html>
			<head>
				<title>Reading List</title>
			</head>
			<body>
				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>

	<xsl:template match="b:books">
		<table>
			<th>Title</th>
			<th>Author</th>
			<th>Pages</th>
			<th>Publisher</th>
			<th>Edition</th>
			<xsl:apply-templates />
		</table>
	</xsl:template>

	<xsl:template match="b:book">
		<xsl:variable name="id">
			<xsl:value-of select="@id" />
		</xsl:variable>
		<tr id="{$id}">
			<xsl:apply-templates />
		</tr>
	</xsl:template>

	<xsl:template match="b:title">
		<td>
			<xsl:value-of select="." />
		</td>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="b:authors">
		<td>
			<xsl:apply-templates />
		</td>
	</xsl:template>

	<xsl:template match="b:author">
		<xsl:value-of select="." />
		<br />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="b:pages">
		<td>
			<xsl:value-of select="." />
		</td>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="b:publisher">
		<td>
			<xsl:value-of select="." />
		</td>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="b:edition">
		<td>
			<xsl:value-of select="." />
			<xsl:choose>
				<xsl:when test=". = '1'">
					<xsl:text>st</xsl:text>
				</xsl:when>
				<xsl:when test=". = '2'">
					<xsl:text>nd</xsl:text>
				</xsl:when>
				<xsl:when test="'3'">
					<xsl:text>rd</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>th</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text> edition, </xsl:text>
			<xsl:value-of select="@publication-date" />
		</td>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="b:isbn-10">
		<td>
			<xsl:value-of select="." />
		</td>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="b:*">
	</xsl:template>

</xsl:stylesheet>
