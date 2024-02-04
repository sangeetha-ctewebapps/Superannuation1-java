package com.lic.epgs.policy.old.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyDepositOldDto implements Serializable {

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
	private String challanNo;
	private BigDecimal amount = BigDecimal.ZERO;
	private BigDecimal adjestmentAmount = BigDecimal.ZERO;
	private BigDecimal availableAmount = BigDecimal.ZERO;
	private String transactionMode;
	private String collectionStatus;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date chequeRealisationDate;
	private String remark;
	private String status;
	private Boolean zeroId = Boolean.FALSE;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn = new Date();
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn = new Date();
	private Boolean isActive= Boolean.TRUE;

}
