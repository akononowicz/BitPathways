/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;

import edu.uj.zbit.bpath.editor.command.*;
import edu.uj.zbit.bpath.editor.dialogs.UserLoginDialog;
import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.User;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowBranch;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartElement;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartNode;
import edu.uj.zbit.bpath.editor.propertyPanel.LinkEditor;
import edu.uj.zbit.bpath.editor.propertyPanel.PropertyPanel;

/**
 * 
 * The main class of the whole application. Builds up the user interface,
 * contains a reference to the data model.
 * 
 * The application starts by checking the language version given by the
 * first element from the args table.
 * 
 * @author Andrzej A. Kononowicz
 *
 */

public class BpathMainFrame extends JFrame{

	public PathModel model;
	private MainPanel mainPanel;
	private JTabbedPane sidePanel;
	private PropertyPanel propertyPanel;
	private HTMLViewPanel htmlViewPanel;
	
	private Action[] actions;
	public DataFlowCanvas canvas;
	
	public static ResourceBundle messages;
	public static String language="pl";
	public static String country="PL";

	public static ServerConfig server=null;
	
	public static final String version="2.1.0";
	
	
	private User user=null;
	public static boolean noserver=false;
	
    private static final Logger log = Logger.getLogger(BpathMainFrame.class);
    BpathMainFrame me;
    
    
    
	/*-----------------------------------------------------------------*/
	
	/**
	 * @param args
	 * [0] - language of the user interface - default en, possible values: en,pl,de
	 */
	public static void main(String[] args) {
		
		
		// Analyze initial parameters
		
		if (args.length>0){
			log.info("Welcome to BIT Pathways. Selected locale="+args[0]);
			log.debug("Debug mode");
			
			if (args[0].equalsIgnoreCase("pl")){
				language="pl";country="PL";
			}else if (args[0].equalsIgnoreCase("de")){
				language="de";country="DE";
			}else if (args[0].equalsIgnoreCase("en")){
				language="en";country="EN";
			}
		}
		
		if (args.length>1){
			
			if (args[1].equalsIgnoreCase("noserver")){
				noserver=true;
			}
			
		}

		
		resetLanguagePack();
		
		new BpathMainFrame();
	}

	public static void resetLanguagePack(){
		messages=ResourceBundle.getBundle("i18n.MessagesBundle",new Locale(language,country));	
	}
	
	/**
	 * Main constructor
	 */
	public BpathMainFrame(){
		
		me=this;
		
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		// user authorization	
	
		
		if (!noserver){
			UserLoginDialog loginD=new UserLoginDialog(this);
			loginD.setVisible(true);
			user=loginD.getUser();
			String username=loginD.getUsername();
			
			if (user==null&&!noserver){
				if (!loginD.isCompleted()) System.exit(0); 			
				String err=loginD.getErrorMessage();
				JOptionPane.showMessageDialog(
						this, 
						err, 
						BpathMainFrame.messages.getString("ERROR"), 
						JOptionPane.ERROR_MESSAGE); 
				System.exit(0);
			}
		}else{
			user=new User(-10,"nouser","null","null","");
			
		}
		
		initModel();
		
		getContentPane().setLayout(new BorderLayout());
		
		setTitle(messages.getString("MAIN.TITLE"));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			
			
			public void windowClosing(WindowEvent e) {
			
				if (model.isModified()&&!(noserver&&(model.getLastSavedFile()==null))){
					
					int result=JOptionPane.
						showOptionDialog(me, messages.getString("ACTION.CONFIRMSAVE"), 
										 messages.getString("ACTION.CONFIRMSAVETITLE"), 
										 JOptionPane.YES_NO_CANCEL_OPTION, 
										 JOptionPane.QUESTION_MESSAGE, 
										 null, 
										 new String[] { messages.getString("COMMON.YES"),
														messages.getString("COMMON.NO"),
														messages.getString("COMMON.CANCEL")}, 
										null);
	
					
					// Cancel
					if (result==2) return;
					
					// Yes - do save
					if (result==0){
						if (noserver){
							// no connection to server = save file
							SaveToFileAction sa=new SaveToFileAction(model,me);
							sa.actionPerformed(new ActionEvent(me,0,null));
						}else{
							// connected to server = save on server
							SaveToServerAction sa=new SaveToServerAction(model,me,canvas);
							sa.actionPerformed(new ActionEvent(me,0,null));

						}
					}
				}
				me.setVisible(false);
				me.dispose();
				System.exit(0);
			}
		
		});
		
		mainPanel=new MainPanel(model);
		canvas=mainPanel.getCanvas();
		sidePanel=new JTabbedPane();
		
		propertyPanel=new PropertyPanel(model);
		htmlViewPanel=new HTMLViewPanel(model);
		
		sidePanel.add(propertyPanel,messages.getString("MAIN.EDIT"));
		sidePanel.add(htmlViewPanel,messages.getString("MAIN.VIEW"));
		sidePanel.setTabPlacement(JTabbedPane.BOTTOM);
		
		sidePanel.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				
				FlowChartElement dfe=model.getFirstSelectedVertex();
				if (dfe==null) dfe=model.getSelectedEdge();
				if (dfe==null) dfe=model.getPathElement();
				if (dfe!=null) {
				
					if (sidePanel.getSelectedComponent()==propertyPanel){
						propertyPanel.reloadPanel(dfe);
					}else{
						htmlViewPanel.reloadPanel(dfe);
					}
				}
			}
		});
			
		JSplitPane horizontalSplit=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,mainPanel,sidePanel);
		
		getContentPane().add(horizontalSplit,BorderLayout.CENTER);
		setBounds(312,84,800,600);
	
		actions=createMenu();
		model.setActions(actions);
		setVisible(true);
		horizontalSplit.setDividerLocation(0.6);
		horizontalSplit.setResizeWeight(1);
		
		repaint();
	}
	
	public void setState(int i){
		mainPanel.canvas.setState(i);
	}
	
	public User getUser(){
		return user;
	}
	
	private void initModel(){
		   model=new PathModel (this);
	}
	
	/*
	 * builds up the menu
	 */
	
	private Action[] createMenu(){
		    
		  	ArrayList actionList=new ArrayList();
		  	
		  	JMenuBar mainBar=new JMenuBar();
		  	setJMenuBar(mainBar);
		  	
		    JMenu menuPath=new JMenu(messages.getString("MENU.PATH"));
		    mainBar.add(menuPath);
		    
		    Action addPath=new NewPathCommmand(model,this);
		    actionList.add(addPath);
		    menuPath.add(addPath);
		    
		    Action changeMetadata=new ChangeMetadataAction(model,this);
		    actionList.add(changeMetadata);
		    menuPath.add(changeMetadata);
		    
		    Action loadPathServer=new LoadFromServerAction(model,this);
		    actionList.add(loadPathServer);
		    menuPath.add(loadPathServer);
		    if(noserver) loadPathServer.setEnabled(false);
		    
		    Action savePathServer=new SaveToServerAction(model,this,canvas);
		    
		    actionList.add(savePathServer);	
		    JMenuItem saveAsPathMenu=new JMenuItem(savePathServer);
		    saveAsPathMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		    menuPath.add(saveAsPathMenu);
		    
		    if(noserver) savePathServer.setEnabled(false); 
		    
		    Action loadPathFile=new LoadFromFileAction(model,this);
		    actionList.add(loadPathFile);
		    menuPath.add(loadPathFile);
		    
		    Action savePathFile=new SaveToFileAction(model,this);
		    actionList.add(savePathFile);	
		    JMenuItem savePathMenu=new JMenuItem(savePathFile);
		    savePathMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
		    menuPath.add(savePathMenu);
		    
		    Action saveAsPathFile=new SaveAsToFileAction(model,this);
		    actionList.add(saveAsPathFile);
		    menuPath.add(saveAsPathFile);

		    Action imagePath=new SaveAsToImageAction(model,this,canvas);
		    actionList.add(imagePath);
		    menuPath.add(imagePath);
		    
		    Action htmlPath=new SaveAsToHTMLAction(model,this,canvas);
		    actionList.add(htmlPath);
		    menuPath.add(htmlPath);

		    Action mvpPath=new SaveAsToMVPAction(model,this,canvas);
		    actionList.add(mvpPath);
		    menuPath.add(mvpPath);

		    Action glifPath=new SaveAsToGLIFAction(model,this,canvas);
		    actionList.add(glifPath);
		    menuPath.add(glifPath);



		   /* Action changeTemplate=new ChangeTemplateAction(model,this);
		    actionList.add(changeTemplate);
		    menuPath.add(changeTemplate);*/
		    
		    Action duplicatePath=new DuplicatePathCommand(model,this,canvas);
		    actionList.add(duplicatePath);
		    menuPath.add(duplicatePath);

		    
		    JMenu menuElements=new JMenu(messages.getString("MENU.ELEMENTS"));
		    mainBar.add(menuElements);
		    
		    Action addComponent=null;
		    
		    // ------------ TOOL elements ----------------
		    
		    String[] vertexTypes={"DataFlowComponent","DataFlowBranch","DataFlowEnd","DataFlowSubpath","DataFlowComment"};
		    Action[] addVertexAction=new Action[vertexTypes.length];
		    
		    for (int i = 0; i < addVertexAction.length; i++) {
		    	try{
				    Class<FlowChartNode> clazz=(Class<FlowChartNode>)Class.forName("edu.uj.zbit.bpath.editor.model.elements."+vertexTypes[i]);
					ImageIcon icon=new ImageIcon(getClass().getResource("/ico/"+vertexTypes[i]+".gif"));
				    addComponent=new AddGenericVertex(model,this,clazz,icon);			    
				    addComponent.setEnabled(false);
				    actionList.add(addComponent);
				    menuElements.add(addComponent);
				    addVertexAction[i]=addComponent;
			    }catch (Exception e) {
					e.printStackTrace();
				}
			}
		    
		    // ------------- MOVE elements --------------
		    JMenu menuMove=new JMenu(messages.getString("ACTION.MOVE"));
		    menuElements.add(menuMove);
		    
		    Action moveLeft=new MoveAllLeftCommmand(model,this);
		    actionList.add(moveLeft);
		    JMenuItem moveLeftItem=new JMenuItem(moveLeft);
		    moveLeftItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK));
		    menuMove.add(moveLeftItem);
		    
		    Action moveRight=new MoveAllRightCommmand(model,this);
		    actionList.add(moveRight);
		    JMenuItem moveRightItem=new JMenuItem(moveRight);
		    moveRightItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.CTRL_DOWN_MASK));		    
		    menuMove.add(moveRightItem);
		  
		    Action moveDown=new MoveAllDownCommmand(model,this);
		    actionList.add(moveDown);
		    JMenuItem moveDownItem=new JMenuItem(moveDown);
		    moveDownItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK));			    
		    menuMove.add(moveDownItem);

		    Action moveUp=new MoveAllUpCommmand(model,this);
		    actionList.add(moveUp);
		    JMenuItem moveUpItem=new JMenuItem(moveUp);
		    moveUpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK));	
		    menuMove.add(moveUpItem);
		    
		    /*JMenu menuDebug=new JMenu("Debug");
		    mainBar.add(menuDebug);
		    
		    Action debugAction=new DebugCommmand(model,this);
		    actionList.add(debugAction);
		    menuDebug.add(debugAction);*/
		    
		    JToolBar jTool=new JToolBar();
		    getContentPane().add(jTool,BorderLayout.NORTH);
		    
		    for (int j = 0; j < addVertexAction.length; j++) {
		    	jTool.add(addVertexAction[j]);
			}
		    
    
		    Object[] objTab=actionList.toArray();
		    Action[] act=new Action[objTab.length];
		    for (int i = 0; i < act.length; i++) {
				act[i]=(Action)objTab[i];
			}
		    
		    return act;
		    
	  }
	
	public boolean isFocusTraversable()
	{
	 return true;
	}
	
	public void emergencySave(){
		for (Action action : actions) {
			
			if (action instanceof SaveAsToFileAction){
				action.actionPerformed(new ActionEvent(this,0,"emergency save"));
			}
		}
	}
	
	
}
