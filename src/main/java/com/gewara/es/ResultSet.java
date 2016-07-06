package com.gewara.es;

import java.util.Date;
import java.util.Map;

import com.gewara.util.DateUtils;

public class ResultSet {
	
	private Map<String,String> setMap;
	
	
	public ResultSet(Map<String,String> setMap){
		this.setMap = setMap;
	}
	
	public String getString(String column){
		
		if(setMap.containsKey(column.toUpperCase())){
			return setMap.get(column.toUpperCase());
		}else{
			return null;
		}
	}
	
	public Long getLong(String column){
		if(setMap.containsKey(column.toUpperCase())){
			return Long.parseLong(setMap.get(column.toUpperCase()));
		}else{
			return null;
		}
	}
	
	public Integer getInteger(String column){
		if(setMap.containsKey(column.toUpperCase())){
			return Integer.parseInt(setMap.get(column.toUpperCase()));
		}else{
			return null;
		}
	}
	
	public String getDateString(String column,String format){
		if(setMap.containsKey(column.toUpperCase())){
			return DateUtils.format(new Date(Long.parseLong(setMap.get(column.toUpperCase()))), format);
		}else{
			return null;
		}
	}
	
}
