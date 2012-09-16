package edu.uj.zbit.bpath.editor;

public class ServerConfig {
	
	/* default db values */
	/* in future version this is to be changed into more secure solution */
	
	private String db_username="bpath_user";
	private String db_pass="bpath_default";
	private String db_name="bpath"; // Name of the db collection 
	private String db_type="existdb";
	
	private String db_address="";
	private String db_port="";
	
	public void setDb_username(String db_username) {
		this.db_username = db_username;
	}

	public void setDb_pass(String db_pass) {
		this.db_pass = db_pass;
	}
	
	private String label="";

	public String getDb_name() {
		return db_name;
	}

	public ServerConfig(String _label, String _db_address, String _db_port){
		label=_label;
		db_address=_db_address;
		db_port=_db_port;
	}

	public String getDb_username() {
		return db_username;
	}

	public String getDb_pass() {
		return db_pass;
	}

	public String getLabel() {
		return label;
	}

	public String getDb_address() {
		return db_address;
	}

	public String getDb_port() {
		return db_port;
	}
}
