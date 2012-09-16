/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model;

/**
 * Represents a user of BIT Pathways
 * @author Andrzej A. Kononowicz
 */

public class User {

	private int uid;
	private String role;
	private String firstname;
	private String famname;
	private String group;
	private UserGroupInfo groupInfo;	

	public User(int _uid,String _role,String _firstname, String _famname, String _group){
		uid=_uid;
		role=_role;
		firstname=_firstname;
		famname=_famname;
		group=_group;
	}

	public int getUid() {
		return uid;
	}

	public String getRole() {
		return role;
	}
	
	public boolean isAdmin(){
		return role.equalsIgnoreCase("admin");
	}

	public String getFamname() {
		return famname;
	}

	public String getFirstname() {
		return firstname;
	}
	
	public String getGroup(){
		return group;
	}
	
	public UserGroupInfo getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(UserGroupInfo groupInfo) {
		this.groupInfo = groupInfo;
	}
	
}
