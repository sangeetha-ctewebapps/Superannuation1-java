package com.lic.epgs.policyservicing.memberlvl.transferout.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferInAndOutMemberDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long trnsfrMemberId;
	private Long trnsfrId;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	private Long memberId;
	private String memberNo;
	private String memberName;
	private Long sourcePolicyId;
	private String sourcePolicyNo;
	private Long destPolicyId;
	private String destPolicyNo;
	private String mphName;
	private Long productId;
	private Long prodVariant;
	private Long category;
	private BigDecimal employerContribution= BigDecimal.ZERO;
	private BigDecimal employeeContribution= BigDecimal.ZERO;
	private BigDecimal voluntaryContribution= BigDecimal.ZERO;
	private Long trnsfrAmount;
	private Boolean isActive;
	private String pan;
	private Long aadharNumber; 
	private Long phone;
	private String memberShipId;
	private Long srcTransferId;
	private String unitCode;
}
