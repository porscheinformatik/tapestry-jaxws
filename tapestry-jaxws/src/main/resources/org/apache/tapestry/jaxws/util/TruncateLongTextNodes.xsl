<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:variable name="limit" select="100"/>

	<!-- standard copy template -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="text()">
		<xsl:choose>
			<xsl:when test="string-length(.) &gt; $limit"><xsl:value-of select="substring(.,0,$limit)"/>...</xsl:when>
			<xsl:otherwise><xsl:copy /></xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>