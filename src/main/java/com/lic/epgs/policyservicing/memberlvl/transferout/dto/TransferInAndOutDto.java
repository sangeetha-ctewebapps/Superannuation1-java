package com.lic.epgs.policyservicing.memberlvl.transferout.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferInAndOutDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long trnsfrId;
	private Long serviceId;
	private Long sourcePolicyId;
	private String sourcePolicyNo;
	private Long destPolicyId;
	private String destPolicyNo;
	private Long interestAccrued;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	private String trnsfrStatus;
	private Integer workflowStatus;
	private Long trnsfrAmount;
	private Boolean isActive;
	private String unitCode;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date reqReceivedDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date serviceEffectiveDate;
	
	private TransferInAndOutMemberDto transOutMembers;
	private List<PolicyServiceNotesDto> notes = new ArrayList<>();
	
	private String pageName;
	private String componentLabel;
	
}
