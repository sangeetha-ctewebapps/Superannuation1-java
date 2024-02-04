package com.lic.epgs.adjustmentcontribution.dto;
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
public class AdjustmentContributionDepositDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long adjustmentContributiondepositId;
	private String adjustmentContributiondepositStatus;
	private Long adjustmentContributionId;
	private Long policyId;
	private String challanNo;
	private String collectionNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date collectionDate;
	private BigDecimal amount = BigDecimal.ZERO;
	private BigDecimal adjustmentAmount = BigDecimal.ZERO;
	private BigDecimal availableAmount = BigDecimal.ZERO;
	private String transactionMode;
	private String collectionStatus;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date chequeRealisationDate;
	private String remark;
	private Boolean zeroId = Boolean.FALSE;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn = new Date();
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn = new Date();
	private Boolean isActive= Boolean.TRUE;

}
