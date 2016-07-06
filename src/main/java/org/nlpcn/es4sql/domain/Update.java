package org.nlpcn.es4sql.domain;

import java.util.Map;

public class Update extends Query {
	
	private String fromString;
	
	private String whereString;
	
	private Map<String,Object> items;

	public Map<String, Object> getItems() {
		return items;
	}

	public void setItems(Map<String, Object> items) {
		this.items = items;
	}

	public String getFromString() {
		return fromString;
	}

	public void setFromString(String fromString) {
		this.fromString = fromString;
	}

	public String getWhereString() {
		return whereString;
	}

	public void setWhereString(String whereString) {
		this.whereString = whereString;
	}
	
	public void addItem(String name,String value) {
		
	}
	
}
