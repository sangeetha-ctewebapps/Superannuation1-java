/**
 * 
 */
package com.lic.epgs.fund.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.common.dto.InterestErrorDetailsDto;
import com.lic.epgs.common.entity.InterestErrorDetailsEntity;
import com.lic.epgs.common.repository.InterestErrorDetailsRepository;
import com.lic.epgs.fund.service.InterestRateLogService;
import com.lic.epgs.integration.dto.InterestRateResponseDto;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.CommonUtils;
import com.lic.epgs.utils.DateUtils;

/**
 * @author Muruganandam
 * @implNote This service is used save the application exception and validation
 *           error
 * @createdDate 19-NOV-2022
 */
@Service
public class InterestRateLogServiceImpl implements InterestRateLogService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private InterestErrorDetailsRepository interestErrorDetailsRepository;

	public List<InterestErrorDetailsDto> errorEntitiesToDtos(List<InterestErrorDetailsEntity> froms) {
		if (CommonUtils.isNonEmptyArray(froms)) {
			List<InterestErrorDetailsDto> tos = new ArrayList<>();
			froms.forEach(from -> {
				InterestErrorDetailsDto to = new InterestErrorDetailsDto();
				BeanUtils.copyProperties(from, to);
				to.setCreatedOn(DateUtils.dateToStringFormatYyyyMmDdHhMmSsSlash(from.getCreatedOn()));
				to.setModifiedOn(DateUtils.dateToStringFormatYyyyMmDdHhMmSsSlash(from.getModifiedOn()));
				tos.add(to);
			});
			return tos;
		}
		return Collections.emptyList();
	}

	public InterestErrorDetailsDto errorEntityToDto(InterestErrorDetailsEntity from) {
		if (from != null) {
			InterestErrorDetailsDto to = new InterestErrorDetailsDto();
			BeanUtils.copyProperties(from, to);
			to.setCreatedOn(DateUtils.dateToStringFormatYyyyMmDdHhMmSsSlash(from.getCreatedOn()));
			to.setModifiedOn(DateUtils.dateToStringFormatYyyyMmDdHhMmSsSlash(from.getModifiedOn()));
			return to;
		}
		return null;
	}

	@Override
	public InterestRateResponseDto viewErrorLog() {
		InterestRateResponseDto responseDto = new InterestRateResponseDto();
		responseDto.setStatus(CommonConstants.ERROR);
		logger.info("viewErrorLog::Start::");
		try {
			List<InterestErrorDetailsEntity> entities = interestErrorDetailsRepository.findAll();
			responseDto.setResponseData(errorEntitiesToDtos(entities));
			responseDto.setStatus(CommonConstants.SUCCESS);
		} catch (IllegalArgumentException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(e.getMessage());
			logger.error("Exception viewErrorLog::End::", e);
		}
		logger.info("viewErrorLog::End::");
		return responseDto;
	}

	@Override
	public InterestRateResponseDto viewErrorLogByPolicy(String policyNo) {
		InterestRateResponseDto responseDto = new InterestRateResponseDto();
		responseDto.setStatus(CommonConstants.ERROR);
		logger.info("viewErrorLogByPolicy::Start::");
		try {
			List<InterestErrorDetailsEntity> entities = interestErrorDetailsRepository
					.findByPolicyNoOrderByIdAsc(policyNo);
			responseDto.setResponseData(errorEntitiesToDtos(entities));
			responseDto.setStatus(CommonConstants.SUCCESS);
		} catch (IllegalArgumentException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(e.getMessage());
			logger.error("Exception viewErrorLogByPolicy::End::", e);
		}
		logger.info("viewErrorLogByPolicy::End::");
		return responseDto;
	}

	@Override
	public InterestRateResponseDto viewErrorLogByMemberId(String memberId) {
		InterestRateResponseDto responseDto = new InterestRateResponseDto();
		responseDto.setStatus(CommonConstants.ERROR);
		logger.info("viewErrorLogByMemberId::Start::");
		try {
			List<InterestErrorDetailsEntity> entities = interestErrorDetailsRepository
					.findByMemberIdOrderByIdAsc(memberId);
			responseDto.setResponseData(errorEntitiesToDtos(entities));
			responseDto.setStatus(CommonConstants.SUCCESS);
		} catch (IllegalArgumentException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(e.getMessage());
			logger.error("Exception viewErrorLogByMemberId::End::", e);
		}
		logger.info("viewErrorLogByMemberId::End::");
		return responseDto;
	}

	@Override
	public InterestRateResponseDto viewErrorLogByrefNo(String refNo) {
		InterestRateResponseDto responseDto = new InterestRateResponseDto();
		responseDto.setStatus(CommonConstants.ERROR);
		logger.info("viewErrorLogByrefNo::Start::");
		try {
			List<InterestErrorDetailsEntity> entities = interestErrorDetailsRepository
					.findByRefNumberOrderByIdAsc(refNo);
			responseDto.setResponseData(errorEntitiesToDtos(entities));
			responseDto.setStatus(CommonConstants.SUCCESS);
		} catch (IllegalArgumentException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(e.getMessage());
			logger.error("Exception viewErrorLogByrefNo::End::", e);
		}
		logger.info("viewErrorLogByrefNo::End::");
		return responseDto;
	}

	@Override
	public InterestRateResponseDto viewFailErrorLog() {
		InterestRateResponseDto responseDto = new InterestRateResponseDto();
		responseDto.setStatus(CommonConstants.ERROR);
		logger.info("viewFailErrorLog::Start::");
		try {
			List<InterestErrorDetailsEntity> entities = interestErrorDetailsRepository.findByIsFailTrueOrderByIdAsc();
			responseDto.setResponseData(errorEntitiesToDtos(entities));
			responseDto.setStatus(CommonConstants.SUCCESS);
		} catch (IllegalArgumentException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(e.getMessage());
			logger.error("Exception viewFailErrorLog::End::", e);
		}
		logger.info("viewFailErrorLog::End::");
		return responseDto;
	}
}
