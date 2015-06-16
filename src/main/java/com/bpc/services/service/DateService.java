/**
 * @author Adam Hardie
 */
package com.bpc.services.service;

import java.text.DateFormatSymbols;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.bpc.services.util.IsoUtils;


public class DateService {


	private static final Logger logger = LogManager.getLogger("com.bpc.services.dates");

	/**
	 * Returns the set of months for a selected locale
	 * @return
	 * @throws Exception
	 */
	public static String getMonths(String languageCode) throws Exception {
		return wrapResponse(monthNames(languageCode));
	}
	
	/**
	 * Returns the set of days of the week for a selected locale
	 */
	public static String getDays(String languageCode) throws Exception {
		return wrapResponse(weekDayNames(languageCode));
	}
	
	private static String weekDayNames(String languageCode) throws Exception{
		Locale usersLocale = setLocaleByLanguageCode(languageCode);
	    DateFormatSymbols dfs = new DateFormatSymbols(usersLocale);
	    StringBuffer result = new StringBuffer();
	    result.append("<days>");
	    String[] names = dfs.getWeekdays();
	    for (int i = 0; i < names.length; i++) {
	    	if(names[i].length() >0){
	    	   result.append( "<day>"+names[i]+"</day>");
	    	}
	    }
	    result.append("</days>");
	    return result.toString();
	}
	
	private static String monthNames(String languageCode) throws Exception{
		Locale usersLocale = setLocaleByLanguageCode(languageCode);
	    DateFormatSymbols dfs = new DateFormatSymbols(usersLocale);
	    StringBuffer result = new StringBuffer();
	    result.append("<months>");
	    String[] names = dfs.getMonths();
	    for (int i = 0; i < names.length; i++) {
	    	if(names[i].length() >0){
	    	   result.append( "<month>"+names[i]+"</month>");
	    	}
	    }
	    result.append("</months>");
	    return result.toString();
	}
	
	private static Locale setLocaleByLanguageCode(String languageCode){
		Locale usersLocale;
		if(isValid(languageCode)){
			usersLocale = new Locale(languageCode);
		}
		else{
			throw new IllegalArgumentException("Invalid ISO Language Code: " + languageCode);
		}
		return usersLocale;
	}
	
	private static boolean isValid(String languageCode) {
		return IsoUtils.isValidISOLanguage(languageCode);
	}
	
	private static String wrapResponse(String content){
		return "<result>" + content + "</result>";
	}
}