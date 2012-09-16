/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.repository;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;
import org.exist.xmldb.RemoteXMLResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.BinaryResource;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.DataFlowCanvas;
import edu.uj.zbit.bpath.editor.model.Metadata;
import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.TemplateInfo;
import edu.uj.zbit.bpath.editor.model.User;
import edu.uj.zbit.bpath.editor.model.UserGroupInfo;
import edu.uj.zbit.bpath.editor.model.XMLProcessor;



public class ExistConnection {

	static String driver = "org.exist.xmldb.DatabaseImpl";
	
    private static final Logger log = Logger.getLogger(ExistConnection.class);
	
    private static String existAddress, existUser, existPass;
    
    public static void init(){
    	existAddress="xmldb:exist://"+
		    BpathMainFrame.server.getDb_address()+
		    ":"+BpathMainFrame.server.getDb_port()+
		    "/exist/xmlrpc/db/"+
		    BpathMainFrame.server.getDb_name();
    	
    	existUser = BpathMainFrame.server.getDb_username();
    	existPass = BpathMainFrame.server.getDb_pass();
    }
    
	public static int save(String id, PathModel model, DataFlowCanvas canvas, String uid){
		Collection col = null;
		  try {
		    
		    Class c = Class.forName(driver);

		    Database database = (Database) c.newInstance();
		    DatabaseManager.registerDatabase(database);
		    col = DatabaseManager.getCollection(existAddress,existUser,existPass);

		    // Check ownership
		    
		    if (!uid.equalsIgnoreCase("1")){
		    	String oid=ExistConnection.getPathwayOwner(id);
		    	if (	(!oid.equalsIgnoreCase("-1")) &&
		    			(!oid.equalsIgnoreCase(uid)) 
		    		)		
		    	return 2;
		    }
		    
		    // Save model
		    
		    XMLResource document = 
		      (XMLResource) col.createResource(id+".xml", "XMLResource");
		    
		    Document xmlDoc=model.getModelAsDOM(canvas);
		    xmlDoc.getElementsByTagName("element").item(0);
		    
		   
		    
		    document.setContentAsDOM(xmlDoc);
		    
		    col.storeResource(document);
		    
		    // Save image
		    
		    BinaryResource image = 
			      (BinaryResource) col.createResource(id+".png", "BinaryResource"); 
		    
		    RenderedImage rendImage = canvas.saveImage();
		    
		    ByteArrayOutputStream output=new ByteArrayOutputStream();
		    
		    ImageIO.write(rendImage, "png",output);
		    image.setContent(output.toByteArray());
		    
		    col.storeResource(image);
		    
		    col.close();
		  }
		  catch (Exception e) {
			  e.printStackTrace();
			  return 1;
		  }
		  
		  return 0;
	}
	
	public static String createNewResource(){
		Collection col = null;
		String id=null;
		 try {
		    
		    Class c = Class.forName(driver);

		    Database database = (Database) c.newInstance();
		    DatabaseManager.registerDatabase(database);
		    col = DatabaseManager.getCollection(existAddress,existUser,existPass);

		    XMLResource document = 
		      (XMLResource) col.createResource(null, "XMLResource");
		    
		    id=document.getId();
		    id=id.substring(0,id.length()-4);
    
		    col.close();
		  }
		  catch (Exception e) {
			  e.printStackTrace();
		  }
		  return id;
	}
	
	public static Hashtable<Metadata,String> getPathList(User user){
		Collection col = null;
		StringBuffer str=new StringBuffer();
		try {
			  Class c = Class.forName(driver);

			  Database database = (Database) c.newInstance();
			  DatabaseManager.registerDatabase(database);
			  col = DatabaseManager.getCollection(existAddress,existUser,existPass);
			  
			  String xquery ="";

				 xquery="for $path in /bitpathways/path ";
			
				 if (user.getRole().equalsIgnoreCase("user"))
				 xquery+="where $path/metadata/access_rights/entity/id="+user.getUid()+" ";
				 
				 xquery+= "return "+
				  "<path id='{data($path/@id)}'>  "+
				  "	 {$path/metadata} "+
				  "</path> ";
					  
			  XPathQueryService service = (XPathQueryService) col.getService("XPathQueryService", "1.0");
			  ResourceSet resultSet = service.query(xquery);
			  ResourceIterator results = resultSet.getIterator();
			  
			  Hashtable<Metadata,String> ret=new Hashtable<Metadata,String>();
			  
			  while (results.hasMoreResources()) {
				  RemoteXMLResource r=(RemoteXMLResource)results.nextResource();
				  Document root=(Document)r.getContentAsDOM();
				  Element path=(Element)root.getElementsByTagName("path").item(0);
				  Metadata metadata=new Metadata();
				  XMLProcessor.readXMLmetadata(metadata, (Element)(path.getElementsByTagName("metadata").item(0)));
				  ret.put(metadata,path.getAttribute("id"));
			  }
			  
			  return ret;
		  } catch (Exception e) {
		    e.printStackTrace();
		    
		  }
		  return new Hashtable<Metadata,String>();
	}
	
	/**
	 * Authorizes a user. Returns the user's object or null if the provided data is not correct.
	 * 
	 * @param username
	 * @param pass
	 * @return user object or null if authorization fails
	 */
	public static User authorizeUser(String username,String pass) throws Exception{
		
		
		  
		  Collection col = null;

		  log.debug("Login attempt for user:"+username);
		
		  Class c = Class.forName(driver);
	
		  Database database = (Database) c.newInstance();
		  DatabaseManager.registerDatabase(database);
		  
		  col = DatabaseManager.getCollection(existAddress,existUser,existPass);
		  
		  String xquery="for $user in //bp_users/bp_user[@name='"+username+"'][@password='"+pass+"'] return <user uid='{$user/@uid}' role='{$user/@role}' firstname='{$user/@firstname}' famname='{$user/@famname}' group='{$user/@group}' />";
	  
		  //log.debug(xquery);
		  
		  ResourceIterator results;
		  
		  try{
			  XPathQueryService service = (XPathQueryService) col.getService("XPathQueryService", "1.0");
			  ResourceSet resultSet = service.query(xquery);
			  results = resultSet.getIterator();
		  }catch(Exception e){
			  throw new Exception(BpathMainFrame.messages.getString("ERROR_SERVER_NORESPONSE"));
		  }
		  		  
		  String uid="-1";
		  String role="user";
		  String firstname="Null";
		  String famname="Null";
		  String group="";
		  
		  
		  if (results.hasMoreResources()){ 
			  RemoteXMLResource r=(RemoteXMLResource)results.nextResource();
			  uid=r.getContentAsDOM().getFirstChild().getAttributes().getNamedItem("uid").getTextContent();
			  role=r.getContentAsDOM().getFirstChild().getAttributes().getNamedItem("role").getTextContent();
			  firstname=r.getContentAsDOM().getFirstChild().getAttributes().getNamedItem("firstname").getTextContent();
			  famname=r.getContentAsDOM().getFirstChild().getAttributes().getNamedItem("famname").getTextContent();
			  try{
				  group=r.getContentAsDOM().getFirstChild().getAttributes().getNamedItem("group").getTextContent();
			  }catch(Exception e){e.printStackTrace();}
		  }
		  
		  log.debug("User authorization with the result:"+uid);
		  
		  if (uid.equalsIgnoreCase("-1")) throw new Exception(BpathMainFrame.messages.getString("ERROR_SERVER_WRONGPASS"));
		  else{
			  User user= new User(Integer.parseInt(uid),role,firstname,famname,group);
			  user.setGroupInfo(getUserGroupInfo(user.getGroup()));
			  
			  return user; 
		  }
			  
	}
	
	public static Node getPath(String id){
		Collection col = null;
		try {
		    
		    Class c = Class.forName(driver);
		
		    Database database = (Database) c.newInstance();
		    DatabaseManager.registerDatabase(database);
		    col = DatabaseManager.getCollection(existAddress,existUser,existPass);
		
		    XMLResource document = 
		      (XMLResource) col.getResource(id+".xml");
		   
		    col.close();
		    
		    return document.getContentAsDOM();
		  }
		  catch (Exception e) {
			  e.printStackTrace();
		  }
		  return (Node) null;
	}
	
	public static String getPathwayOwner(String pathid){
		  try{
			  
			  Collection col = null;
	
			  log.debug("Check ownership of path"+pathid);
			
			  Class c = Class.forName(driver);
		
			  Database database = (Database) c.newInstance();
			  DatabaseManager.registerDatabase(database);
			  
			  col = DatabaseManager.getCollection(existAddress,existUser,existPass);
			  
			  String xquery="for $path in //bitpathway/path[@id='"+pathid+"'] return <owner uid='{$path/@uid}'/>";
			  			  
			  ResourceIterator results=null;
			  
			  try{
				  XPathQueryService service = (XPathQueryService) col.getService("XPathQueryService", "1.0");
				  ResourceSet resultSet = service.query(xquery);
				  results = resultSet.getIterator();
			  }catch(Exception e){
				 e.printStackTrace();
			  }
			  		  
			  String uid="-1";
			  
			  if (results==null) return "-1";
			  
			  if (results.hasMoreResources()){ 
				  RemoteXMLResource r=(RemoteXMLResource)results.nextResource();
				  uid=r.getContentAsDOM().getFirstChild().getAttributes().getNamedItem("uid").getTextContent();
			  }
			  
			  return uid;
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  
		  return "";
		  
	}
	
	public static UserGroupInfo getUserGroupInfo(String group){
 		  
		 UserGroupInfo userGroup=new UserGroupInfo();
		
		  try{
			  Collection col = null;
			  Class c = Class.forName(driver);
		
			  Database database = (Database) c.newInstance();
			  DatabaseManager.registerDatabase(database);
			  
			  col = DatabaseManager.getCollection(existAddress,existUser,existPass);
			  
			  String xquery="for $group in //groups/group[@id='"+group+"'] return $group";
			  
			  			  
			  ResourceIterator results;
	
			  XPathQueryService service = (XPathQueryService) col.getService("XPathQueryService", "1.0");
			  ResourceSet resultSet = service.query(xquery);
			  results = resultSet.getIterator();
			  			  
			  if (results.hasMoreResources()){ 
				  
				  RemoteXMLResource r=(RemoteXMLResource)results.nextResource();
				  Document root=(Document)r.getContentAsDOM();			  

				  // 1. Read topics	  
				  NodeList topics=root.getElementsByTagName("topics");
				  String[] topicsTab=null;
				  
				  if (topics.getLength()>0){ 
					NodeList topicsList=((Element)topics.item(0)).getElementsByTagName("topic");
					topicsTab=new String[topicsList.getLength()];
					for (int i = 0; i < topicsTab.length; i++) {
						topicsTab[i]=topicsList.item(i).getTextContent();
					}
				  }
				  
				  // 2. Read user groups
				  NodeList subgroups=root.getElementsByTagName("subgroups");
				  String[] subgroupsTab=null;
				  
				  if (subgroups.getLength()>0){ 
					NodeList subgroupsList=((Element)subgroups.item(0)).getElementsByTagName("subgroup");
					subgroupsTab=new String[subgroupsList.getLength()];
					for (int i = 0; i < subgroupsTab.length; i++) {
						subgroupsTab[i]=subgroupsList.item(i).getTextContent();
					}
				  }
				  
				  // 3. Read teachers
				  NodeList teachers=root.getElementsByTagName("teachers");
				  String[] teachersTab=null;
				  
				  if (teachers.getLength()>0){ 
					NodeList teachersList=((Element)teachers.item(0)).getElementsByTagName("teacher");
					teachersTab=new String[teachersList.getLength()];
					for (int i = 0; i < teachersTab.length; i++) {
						String teacher=teachersList.item(i).getTextContent();
						teachersTab[i]=teacher;
					}
				  }				  
				  
				  // 4. Read templates				  
				  NodeList templates=root.getElementsByTagName("templates");
				  TemplateInfo[] templatesTab=null;
				  
				  if (templates.getLength()>0){ 
					NodeList templatesList=((Element)templates.item(0)).getElementsByTagName("template");
					templatesTab=new TemplateInfo[templatesList.getLength()];
					for (int i = 0; i < templatesTab.length; i++) {
						Element template=(Element)templatesList.item(i);
						
						String templateName=((Element)(template.getElementsByTagName("name").item(0))).getTextContent();
						String templateURL=((Element)(template.getElementsByTagName("url").item(0))).getTextContent();
						
						templatesTab[i]=new TemplateInfo(templateName, templateURL);
					}
				  }	
				  
				  userGroup.setTopics(topicsTab);
				  userGroup.setSubgroups(subgroupsTab);
				  userGroup.setTeachers(teachersTab);
				  userGroup.setTemplates(templatesTab);
				  
			  }
			  
		  }catch(Exception e){
			 e.printStackTrace();
		  }
		  	  
		  return userGroup;
	}
	
}
