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
public class PolicyZeroActDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String MemberId;
	
	private String MemberShipId;
	private String MembershipNumber;
	private String TypeOfMembershipNo;
	
	private String MemberStatus;
	
	private Integer QuotationId;
	private Long PolicyId;
	private String PolicyNo;
	private String PolicyStatus;
	
	private String proposalNumber;
	
	private String MphCode;
	private String MphName;
	
	private String LicId;
	
	private String Role;
	
	private String UnitCode;
	
	private String CreatedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date CreatedOn= new Date();
	
	private String ModifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date ModifiedOn= new Date();
	
	private Boolean IsActive = Boolean.TRUE;
	
	private Boolean IsZeroId = Boolean.FALSE;
	private String ZeroId;
	private String ZeroIdStatus;
	
	private BigDecimal ZeroAvailableAmount= BigDecimal.ZERO;
	private BigDecimal ZeroUsedAmount= BigDecimal.ZERO;
	
}