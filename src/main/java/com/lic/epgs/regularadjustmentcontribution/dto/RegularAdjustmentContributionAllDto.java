package com.lic.epgs.regularadjustmentcontribution.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.lic.epgs.policy.entity.MphBankEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegularAdjustmentContributionAllDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long regularContributionId;

	private RegularAdjustmentContributionDepositAdjustmentDto regAdjustmentConAdjustmentDto;
	private List<RegularAdjustmentContributionDepositAdjustmentDto> regAdjustmentContributionDepositAdjustmentDtos;
	
	private RegularAdjustmentContributionDepositDto regAdjustmentContDepositDto;
	private List<RegularAdjustmentContributionDepositDto> regAdjustmentContributDepositDtos;
	
	private RegularAdjustmentContributionDto regAdjustmentContribution;
	private List<RegularAdjustmentContributionDto> regAdjustmentContributions;
	
	private RegularAdjustmentContributionNotesDto regAdjustmentNotesDto;
	private List<RegularAdjustmentContributionNotesDto> regAdjustmentNotesDtos;
	
	private RegularAdjustmentContributionMemberDto regAdjustmentMember;
	private List<RegularAdjustmentContributionMemberDto> regAdjustmentMembers;
	private List<MphBankEntity> bank;
	private BigDecimal totalDeposit;
	private transient Object zeroRow;
}

