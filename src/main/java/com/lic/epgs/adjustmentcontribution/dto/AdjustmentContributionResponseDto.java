package com.lic.epgs.adjustmentcontribution.dto;
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
import com.lic.epgs.policy.entity.PolicyFrequencyDetailsTempEntity;
import com.lic.epgs.policy.old.dto.PolicyDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdjustmentContributionResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long adjustementContributionId;
	
	private String transactionStatus;
	private String transactionMessage;
	
	private transient Object responseData;
	private transient Object zeroRow;
	private Boolean isCommencementdateOneYr;
	
	private BigDecimal totalDeposit;
	
	private List<MphBankEntity> bank;

	private PolicyDto poilcyDto;
	private MphMasterDto mphMasterDto;
	private List<PolicyFrequencyDetailsEntity> frequencyDtoEntities;
	private List<PolicyFrequencyDetailsDto> frequencyDtos;
	private PolicyFrequencyDetailsDto frequencyDto;

	private List<PolicyFrequencyDetailsTempEntity> frequencyDtosTemp;
	private PolicyFrequencyDetailsDto frequencyDtoTemp;
	
	private List<ACPolicyDepositAdjustmentDto> policyDepositAdjustmentDtos;
	private ACPolicyDepositAdjustmentDto policyDepositAdjustmentDto;

	private Long policyId;
	private Long tempPolicyId;
	private Long mphId;
	private Long tempMphId;
	
	
	
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
//	private Date test;
//	private String test1;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
//	private Date test2;
	
	
}