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
//import com.lic.epgs.quotation.dto.QuotationMemberBankDetailApiResponseDto;
//import com.lic.epgs.quotation.dto.QuotationMemberBankDetailDto;
//import com.lic.epgs.quotation.entity.QuotationMemberBankDetailTempEntity;
//import com.lic.epgs.quotation.repository.QuotationMemberBankDetailTempRepository;
//import com.lic.epgs.quotation.service.QuotationMemberBankDetailService;
//import com.lic.epgs.utils.CommonConstants;
//
//@Service
//public class QuotationMemberBankDetailServiceImpl implements QuotationMemberBankDetailService {
//
//	protected final Logger logger = LogManager.getLogger(getClass());
//
//	@Autowired
//	private QuotationMemberBankDetailTempRepository quotationMemberBankDetailTempRepository;
//
//	@Autowired
//	private ModelMapper modelMapper;
//
//	@Override
//	public QuotationMemberBankDetailApiResponseDto getQuotationMemberBankDetail(Long bankid, Long quotationMemberId) {
//		QuotationMemberBankDetailApiResponseDto quotationMemberBankDetailApiResponseDto = new QuotationMemberBankDetailApiResponseDto();
//		logger.info("bankid::{}quotationMemberId::{}::{}", bankid, quotationMemberId, LOGSTART);
//		try {
//			final Optional<QuotationMemberBankDetailTempEntity> result = quotationMemberBankDetailTempRepository
//					.findByBankIdAndMemberId(bankid, quotationMemberId);
//			if (result.isPresent()) {
//				QuotationMemberBankDetailTempEntity dbQuotationMemberBankDetailTempEntity = result.get();
//				quotationMemberBankDetailApiResponseDto
//						.setQuotationMemberBankId(dbQuotationMemberBankDetailTempEntity.getBankId());
//				quotationMemberBankDetailApiResponseDto.setQuotationMemberBankDetailDto(
//						modelMapper.map(dbQuotationMemberBankDetailTempEntity, QuotationMemberBankDetailDto.class));
//				quotationMemberBankDetailApiResponseDto.setTransactionStatus(CommonConstants.STATUS);
//				quotationMemberBankDetailApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
//			} else {
//				logger.info(
//						"Quotation member Bank detail not found for the given Bank Id: {} and QuotationMemberID: {}",
//						bankid, quotationMemberId);
//				quotationMemberBankDetailApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//				quotationMemberBankDetailApiResponseDto
//						.setTransactionStatus("Quotation member address not found for the given Bank Id:" + bankid
//								+ " and QuotationMemberId: " + quotationMemberId);
//			}
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- QuotationMemberBankDetailServiceImpl-- getQuotationMemberBankDetail--", e);
//			quotationMemberBankDetailApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//			quotationMemberBankDetailApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
//		}
//		logger.info("bankid::{}quotationMemberId::{}::{}", bankid, quotationMemberId, LOGEND);
//		return quotationMemberBankDetailApiResponseDto;
//	}
//
//	@Override
//	public QuotationMemberBankDetailApiResponseDto getQuotationMemberBankDetails(Long quotationMemberId) {
//		QuotationMemberBankDetailApiResponseDto quotationMemberBankDetailApiResponseDto = new QuotationMemberBankDetailApiResponseDto();
//		logger.info("quotationMemberId::{}::{}", quotationMemberId, LOGSTART);
//
//		try {
//			final Optional<List<QuotationMemberBankDetailTempEntity>> result = quotationMemberBankDetailTempRepository
//					.findByMemberId(quotationMemberId);
//			if (result.isPresent()) {
//				List<QuotationMemberBankDetailTempEntity> dbQuotationMemberBankDetailTempEntity = result.get();
//				List<QuotationMemberBankDetailDto> quotationMemberBankDetailDtos = mapList(
//						dbQuotationMemberBankDetailTempEntity, QuotationMemberBankDetailDto.class);
//				quotationMemberBankDetailApiResponseDto.setResponseData(quotationMemberBankDetailDtos);
//				quotationMemberBankDetailApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
//				quotationMemberBankDetailApiResponseDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
//			} else {
//				logger.info("Quotation member Bank details not found for the given QuotationMemberId: {}",
//						quotationMemberId);
//				quotationMemberBankDetailApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//				quotationMemberBankDetailApiResponseDto.setTransactionStatus(
//						"Quotation member Bank details not found for the given QuotationMemberId: "
//								+ quotationMemberId);
//			}
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- QuotationMemberBankDetailServiceImpl-- getQuotationMemberBankDetails--", e);
//			quotationMemberBankDetailApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//			quotationMemberBankDetailApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
//		}
//		logger.info("quotationMemberId::{}::{}", quotationMemberId, LOGEND);
//		return quotationMemberBankDetailApiResponseDto;
//	}
//
//	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
//		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
//	}
//
//	@Override
//	public QuotationMemberBankDetailApiResponseDto deleteQuotationMemberBankDetail(Long bankId) {
//		QuotationMemberBankDetailApiResponseDto quotationMemberBankDetailApiResponseDto = new QuotationMemberBankDetailApiResponseDto();
//		logger.info("bankId::{}::{}", bankId, LOGSTART);
//		try {
//			quotationMemberBankDetailTempRepository.deleteById(bankId);
//			quotationMemberBankDetailApiResponseDto.setTransactionStatus(CommonConstants.STATUS);
//			quotationMemberBankDetailApiResponseDto.setTransactionMessage(CommonConstants.DELETED);
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- QuotationMemberBankDetailServiceImpl-- deleteQuotationMemberAppointee--", e);
//			quotationMemberBankDetailApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//			quotationMemberBankDetailApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
//		}
//		logger.info("bankId::{}::{}", bankId, LOGEND);
//		return quotationMemberBankDetailApiResponseDto;
//	}
//
//}
