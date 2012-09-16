/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import java.awt.Color;

import javax.swing.table.DefaultTableCellRenderer;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.uj.zbit.bpath.editor.model.XMLProcessor;

public class DurationCellRenderer extends DefaultTableCellRenderer{
	
	public DurationCellRenderer() {
		super();
	}
	
	protected void setValue(Object value){
		super.setValue(value);		
	}

	/**
	* Method used to expand from "PYMDTHMS" format to "Years Months etc." format
	*
	* @param duration - string to expand, 
	* @param lang - string having an information about language of the expansion
	* 
	* @author Maciej Wilk
	* 
	* @return expanded string
	*/
	
	public static String getDurationLabel(String duration, String lang){
		
		duration = duration.toUpperCase();
		String days = "";
		String time = "";
		if(duration.contains("T")){
			int middle = duration.indexOf("T");
			days = duration.substring(0, middle);
			int end = duration.length();
			time = duration.substring(middle, end);
		}
		else{
			days = duration.substring(0);
		}
		int[] durationTable = new int[6];
		
		durationTable[0] = getDateItem(days, "Y");
		durationTable[1] = getDateItem(days, "M");
		durationTable[2] = getDateItem(days, "D");
		durationTable[3] = getDateItem(time, "H");
		durationTable[4] = getDateItem(time, "M");
		durationTable[5] = getDateItem(time, "S");

		if(lang.equalsIgnoreCase("pl")){
			duration = getLabelPolish(durationTable);
		}
		else{
		if(lang.equalsIgnoreCase("de")){
			duration = getLabelGerman(durationTable);
		}
		else{
			duration = getLabelEnglish(durationTable);
		}
		}

		return duration;
	}
	
	private static String getLabelPolish(int[] durationTable){
		
		String[][] expansionTablePl = {
                {"rok","lata","lat"},
                {"miesi¹c","miesi¹ce","miesiêcy"},
                {"dzieñ","dni","dni"},
                {"godzina","godziny","godzin"},
                {"minuta","minuty","minut"},
                {"sekunda","sekundy","sekund"}
		};
		
		String ret = "";
		
		for(int i = 0; i < 6; i++){
		if(durationTable[i]>0){
			if(durationTable[i]==1){
				ret += durationTable[i] +" "+ expansionTablePl[i][0]+" ";
			}
			if(durationTable[i] > 1 && durationTable[i] < 5){
				ret += durationTable[i]+" " + expansionTablePl[i][1]+" ";
			}
			if(durationTable[i]>=5){
				ret += durationTable[i]+" " + expansionTablePl[i][2]+" ";
			}
		}
		}
		return ret;
	}
	
	private static String getLabelGerman(int[] durationTable){
		
		String[][] expansionTableDe= {
                {"Jahr","Jahre"},
                {"Monat","Monate"},
                {"Tag","Tage"},
                {"Stunde","Studen"},
                {"Minute","Minuten"},
                {"Sekunde","Sekunden"}
		};
				
		String ret = "";
		
		for(int i = 0; i < 6; i++){
		if(durationTable[i]>0){
			if(durationTable[i]==1){
				ret += durationTable[i]+" " + expansionTableDe[i][0]+" ";
			}
			if(durationTable[i]>1){
				ret += durationTable[i]+" " + expansionTableDe[i][1]+" ";
			}
		}
		}
		return ret;
	}

	private static String getLabelEnglish(int[] durationTable){

		String[][] expansionTableEn = {{"year"},{"month"},{"day"},{"hour"},{"minute"},{"second"}};

		String reteng = "";
		
		for(int i = 0; i < 6; i++){
		if(durationTable[i]>0){
			if(durationTable[i]==1){
				reteng += durationTable[i]+" " + expansionTableEn[i][0] + " ";
			}
			if(durationTable[i] > 1){
				reteng += durationTable[i]+" " + expansionTableEn[i][0] + "s ";
			}
		}
		}
		return reteng;
	}
	
	private static int getDateItem(String date, String character){
		
		int pos = date.indexOf(character)-1;
		String value = "";
		for(int i=pos; i>=0; i--){
			char charct = date.charAt(i);
			if((charct >= '0') && (charct <= '9')){
				value = charct + value;
			}
			else break;
		}
		
		int number = 0;
		
		if (value.length()>0){
			number=Integer.parseInt(value);
		}
		return number;
	}
	
	
}
