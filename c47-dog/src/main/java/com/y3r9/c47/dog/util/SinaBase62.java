package com.y3r9.c47.dog.util;

public class SinaBase62 {
	private static final String DICTIONARY = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private String str;
	public static String base62(String mid) {
		long int_mid = Long.valueOf(mid);
		StringBuilder result = new StringBuilder("");
		do {
			long a = int_mid % 62;
			result.insert(0, DICTIONARY.substring((int)a, (int)a + 1));
			int_mid = (int_mid - a) / 62;
		} while (int_mid > 0);
		int b = 4 - result.length();
		if (b > 0) {
			for (int i=0; i<b; i++) {
				result.insert(0, "0");
			}
		}
		return result.toString();
	}
	
	public static String getSinamid(String mid)  {
	//	long int_mid = Long.valueOf(mid);
	    StringBuilder result = new StringBuilder();
	    for (int i=mid.length()-7; i>-7; i-=7) {
	          int offset1 = (i < 0) ? 0 : i;
	          int offset2 = i + 7;
	          System.out.println(offset1 + " [] " + offset2);
	          String num = base62(mid.substring(offset1, offset2));
	          result.insert(0, num);
	    }
	    while (result.charAt(0) == '0') {
	    	result.deleteCharAt(0);
	    };
	    return result.toString();
	}
	
	public static void main(String[] args) {
		String mid = "3491273850170657";
		new SinaBase62();
		System.out.println(SinaBase62.getSinamid(mid));
		System.out.println(System.currentTimeMillis());
		System.out.println(null == new SinaBase62().getStr());
	}
	
	public String getStr() {
		return str;
	}
}
