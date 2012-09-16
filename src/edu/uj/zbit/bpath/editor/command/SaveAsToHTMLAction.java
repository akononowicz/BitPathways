/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.DataFlowCanvas;
import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.XMLProcessor;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowEnd;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartElement;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartNode;
import edu.uj.zbit.bpath.helper.xslt.FileManager;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.image.RenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;

public class SaveAsToHTMLAction extends AbstractAction  {
	
	private PathModel model;
	private JFrame parent;
	private DataFlowEnd newEndElement;
	private JFileChooser chooser;
	private DataFlowCanvas canvas;

	
	public SaveAsToHTMLAction(PathModel _model,JFrame _parent,DataFlowCanvas _canvas){
		super(BpathMainFrame.messages.getString("ACTION.HTMLSAVE"));
		model=_model;
		parent=_parent;
		canvas=_canvas;
		chooser=new JFileChooser(model.getLastSavedFile());
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	}
	

	public SaveAsToHTMLAction(String name, Icon icon){
		super(name,icon);
	}
	
	public void actionPerformed(ActionEvent e){
		if (enabled){
			
			
			chooser.showSaveDialog(parent);
			File dir=chooser.getSelectedFile();
			if (dir==null) return; 
			
			String pathid=model.getPathId();
			
			model.deactivateSelection();
			
			// create image
			
			RenderedImage rendImage = canvas.saveImage();
			try{
				ImageIO.write(rendImage, "png", new File(dir.getAbsolutePath()+"/"+pathid+".png"));
			}catch(Exception err){
				err.printStackTrace();
			}
			
			// create html
			
			//String xml=model.getModelAsXMLString(canvas);
			String html=getPathHTML(pathid, canvas, model);
			
			try{
				BufferedWriter bout=new BufferedWriter(new FileWriter(dir.getAbsolutePath()+"/"+pathid+".html"));
				bout.write(html);
				bout.close();
				
			}catch(Exception err){
				err.printStackTrace();
			}
			
			// copy style and images
			
			new File(dir.getAbsolutePath()+"/css").mkdir();
			new File(dir.getAbsolutePath()+"/pics").mkdir();

			
			/*FileManager.copyfile(
					XMLProcessor.class.getClass().getResource("/resources/style.css").getFile(), 
					dir.getAbsolutePath()+"/css/style.css");
			
			FileManager.copyfile(
					XMLProcessor.class.getClass().getResource("/resources/logo.png").getFile(), 
					dir.getAbsolutePath()+"/pics/logo.png");
			*/
			
			
			FileManager.copyfile(
					"resources/style.css", 
					dir.getAbsolutePath()+"/css/style.css");
			
			FileManager.copyfile(
					"resources/logo.png", 
					dir.getAbsolutePath()+"/pics/logo.png");
			
			
			// create subpathways
					
		}
	}
	
	
	public String getPathHTML(String id,DataFlowCanvas canvas, PathModel model){
			
		String result=new String();

		// create html header
		result+=getHtmlHeader();
		
		//create path header
		result+=getPathHeader(model);
		
		// create map
		
		Rectangle r=canvas.getImageRectangle();
		result+=getMapElement(model, new Point(r.x,r.y));
		
		// create flowchart
		
		result+=getFlowchartElement(id);
		
		// create popups
		
		FlowChartElement[] tab=model.getAllVertices();
		
		for (FlowChartElement flowChartElement : tab) {
			result+=getElementBox(flowChartElement);
		}
		
		// create html footer
		
		result+=getHtmlFooter();
		
		
		
		result=result.replaceAll("!spacja", "&nbsp;");
		result=result.replaceAll("&lt;", "<");
		result=result.replaceAll("&gt;", ">");		
		result=result.replaceAll("&amp;", "&");
		
		/*for (int i = 0; i < uu_str.length; i++) {
			result=result.replaceAll(uu_str[i], ""+pl_str[i]);
			
		}*/
		
        return result;
	}
	
	
	public String getHtmlHeader(){
		
		String htmlHeader="<html>"+
		"<head>"+
		"		<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />"+
		"		<link href='css/style.css' rel='stylesheet' type='text/css'/>"+
		"</head>"+
		
	    "  <script language=\"JavaScript\"> \n"+
		"	function displayComponent(id){ \n"+
	    "	my_window= window.open (\"\",\"Szczegoly\",\"scrollbars=yes,status=no,location=no,width=500,height=400,screenX=524,screenY=0\");\n"+
	    "	element=document.getElementById(id); \n"+
	    "	my_window.document.write(\"<html><head><META http-equiv='Content-Type' content='text/html; charset=UTF-8'><link href='css/style.css' rel='stylesheet' type='text/css'></head> <body>\");\n"+					
	    "	my_window.document.write(element.innerHTML);  \n"+
	    "	my_window.document.write(\"</body>\");  \n"+
	    "	my_window.document.close();	\n"+
	    "	}			\n"+
	    "	</script>\n"+
		
		"<body>";
		
		return htmlHeader;
	}
	
	public String getPathHeader(PathModel model){
		
		String str=
			
	
		"<center> "+
	  	"<table width='90%' align='center' class='main' cellspacing='0' cellpadding='0'> "+
	  	"	<tr> "+
	  	"		<td width='20px'> </td> "+
	  	"		<td width='250px'><img src='pics/logo.png'/></td> "+
	  	"		<td>  "+
	  	"			<table cellspacing='0' cellpadding='10' width='100%'> "+
	 
	  	getAttributeRow("Title","<div class='myheader1'>"+model.getMetadata().getTitleMetadata().getName()+"</div>")+
	  	getAttributeRow("Author","<b>"+model.getMetadata().getAuthorsMetadata()[0].getName()+"</b>")+
	  	
	  	"			</table> "+
		"			<br/> "+
	  	"		</td> "+
	  	"	</tr> "+
	  	"</table> <p/>"+
	
		
		XMLProcessor.transformElement2(model.getPathElement())+
		
	  	"</center> ";
		return str;
	}
	
	
	public String getElementBox(FlowChartElement element){
		
		String str=
			
	
		"<p><center> "+
		"<div style='display: none' id='"+element.getId()+"'>"+
	  		
		XMLProcessor.transformElement2(element)+
		
	  	"</div></center></p> \n";
		return str;
	}
	

	
	
	private String getAttributeRow(String name, String value){
	 	String str=""+
		"			    <tr> "+
	  	"			    	<td width='100px' class='blineh'><b>"+name+"</b></td> "+
	  	"			    	<td valign='center' class='bline'>"+value+"</td> "+
	  	"			    </tr> ";
	 	return str;
	}
	
	public String getMapElement(PathModel model, Point offset){
		String str="<map name='pathmap'> ";
		
		FlowChartNode[] tab=model.getAllVertices();
		
		for (FlowChartNode flowChartElement : tab) {
			str+=getAreaItem(flowChartElement,offset);
		}
		
		str+="</map>";
		return str;
	}
	
	public String getAreaItem(FlowChartNode element,Point offset){
		return "<area shape='rect' coords='"+
		(element.x-offset.x)+","+
		(element.y-offset.y)+","+
		(element.x+element.width-offset.x)+","+
		(element.y+element.height-offset.y)+
		"' onClick=\"displayComponent('"+element.getId()+"');\">";
	}
	
	
	public String getFlowchartElement(String id){
		String str="<p/> \n"+
					"<map name='pathmap'></map> "+
					" <center> "+
					" <img usemap='#pathmap' src='"+id+".png'></center>"+
					" <p/> \n";
		
		return str;
	}
	
	
	public String getHtmlFooter(){
		return "</body> " +
				"</html>"; 
	}
	
	
	
}
