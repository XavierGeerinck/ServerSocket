package com.hc.framework.utils;

public class StringUtils {
	public static String joinArray(String[] stringArray) {
        int k = stringArray.length;
		if (k == 0)
		  return null;
		StringBuilder out = new StringBuilder();
		out.append( stringArray[0] );
		for (int x=1;x<k;++x)
		  out.append(" ").append( stringArray[x] );
		
		return out.toString();
	}
}
