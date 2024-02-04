package com.lic.epgs.policyservicing.policylvl.dto.freelookcancellation;

/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceDocumentDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FreeLookCancellationDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long freeLookId;
	
	private Set<PolicyServiceDocumentDto> documents = new HashSet<>();
	private Set<PolicyServiceNotesDto> notes = new HashSet<>();
	
	private Long policyId;
	private String policyStatus;
	private String policyNumber;
	
	private Long serviceId;
	private String serviceStatus;
	private String serviceNumber;
	
	
	private String policyCancellationRequestNumber;
	private String freeLookStatus;
	private String workflowStatus;
	
	private String requestReceivedFrom;
	private String policyBondRequestLetterRecevied;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date cancelRequestDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyBondDispatchDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyBondRecievedDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date effectiveCancellationDate;
	
	private BigDecimal totalFundValue = BigDecimal.ZERO;
	private BigDecimal totalContribution = BigDecimal.ZERO;

	private BigDecimal totalIntrestAccuredOrPaid = BigDecimal.ZERO;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date claimsPainOnDate;
	private String reCovery;
	private BigDecimal totalrefundamount = BigDecimal.ZERO;
	private String agentName;
	private BigDecimal commissionAmountToBeReversed = BigDecimal.ZERO;
	
	private BigDecimal oldFundValue = BigDecimal.ZERO;
	private BigDecimal oldTotalIntrestAccuredOrPaid = BigDecimal.ZERO;
	private BigDecimal oldArrearValue = BigDecimal.ZERO;
	private BigDecimal oldRecoveryValue = BigDecimal.ZERO;
	
	private String commentFundValue;
	private String commentTotalIntrestAccuredOrPaid;
	private String commentArrearValue;
	private String commentRecoveryValue;
	
	private BigDecimal newFundValue = BigDecimal.ZERO;
	private BigDecimal newTotalIntrestAccuredOrPaid = BigDecimal.ZERO;
	private BigDecimal newArrearValue = BigDecimal.ZERO;
	private BigDecimal newRecoveryValue = BigDecimal.ZERO;
	
	private String unitOffice;
	
	private String unitCode;
	
	private String mphCode;
	private String mphName;
	private String product;
	private String lineOfBusiness;
	private String policyFromDate;
	private String policyToDate;
	

	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn = new Date();
	
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn = new Date();
	
	private Boolean isActive = Boolean.TRUE;
	
	private String rejectionReasonCode;
	private String rejectionRemarks;
}