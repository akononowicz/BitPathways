/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.dialogs.mapping;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * Main window of the Template Mapping editor. Maps attributes from 
 * one template (source) onto another (destination). Not all source attributes must be mapped.
 * Some source attributes may be used by several target (destination) attributes. 
 * Not all destination attributes have to be mapped.  
 * 
 * @author Andrzej Kononowicz
 */

public class TemplateMappingDialog extends JDialog {

	/* TODO: user may select src, dest templates and mapping file name*/
	private String srcTemplateName="t_cp_v001.xml";
	private String destTemplateName="t_vp_v001.xml";
	private String mappingFileName="output/cp2vp_v001.xml";
	
	private TemplateProcessor srcTemplate=new TemplateProcessor("input/"+srcTemplateName);
	private TemplateProcessor destTemplate=new TemplateProcessor("input/"+destTemplateName);
	
	private JPanel headerPanel=new JPanel();
	private JPanel mainPanel=new JPanel();
	private JPanel optionPanel=new JPanel();
	
	private JLabel label1=new JLabel("Element: ");
	
	private JButton newButton = new JButton("Nowy");
	private JButton loadButton= new JButton("Otwórz");
	private JButton saveButton= new JButton("Zapisz");
	
	private JTable table;
	private ElementMappingModel model;
	
	/** stores mapping objects (value) for all element types of the destination template */ 
	private Hashtable<String, ElementMapping> mappingModel=new Hashtable<String, ElementMapping>(); 

	private JComboBox combo;
	
	public TemplateMappingDialog(){
		
		setTitle("Narzêdzie do mapowania szablonów programu Bit Pathways");
		getContentPane().setLayout(new BorderLayout());
		
		// ---- model initialization
		modelInitialization();

		// --- GUI 	initialization	
		
		table=new JTable(model);
		
		JScrollPane scrollPane = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension(750, 300));
		table.setFillsViewportHeight(true);
		

		mainPanel.add(scrollPane);
		
		combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				model.fireTableStructureChanged();
				setModelElement(combo.getSelectedItem().toString());

			}
		});
		
		setModelElement(combo.getSelectedItem().toString());
		
		headerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		headerPanel.add(label1);
		headerPanel.add(combo);
		
		optionPanel.add(newButton);
		optionPanel.add(loadButton);
		optionPanel.add(saveButton);		
	
		getContentPane().add(headerPanel,BorderLayout.NORTH);
		getContentPane().add(mainPanel,BorderLayout.CENTER);
		getContentPane().add(optionPanel,BorderLayout.SOUTH);
		
		
		// place for saving mapping into a file
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					
					// create new document
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				
					 //root elements
					Document doc = docBuilder.newDocument();
					Element rootElement = doc.createElement("bitpathways");
					doc.appendChild(rootElement);
					
					rootElement.setAttribute("version", "2.0");
					
					Element mappingElement = (Element)rootElement.appendChild(doc.createElement("mapping"));
					mappingElement.setAttribute("src_template", srcTemplateName.split(".xml")[0]);
					mappingElement.setAttribute("dest_template", destTemplateName.split(".xml")[0]);
					mappingElement.setAttribute("src_repository", "puls.cm-uj.krakow.pl/bpath/templates");					
					mappingElement.setAttribute("dest_repository", "puls.cm-uj.krakow.pl/bpath/templates");
					
					Enumeration<String> elementKeys=mappingModel.keys();
					
					while (elementKeys.hasMoreElements()) {
						String type = (String) elementKeys.nextElement();
						mappingElement.appendChild((appendElementMapping(doc,mappingModel.get(type))));						
					}
					
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result =  new StreamResult(new File(mappingFileName));
					transformer.transform(source, result);
					
					System.out.println("Saved!");
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}	
			}
		});
	
		
		// place for saving mapping into a mapping file
		loadButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				// ---- model initialization
				modelInitialization();
				
				// MAÆKU !!! - ZADANIE 1 miejsce na Twoja aktywnosc. Po nacisnieciu na przycisk "Otwórz" 
				// Trzeba zaladowac plik ze zmiennej mappingFileName do pamieci (zmiennej mappingModel)
				// czytasz plik po typach elementów PATH, TASK, itd
				// i wpisujesz dla kazdego destAttribute wartosc srcAttribute 
				// do odpowiedniego ElementMappingu (z mappingModel) przy uzyciu metody
				// ElementMapping.setSrcValue4Dest.
				
			}
		});
		
		newButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				
				// MAÆKU !!! - ZADANIE 2 to jest drugie miejsce na Twoja aktywnosc
				// ale to juz jest bardziej zaawansowane
				// potrzebne by bylo otwierane w reakcji na nacisniecie guzika "Nowy"
				// okienko za pomoca ktorego mozna by bylo sobie wybrac
				// wartosci srcTemplateName, destTemplateName
				// oraz mappingFileName z klasy TemplateMappingDialog
				
				// okienko powinno dziedziczyc z JDialog, 
				// srcTemplateName i destTemplateName powinny byc wybierane po kliknieciu na odpowiednie guziki
				// w nowym okienku. Wybor pliku przez standardowy wybieracz plikow systemu operacyjnego (nigy nie pisz tego sam)
				// czyli javax.swing.JFileChooser. dla mappingFileName wybieraloby sie tylko katalog (tez za pomoca javax.swing.JFileChoosers) 
				// natomiast nazwa pliku byla by srcTemplateName+"2"+destTemplateName+"_mapping.xml".

				
				// ---- model initialization
				modelInitialization();
				
			}
		});
		
	}
	
	private void modelInitialization(){
		String[] types=destTemplate.getElementNames();
		
		for (String type : types) {
			mappingModel.put(type, new ElementMapping(srcTemplate, destTemplate, type));
		}
		
		combo=new JComboBox(types);
		combo.setSelectedItem("PATH");
		
		model=new ElementMappingModel(srcTemplateName,destTemplateName);
	}
	
	private Element appendElementMapping(Document doc,ElementMapping map){
		Element elementMap=doc.createElement("element_map");
		elementMap.setAttribute("id", map.getElementName());
		
		for (int i = 0; i < map.srcTab.length; i++) {
			if((map.srcTab[i]!=null)&&(map.srcTab[i]!="")){
				Element mapE=doc.createElement("map");
				mapE.setAttribute("src", map.srcTab[i]);
				mapE.setAttribute("dest", map.destTab[i]);
				elementMap.appendChild(mapE);
			}
		}
		
		return elementMap; 
	}
	
	public void setModelElement(String element){
		
		model.setElementMapping(mappingModel.get(element));
		table.getColumnModel().getColumn(1).setCellEditor(new SrcElementEditor(mappingModel.get(element)));
		
	}
	
	
}
