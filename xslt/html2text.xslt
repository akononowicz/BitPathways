<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text"/>
	<!-- match the document root -->

	<!-- match a <messageBoard> element -->
	<xsl:template match="*">
		<xsl:value-of select="."/>
	</xsl:template>
</xsl:stylesheet>
