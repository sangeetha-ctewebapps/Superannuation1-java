package com.lic.epgs.payout.temp.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.utils.CommonDateUtils;
import com.lic.epgs.payout.constants.PayoutErrorConstants;
import com.lic.epgs.payout.dto.PayoutNotesDto;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutNotesEntity;
import com.lic.epgs.payout.temp.repository.TempPayoutNotesRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutRepository;
import com.lic.epgs.payout.temp.service.TempPayoutNotesService;
import com.lic.epgs.payout.temp.service.TempSavePayoutService;

@Service
@Transactional
public class TempPayoutNotesServiceImpl implements TempPayoutNotesService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private TempPayoutNotesRepository tempPayoutNotesRepository;

	@Autowired
	TempPayoutRepository tempPayoutRepository;

	@Autowired
	TempSavePayoutService savePayoutService;

	@Override
	public ApiResponseDto<Set<PayoutNotesDto>> add(PayoutNotesDto payoutNotesDto) {
		try {
			logger.info("PayoutNotesServiceImpl:add:Start");

			Optional<TempPayoutEntity> result = tempPayoutRepository.findByInitiMationNoAndIsActive(payoutNotesDto.getInitiMationNo(),Boolean.TRUE);
			
			if (result.isPresent()) {
				
				TempPayoutEntity  payoutTemp = result.get();
				payoutTemp = convertToEntity(payoutNotesDto, payoutTemp);
				payoutTemp = tempPayoutRepository.save(payoutTemp);
				
				
				if (payoutTemp.getPayoutNotes() !=null) {
					Set<PayoutNotesDto> response = payoutTemp.getPayoutNotes().stream().map(this::convertEntityToDto).collect(Collectors.toSet());
					return ApiResponseDto.success(response,"");
				}
				
				
				
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

	private TempPayoutEntity convertToEntity(PayoutNotesDto payoutNotesDto, TempPayoutEntity tempPayouts) {
		List<TempPayoutNotesEntity> notesList = tempPayouts.getPayoutNotes();

		TempPayoutNotesEntity entity = new TempPayoutNotesEntity();
		entity.setCreatedBy(payoutNotesDto.getCreatedBy());
		entity.setCreatedOn(CommonDateUtils.sysDate());
		entity.setNoteContents(payoutNotesDto.getNoteContents());
		entity.setPayout(tempPayouts);
		notesList.add(entity);
		tempPayouts.setPayoutNotes(notesList);
		
		return tempPayouts;
	}

	@Override
	public ApiResponseDto<Set<PayoutNotesDto>> getNotesByPayoutNo(String payoutNo) {
		try {
			logger.info("PayoutNotesServiceImpl:add:Start");

			List<TempPayoutNotesEntity> result = tempPayoutNotesRepository.findByPayoutNo(payoutNo);

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

	private PayoutNotesDto convertEntityToDto(TempPayoutNotesEntity entity) {
		PayoutNotesDto payoutNotesDto = new PayoutNotesDto();
		payoutNotesDto.setPayoutNo(entity.getPayoutNo());
		payoutNotesDto.setNoteContents(entity.getNoteContents());
		payoutNotesDto.setCreatedBy(entity.getCreatedBy());
		payoutNotesDto.setCreatedOn(entity.getCreatedOn());
		return payoutNotesDto;
	}

}
