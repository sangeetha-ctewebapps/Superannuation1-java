package com.lic.epgs.payout.service.impl;

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

import com.lic.epgs.payout.constants.PayoutErrorConstants;
import com.lic.epgs.payout.dto.PayoutNotesDto;
import com.lic.epgs.payout.entity.PayoutEntity;
import com.lic.epgs.payout.entity.PayoutNotesEntity;
import com.lic.epgs.payout.repository.PayoutNotesRepository;
import com.lic.epgs.payout.repository.PayoutRepository;
import com.lic.epgs.payout.service.PayoutNotesService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.utils.CommonDateUtils;

@Service
@Transactional
public class PayoutNotesServiceImpl implements PayoutNotesService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private PayoutNotesRepository payoutNotesRepository;

	@Autowired
	PayoutRepository payoutRepository;

	@Override
	public ApiResponseDto<Set<PayoutNotesDto>> add(PayoutNotesDto payoutNotesDto) {
		try {
			logger.info("PayoutNotesServiceImpl:add:Start");

			Optional<PayoutEntity> result = payoutRepository.findByPayoutNoAndIsActive(payoutNotesDto.getPayoutNo(),
					Boolean.TRUE);
			if (result.isPresent()) {

				PayoutEntity payoutEntity = result.get();

				PayoutNotesEntity entity = convertToEntity(payoutNotesDto, payoutEntity);
				entity.setPayout(payoutEntity);
				payoutNotesRepository.save(entity);
				return ApiResponseDto.success(null, "Notes saved succesfully.");
			} else {
				return ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PayoutNotesServiceImpl:add", e);
			return ApiResponseDto.error(ErrorDto.builder().message("Error saving payout notes").build());
		} finally {
			logger.info("PayoutNotesServiceImpl:add:Ends");
		}
	}

	private PayoutNotesEntity convertToEntity(PayoutNotesDto payoutNotesDto, PayoutEntity payouts) {
		PayoutNotesEntity entity = new PayoutNotesEntity();
		entity.setCreatedBy(payoutNotesDto.getCreatedBy());
		entity.setCreatedOn(CommonDateUtils.sysDate());
		entity.setNoteContents(payoutNotesDto.getNoteContents());
		entity.setPayoutNo(payouts.getPayoutNo());
		return entity;
	}

	@Override
	public ApiResponseDto<Set<PayoutNotesDto>> getNotesByPayoutNo(String payoutNo) {
		try {
			logger.info("PayoutNotesServiceImpl:add:Start");

			List<PayoutNotesEntity> result = payoutNotesRepository.findByPayoutNo(payoutNo);

			if (!result.isEmpty()) {
				Set<PayoutNotesDto> response = result.stream().map(this::convertEntityToDto).collect(Collectors.toSet());
				return ApiResponseDto.success(response);
			}
			return ApiResponseDto.success(Collections.emptySet());
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PayoutNotesServiceImpl:add", e);
			return ApiResponseDto.error(ErrorDto.builder().message("Error fetching payout notes").build());
		} finally {
			logger.info("PayoutNotesServiceImpl:add:Ends");
		}
	}

	private PayoutNotesDto convertEntityToDto(PayoutNotesEntity entity) {
		PayoutNotesDto payoutNotesDto = new PayoutNotesDto();
		payoutNotesDto.setPayoutNo(entity.getPayoutNo());
		payoutNotesDto.setNoteContents(entity.getNoteContents());
		payoutNotesDto.setCreatedBy(entity.getCreatedBy());
		payoutNotesDto.setCreatedOn(entity.getCreatedOn());
		return payoutNotesDto;
	}

}
