/**
* @author Kathiravan
* @description SimpleDateFormat thread safety issue : https://www.callicoder.com/java-simpledateformat-thread-safety-issues/
 */
package com.lic.epgs.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DateUtils {

	protected static final Logger logger = LogManager.getLogger(DateUtils.class);

	static {
		/**
		 * 
		 * TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
		 * 
		 **/
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

	}

	public DateUtils() {
		/**
		 * private constructor
		 */
	}

	private static DecimalFormat decimalFormatHHHMMM = new DecimalFormat("######");
	private static final String DDMMYYYY = "dd/MM/yyyy";
	private static final String DDMMYYYY_HYPEN = "dd-MM-yyyy";
	private static final String DDMMYYYYHMMA = "dd/MM/yyyy H:mm a";
	private static final String DDMMYYYYHHMMSS = "dd/MM/yyyy HH:mm:ss";
	private static final String YYYY_MM_DD_HHMMSS_HYPHEN = "yyyy-MM-dd HH:mm:ss.S";

	private static final String YYYY_MM_DD_HYPHEN = "yyyy-MM-dd";

	public static Date sysDate() {
		return new Date();
	}

	public static Date convertStringToDate(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat(DDMMYYYY);
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				logger.error("Parse Exception::", e);
			}
		}
		return null;
	}

	public static String dateToStringDDMMYYYY(Date value) {
		String val = "";
		if (value != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(DDMMYYYY);
			val = sdf.format(value);
		}
		return val;
	}

	public static String dateToHypenStringDDMMYYYY(Date value) {
		String val = "";
		if (value != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(DDMMYYYY_HYPEN);
			val = sdf.format(value);
		}
		return val;
	}

	public static String dateToStringFormatYyyyMmDdHhMmSsSlash(Date value) {
		String val = "";
		if (value != null) {

			SimpleDateFormat sdf = new SimpleDateFormat(DDMMYYYYHHMMSS);
			val = sdf.format(value);
		}
		return val;
	}

	public static String dateToStringFormatHhMmSs(Date value) {
		String val = "";
		if (value != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			val = sdf.format(value);
		}
		return val;
	}

	public static Date convertStringToDateTimeWithPeriod(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat(DDMMYYYYHMMA);
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				/***
				 * Error
				 */
			}
		}
		return null;
	}

	public static String dateToStringFormatYyyyMmDdHhMmSsSAlash(Date value) {
		String val = "";
		if (value != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
			val = sdf.format(value);
		}
		return val;
	}

	public static Map<String, String> findTwoDaysDiffHourMinitsSecendsMilliseconds(Date fromDate, Date toDate) {
		Map<String, String> map = new HashMap<>();
		if (fromDate != null && toDate != null) {
			try {
				long diff = fromDate.getTime() - toDate.getTime();
				int years = (int) (diff / (24 * 60 * 60 * 1000)) / 365;
				map.put("Age", String.valueOf(years));
				int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
				map.put("Days", String.valueOf(diffDays));
				int diffhours = (int) (diff / (60 * 60 * 1000));
				map.put("hours", decimalFormatHHHMMM.format(diffhours));
				int diffmin = (int) (diff / (60 * 1000));
				map.put("Minitues", decimalFormatHHHMMM.format(diffmin));
				int diffsec = (int) (diff / (1000));
				map.put("Secends", decimalFormatHHHMMM.format(diffsec));
				map.put("Milliseconds", decimalFormatHHHMMM.format(diff));
			} catch (IllegalArgumentException e) {
				/**
				 * Error
				 */
			}
		}
		return map;
	}

	/**
	 * (d/m/yyyy h:mm:ss tt", "dd/MM/yyyy hh:mm:ss", "d/M/yyyy hh:mm tt", "d/m/yyyy
	 * h:mm tt", "d/M/yyyy h:mm:ss", "d/M/yyyy hh tt", "d/M/yyyy h:mm", "d/M/yyyy
	 * h:mm","dd/MM/yyyy hh:mm", "dd/M/yyyy hh:mm)
	 **/
	public static Boolean validateIsDateFormatInHHMM(String dateValue) {
		boolean isValue = false;
		if (StringUtils.isNotEmpty(dateValue)) {
			isValue = true;
		}
		return isValue;
	}

	public static String uniqueNoYYYMMYDDMilli() {
		SimpleDateFormat dfFormat = new SimpleDateFormat("yyyMMyddkkmmssSS");
		return dfFormat.format(new Date());
	}

	public static String uniqueNoMilli() {
		SimpleDateFormat dfFormat = new SimpleDateFormat("ddMMyyyykkmm");
		return dfFormat.format(new Date());
	}

	public static String uniqueNoYYYY() {
		SimpleDateFormat dfFormat = new SimpleDateFormat("yyyy");
		return dfFormat.format(new Date());
	}

	public static String uniqueNoYYYYMMDD() {
		SimpleDateFormat dfFormat = new SimpleDateFormat("yyyyMMdd");
		return dfFormat.format(new Date());
	}

	public static long dateDifferenceInHour(Date fromDate, Date toDate) {
		LocalDateTime fromDateTime = dateToLocalDateTime(fromDate);
		LocalDateTime toDateTime = dateToLocalDateTime(toDate);
		/**
		 * long diffInDays = ChronoUnit.DAYS.between(fromDateTime, toDateTime); long
		 * diffInMin = ChronoUnit.MINUTES.between(fromDateTime, toDateTime); long
		 * diffInSec = ChronoUnit.SECONDS.between(fromDateTime, toDateTime); long
		 * diffInHours = ChronoUnit.HOURS.between(fromDateTime, toDateTime);
		 */
		return ChronoUnit.HOURS.between(fromDateTime, toDateTime);
	}

	public static LocalDateTime dateToLocalDateTime(Date date) {
		return date != null ? LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()) : null;
	}

	public static boolean isDatebetweenTwoDates(Date fromDate, Date toDate, Date currentDate) {
		return currentDate.after(fromDate) && currentDate.before(toDate);
	}

	public static List<LocalDate> getDatesBetweenUsingJava8(LocalDate startDate, LocalDate endDate) {
		long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
		return IntStream.iterate(0, i -> i + 1).limit(numOfDaysBetween).mapToObj(startDate::plusDays)
				.collect(Collectors.toList());
	}

	public static String convertLdtToString(LocalDate localDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DDMMYYYY);
		return localDate.format(formatter);
	}

	/*
	 * request format is convertStringToDBDateWithHHMMSSA("02/16/2021 1:36:00 pm")
	 */
	/* written from Kathiravan */
	@SuppressWarnings("deprecation")
	public static Date convertStringToDBDateWithHHMMSSA(String date) {
		try {
			if (StringUtils.isNotBlank(date) && !date.equalsIgnoreCase("null")) {
				return new Date(String.format(date));
			}
		} catch (IllegalArgumentException e) {
			logger.error("ParseException::convertStringToDBDateWithHHMMSSA::", e);
		}
		return null;
	}

	public static String uniqueNoYYYYMM() {
		SimpleDateFormat dfFormat = new SimpleDateFormat("yyyyMM");
		return dfFormat.format(new Date());
	}

	public static Date convertStringYYYYMMDDTHHMMSSZ(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				logger.error("ParseException::convertStringYYYYMMDDTHHMMSSZ::", e);
			}
		}
		return null;
	}

	public static Date convertStringDDMMYYYYYHHMMSS(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				logger.error("ParseException::convertStringDDMMYYYYYHHMMSS::", e);
			}
		}
		return null;
	}

	public static Date convertStringYYYYMMDDTHHMMSSSSZ(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				logger.error("ParseException:convertStringYYYYMMDDTHHMMSSSSZ:", e);
			}
		}
		return null;
	}

	public static Date getDateOnly() {
		Date today = sysDate();
		DateFormat formatter = new SimpleDateFormat(DDMMYYYY);
		try {
			return formatter.parse(formatter.format(today));
		} catch (ParseException e) {
			logger.error("ParseException:getDateOnly:", e);
		}

		return null;
	}

	public static boolean isValidDate1(String d) {
		String regex = "^([1-9])/([1-9])/[0-9]{4} [0-9]{2} [A-Z][A-Z]$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(d);
		return matcher.matches();
	}

	public static Date convertStringToDateYYYYMMDD(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				logger.error("ParseException::", e);
			}
		}
		return null;
	}

	public static Long dateDifferenceInMinutes(Date fromDate, Date toDate) {
		LocalDateTime fromDateTime = dateToLocalDateTime(fromDate);
		LocalDateTime toDateTime = dateToLocalDateTime(toDate);
		return ChronoUnit.MINUTES.between(fromDateTime, toDateTime);
	}

	public static Date convertStringToDateDDMMYYYYYHHMMSS(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat(DDMMYYYYHHMMSS);
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				logger.error("ParseException::", e);
			}
		}
		return null;
	}

	public static Long stringDateDifferenceInDays(String fromDate, String toDate) {
		LocalDateTime fromDateTime = dateToLocalDateTime(convertStringToDate(fromDate));
		LocalDateTime toDateTime = dateToLocalDateTime(convertStringToDate(toDate));
		return (fromDateTime != null && toDateTime != null) ? ChronoUnit.DAYS.between(fromDateTime, toDateTime) : null;
	}

	public static Long dateDifferenceInDays(Date fromDate, Date toDate) {
		if (fromDate != null && toDate != null) {
			LocalDateTime fromDateTime = dateToLocalDateTime((fromDate));
			LocalDateTime toDateTime = dateToLocalDateTime((toDate));
			return ChronoUnit.DAYS.between(fromDateTime, toDateTime);
		}
		return null;
	}

	public static Date convertStringToDateTimeWithSecsPeriod(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat("dd/MM/yyyy H:mm:ss a");
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				/***
				 * error
				 */
			}
		}
		return null;
	}

	public static java.sql.Date sysSQLDate() {
		return new java.sql.Date(System.currentTimeMillis());
	}

	public static Date getDateFromStringDateHyPen(String value) {
		Date date = null;
		if (value == null || value.isEmpty()) {
			return date;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DDMMYYYY_HYPEN);
			date = sdf.parse(value);
			if (!value.equals(sdf.format(date))) {
				date = null;
			}
		} catch (ParseException ex) {
			/***
			 * error
			 */
		}
		return date;
	}

	/////////////// common////////////////////////

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

	private static final MonthDay FINANCIAL_START = MonthDay.of(4, 1);
	private static final MonthDay FINANCIAL_END = MonthDay.of(3, 31);

	public static LocalDate getStartOfFinancialYear(LocalDate date) {
		/** Try "the same year as the date we've been given" */
		LocalDate candidate = date.with(FINANCIAL_START);
		/**
		 * If we haven't reached that yet, subtract a year. Otherwise, use it.
		 */
		return candidate.isAfter(date) ? candidate.minusYears(1) : candidate;
	}

	public static LocalDate getEndOfFinancialYear(LocalDate date) {
		/** Try "the same year as the date we've been given" */
		LocalDate candidate = date.with(FINANCIAL_END);
		/**
		 * If we haven't reached that yet, subtract a year. Otherwise, use it.
		 */
		return candidate.isBefore(date) ? candidate.plusYears(1) : candidate;
	}

	public static LocalDate dateToLocalDate(Date date) {
		return date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
	}

	public static String getFinancialYrByDt(Date date) {
		LocalDate txnDate = dateToLocalDate(date);
		if (txnDate != null) {
			int startYear = getStartOfFinancialYear(txnDate).getYear();
			int endYear = getEndOfFinancialYear(txnDate).getYear();
			return startYear + "-" + endYear;
		}
		return null;
	}

	public static Integer getQuarterByStrDate(String strDate) {

		LocalDate date = dateToLocalDate(DateUtils.convertStringToDate(strDate));
		if (date != null) {
			LocalDate syntheticQuarterDate = date.minus(1, IsoFields.QUARTER_YEARS);

			/** Get quarter number and year as int */
			return syntheticQuarterDate
					.get(IsoFields.QUARTER_OF_YEAR);/**
													 * int year = syntheticQuarterDate.getYear();
													 * 
													 * long delta = IsoFields.QUARTER_YEARS.between(LocalDate.of(2015,
													 * 1, 1), LocalDate.of(2015, 4, 1));
													 */
		}
		return 0;

	}

	public static String dateToStringFormatYyyyMmDd(Date value) {
		String val = "";
		if (value != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			val = sdf.format(value);
		}
		return val;
	}

	public static Integer getQuarterByDate(Date dateVal) {

		LocalDate date = dateToLocalDate(dateVal);
		if (date != null) {
			LocalDate syntheticQuarterDate = date.minus(1, IsoFields.QUARTER_YEARS);

			/** Get quarter number and year as int */
			return syntheticQuarterDate
					.get(IsoFields.QUARTER_OF_YEAR);/**
													 * int year = syntheticQuarterDate.getYear(); long delta =
													 * IsoFields.QUARTER_YEARS.between(LocalDate.of(2015, 1, 1),
													 * LocalDate.of(2015, 4, 1));
													 */
		}
		return 0;
	}

	public static void main(String[] args) {
		System.out.println(getFinancialYrByDt(DateUtils.sysDate()));
	}

	public static List<String> datemonthDifference(int month, Date date2, Date endDate) throws java.lang.Exception {

		List<String> dates = new ArrayList<>();

		String DATE_FORMAT = "dd/MM/yyyy";
		String date_string = dateToStringFormatYyyyMmDd(endDate);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
		Date date = (Date) sdf.parse(date_string);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.MONTH, 1);

		for (int i = 0; i <= month - 1; i++) {
			c1.add(Calendar.MONTH, -1);
			dates.add(sdf.format(c1.getTime()));

		}
		return dates;
	}

	public static List<String> dateQuarterDifference(int month, Date policyCommencementDate, Date endDate)
			throws ParseException {
		List<String> dates = new ArrayList<>();

		String DATE_FORMAT = "dd/MM/yyyy";
		String date_string = dateToStringFormatYyyyMmDd(endDate);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
		Date date = (Date) sdf.parse(date_string);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.MONTH, 3);
		for (int i = 0; i < month / 3; i++) {
			c1.add(Calendar.MONTH, -3);
			dates.add(sdf.format(c1.getTime()));

		}

		return dates;
	}

	public static List<String> datehalfDifference(int month, Date policyCommencementDate, Date endDate)
			throws ParseException {

		List<String> dates = new ArrayList<>();
		String DATE_FORMAT = "dd/MM/yyyy";
		String date_string = dateToStringFormatYyyyMmDd(endDate);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
		Date date = (Date) sdf.parse(date_string);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.MONTH, 6);

		for (int i = 0; i < month / 6; i++) {
			c1.add(Calendar.MONTH, -6);
			dates.add(sdf.format(c1.getTime()));

		}

		return dates;
	}

	public static List<String> convertDateTimeToStringWithPeriod(int month, Date policyCommencementDate, Date endDate)
			throws ParseException {
		List<String> dates = new ArrayList<>();
		String DATE_FORMAT = "dd/MM/yyyy";
		String date_string = dateToStringFormatYyyyMmDd(endDate);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
		Date date = (Date) sdf.parse(date_string);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.MONTH, 12);

		for (int i = 0; i < month / 12; i++) {

			c1.add(Calendar.MONTH, -12);
			dates.add(sdf.format(c1.getTime()));

		}

		return dates;
	}

	public static int getTotalInBetweenMonths(Date policyLastcontribution, Date ard) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(policyLastcontribution);

		Calendar last = Calendar.getInstance();
		last.setTime(ard);
		int yearsInBetween = last.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
		int monthsDiff = last.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
		long ageInMonths = yearsInBetween * 12 + monthsDiff;
//	        long age = yearsInBetween;

		return NumericUtils.convertLongToInteger(ageInMonths);
	}

	public static Date stringToDateYYYYMMDDHHMMSSHyphen(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat(YYYY_MM_DD_HHMMSS_HYPHEN);
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				logger.error("Parse Exception::", e);
			}
		}
		return null;
	}

	public static Date stringToDateYYYYMMDDHyphen(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat(YYYY_MM_DD_HYPHEN);
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				logger.error("Parse Exception::", e);
			}
		}
		return null;
	}

	public static String DateTostringYYYYMMDDHyphen(Date value) {
		String val = "";
		if (value != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HYPHEN);
			val = sdf.format(value);
		}
		return val;
	}

	public static String sysDateStringSlash() {
		DateFormat sdf = new SimpleDateFormat(DDMMYYYY);
		return sdf.format(sysDate());
	}

	public static List<String> datemonthDifferenceForNB(int totalmonths, Date policyCommencementDate,
			Date policyEndDate) throws ParseException {
		List<String> dates = new ArrayList<>();

		String DATE_FORMAT = "dd/MM/yyyy";
		String date_string = dateToStringFormatYyyyMmDd(policyEndDate);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
		Date date = (Date) sdf.parse(date_string);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.MONTH, 1);

		for (int i = 0; i <= totalmonths; i++) {
			c1.add(Calendar.MONTH, -1);
			dates.add(sdf.format(c1.getTime()));

		}
		return dates;
	}

	public static List<String> dateQuarterDifferenceForNB(int totalmonths, Date policyCommencementDate,
			Date policyEndDate) throws ParseException {
		List<String> dates = new ArrayList<>();

		String DATE_FORMAT = "dd/MM/yyyy";
		String date_string = dateToStringFormatYyyyMmDd(policyEndDate);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
		Date date = (Date) sdf.parse(date_string);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.MONTH, 3);
		for (int i = 0; i <= totalmonths / 3; i++) {
			c1.add(Calendar.MONTH, -3);
			dates.add(sdf.format(c1.getTime()));

		}

		return dates;
	}

	public static List<String> convertDateTimeToStringWithPeriodForNB(int totalmonths, Date policyCommencementDate,
			Date policyEndDate) throws ParseException {
		List<String> dates = new ArrayList<>();
		String DATE_FORMAT = "dd/MM/yyyy";
		String date_string = dateToStringFormatYyyyMmDd(policyEndDate);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
		Date date = (Date) sdf.parse(date_string);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.MONTH, 12);
		for (int i = 0; i <= totalmonths / 12; i++) {
			c1.add(Calendar.MONTH, -12);
			dates.add(sdf.format(c1.getTime()));

		}

		return dates;
	}

	public static List<String> datehalfDifferenceForNB(int totalmonths, Date policyCommencementDate, Date policyEndDate)
			throws ParseException {

		List<String> dates = new ArrayList<>();
		String DATE_FORMAT = "dd/MM/yyyy";
		String date_string = dateToStringFormatYyyyMmDd(policyEndDate);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
		Date date = (Date) sdf.parse(date_string);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.MONTH, 6);

		for (int i = 0; i <= totalmonths / 6; i++) {
			c1.add(Calendar.MONTH, -6);
			dates.add(sdf.format(c1.getTime()));

		}

		return dates;
	}

	private static final String DDMMYYYY_SLASH = "dd/MM/yyyy";

	public static Date convertStringToDateDDMMYYYYSlash(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat(DDMMYYYY_SLASH);
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				/***  */
			}
		}
		return null;
	}

	public static String dateToStringFormatDDMMYYYYSlash(Date value) {
		String val = "";
		if (value != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(DDMMYYYY_SLASH);
			val = sdf.format(value);
		}
		return val;
	}
	
	public static String stringToStringDatewithOutTime(String value) {
		if (StringUtils.isNotEmpty(value)) {
			try {
				Date date=stringToDateYYYYMMDDHHMMSSHyphen(value);
				String s=dateToStringDDMMYYYY(date);
				return s;
			} catch (Exception e) {
				/***  */
				logger.error("stringToStringDatewithOutTime" + e.getMessage());
			}
		}
		return null;
	}
}
