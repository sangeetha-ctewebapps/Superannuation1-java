package com.lic.epgs.policyservicing.memberlvl.transferout.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferInAndOutReqDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String serviceDoneBy;
	private Date reqReceivedDate;
	private Date serviceEffectiveDate;
	private Long trnsfrId;
	private String pageName;
	private String componentLabel;
	private String memberShipId;
	private Long policyId;
	private String unitCode;
	private String createdBy;
	private String modifiedBy;
	
	private String memberShipNumber;
	private String phone;
	private String pan;
	private Long aadharNumber;
	private String firstname;
	private Date dateofbirth;
	private String policyStatus;
	private String policyFrom;
	private String policyTo;
	private String policyNumber;
	
	private List<PolicyServiceNotesDto> notes = new ArrayList<>();
	private TransferInAndOutDto transInPolicyDetails;
	
}
