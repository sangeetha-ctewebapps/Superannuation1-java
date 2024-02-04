package com.lic.epgs.claim.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.claim.constants.ClaimErrorConstants;
import com.lic.epgs.claim.dto.ClaimNotesDto;
import com.lic.epgs.claim.entity.ClaimEntity;
import com.lic.epgs.claim.entity.ClaimNotesEntity;
import com.lic.epgs.claim.repository.ClaimNotesRepository;
import com.lic.epgs.claim.repository.ClaimRepository;
import com.lic.epgs.claim.service.ClaimNotesService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.utils.CommonDateUtils;

@Service
@Transactional
public class ClaimNotesServiceImpl implements ClaimNotesService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private ClaimNotesRepository claimNotesRepository;

	@Autowired
	ClaimRepository claimRepository;

	@Override
	public ApiResponseDto<String> add(ClaimNotesDto claimNotesDto) {
		try {
			logger.info("ClaimNotesServiceImpl:add:Start");

			Optional<ClaimEntity> result = claimRepository.findByClaimNoAndIsActive(claimNotesDto.getClaimNo(),
					Boolean.TRUE);
			if (result.isPresent()) {

				ClaimEntity claimEntity = result.get();

				ClaimNotesEntity entity = convertToEntity(claimNotesDto, claimEntity);
				entity.setClaim(claimEntity);
				claimNotesRepository.save(entity);
				return ApiResponseDto.success(null, "Notes saved succesfully.");
			} else {
				return ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_INTIMATION_NO).build());
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:ClaimNotesServiceImpl:add", e);
			return ApiResponseDto.error(ErrorDto.builder().message("Error saving claim notes").build());
		} finally {
			logger.info("ClaimNotesServiceImpl:add:Ends");
		}
	}

	private ClaimNotesEntity convertToEntity(ClaimNotesDto claimNotesDto, ClaimEntity claims) {
		ClaimNotesEntity entity = new ClaimNotesEntity();
		entity.setCreatedBy(claimNotesDto.getCreatedBy());
		entity.setCreatedOn(CommonDateUtils.sysDate());
		entity.setNoteContents(claimNotesDto.getNoteContents());
		entity.setClaimNo(claims.getClaimNo());
		return entity;
	}

	@Override
	public ApiResponseDto<Set<ClaimNotesDto>> getNotesByClaimNo(String claimNo) {
		try {
			logger.info("ClaimNotesServiceImpl:add:Start");

			List<ClaimNotesEntity> result = claimNotesRepository.findByClaimNo(claimNo);

			if (!result.isEmpty()) {
				Set<ClaimNotesDto> response = result.stream().map(this::convertEntityToDto).collect(Collectors.toSet());
				return ApiResponseDto.success(response);
			}
			return ApiResponseDto.success(Collections.emptySet());
		} catch (IllegalArgumentException e) {
			logger.error("Exception:ClaimNotesServiceImpl:add", e);
			return ApiResponseDto.error(ErrorDto.builder().message("Error fetching claim notes").build());
		} finally {
			logger.info("ClaimNotesServiceImpl:add:Ends");
		}
	}

	private ClaimNotesDto convertEntityToDto(ClaimNotesEntity entity) {
		ClaimNotesDto claimNotesDto = new ClaimNotesDto();
		claimNotesDto.setClaimNo(entity.getClaimNo());
		claimNotesDto.setNoteContents(entity.getNoteContents());
		claimNotesDto.setCreatedBy(entity.getCreatedBy());
		claimNotesDto.setCreatedOn(entity.getCreatedOn());
		return claimNotesDto;
	}

}
