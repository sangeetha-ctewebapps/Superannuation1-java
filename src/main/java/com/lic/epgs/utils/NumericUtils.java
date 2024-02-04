/**
 * 
 */
package com.lic.epgs.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContextException;

import com.lic.epgs.common.exception.ApplicationException;

/**
 * @author Kathiravan
 * @Created Date : Wed Sep 23 19:33:18 IST 2020
 */
public class NumericUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Integer convertStringToInteger(String value) {
		try {
			return StringUtils.isNotBlank(value) ? Integer.valueOf(value) : 0;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Integer stringToInteger(String value) {
		try {
			return StringUtils.isBlank(value) ? null : Integer.valueOf(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Long convertStringToLong(String value) {
		try {
			return StringUtils.isBlank(value) ? null : Long.valueOf(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Long stringToLong(String value) {
		try {
			return StringUtils.isBlank(value) ? null : Long.valueOf(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static String convertLongToString(Long value) {
		try {
			return value == null ? "" : String.valueOf(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Double stringToDouble(String value) {
		try {
			if (StringUtils.isNotBlank(value) && !value.equalsIgnoreCase("null")) {
				return Double.valueOf(value);
			}
		} catch (NumberFormatException e) {
			/****
			 * NumberFormatException
			 */
		}
		return null;
	}

	public static Double convertStringToDouble(String value) {
		try {
			if (StringUtils.isNotBlank(value) && !value.equalsIgnoreCase("null")) {
				return Double.valueOf(value);
			}
		} catch (NumberFormatException e) {
			/***
			 * NumberFormatException
			 */
		}
		return 0.0;
	}

	public static String convertIntegerToString(Integer value) {
		String val = null;
		if (value != null) {
			val = String.valueOf(value);
		} else {
			val = "";
		}

		return val;
	}

	public static String convertDoubleToString(Double value) {
		String val = null;
		if (value != null) {
			val = String.valueOf(value);
		} else {
			val = "";
		}

		return val;
	}

	public static String convertBooleanToString(Boolean value) {
		String val = null;
		if (value != null) {
			val = String.valueOf(value);
		} else {
			val = "";
		}

		return val;
	}

	public static Boolean convertStringToBoolean(String value) {
		Boolean val = null;
		if (StringUtils.isNotBlank(value)) {
			val = Boolean.valueOf(value);
		} else {
			val = true;
		}
		return val;
	}

	public static Long sumOfExtingAndCurrentAmount(Long newCount, Long exstingCount) {

		if (exstingCount != null) {
			return exstingCount + newCount;
		}

		return newCount;
	}

	public static List<Long> convertStringToListOfLong(String value) {
		try {
			return Arrays.asList(value.split(",")).stream().map(obj -> Long.parseLong(obj.trim()))
					.collect(Collectors.toList());
		} catch (NumberFormatException e) {
			return Collections.emptyList();
		}
	}

	public static Long convertIntegerToLong(Integer value) {
		Long val = null;
		if (value != null) {
			val = Long.valueOf(value);
		} else {
			val = null;
		}

		return val;
	}

	public static boolean isBoolTrue(Boolean bool) {
		return bool != null && bool;
	}

	public static Integer convertLongToInteger(Long value) {
		Integer val = null;
		if (value != null) {
			val = value.intValue();
		} else {
			val = null;
		}

		return val;
	}

	public static BigDecimal bigDecimalValid(BigDecimal value) {
		return value != null ? value : BigDecimal.ZERO;
	}

	public static BigDecimal doubleToBigDecimal(Double value) {
		return value != null && !value.isNaN() ? BigDecimal.valueOf(value) : BigDecimal.ZERO;
	}

	public static BigDecimal add(BigDecimal... values) {
		List<BigDecimal> list = new ArrayList<>();
		if (values != null && values.length > 0)
			for (BigDecimal value : values) {
				list.add(bigDecimalValid(value));
			}
		return bigDecimalValid(list.stream().reduce(BigDecimal.ZERO, BigDecimal::add));

	}

	public static BigDecimal bigDecimalNegative(BigDecimal value) {
		return (value != null && value.compareTo(BigDecimal.ZERO) > 0) ? BigDecimal.valueOf(-1).multiply(value) : value;
	}

	public static BigDecimal stringToBigDecimal(String value) {
		if (StringUtils.isNotBlank(value)) {
			Double doubleVal = stringToDouble(value);
			if (doubleVal != null && !doubleVal.isNaN()) {
				return BigDecimal.valueOf(stringToDouble(value));
			}
		}
		return BigDecimal.ZERO;
	}

	public static String getNotNullStringVal(String value) {
		try {
			if (value != null && StringUtils.isNotBlank(String.valueOf(value))
					&& !String.valueOf(value).equalsIgnoreCase("null")) {
				return String.valueOf(value);

			}
		} catch (NumberFormatException e) {
			/**
			* */
		}
		return null;
	}

	public static Double doubleRoundResponse(Double value) {
		if (value != null) {
			DecimalFormat decfor = new DecimalFormat("0.00");
			return NumericUtils.convertStringToDouble(decfor.format(value));
		} else {
			return value;
		}

	}

	public static double doubleRoundInMath(double value, int places) {
		double scale = Math.pow(10, places);
		return Math.round(value * scale) / scale;
	}

	public static double doubleRoundUpInMath(double value) {
		return Math.ceil(value);
	}

	public static String convertBigDecimalToString(BigDecimal value) {
		String val = null;
		if (value != null) {
			val = value.toString();
		} else {
			val = null;
		}
		return val;

	}

	public static Double convertBigDecimalToDouble(BigDecimal value) {
		Double val = null;
		if (value != null) {
			val = value.doubleValue();
		} else {
			val = null;
		}
		return val;
	}

	/**
	 * Note:-New NumberToWord Convert 04-04-2023
	 **/

	private static final String EMPTY = "";

	private static final String[] X = { EMPTY, "One ", "Two ", "Three ", "Four ", "Five ", "Six ", "Seven ", "Eight ",
			"Nine ", "Ten ", "Eleven ", "Twelve ", "Thirteen ", "Fourteen ", "Fifteen ", "Sixteen ", "Seventeen ",
			"Eighteen ", "Nineteen " };

	private static final String[] Y = { EMPTY, EMPTY, "Twenty ", "Thirty ", "Forty ", "Fifty ", "Sixty ", "Seventy ",
			"Eighty ", "Ninety " };

	// Function to convert a single-digit or two-digit number Longo words
	private static String convertToDigit(long n, String suffix) {
		// if `n` is zero
		if (n == 0) {
			return EMPTY;
		}

		// split `n` if it is more than 19
		if (n > 19) {
			return Y[(int) (n / 10)] + X[(int) (n % 10)] + suffix;
		} else {
			return X[(int) n] + suffix;
		}
	}

	// Function to convert a given number (max 9-digits) Longo words
	public static String convertAmountToWord(long n) {
		// for storing the word representation of the given number
		StringBuilder res = new StringBuilder();

		// add digits at ten million and hundred million place
		res.append(convertToDigit((n / 1000000000) % 100, "Billion  "));

		// add digits at ten million and hundred million place
		res.append(convertToDigit((n / 10000000) % 100, "Crore  "));

		// add digits at hundred thousand and one million place
		res.append(convertToDigit(((n / 100000) % 100), "Lakh  "));

		// add digits at thousand and tens thousand place
		res.append(convertToDigit(((n / 1000) % 100), "Thousand  "));

		// add digit at hundred place
		res.append(convertToDigit(((n / 100) % 10), "Hundred "));

		if ((n > 100) && (n % 100 != 0)) {
			res.append("and ");
		}

		// add digits at ones and tens place
		res.append(convertToDigit((n % 100), ""));

		return res.toString().trim().replace(", and", " and").replaceAll("^(.*),$", "$1"); // remove trailing comma
	}

	public static boolean isBooleanNotNull(Boolean bool) {
		return bool != null && bool;
	}


	public static Integer convertDoubleToInteger(Double value) {
		try {
			return value != null ? value.intValue() : 0;
		} catch (NumberFormatException e) {
			/***  */
			return null;
		}
	}

	public static Boolean convertStringNumberToBoolean(String value) {
		if (StringUtils.isNotBlank(value)) {
			switch (value) {
			case "1":
				return true;
			case "0":
				return false;
			default:
				break;
			}
		}
		return false;
	}

	public static String charToString(Character val) {
		try {
			if (val != null) {
				return String.valueOf(val);
			}
		} catch (IllegalArgumentException e) {
			/**
			 * 
			 */
		}

		return null;
	}

	public static Character stringToChar(String val) {
		try {
			if (StringUtils.isNotBlank(val) && val.length() == 1) {
				return val.charAt(0);
			}
		} catch (IllegalArgumentException e) {
			/**
			 * 
			 */
		}
		return null;
	}

	public static char booleanStringToChar(String trueFalse) {
		try {
			if (StringUtils.isNotBlank(trueFalse) && trueFalse.equalsIgnoreCase("true")) {
				return 'Y';
			}
		} catch (IllegalArgumentException e) {
			/**
			 * 
			 */
		}
		return 'N';
	}

	public static String charToBooleanString(String yN) {
		try {
			if (StringUtils.isNotBlank(yN) && yN.equalsIgnoreCase("Y")) {
				return "true";
			}
		} catch (IllegalArgumentException e) {
			/**
			 * 
			 */
		}
		return "false";
	}

	public static boolean isLongNull(Long number) {
		return number != null && number != 0;
	}

	public static Double doubleValueCheck(Double number) {
		return number != null ? number : 0.0;
	}



	public static String notNullString(String value) {
		try {
			if (value != null && StringUtils.isNotBlank(String.valueOf(value))
					&& !String.valueOf(value).equalsIgnoreCase("null")) {
				return String.valueOf(value);
			}
		} catch (NumberFormatException e) {
			/***
			 * 
			 */
		}
		return "";
	}

	public static BigDecimal setScale(BigDecimal value) {
		return value != null ? value.setScale(2, RoundingMode.HALF_EVEN) : BigDecimal.ZERO;
	}

	public static BigDecimal set2Scale(BigDecimal value) {
		return value != null ? value.setScale(2, RoundingMode.HALF_EVEN) : BigDecimal.ZERO;
	}

	public static BigDecimal set4Scale(BigDecimal value) {
		return value != null ? value.setScale(4, RoundingMode.HALF_EVEN) : BigDecimal.ZERO;
	}

	public static BigDecimal bigDecimalPositiveValue(BigDecimal value) {
		if (value == null) {
			return BigDecimal.ZERO;
		}
		return (value.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.valueOf(-1).multiply(value) : value;
	}

	public static boolean isNotNullZeroNumber(BigDecimal value) {
		value = value != null ? value.setScale(2, RoundingMode.HALF_EVEN) : BigDecimal.ZERO;
		return value.compareTo(BigDecimal.ZERO) > 0;
	}


	public static String quraterVal(Integer quarter) {
		return quarter != null && quarter > 0 && quarter < 5 ? String.valueOf("Q" + quarter) : "YR";
	}

	public static BigDecimal nonNegativeBigDecimal(BigDecimal value) {
		if (value != null && value.compareTo(BigDecimal.ZERO) >= 0) {

			return value;
		} else {
			throw new ApplicationContextException("The given amount is less than ZERO");
		}
	}


	public static boolean validateDebitAmount(BigDecimal value) throws ApplicationException {
		if (value != null && value.compareTo(BigDecimal.ZERO) > 0) {
			return true;
		}
		throw new ApplicationException("Debit Amount is not valid. It is ZERO or null");

	}
	
	public static String validObjectToString(Object object) {
		try {
			if (object != null && StringUtils.isNotBlank(String.valueOf(object))
					&& !String.valueOf(object).equalsIgnoreCase("null")) {
				return String.valueOf(object);
			}
		} catch (NumberFormatException e) {
		}
		return null;
	}

}
