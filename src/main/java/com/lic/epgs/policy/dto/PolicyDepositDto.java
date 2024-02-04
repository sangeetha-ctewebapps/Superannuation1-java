package com.lic.epgs.policy.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyDepositDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long depositId;
	private Long policyId;
	private Long masterPolicyId;
	private Long tempPolicyId;
	private Long adjustmentContributionId;
	private Long regularContributionId;
	private String contributionType;
	private String collectionNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date collectionDate;
	private String collectionStatus;
	private String challanNo;
	private BigDecimal depositAmount;
	private BigDecimal adjustmentAmount;
	private BigDecimal availableAmount;
	private String transactionMode;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date chequeRealisationDate;
	private String remark;
	private String status;
	private Boolean zeroId;
	private Boolean isDeposit;
	private Boolean isActive;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;	
}
