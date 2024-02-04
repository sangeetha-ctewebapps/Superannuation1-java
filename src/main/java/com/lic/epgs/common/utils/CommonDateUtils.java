package com.lic.epgs.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Dhatchanamoorthy M
 *
 */

public class CommonDateUtils {

	private CommonDateUtils() {
	}

	static {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
	}

	public static Date sysDate() {
		return new Date();
	}

	public static Date stringToDateFormatter(String value) /* throws ApplicationException */ {

		Date date = new Date();

		try {
			if (value != null) {
				date = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(value);
			} else {
				return null;
			}
		} catch (ParseException e) {
			try {
				date = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").parse(value);
			} catch (ParseException e1) {
				return null;
			}
		}
		return date;
	}

	public static Date stringDateToDateFormatter(String value) /* throws ApplicationException */ {

		Date date = new Date();

		try {
			if (value != null) {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(value);
			} else {
				return null;
			}
		} catch (ParseException e) {
			try {
				date = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").parse(value);
			} catch (ParseException e1) {
				return null;
			}
		}
		return date;
	}

	public static String dateToStringFormatYyyyMmDdHhMmSsSlash(Date value) {
		String val = "";
		if (value != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			val = sdf.format(value);
		}
		return val;
	}

	public static Date convertStringToDate(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat("dd/MM/yyyy");
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				/***
				 * ParseException
				 */
			}
		}
		return null;
	}

	public static String dateToStringFormatYyyyMmDdSlash(Date value) {
		String val = "";
		if (value != null) {
			final String DATE_FORMAT_NOW = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			val = sdf.format(value);
		}
		return val;
	}

	public static Date stringToDateFormatYyyyMmDdDash(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				/***
				 * 
				 */
			}
		}
		return null;
	}

	public static Date constructeStartDateTime(Date start) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 00);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		cal.set(Calendar.MILLISECOND, 00);
		return cal.getTime();
	}
	
	public static Date constructeEndDateTime(Date end) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(end);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 59);
		return cal.getTime();
	}

}
