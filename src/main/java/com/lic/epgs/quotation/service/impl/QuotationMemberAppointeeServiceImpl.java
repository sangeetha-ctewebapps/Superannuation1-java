//package com.lic.epgs.quotation.service.impl;
//
//import static com.lic.epgs.utils.CommonConstants.LOGEND;
//import static com.lic.epgs.utils.CommonConstants.LOGSTART;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.lic.epgs.quotation.dto.QuotationMemberAppointeeApiResponseDto;
//import com.lic.epgs.quotation.dto.QuotationMemberAppointeeDto;
//import com.lic.epgs.quotation.entity.QuotationMemberAppointeeTempEntity;
//import com.lic.epgs.quotation.repository.QuotationMemberAppointeeTempRepository;
//import com.lic.epgs.quotation.service.QuotationMemberAppointeeService;
//import com.lic.epgs.utils.CommonConstants;
//
//@Service
//public class QuotationMemberAppointeeServiceImpl implements QuotationMemberAppointeeService {
//
//	protected final Logger logger = LogManager.getLogger(getClass());
//
//	@Autowired
//	private QuotationMemberAppointeeTempRepository quotationMemberAppointeeTempRepository;
//
//	@Autowired
//	private ModelMapper modelMapper;
//
//	@Override
//	public QuotationMemberAppointeeApiResponseDto getQuotationMemberAppointee(Long appointeeId,
//			Long quotationMemberId) {
//		logger.info("appointeeId::{}quotationMemberId::{}::{}", appointeeId, quotationMemberId, LOGSTART);
//		QuotationMemberAppointeeApiResponseDto quotationMemberAppointeeApiResponseDto = new QuotationMemberAppointeeApiResponseDto();
//
//		try {
//			final Optional<QuotationMemberAppointeeTempEntity> result = quotationMemberAppointeeTempRepository
//					.findByAppointeeIdAndMemberId(appointeeId, quotationMemberId);
//			if (result.isPresent()) {
//				QuotationMemberAppointeeTempEntity dbQuotationMemberAppointeeTempEntity = result.get();
//				quotationMemberAppointeeApiResponseDto
//						.setQuotationMemberAppointeeId(dbQuotationMemberAppointeeTempEntity.getAppointeeId());
//				quotationMemberAppointeeApiResponseDto.setQuotationMemberAppointeeDto(
//						modelMapper.map(dbQuotationMemberAppointeeTempEntity, QuotationMemberAppointeeDto.class));
//				quotationMemberAppointeeApiResponseDto.setTransactionStatus(CommonConstants.STATUS);
//				quotationMemberAppointeeApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
//			} else {
//				logger.info(
//						"Quotation member Appointee not found for the given Appointee Id: {} and QuotationMemberID: {}",
//						appointeeId, quotationMemberId);
//				quotationMemberAppointeeApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//				quotationMemberAppointeeApiResponseDto
//						.setTransactionStatus("Quotation member Appointee found for the given Appointee Id:"
//								+ appointeeId + " and QuotationMemberId: " + quotationMemberId);
//			}
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- QuotationMemberAppointeeServiceImpl-- getQuotationMemberAppointee--", e);
//			quotationMemberAppointeeApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//			quotationMemberAppointeeApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
//		}
//		logger.info("appointeeId::{}quotationMemberId::{}::{}", appointeeId, quotationMemberId, LOGEND);
//		return quotationMemberAppointeeApiResponseDto;
//	}
//
//	@Override
//	public QuotationMemberAppointeeApiResponseDto getQuotationMemberAppointees(Long quotationMemberId) {
//		QuotationMemberAppointeeApiResponseDto quotationMemberAppointeeApiResponseDto = new QuotationMemberAppointeeApiResponseDto();
//		logger.info("getQuotationMemberAppointees::quotationMemberId::{}::{}", quotationMemberId, LOGSTART);
//		try {
//			final Optional<List<QuotationMemberAppointeeTempEntity>> result = quotationMemberAppointeeTempRepository
//					.findByMemberId(quotationMemberId);
//			if (result.isPresent()) {
//				List<QuotationMemberAppointeeTempEntity> dbQuotationMemberAppointeeTempEntity = result.get();
//				List<QuotationMemberAppointeeDto> quotationMemberAppointeeDtos = mapList(
//						dbQuotationMemberAppointeeTempEntity, QuotationMemberAppointeeDto.class);
//				quotationMemberAppointeeApiResponseDto.setResponseData(quotationMemberAppointeeDtos);
//				quotationMemberAppointeeApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
//				quotationMemberAppointeeApiResponseDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
//			} else {
//				logger.info("Quotation member Appointees not found for the given QuotationMemberId: {}",
//						quotationMemberId);
//				quotationMemberAppointeeApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//				quotationMemberAppointeeApiResponseDto.setTransactionStatus(
//						"Quotation member Appointees not found for the given QuotationMemberId: " + quotationMemberId);
//			}
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- QuotationMemberAppointeeServiceImpl-- getQuotationMemberAppointees--", e);
//			quotationMemberAppointeeApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//			quotationMemberAppointeeApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
//		}
//		logger.info("getQuotationMemberAppointees::quotationMemberId::{}::{}", quotationMemberId, LOGEND);
//		return quotationMemberAppointeeApiResponseDto;
//	}
//
//	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
//		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
//	}
//
//	@Override
//	public QuotationMemberAppointeeApiResponseDto deleteQuotationMemberAppointee(Long appointeeId) {
//		QuotationMemberAppointeeApiResponseDto quotationMemberAppointeeApiResponseDto = new QuotationMemberAppointeeApiResponseDto();
//		logger.info("quotationMemberId::{}::{}", appointeeId, LOGSTART);
//		try {
//			quotationMemberAppointeeTempRepository.deleteById(appointeeId);
//			quotationMemberAppointeeApiResponseDto.setTransactionStatus(CommonConstants.STATUS);
//			quotationMemberAppointeeApiResponseDto.setTransactionMessage(CommonConstants.DELETED);
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- QuotationMemberAppointeeServiceImpl-- deleteQuotationMemberAppointee--", e);
//			quotationMemberAppointeeApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//			quotationMemberAppointeeApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
//		}
//
//		logger.info("quotationMemberId::{}::{}", appointeeId, LOGEND);
//		return quotationMemberAppointeeApiResponseDto;
//	}
//
//}
