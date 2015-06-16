/**
 * @author Adam Hardie
 */
package com.bpc.services.util;

import java.util.Date;

public class DateUtils {
	/**
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	public static int daysBetween(Date date1, Date date2) throws Exception {
		return (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
	}
}
