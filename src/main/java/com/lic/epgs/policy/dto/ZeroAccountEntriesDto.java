package com.lic.epgs.policy.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ZeroAccountEntriesDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long zeroAccEntId;
	private Long policyId;
	private Long policyConId;
	private BigDecimal zeroIdAmount;
	private String transactionType;
	private Long memberConId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date transactionDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;
	private String modifiedBy;
	private Boolean isActive;
	private Boolean isMoved;
	private Long masterPolicyId;
	private Long tempPolicyId;
}
