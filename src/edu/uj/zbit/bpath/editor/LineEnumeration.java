/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Enumeration;

/**
 * Gives an enumeration of lines which fit into a shape. 
 * Applied in labeling nodes.
 * 
 * @author Andrzej A. Kononowicz
 *
 */

public class LineEnumeration implements Enumeration{

	private Font font;
	private String text;

	private int width;
	private int position;
	private int length;
	private int start = 0;
	private Graphics2D g;

	public LineEnumeration(Graphics2D g,Font font, String text, int width){
		this.font = font;
		this.text = text;
		this.width = width;
		this.length = text.length();
		this.g=g;
	}

	public boolean hasMoreElements(){
		return (position < (length-1));
	}

	public Object nextElement(){
		try{
			return text.substring(start,(start = next()));
		}catch ( IndexOutOfBoundsException e ){
			e.printStackTrace();
		}
		return (Object)null;
	}

	private int next(){
		int i = position;
		int lastBreak = -1;

		FontMetrics fontMetrics=g.getFontMetrics(font);
		
		for ( ;i < length && fontMetrics.stringWidth(text.substring(position,i)) <= width; i++ ){
			if ( text.charAt(i) == ' ' ){
				lastBreak = i;}
			else if ( text.charAt(i) == '\n' ){
				lastBreak = i;
				break;
			}
		}

		if ( i == length ){
			position = i;
		}
		else if ( lastBreak <= position ){
			position = i;
		}else{
			position = lastBreak;
		}

		return position;
	}
}
