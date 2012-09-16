/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.helper.xslt;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * 
 * W transformatach XSLT bazy eXist wystepuje problem z tlumaczeniem znakow specjalnych 
 * zagniezdzonego w tekscie HTML. Atrybut "disable-output-escaping" nie dziala.
 * Dlatego napisana zostala ponizsza funkcja, ktora przerabia HTML z CDATA na DOMa.
 * Funkcja ta jest wywolywana przez transformaty z poziomu XSLT. Skompilowany pakiet jar
 * zawierajacy ta klase powinien znalezc sie w classpath Tomcata - 
 * np.C:\Java\Tomcat6\webapps\exist\WEB-INF\lib\ 
 * 
 * @author Andrzej Kononowicz
 *
 */

public class XSLTMethods {

	public static Node doesc(String input){
		
		input=input.replaceAll("&lt;", "<");
		input=input.replaceAll("&gt;", ">");
		input=input.replaceAll("&amp;", "&");
		input="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+"<div>"+input+"</div>";
		System.out.println(input);
		try {
			
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                
            Document doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(input)));
            // Insert the root element node
            
            return doc;
        } catch (Exception e) {
        	e.printStackTrace();
        }	        
        return null;
		
		
	}

}
