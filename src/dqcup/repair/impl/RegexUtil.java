package dqcup.repair.impl;

import java.util.regex.Pattern;

public class RegexUtil {
	private static Pattern ssnPattern = Pattern.compile("\\d{9}");
	private static Pattern fnamePattern = Pattern.compile("^[A-Z][a-z,.]*");
	private static Pattern minitPattern = Pattern.compile("[A-Z]|");
	private static Pattern lnamePattern = Pattern.compile("^[A-Z][a-z,.]*");
	private static Pattern stnumPattern = Pattern.compile("\\d{1,4}");
	private static Pattern stadd1Pattern = Pattern.compile("[A-Za-z ,.]*");
	private static Pattern stadd2Pattern = Pattern.compile("PO Box \\d{1,4}");
	private static Pattern apmtPattern = Pattern.compile("\\d[a-z]\\d");
	private static Pattern cityPattern = Pattern.compile("[a-z'-/. ]*");
	private static Pattern statePattern = Pattern.compile("[A-Z]{2}");
	private static Pattern zipPattern = Pattern.compile("\\d{5}");
	

	public static boolean isSsnValid(String value){
		return ssnPattern.matcher(value).matches();
	}
	
	public static boolean isFnameValid(String value){
		return fnamePattern.matcher(value).matches();
	}
	
	public static boolean isMinitValid(String value){
		return minitPattern.matcher(value).matches();
	}
	
	public static boolean isLnameValid(String value){
		return lnamePattern.matcher(value).matches();
	}
	
	public static boolean isStnumValid(String value, String stadd){
		if(stadd.startsWith("PO Box") && isStaddValid(stadd) && (value == "" || value == null))
			return true;
		return stnumPattern.matcher(value).matches();
	}
	
	public static boolean isStaddValid(String value){
		if(value.startsWith("PO Box"))
			return stadd2Pattern.matcher(value).matches();
		return stadd1Pattern.matcher(value).matches();
	}
	
	public static boolean isApmtValid(String value, String stadd){
		// if(stadd.startsWith("PO Box") && isStaddValid(stadd) && (value == "" || value == null))
		// if(stadd.startsWith("PO Box") && (value == null || value == "" ))
		if(stadd.startsWith("PO Box"))
			return true;
		return apmtPattern.matcher(value).matches();
	}
	
	public static boolean isCityValid(String value){
		return cityPattern.matcher(value).matches();
	}
	
	public static boolean isStateValid(String value){
		return statePattern.matcher(value).matches();
	}
	
	public static boolean isZipValid(String value){
		return zipPattern.matcher(value).matches();
	}

	
	public static void main(String[] args) {
		System.out.println(isSsnValid("999999999"));
		System.out.println(isFnameValid("Steve,.."));
		System.out.println(isMinitValid("A"));
		System.out.println(isStnumValid("", "PO Box sl"));
		System.out.println(isStaddValid("PO Box 4940"));
		System.out.println(isCityValid("'ehll'  / - . "));
	}
}
