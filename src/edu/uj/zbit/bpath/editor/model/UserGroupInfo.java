/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model;

/**
 * Informs about topics, instructors, templates, classes assigned to a particular
 * user group. Comes in usage when creating new pathways (NewPathWizard). 
 * Created by ExistConnect - getUserGroupInfo.    
 */


public class UserGroupInfo {

	String[] topics=null;	
	String[] teachers=null;
	String[] subgroups=null;
	TemplateInfo[] templates=null;
	
	public TemplateInfo[] getTemplates() {
		return templates;
	}
	public void setTemplates(TemplateInfo[] templates) {
		this.templates = templates;
	}
	public String[] getTopics() {
		return topics;
	}
	public String[] getTeachers() {
		return teachers;
	}
	public String[] getSubgroups() {
		return subgroups;
	}
	public void setTopics(String[] topics) {
		this.topics = topics;
	}
	public void setTeachers(String[] teachers) {
		this.teachers = teachers;
	}
	public void setSubgroups(String[] subgroups) {
		this.subgroups = subgroups;
	}

}
