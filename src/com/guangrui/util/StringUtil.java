package com.guangrui.util;

public class StringUtil {
	
	public static boolean isNotBlank(String s){
		if(s == null){
			return false;
		}
		return !"".equals(s.trim());
	}

}
