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
//import org.springframework.util.CollectionUtils;
//
//import com.lic.epgs.quotation.dto.QuotationMemberNomineeApiResponseDto;
//import com.lic.epgs.quotation.dto.QuotationMemberNomineeDto;
//import com.lic.epgs.quotation.entity.QuotationMemberNomineeTempEntity;
//import com.lic.epgs.quotation.repository.QuotationMemberNomineeTempRepo;
//import com.lic.epgs.quotation.service.QuotationMemberNomineeService;
//import com.lic.epgs.utils.CommonConstants;
//
//@Service
//public class QuotationMemberNomineeServiceImpl implements QuotationMemberNomineeService {
//
//	@Autowired
//	private QuotationMemberNomineeTempRepo quotationMemberNomineeTempRepo;
//
//	@Autowired
//	private ModelMapper modelMapper;
//
//	protected final Logger logger = LogManager.getLogger(getClass());
//
//	@Override
//	public QuotationMemberNomineeApiResponseDto getQuotationMemberNominee(Long memberId, Long nomineeId) {
//		QuotationMemberNomineeApiResponseDto commonDto = new QuotationMemberNomineeApiResponseDto();
//		logger.info("memberId::{}nomineeId::{}::{}", memberId, nomineeId, LOGSTART);
//		try {
//			final Optional<QuotationMemberNomineeTempEntity> result = quotationMemberNomineeTempRepo
//					.findByMemberIdAndNomineeId(memberId, nomineeId);
//			if (result.isPresent()) {
//				QuotationMemberNomineeTempEntity memberNomineeTempEntity = result.get();
//				commonDto.setMemberId(memberNomineeTempEntity.getMemberId());
//				commonDto.setQuotationMemberNomineeDto(
//						(modelMapper.map(memberNomineeTempEntity, QuotationMemberNomineeDto.class)));
//				commonDto.setTransactionStatus(CommonConstants.STATUS);
//				commonDto.setTransactionMessage(CommonConstants.FETCH);
//			} else {
//				logger.info("Quotation member nominee not found for the given QuotationMemberId: {} and NomineeId: {}",
//						memberId, nomineeId);
//				commonDto.setTransactionMessage(CommonConstants.FAIL);
//				commonDto.setTransactionStatus("Quotation member nominee not found for the given QuotationMemberId: "
//						+ memberId + " and NomineeId: " + nomineeId);
//			}
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- QuotationMemberNomineeServiceImpl-- getQuotationMemberNominee--", e);
//			commonDto.setTransactionMessage(CommonConstants.FAIL);
//			commonDto.setTransactionStatus(CommonConstants.ERROR);
//		}
//		return commonDto;
//	}
//
//	@Override
//	public QuotationMemberNomineeApiResponseDto getQuotationMemberNominees(Long memberId) {
//		logger.info("memberId::{}::{}", memberId, LOGSTART);
//
//		QuotationMemberNomineeApiResponseDto commonDto = new QuotationMemberNomineeApiResponseDto();
//
//		try {
//			final List<QuotationMemberNomineeTempEntity> quotationMemberNomineeTempEntities = quotationMemberNomineeTempRepo
//					.findAllByMemberId(memberId);
//			if (!CollectionUtils.isEmpty(quotationMemberNomineeTempEntities)) {
//
//				List<QuotationMemberNomineeDto> nomineeDtos = mapList(quotationMemberNomineeTempEntities,
//						QuotationMemberNomineeDto.class);
//				commonDto.setQuotationMemberNomineeDtos(nomineeDtos);
//				commonDto.setMemberId(memberId);
//				commonDto.setTransactionStatus(CommonConstants.STATUS);
//				commonDto.setTransactionMessage(CommonConstants.FETCH);
//			} else {
//				logger.info("Quotation member nominees not found for the given MemberId: {}", memberId);
//				commonDto.setTransactionMessage(CommonConstants.FAIL);
//				commonDto.setTransactionStatus(
//						"Quotation member nominees not found for the given MemberId: " + memberId);
//			}
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- QuotationMemberNomineeServiceImpl-- getQuotationMemberNominees--", e);
//			commonDto.setTransactionMessage(CommonConstants.FAIL);
//			commonDto.setTransactionStatus(CommonConstants.ERROR);
//		}
//		logger.info("memberId::{}::{}", memberId, LOGEND);
//		return commonDto;
//
//	}
//
//	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
//		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
//	}
//
//	@Override
//	public QuotationMemberNomineeApiResponseDto deleteQuotationMemberNominee(Long nomineeId) {
//		logger.info("nomineeId::{}::{}", nomineeId, LOGSTART);
//		QuotationMemberNomineeApiResponseDto commonDto = new QuotationMemberNomineeApiResponseDto();
//		try {
//			quotationMemberNomineeTempRepo.deleteById(nomineeId);
//			commonDto.setTransactionStatus(CommonConstants.STATUS);
//			commonDto.setTransactionMessage(CommonConstants.DELETED);
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- QuotationMemberNomineeServiceImpl-- deleteQuotationMemberNominee--", e);
//			commonDto.setTransactionMessage(CommonConstants.FAIL);
//			commonDto.setTransactionStatus(CommonConstants.ERROR);
//		}
//		logger.info("nomineeId::{}::{}", nomineeId, LOGEND);
//		return commonDto;
//	}
//}
