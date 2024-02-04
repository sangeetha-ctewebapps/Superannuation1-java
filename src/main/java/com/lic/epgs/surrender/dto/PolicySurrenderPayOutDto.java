package com.lic.epgs.surrender.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Pradeep Ramesh Date:25.05.2022
 */
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicySurrenderPayOutDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long payoutId;
	private Long policyId;
	private Long surrenderId;
	private Long surrenderAmtCld;
	private Long surrenderAmtNew;
	private String surrenderAmtComment;
	private Long surrenderChrgOld;
	private Long surrenderChrgNew;
	private String surrenderChrgComment;
	private Long finSurrenderOld;
	private Long finSurrenderNew;
	private String finSurrenderComment;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dtOfSurrender;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dtDocSubmit;
	private Long settlementMode;
	private Long totMembers;
	private Long surrenderAmt;
	private Long surrenderCharges;
	private Long marketValueAdj;
	private Long finSurrenderAmt;
	private String modeOfPay;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	private Boolean isActive;
	
	private Set<PolicySurrenderPayOutInstallmentDto> payoutInstallments;
}
