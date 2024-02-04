package com.lic.epgs.claim.temp.service.impl;

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

import com.lic.epgs.claim.constants.ClaimErrorConstants;
import com.lic.epgs.claim.dto.ClaimDocumentDetailDto;
import com.lic.epgs.claim.dto.ClaimDocumentStatusRequestDto;
import com.lic.epgs.claim.temp.entity.TempClaimDocumentDetail;
import com.lic.epgs.claim.temp.entity.TempClaimEntity;
import com.lic.epgs.claim.temp.repository.TempClaimDocumentDetailRepository;
import com.lic.epgs.claim.temp.repository.TempClaimRepository;
import com.lic.epgs.claim.temp.service.TempClaimDocsService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.utils.CommonDateUtils;

@Service
@Transactional
public class TempClaimDocsServiceImpl implements TempClaimDocsService {

	protected final Logger logger = LogManager.getLogger(getClass());
	

	@Autowired
	TempClaimDocumentDetailRepository tempClaimDocumentDetailRepository;

	@Autowired
	TempClaimRepository tempClaimRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public ApiResponseDto<ClaimDocumentDetailDto> uploadClaimReqDoc(ClaimDocumentDetailDto docsDto) {
		try {
			Optional<TempClaimEntity> result = tempClaimRepository.findByClaimNoAndIsActive(docsDto.getClaimNo(),
					Boolean.TRUE);
			if (result.isPresent()) {
				TempClaimDocumentDetail docsEntity = convertDtoToEntity(docsDto);
				docsEntity = tempClaimDocumentDetailRepository.save(docsEntity);
				return ApiResponseDto.success(convertEntityToDto(docsEntity), "Document saved succesfully.");
			} else {
				return ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_NO).build());
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception-- ClaimDocsServiceImpl-- uploadClaimReqDoc --", e);
			return ApiResponseDto.error(ErrorDto.builder().message("Error saving document.").build());
		}
	}

	private TempClaimDocumentDetail convertDtoToEntity(ClaimDocumentDetailDto docsDto) {
		ModelMapper dtoModelMapper = new ModelMapper();
		dtoModelMapper.addMappings(new PropertyMap<ClaimDocumentDetailDto, TempClaimDocumentDetail>() {
			@Override
			protected void configure() {
				skip(destination.getIssuedDate());
			}
		});
		TempClaimDocumentDetail entity = dtoModelMapper.map(docsDto, TempClaimDocumentDetail.class);
		entity.setIssuedDate(CommonDateUtils.convertStringToDate(docsDto.getIssuedDate()));
		entity.setCreatedOn(CommonDateUtils.sysDate());
		entity.setIsDeleted(Boolean.FALSE);
		return entity;
	}

	@Override
	public ApiResponseDto<List<ClaimDocumentDetailDto>> viewClaimReqDoc(String claimNo) {
		List<TempClaimDocumentDetail> result = tempClaimDocumentDetailRepository.findByClaimNoAndIsDeleted(claimNo,
				Boolean.FALSE);

		if (!result.isEmpty()) {
			List<ClaimDocumentDetailDto> response = result.stream().map(this::convertEntityToDto)
					.collect(Collectors.toList());
			return ApiResponseDto.success(response);
		}
		return ApiResponseDto.success(Collections.emptyList());
	}

	private ClaimDocumentDetailDto convertEntityToDto(TempClaimDocumentDetail claimDocumentDetail) {
		ModelMapper dtoModelMapper = new ModelMapper();
		dtoModelMapper.addMappings(new PropertyMap<TempClaimDocumentDetail, ClaimDocumentDetailDto>() {
			@Override
			protected void configure() {
				skip(destination.getIssuedDate());
			}
		});
		ClaimDocumentDetailDto dto = dtoModelMapper.map(claimDocumentDetail, ClaimDocumentDetailDto.class);
		dto.setIssuedDate(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(claimDocumentDetail.getIssuedDate()));
		return dto;
	}

	@Override
	public ApiResponseDto<String> delete(Long documentId) {
		TempClaimDocumentDetail result = tempClaimDocumentDetailRepository.findByDocumentId(documentId);
		if (result != null) {
			result.setIsDeleted(Boolean.TRUE);
			tempClaimDocumentDetailRepository.save(result);
			return ApiResponseDto.success(null, "Document deleted successfully");
		}
		return ApiResponseDto.error(ErrorDto.builder().message("Error deleting document.").build());
	}

	@Override
	public ApiResponseDto<String> updateStatus(ClaimDocumentStatusRequestDto requestDto) {
		TempClaimDocumentDetail result = tempClaimDocumentDetailRepository.findByDocumentId(requestDto.getDocumentId());
		if (result != null) {
			result.setDocStatus(requestDto.getDocStatus());
			result.setModifiedBy(requestDto.getUpdatedBy());
			result.setModifiedOn(CommonDateUtils.sysDate());
			tempClaimDocumentDetailRepository.save(result);
			return ApiResponseDto.success(null, "Document updated successfully");
		}
		return ApiResponseDto.error(ErrorDto.builder().message("Error updating document.").build());
	}

}
