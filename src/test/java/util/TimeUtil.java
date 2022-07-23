package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	public static String getToday(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date = new Date();
		String expectedToday = formatter.format(date);
		return expectedToday;
	}
	
	public static String getYesterday(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date yesterdayDate = cal.getTime();
		String yesterday = formatter.format(yesterdayDate);
		return yesterday;
	}
}
