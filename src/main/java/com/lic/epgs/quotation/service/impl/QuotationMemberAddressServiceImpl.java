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
//import com.lic.epgs.quotation.dto.QuotationMemberAddressApiResponseDto;
//import com.lic.epgs.quotation.dto.QuotationMemberAddressDto;
//import com.lic.epgs.quotation.entity.QuotationMemberAddressTempEntity;
//import com.lic.epgs.quotation.repository.QuotationMemberAddressTempRepository;
//import com.lic.epgs.quotation.service.QuotationMemberAddressService;
//import com.lic.epgs.utils.CommonConstants;
//import com.lic.epgs.utils.DateUtils;
//
//@Service
//public class QuotationMemberAddressServiceImpl implements QuotationMemberAddressService {
//
//	protected final Logger logger = LogManager.getLogger(getClass());
//
//	@Autowired
//	private QuotationMemberAddressTempRepository quotationMemberAddressTempRepository;
//
//	@Autowired
//	private ModelMapper modelMapper;
//
//	@Override
//	public QuotationMemberAddressApiResponseDto saveQuotationMemberAddress(
//			QuotationMemberAddressDto quotationMemberAddressDto) {
//		QuotationMemberAddressApiResponseDto quotationMemberAddressApiResponseDto = new QuotationMemberAddressApiResponseDto();
//		logger.info("{}", LOGSTART);
//		try {
//			QuotationMemberAddressTempEntity quotationMemberAddressTempEntity = modelMapper
//					.map(quotationMemberAddressDto, QuotationMemberAddressTempEntity.class);
//			QuotationMemberAddressTempEntity dbQuotationMemberAddressTempEntity = quotationMemberAddressTempRepository
//					.save(quotationMemberAddressTempEntity);
//			quotationMemberAddressApiResponseDto
//					.setQuotationMemberAddressId(dbQuotationMemberAddressTempEntity.getAddressId());
//			quotationMemberAddressApiResponseDto.setQuotationMemberAddressDto(
//					modelMapper.map(dbQuotationMemberAddressTempEntity, QuotationMemberAddressDto.class));
//			quotationMemberAddressApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
//			quotationMemberAddressApiResponseDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- QuotationMemberAddressServiceImpl-- saveQuotationMemberAddress --", e);
//			quotationMemberAddressApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//			quotationMemberAddressApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
//		}
//		logger.info("{}", LOGEND);
//		return quotationMemberAddressApiResponseDto;
//	}
//
//	@Override
//	public QuotationMemberAddressApiResponseDto getQuotationMemberAddress(Long quotationMemberAddressId,
//			Long quotationMemberId) {
//		QuotationMemberAddressApiResponseDto quotationMemberAddressApiResponseDto = new QuotationMemberAddressApiResponseDto();
//		logger.info("quotationMemberAddressId::{}quotationMemberId::{}::{}", quotationMemberAddressId,
//				quotationMemberId, LOGSTART);
//		try {
//			final Optional<QuotationMemberAddressTempEntity> result = quotationMemberAddressTempRepository
//					.findByAddressIdAndMemberId(quotationMemberAddressId, quotationMemberId);
//			if (result.isPresent()) {
//				QuotationMemberAddressTempEntity dbQuotationMemberAddressTempEntity = result.get();
//				quotationMemberAddressApiResponseDto
//						.setQuotationMemberAddressId(dbQuotationMemberAddressTempEntity.getAddressId());
//				quotationMemberAddressApiResponseDto.setQuotationMemberAddressDto(
//						modelMapper.map(dbQuotationMemberAddressTempEntity, QuotationMemberAddressDto.class));
//				quotationMemberAddressApiResponseDto.setTransactionStatus(CommonConstants.STATUS);
//				quotationMemberAddressApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
//			} else {
//				logger.info(
//						"Quotation member Address not found for the given QuotationMemberAddressId: {} and QuotationMemberID: {}",
//						quotationMemberAddressId, quotationMemberId);
//				quotationMemberAddressApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//				quotationMemberAddressApiResponseDto.setTransactionStatus(
//						"Quotation member address not found for the given QuotationMemberAddressId:"
//								+ quotationMemberAddressId + " and QuotationMemberId: " + quotationMemberId);
//			}
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- QuotationMemberAddressServiceImpl-- getQuotationMemberAddress--", e);
//			quotationMemberAddressApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//			quotationMemberAddressApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
//		}
//		logger.info("quotationMemberAddressId::{}quotationMemberId::{}::{}", quotationMemberAddressId,
//				quotationMemberId, LOGEND);
//		return quotationMemberAddressApiResponseDto;
//	}
//
//	@Override
//	public QuotationMemberAddressApiResponseDto getQuotationMemberAddresses(Long quotationMemberId) {
//		QuotationMemberAddressApiResponseDto quotationMemberAddressApiResponseDto = new QuotationMemberAddressApiResponseDto();
//		logger.info("getQuotationMemberAddresses::quotationMemberId::{}::{} ", quotationMemberId, LOGSTART);
//		try {
//			final Optional<List<QuotationMemberAddressTempEntity>> result = quotationMemberAddressTempRepository
//					.findByMemberId(quotationMemberId);
//			if (result.isPresent()) {
//				List<QuotationMemberAddressTempEntity> dbQuotationMemberAddressTempEntity = result.get();
//				List<QuotationMemberAddressDto> quotationMemberAddressDtos = mapList(dbQuotationMemberAddressTempEntity,
//						QuotationMemberAddressDto.class);
//				quotationMemberAddressApiResponseDto.setResponseData(quotationMemberAddressDtos);
//				quotationMemberAddressApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
//				quotationMemberAddressApiResponseDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
//			} else {
//				logger.info("Quotation member addresses not found for the given QuotationMemberId: {}",
//						quotationMemberId);
//				quotationMemberAddressApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//				quotationMemberAddressApiResponseDto.setTransactionStatus(
//						"Quotation member address not found for the given QuotationMemberId: " + quotationMemberId);
//			}
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- QuotationMemberServiceImpl-- getQuotationMemberAddresses--", e);
//			quotationMemberAddressApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//			quotationMemberAddressApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
//		}
//		logger.info("getQuotationMemberAddresses::quotationMemberId::{}::{}", quotationMemberId, LOGEND);
//		return quotationMemberAddressApiResponseDto;
//	}
//
//	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
//		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
//	}
//
//	@Override
//	public QuotationMemberAddressApiResponseDto updateQuotationMemberAddress(Long quotationMemberAddressId,
//			QuotationMemberAddressDto quotationMemberAddressDto) {
//		QuotationMemberAddressApiResponseDto quotationMemberAddressApiResponseDto = new QuotationMemberAddressApiResponseDto();
//		logger.info("quotationMemberId::{}::{}", quotationMemberAddressId, LOGSTART);
//		try {
//			final Optional<QuotationMemberAddressTempEntity> result = quotationMemberAddressTempRepository
//					.findById(quotationMemberAddressId);
//			if (result.isPresent()) {
//				QuotationMemberAddressTempEntity dbQuotationMemberAddressTempEntity = result.get();
//				QuotationMemberAddressTempEntity quotationMemberAddressTempEntity = modelMapper
//						.map(quotationMemberAddressDto, QuotationMemberAddressTempEntity.class);
//				quotationMemberAddressTempEntity.setAddressId(dbQuotationMemberAddressTempEntity.getAddressId());
//				quotationMemberAddressTempEntity.setModifiedOn(DateUtils.sysDate());
//				dbQuotationMemberAddressTempEntity = quotationMemberAddressTempRepository
//						.save(dbQuotationMemberAddressTempEntity);
//				quotationMemberAddressApiResponseDto
//						.setQuotationMemberAddressId(dbQuotationMemberAddressTempEntity.getAddressId());
//				quotationMemberAddressApiResponseDto.setQuotationMemberAddressDto(
//						modelMapper.map(dbQuotationMemberAddressTempEntity, QuotationMemberAddressDto.class));
//				quotationMemberAddressApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
//				quotationMemberAddressApiResponseDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
//			} else {
//				logger.info("Quotation member address not found for the given QuotationMemberId: {}",
//						quotationMemberAddressId);
//				quotationMemberAddressApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//				quotationMemberAddressApiResponseDto
//						.setTransactionStatus("Quotation member address not found for the given QuotationMemberId: "
//								+ quotationMemberAddressId);
//			}
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- QuotationMemberAddressServiceImpl-- updateQuotationMember--", e);
//			quotationMemberAddressApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//			quotationMemberAddressApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
//		}
//		logger.info("quotationMemberId::{}::{}", quotationMemberAddressId, LOGEND);
//		return quotationMemberAddressApiResponseDto;
//	}
//
//	@Override
//	public QuotationMemberAddressApiResponseDto deleteQuotationMemberAddress(Long addressId) {
//		QuotationMemberAddressApiResponseDto quotationMemberAddressApiResponseDto = new QuotationMemberAddressApiResponseDto();
//		logger.info("addressId::{}::{}", addressId, LOGSTART);
//		try {
//			quotationMemberAddressTempRepository.deleteById(addressId);
//			quotationMemberAddressApiResponseDto.setTransactionStatus(CommonConstants.STATUS);
//			quotationMemberAddressApiResponseDto.setTransactionMessage(CommonConstants.DELETED);
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- QuotationMemberAddressServiceImpl-- deleteQuotationMemberAddress--", e);
//			quotationMemberAddressApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
//			quotationMemberAddressApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
//		}
//		logger.info("addressId::{}::{}", addressId, LOGEND);
//		return quotationMemberAddressApiResponseDto;
//	}
//
//}
