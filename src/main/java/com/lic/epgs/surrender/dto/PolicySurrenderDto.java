package com.lic.epgs.surrender.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.common.dto.BankAccountDetailsDto;
import com.lic.epgs.common.dto.CommonDocsDto;
import com.lic.epgs.common.dto.CommonNotesDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicySurrenderDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long surrenderId;
	private String surrenderNo;
	private Long policyId;
	private Long serviceId;
	private String surrenderStatus;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date surrenderEffctDt;
	private String surrenderReason;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date reqRecvDt;
	private Long surrenderType;
	private String policyNo;
	private Long reqRecvdFrom;
	private Long surrenderDoneBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date annualRenewDt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date lastPaidDt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date nextDueDt;
	private Long lastAmtPaid;
	private Long nextAmtPaid;
	private String AGING;
	private Long waitPeriod;
	private Long noticePeriod;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	private Boolean isActive;
	private String roleType;
	private PolicySurrenderPayOutDto surrenderPayOutDto;
	private Set<BankAccountDetailsDto> bankAccountDetailsDto = new HashSet<>();
	private Set<CommonDocsDto> docs = new HashSet<>();
	private Set<CommonNotesDto> notes = new HashSet<>();
	private String mphCode;
	private String mphName;
	private String product;
	private String unitCode;
}
