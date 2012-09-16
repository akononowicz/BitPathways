/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model;

import java.util.Hashtable;

public class Metadata extends Hashtable<String, MetadataEntity[]> implements Comparable{
	
	private static String SORT_METHOD="authors";

	public static String getSORT_METHOD() {
		return SORT_METHOD;
	}

	public static void setSORT_METHOD(String sort_method) {
		SORT_METHOD = sort_method;
	}

	
	/**
	 * this method is imperfect - good enough for first version
	 */
	@Override
	public int compareTo(Object _metadata) {
		
		if (_metadata instanceof Metadata){
			
		try{
			Metadata m=(Metadata) _metadata;
			
			MetadataEntity field1=this.get(SORT_METHOD)[0];
			MetadataEntity field2=m.get(SORT_METHOD)[0];
				
			return field1.getName().compareTo(field2.getName());
		}catch(Exception e){return -1;}
		
		}else return 0;
	} 
	
	public MetadataEntity getCreatedMetadata(){
		if (get("created")==null) return null;
		return get("created")[0];
	}
	
	public MetadataEntity getLastModifiedMetadata(){
		if (get("last_modified")==null) return null;
		return get("last_modified")[0];
	}
	
	public MetadataEntity getTeacherMetadata(){
		if (get("teacher")==null) return null;
		return get("teacher")[0];
	}	
	
	public MetadataEntity getGroupMetadata(){
		if (get("group")==null) return null;
		return get("group")[0];
	}	
	
	public MetadataEntity getTopicMetadata(){
		if (get("topic")==null) return null;
		return get("topic")[0];
	}	
	
	public MetadataEntity getTitleMetadata(){
		if (get("title")==null) return null;
		return get("title")[0];
	}	

	public void setTitleMetadata(String title){
		get("title")[0].setName(title);
	}	
	
	public MetadataEntity[] getAuthorsMetadata(){
		return get("authors");
	}	
}
