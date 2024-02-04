package com.lic.epgs.payout.temp.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.payout.constants.PayoutErrorConstants;
import com.lic.epgs.payout.dto.PayoutDocumentDetailDto;
import com.lic.epgs.payout.dto.PayoutDocumentStatusRequestDto;
import com.lic.epgs.payout.temp.entity.TempPayoutDocumentDetail;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;
import com.lic.epgs.payout.temp.repository.TempPayoutDocumentDetailRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutRepository;
import com.lic.epgs.payout.temp.service.TempPayoutDocsService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.utils.CommonDateUtils;

@Service
@Transactional
public class TempPayoutDocsServiceImpl implements TempPayoutDocsService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	TempPayoutDocumentDetailRepository tempPayoutDocumentDetailRepository;

	@Autowired
	TempPayoutRepository tempPayoutRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public ApiResponseDto<PayoutDocumentDetailDto> uploadPayoutReqDoc(PayoutDocumentDetailDto docsDto) {
		try {
			Optional<TempPayoutEntity> result = tempPayoutRepository.findByInitiMationNoAndIsActive(docsDto.getInitiMationNo(),Boolean.TRUE);
			if (result.isPresent()) {
				TempPayoutDocumentDetail docsEntity = convertDtoToEntity(docsDto);
				docsEntity = tempPayoutDocumentDetailRepository.save(docsEntity);
				return ApiResponseDto.success(convertEntityToDto(docsEntity), "Document saved succesfully.");
			} else {
				return ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception-- PayoutDocsServiceImpl-- uploadPayoutReqDoc --", e);
			return ApiResponseDto.error(ErrorDto.builder().message("Error saving document.").build());
		}
	}

	private TempPayoutDocumentDetail convertDtoToEntity(PayoutDocumentDetailDto docsDto) {
		ModelMapper dtoModelMapper = new ModelMapper();
		dtoModelMapper.addMappings(new PropertyMap<PayoutDocumentDetailDto, TempPayoutDocumentDetail>() {
			@Override
			protected void configure() {
				skip(destination.getIssuedDate());
			}
		});
		TempPayoutDocumentDetail entity = dtoModelMapper.map(docsDto, TempPayoutDocumentDetail.class);
		entity.setIssuedDate(CommonDateUtils.convertStringToDate(docsDto.getIssuedDate()));
		entity.setCreatedOn(CommonDateUtils.sysDate());
		entity.setIsDeleted(Boolean.FALSE);
		return entity;
	}

	@Override
	public ApiResponseDto<List<PayoutDocumentDetailDto>> viewPayoutReqDoc(String payoutNo) {
		List<TempPayoutDocumentDetail> result = tempPayoutDocumentDetailRepository.findByPayoutNoAndIsDeleted(payoutNo,
				Boolean.FALSE);

		if (!result.isEmpty()) {
			List<PayoutDocumentDetailDto> response = result.stream().map(this::convertEntityToDto)
					.collect(Collectors.toList());
			return ApiResponseDto.success(response);
		}
		return ApiResponseDto.success(Collections.emptyList());
	}

	private PayoutDocumentDetailDto convertEntityToDto(TempPayoutDocumentDetail payoutDocumentDetail) {
		ModelMapper dtoModelMapper = new ModelMapper();
		dtoModelMapper.addMappings(new PropertyMap<TempPayoutDocumentDetail, PayoutDocumentDetailDto>() {
			@Override
			protected void configure() {
				skip(destination.getIssuedDate());
			}
		});
		PayoutDocumentDetailDto dto = dtoModelMapper.map(payoutDocumentDetail, PayoutDocumentDetailDto.class);
		dto.setIssuedDate(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(payoutDocumentDetail.getIssuedDate()));
		return dto;
	}

	@Override
	public ApiResponseDto<String> delete(Long documentId) {
		TempPayoutDocumentDetail result = tempPayoutDocumentDetailRepository.findByDocumentId(documentId);
		if (result != null) {
			result.setIsDeleted(Boolean.TRUE);
			tempPayoutDocumentDetailRepository.save(result);
			return ApiResponseDto.success(null, "Document deleted successfully");
		}
		return ApiResponseDto.error(ErrorDto.builder().message("Error deleting document.").build());
	}

	@Override
	public ApiResponseDto<String> updateStatus(PayoutDocumentStatusRequestDto requestDto) {
		TempPayoutDocumentDetail result = tempPayoutDocumentDetailRepository.findByDocumentId(requestDto.getDocumentId());
		if (result != null) {
			result.setDocStatus(requestDto.getDocStatus());
			result.setModifiedBy(requestDto.getUpdatedBy());
			result.setModifiedOn(CommonDateUtils.sysDate());
			tempPayoutDocumentDetailRepository.save(result);
			return ApiResponseDto.success(null, "Document updated successfully");
		}
		return ApiResponseDto.error(ErrorDto.builder().message("Error updating document.").build());
	}

}
