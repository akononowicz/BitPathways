<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" version="1.0" encoding="Windows-1250" indent="yes"/>

	<xsl:param name="filename"/>

	<!-- ============================================================================================= -->

	<xsl:template match="/">
		<html>
			<head>
					<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
					<link href="css/style.css" rel="stylesheet" type="text/css"/>
			</head>
			
			<script language="JavaScript">
				function displayComponent(id){
					my_window= window.open ("","Szczegoly","scrollbars=yes,status=no,location=no,width=500,height=400,screenX=524,screenY=0");

					element=document.getElementById(id);
					my_window.document.write("&lt;html&gt;&lt;head&gt;&lt;META http-equiv='Content-Type' content='text/html; charset=UTF-8'&gt;&lt;link href='css/style.css' rel='stylesheet' type='text/css'&gt;&lt;/head&gt; &lt;body&gt;");					
					my_window.document.write(element.innerHTML);  
					my_window.document.write("&lt;/body&gt;");  
					my_window.document.close();
					
				}
	
				
			</script>
			
			<body>
			
				<xsl:call-template name="header"/>
				<xsl:call-template name="flowchart">
					<xsl:with-param name="filename"><xsl:value-of select="$filename"/></xsl:with-param>
				</xsl:call-template>
										

				<xsl:for-each select="bitpathway/path/elements/element[@class='DataFlowBranch']">
					<xsl:call-template name="dataFlowBranch"/>
				</xsl:for-each>

				<xsl:for-each select="bitpathway/path/elements/element[@class='DataFlowComponent']">
					<xsl:call-template name="dataFlowComponent"/>
				</xsl:for-each>

				<xsl:for-each select="bitpathway/path/elements/element[@class='DataFlowStart']">
					<xsl:call-template name="dataFlowStart"/>
				</xsl:for-each>
				
				<xsl:for-each select="bitpathway/path/elements/element[@class='DataFlowEnd']">
					<xsl:call-template name="dataFlowEnd"/>
				</xsl:for-each>
				
			</body>
		</html>
	</xsl:template> 

	<!-- ============================================================================================= -->
	
 <xsl:template name="header">
  
  	<center>
  	<table width="90%" align="center" class="main" cellspacing="0" cellpadding="0">
  		<tr>
  			<td width="20px">!spacja</td>
  			<td width="250px"><img src="pics/logo.png"/></td>
  			<td> 
  				<table cellspacing="0" cellpadding="10" width="100%">
  				    <tr>
  				    	<td width="100px" class="blineh"><b>Ścieżka</b></td>
  				    	<td valign="center" class="bline"><div class="myheader1"><b><xsl:value-of select="/bitpathway/path/header/attrib_groups/attrib_group/attrib[@class_id='0.0.0']/value"/></b></div>!spacja</td>
  				    </tr>
  				    <tr>
  				    	<td class="blineh"><b>Opis:</b></td>
  				    	<td class="bline" valign="center"><xsl:value-of disable-output-escaping="yes" select="/bitpathway/path/header/attrib_groups/attrib_group/attrib[@class_id='0.0.1']/value"/>!spacja</td>
  				    </tr>  	
  				  	<tr>
  				    	<td class="blineh"><b>Autorzy:</b></td>
  				    	<td class="bline" valign="center">
  				    		<ul>
  				    		<xsl:for-each select="/bitpathway/path/header/attrib_groups/attrib_group/attrib[@class_id='0.0.4']/value/item">
								<xsl:call-template name="author"/>
							</xsl:for-each>
							</ul>
  				    	!spacja
  				    	</td>
  				    </tr>    				 
  				       			    
  				</table>
				<br/>
  			</td>
  		</tr>
  	</table>
  	</center>

	</xsl:template>

	<!-- ============================================================================================= -->
	
  <xsl:template name="author">
	  <li>
	  <xsl:value-of select="person/firstname"/><xsl:text> 
	  </xsl:text><b><xsl:value-of select="person/famname"/></b>
	  </li>
  </xsl:template>

	<!-- ============================================================================================= -->

  <xsl:template name="flowchart">
	 <p/>
	 
	 		<xsl:variable name="img_offset_x" select="//elements/@img_offset_x"/>
			<xsl:variable name="img_offset_y" select="//elements/@img_offset_y"/>

		<map name="pathmap">
			<xsl:for-each select="//elements/element[@class='DataFlowBranch']">
				<area shape="rect">
					<xsl:attribute name="coords"><xsl:value-of select="number(bounds/@x)-number($img_offset_x)"/>,<xsl:value-of select="number(bounds/@y)-number($img_offset_y)"/>,<xsl:value-of select="number(bounds/@width)+number(bounds/@x)-number($img_offset_x)"/>,<xsl:value-of select="number(bounds/@height)+number(bounds/@y)-number($img_offset_y)"/></xsl:attribute>
					<xsl:attribute name="onClick">displayComponent('<xsl:value-of select="@id"/>');</xsl:attribute>						
				</area>
			</xsl:for-each>
			<xsl:for-each select="//elements/element[@class='DataFlowComponent']">
				<area shape="rect">
					<xsl:attribute name="coords"><xsl:value-of select="number(bounds/@x)-number($img_offset_x)"/>,<xsl:value-of select="number(bounds/@y)-number($img_offset_y)"/>,<xsl:value-of select="number(bounds/@width)+number(bounds/@x)-number($img_offset_x)"/>,<xsl:value-of select="number(bounds/@height)+number(bounds/@y)-number($img_offset_y)"/></xsl:attribute>
					<xsl:attribute name="onClick">displayComponent('<xsl:value-of select="@id"/>');</xsl:attribute>						
				</area>
			</xsl:for-each>			
			<xsl:for-each select="//elements/element[@class='DataFlowStart']">
				<area shape="rect">
					<xsl:attribute name="coords"><xsl:value-of select="number(bounds/@x)-number($img_offset_x)"/>,<xsl:value-of select="number(bounds/@y)-number($img_offset_y)"/>,<xsl:value-of select="number(bounds/@width)+number(bounds/@x)-number($img_offset_x)"/>,<xsl:value-of select="number(bounds/@height)+number(bounds/@y)-number($img_offset_y)"/></xsl:attribute>
					<xsl:attribute name="onClick">displayComponent('<xsl:value-of select="@id"/>');</xsl:attribute>						
				</area>
			</xsl:for-each>
						<xsl:for-each select="//elements/element[@class='DataFlowEnd']">
				<area shape="rect">
					<xsl:attribute name="coords"><xsl:value-of select="number(bounds/@x)-number($img_offset_x)"/>,<xsl:value-of select="number(bounds/@y)-number($img_offset_y)"/>,<xsl:value-of select="number(bounds/@width)+number(bounds/@x)-number($img_offset_x)"/>,<xsl:value-of select="number(bounds/@height)+number(bounds/@y)-number($img_offset_y)"/></xsl:attribute>
					<xsl:attribute name="onClick">displayComponent('<xsl:value-of select="@id"/>');</xsl:attribute>						
				</area>
			</xsl:for-each>
			<xsl:for-each select="//elements/element[@class='DataFlowSubpath']">
				<area shape="rect">
					<xsl:attribute name="coords"><xsl:value-of select="number(bounds/@x)-number($img_offset_x)"/>,<xsl:value-of select="number(bounds/@y)-number($img_offset_y)"/>,<xsl:value-of select="number(bounds/@width)+number(bounds/@x)-number($img_offset_x)"/>,<xsl:value-of select="number(bounds/@height)+number(bounds/@y)-number($img_offset_y)"/></xsl:attribute>
					<xsl:attribute name="href"><xsl:value-of select="attrib_groups/attrib_group/attrib/value/subpath/id"/>.html</xsl:attribute>						
				</area>
			</xsl:for-each>			
			
		</map>	 
	 
	 <center>
		 <xsl:element name="img">
			 <xsl:attribute name="usemap">#pathmap</xsl:attribute>
			<xsl:attribute name="src"><xsl:value-of select="$filename"/>.png</xsl:attribute>
		 </xsl:element>
	</center>
	<p/>
	<hr/>
  </xsl:template>

	<!-- ============================================================================================= -->


  <xsl:template name="dataFlowComponent">
	   <div style="display: none">
		   <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			<center>
			<table class="main" width="90%" cellspacing="0" cellpadding="5">
				<tbody>
	
					<xsl:for-each select="attrib_groups/attrib_group/attrib[@class_id='1.0.0']">
						<xsl:call-template name="myattribute"/>
					</xsl:for-each>	
					<xsl:for-each select="attrib_groups/attrib_group/attrib[@class_id='1.0.3']">
						<xsl:call-template name="myattribute"/>
					</xsl:for-each>
					<xsl:for-each select="attrib_groups/attrib_group/attrib[@class_id='1.0.1']">
						<xsl:call-template name="myattribute"/>
					</xsl:for-each>
					<xsl:for-each select="attrib_groups/attrib_group/attrib[@class_id='1.0.2']">
						<xsl:call-template name="myattribute"/>
					</xsl:for-each>										
					<xsl:for-each select="attrib_groups/attrib_group/attrib[@class_id='1.3.0']">
						<xsl:call-template name="myattribute"/>
					</xsl:for-each>		
					<xsl:for-each select="attrib_groups/attrib_group/attrib[@class_id='1.4.0']">
						<xsl:call-template name="myattribute"/>
					</xsl:for-each>							
		
				</tbody>
			</table>
			</center>
			</div>		
			<p/>
				
  </xsl:template>
  
  
	<!-- ============================================================================================= -->

  <xsl:template name="dataFlowStart">
	   <div style="display: none">
		   <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			<center>
			<table class="main" width="90%" cellspacing="0" cellpadding="5">
				<tbody>
	
					<xsl:for-each select="attrib_groups/attrib_group/attrib[@class_id='4.0.0']">
						<xsl:call-template name="myattribute"/>
					</xsl:for-each>	

					<xsl:for-each select="attrib_groups/attrib_group/attrib[@class_id='4.0.1']">
						<xsl:call-template name="myattribute"/>
					</xsl:for-each>	
				
				</tbody>
			</table>
			</center>
			</div>		
			<p/>
				
  </xsl:template>


	<!-- ============================================================================================= -->

  <xsl:template name="dataFlowEnd">
	   <div style="display: none">
		   <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			<center>
			<table class="main" width="90%" cellspacing="0" cellpadding="5">
				<tbody>
	
					<xsl:for-each select="attrib_groups/attrib_group/attrib[@class_id='5.0.0']">
						<xsl:call-template name="myattribute"/>
					</xsl:for-each>	

					<xsl:for-each select="attrib_groups/attrib_group/attrib[@class_id='5.0.1']">
						<xsl:call-template name="myattribute"/>
					</xsl:for-each>	
				
				</tbody>
			</table>
			</center>
			</div>		
			<p/>
				
  </xsl:template>
  
  	<!-- ============================================================================================= -->

  <xsl:template name="dataFlowBranch">
	   <div style="display: none">
		   <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			<center>
			<table class="main" width="90%" cellspacing="0" cellpadding="5">
				<tbody>
	
					<xsl:for-each select="attrib_groups/attrib_group/attrib[@class_id='2.0.0']">
						<xsl:call-template name="myattribute"/>
					</xsl:for-each>	

					<xsl:for-each select="attrib_groups/attrib_group/attrib[@class_id='2.0.1']">
						<xsl:call-template name="myattribute"/>
					</xsl:for-each>	
				
				</tbody>
			</table>
			</center>
			</div>		
			<p/>
				
  </xsl:template>

	<!-- ============================================================================================= -->

  <xsl:template name="myattribute">
		<tr>
			<td class="blineh" width="30%">		<b><xsl:value-of select="name[@lang='pl']/@value"/> </b></td>
			<td class="bline">		<xsl:value-of select="value"/>
			<xsl:text disable-output-escaping="yes">!spacja</xsl:text></td>			
		</tr>
 </xsl:template>
  
    <xsl:template name="myimg">
		<tr>
			<td class="blineh" width="30%">		<b>Description</b></td>
			<td class="bline">		
			<img>
				<xsl:attribute name="src"><xsl:value-of select="value"/></xsl:attribute>
			</img>
			<xsl:text disable-output-escaping="yes">!spacja</xsl:text></td>			
		</tr>
  </xsl:template>


</xsl:stylesheet>
