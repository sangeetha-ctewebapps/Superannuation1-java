/**
 * 
 */
package com.lic.epgs.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lic.epgs.common.utils.JsonRequest;
import com.lic.epgs.integration.dto.InterestRateResponseDto;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.entity.PolicyMasterTempEntity;
import com.lic.epgs.quotation.dto.QuotationSearchDto;


/**
 * @author Muruganandam
 *
 */
public class CommonUtils {

	private CommonUtils() {
		/**
		 * private constructor
		 */
	}

	
	public static boolean isNonEmptyArray(Collection<?> list) {
		return list != null && !list.isEmpty();
	}

	public static boolean isNonEmptyArray(List<?> list) {
		return list != null && !list.isEmpty();
	}

	public static PolicyMasterEntity setToPolicy(Set<PolicyMasterEntity> policySet) {
		if (isNonEmptyArray(policySet)) {
			return policySet.stream().findAny().orElse(null);
		}
		return null;
	}

	public static PolicyMasterTempEntity setToPolicyTemp(Set<PolicyMasterTempEntity> policySet) {
		if (isNonEmptyArray(policySet)) {
			return policySet.stream().findAny().orElse(null);
		}
		return null;
	}

	public static QuotationSearchDto jsonToSearchLeadDto(String payload) throws IOException {
		JsonRequest<QuotationSearchDto> jsonRequest = new JsonRequest<>();
		try {
			return StringUtils.isNotBlank(payload) ? jsonRequest.readEncoded(payload.trim(), QuotationSearchDto.class)
					: new QuotationSearchDto();
		} catch (JsonProcessingException e) {
			return new QuotationSearchDto();
		}
	}

	public static PolicySearchDto jsonToSearchPolicySearchDto(String payload) throws IOException {
		JsonRequest<PolicySearchDto> jsonRequest = new JsonRequest<>();
		try {
			return StringUtils.isNotBlank(payload) ? jsonRequest.readEncoded(payload.trim(), PolicySearchDto.class)
					: new PolicySearchDto();
		} catch (JsonProcessingException e) {
			return new PolicySearchDto();
		}
	}

	public static boolean isBoolTrue(Boolean val) {
		return val != null && val;
	}

	public static String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}

	public static String getRootCauseMessage(final Throwable throwable) {
		return ExceptionUtils.getStackTrace(throwable);
	}

	public static String getRootCauseStackTrace(final Throwable throwable) {

		List<String> list = Arrays.asList(ExceptionUtils.getRootCauseStackTrace(throwable));
		if (CommonUtils.isNonEmptyArray(list)) {
			if (list.size() > 2) {
				return list.subList(0, 2).toString();
			} else if (list.size() <= 2) {
				return list.subList(0, 1).toString();
			} else {
				return list.toString();
			}
		}
		return "Internal Server Error";

	}

	public static String exceptionName(String exceptionMessage) {
		if (StringUtils.isNotBlank(exceptionMessage)) {
			String delimiter = "Exception:";
			String[] str = exceptionMessage.split(delimiter, 0);
			if (str != null) {
				return (str.length > 1 ? StringUtils.trim(str[1]) : CommonConstants.INTERNAL_APP_ERROR);
			}
		} else {
			return CommonConstants.INTERNAL_APP_ERROR;
		}
		return CommonConstants.INTERNAL_APP_ERROR;
	}

	public static boolean isSuccessResponse(InterestRateResponseDto response) {
		return response != null && StringUtils.isNotBlank(response.getStatus())
				&& response.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS);

	}

	public static String quraterVal(Integer quarter) {
		return quarter != null && quarter > 0 && quarter < 5 ? String.valueOf("Q" + quarter) : "YR";
	}

	public static String txnRefNo(String prefix, Long refId, Long txnId) {
		if (refId == null) {
			refId = 0l;
		}
		if (txnId == null) {
			txnId = 0l;
		}
		return String.valueOf(prefix + "" + refId + "" + txnId);
	}

	public static List<Object> objectArrayToList(Object obs) {
		if (obs != null) {
			Object[] ob = (Object[]) obs;
			return (Arrays.asList(ob));
		}
		return Collections.emptyList();
	}
	
	public static boolean anyMatch(String type, String... list) {
		if (list != null && list.length > 0 && StringUtils.isNotBlank(type)) {
			List<String> asList = (Arrays.asList(list));
			return asList.stream().filter(type::equalsIgnoreCase).findAny().orElse(null) != null;

		}
		return false;
	}


	public static String objectValue(Object[] ob, int index) {
		try {
			return NumericUtils.getNotNullStringVal(String.valueOf(ob[index]));
		} catch (Exception e) {
			/****
			 * 
			 */
		}
		return null;
	}
	
	public static String stringMax(String value) {
		if (StringUtils.isNotBlank(value)) {
			if (value.length() > 4000) {
				return value.substring(0, 3999);
			} else {
				return value;
			}
		}
		return value;
	}

}
