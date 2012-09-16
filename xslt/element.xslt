<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html"/>
	<xsl:param name="lang"></xsl:param>
	
	<!-- match the document root -->
	<xsl:template match="/">
	
			<html>
				<body style="font-family:Arial, Helvetica, sans-serif">
					<xsl:apply-templates select="node()"/>					
				</body>
			</html>
		
	</xsl:template>

	<!-- match a <messageBoard> element -->
	<xsl:template match="element">
		<table width="100%">
			<tr>
				<td style="font-family:Helvetica, sans-serif;font-size:9px;background-color:#87b4fb">
					<b><xsl:value-of select="@label"/></b>
				</td>
			</tr>
		</table>

		<xsl:apply-templates select="attrib_groups"/> 
		
	</xsl:template>
	
	<xsl:template match="attrib_group">
		<table width="100%" border="1">
			<tbody>
				<tr>
					<td style="font-family:Helvetica, sans-serif;font-size:9px;background-color:#c7caea" colspan="2">
						<b><xsl:value-of select="name[@lang=$lang]/@value"/></b>
					</td>
				</tr>
				
				<xsl:for-each select="attrib">				
					<tr>
						<td style="font-family:Helvetica, sans-serif;font-size:9px;background-color:#efefef;">
							<b><xsl:value-of select="name[@lang=$lang]/@value"/></b>
						</td>
						<td style="font-family:Helvetica, sans-serif;font-size:9px">
							<xsl:choose>
								<xsl:when test="@type='list'"><xsl:call-template name="list"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="value" disable-output-escaping="yes"/></xsl:otherwise>
							</xsl:choose>
							
						</td>		
					</tr>
				</xsl:for-each>
				
			</tbody>
		</table>
	</xsl:template>


	<xsl:template name="list">
		<xsl:choose>
			<xsl:when test="@item_type='string'">
				<xsl:call-template name="simpleBulletList"/>
			</xsl:when>
			<xsl:when test="@item_type='person'">
				<xsl:call-template name="person"/>
			</xsl:when>
			<xsl:when test="@item_type='activity'">
				<xsl:call-template name="activity"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="simpleBulletList"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	
	<xsl:template name="simpleBulletList">
		<ul>
			<xsl:for-each select="value/item">
				<li><xsl:value-of select="."/></li>
			</xsl:for-each>
		</ul>
	</xsl:template>
	
	<xsl:template name="person">
		<xsl:for-each select="value/item">
		<xsl:value-of select="person/firstname"/>_<b> <xsl:value-of select="person/famname"/></b> <br/>
		</xsl:for-each>
	</xsl:template>	

	<xsl:template name="activity">
		<xsl:for-each select="value/item">
		<b>+</b>	<xsl:value-of select="activity/name"/> <br/>
		</xsl:for-each>
	</xsl:template>	
	
</xsl:stylesheet>
