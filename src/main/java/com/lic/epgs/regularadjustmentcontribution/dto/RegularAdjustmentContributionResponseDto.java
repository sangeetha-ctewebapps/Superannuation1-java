package com.lic.epgs.regularadjustmentcontribution.dto;

/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.lic.epgs.policy.dto.MphMasterDto;
import com.lic.epgs.policy.dto.PolicyFrequencyDetailsDto;
import com.lic.epgs.policy.entity.MphBankEntity;
import com.lic.epgs.policy.entity.PolicyFrequencyDetailsEntity;
import com.lic.epgs.policy.old.dto.PolicyDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegularAdjustmentContributionResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long regularContributionId;

	private String transactionStatus;
	private String transactionMessage;
	
	private transient Object responseData;
	private transient Object zeroRow;
	private Boolean isCommencementdateOneYr;

	private BigDecimal totalDeposit;
	
	private List<MphBankEntity> bank;
	
	private Long policyId;
	private Long tempPolicyId;
	private Long mphId;
	private Long tempMphId;

	private PolicyDto poilcyDto;
	private MphMasterDto mphMasterDto;
	
	private List<PolicyFrequencyDetailsEntity> frequencyDtos;
	private PolicyFrequencyDetailsDto frequencyDto;
	
	
//	private RegularAdjustmentContributionDto adjustmentContribution;
//	private List<RegularAdjustmentContributionDto> adjustmentContributions;
//
//	private RegularAdjustmentContributionNotesDto adjustmentNotesDto;
//	private List<RegularAdjustmentContributionNotesDto> adjustmentNotesDtos;
//
//	private RegularAdjustmentContributionDepositDto regularAdjustmentContributionDepositsDto;
//	private List<RegularAdjustmentContributionDepositDto> regularAdjustmentContributionDepositsDtos;
//
//	private RegularAdjustmentContributionDepositAdjustmentDto regularAdjustmentContributionDepositAdjustmentDto;
//	private List<RegularAdjustmentContributionDepositAdjustmentDto> regularAdjustmentContributionDepositAdjustmentDtos;





}