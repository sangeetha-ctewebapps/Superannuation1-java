package com.lic.epgs.common.gi.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.common.gi.contants.DepositTransfercontants;
import com.lic.epgs.common.gi.dto.DepositTransferDto;
import com.lic.epgs.common.gi.dto.GiRequestDto;
import com.lic.epgs.common.gi.dto.GiResponseDto;
import com.lic.epgs.common.gi.dto.GiServiceRequestDto;
import com.lic.epgs.common.gi.service.DepositTransferService;
import com.lic.epgs.utils.CommonConstants;


/**
 * @author Ramprasad
 *
 */
@Service
public class DepositTransferServiceImpl implements DepositTransferService, DepositTransfercontants {

	@Autowired
	private Environment environment;
	@Autowired
	@Qualifier("restTemplateService")
	private RestTemplate restTemplateService;

	protected final Logger logger = LogManager.getLogger(getClass());
	
	public HttpHeaders getAuthHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(CommonConstants.AUTHORIZATION, "Bearer ");
		headers.add(CommonConstants.CORELATIONID, UUID.randomUUID().toString());
		headers.add(CommonConstants.BUSINESSCORELATIONID, UUID.randomUUID().toString());
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	private String dateFormate(String date) {

		String fromDate = date;
		String[] olddate = fromDate.split("/");
		String dd = olddate[0];
		String mm = olddate[1];
		String year = olddate[2];

		String dateFormate = year + "-" + mm + "-" + dd;

		return dateFormate;
	}

	private GiRequestDto policySearch(GiServiceRequestDto dto) {
		GiRequestDto responseDto = new GiRequestDto();
		try {
			String fromdate = null;
			String toDate = null;
			if (StringUtils.isNoneBlank(dto.getPolicyCreationFromDate())
					&& StringUtils.isNoneBlank(dto.getPolicyCreationToDate())) {
				fromdate = dateFormate(dto.getPolicyCreationFromDate());
				toDate = dateFormate(dto.getPolicyCreationToDate());
				responseDto.setPolicyCreationFromDate(LocalDate.parse(fromdate));
				responseDto.setPolicyCreationToDate(LocalDate.parse(toDate));
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");
			responseDto.setCustomerCode(dto.getCustomerCode());
			responseDto.setGstIn(dto.getGstIn());
			responseDto.setMphName(dto.getMphName());
			responseDto.setPanNumber(dto.getPanNumber());
			responseDto.setPolicyNumber(dto.getPolicyNumber());
			responseDto.setProposalNumber(dto.getProposalNumber());
			responseDto.setQuotationNumber(dto.getQuotationNumber());
			responseDto.setRollType(dto.getRollType());
			responseDto.setVanNumber(dto.getVanNumber());
			responseDto.setMphCode(dto.getMphCode());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseDto;
	}

	public static boolean isNonEmptyArray(List<?> list) {
		return list != null && !list.isEmpty();
	}

	@Override
	public DepositTransferDto giPolicySearch(GiServiceRequestDto dto) {
		logger.info("DepositTransferServiceImpl {}::giPolicySearch {}::{} started");
		DepositTransferDto responseDto = new DepositTransferDto();
//		GiResponseDto giresponseDto = null;
		List<GiResponseDto> giresponseDto =null;
		try {
			GiRequestDto dto2 = policySearch(dto);
			String url = "https://d1utvrrpgca01.licindia.com:8443/gicservice/LIC_ePGS/gic/mphDetails/getDepositTransfer";
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				if (StringUtils.isNotBlank(url)) {
					HttpHeaders headerss = getAuthHeaders();
					HttpEntity<GiRequestDto> entity = new HttpEntity<>(dto2, headers);

					giresponseDto = restTemplateService.exchange(url, HttpMethod.POST, entity,
							new ParameterizedTypeReference<List<GiResponseDto>>() {
							}).getBody();
					if (isNonEmptyArray(giresponseDto)) {
						responseDto.setResponseObj(giresponseDto);
						responseDto.setTransactionMessage(RETRIVE);
						responseDto.setTransactionStatus(SUCCESS);
					}else {
						responseDto.setTransactionMessage(FAIL);
						responseDto.setTransactionStatus(NO_RECORD_FOUND);
					}
				} else {
					throw new ApplicationException(GI_SERVICE_URL);
				}
			}

		} catch (Exception e) {
			logger.error("DepositTransferServiceImpl {}::giPolicySearch {}::{} error is " + e);
			responseDto.setTransactionMessage("Exception IS :- " + e);
			responseDto.setTransactionStatus(ERROR);

		}
		logger.info("DepositTransferServiceImpl {}::giPolicySearch {}::{} ended");
		return responseDto;
	}

	


}
