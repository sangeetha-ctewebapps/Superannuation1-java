/**
 * 
 */
package com.lic.epgs.fund.service;

import com.lic.epgs.integration.dto.InterestRateResponseDto;

/**
 * @author Muruganandam
 *
 */
public interface InterestRateLogService {

	InterestRateResponseDto viewErrorLog();

	InterestRateResponseDto viewFailErrorLog();

	InterestRateResponseDto viewErrorLogByPolicy(String policyNo);

	InterestRateResponseDto viewErrorLogByMemberId(String memberId);

	InterestRateResponseDto viewErrorLogByrefNo(String refNo);

}
