package com.lic.epgs.adjustmentcontribution.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
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
public class AdjustmentContributionAllDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long adjustmentContributionId;
	
	private AdjustmentContributionDto adjustmentContribution;
	private List<AdjustmentContributionDto> adjustmentContributions;
	
	private AdjustmentContributionDepositDto adjustmentDepositDto;
	private List<AdjustmentContributionDepositDto> adjustmentDepositDtos;
	
	private AdjustmentContributionDepositAdjustmentDto adjustmentDepositAdjustmentDto;
	private List<AdjustmentContributionDepositAdjustmentDto> adjustmentDepositAdjustmentDtos;
	
	private AdjustmentContributionNotesDto adjustmentNotesDto;
	private List<AdjustmentContributionNotesDto> adjustmentNotesDtos;
	
	private AdjustmentContributionMemberDto adjustmentMember;
	private List<AdjustmentContributionMemberDto> adjustmentMembers;
	private List<MphBankEntity> bank;
	private transient Object zeroRow;
	
}
