package com.lic.epgs.surrender.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicySurrenderPayOutInstallmentDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long installmtId;
	private Long payoutId;
	private Long policyId;
	private String installmtNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date installmtDt;
	private Long fundPayable;
	private Long accruedInt;
	private Long totalPayable;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	private Boolean isActive;

}
